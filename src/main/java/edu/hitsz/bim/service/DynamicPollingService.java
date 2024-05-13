package edu.hitsz.bim.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledFuture;

import static edu.hitsz.bim.service.DJIHandler.*;

@Service
public class DynamicPollingService {

    private static final String HOST = "https://openapi-cn.dji.com";


    public void pollOperation(String uuid) {
        try {
            String uri = "/terra-rescon-be/v2/jobs/" + uuid;
            String url = HOST + uri;
            String xDate = getGMTDate();
            String lowerMethod = "get";
            String digest = encodeBase64(sha256(""));  // GET请求没有body，故此处为空字符串

            String signatureContent = String.format(
                    "date: %s\n@request-target: %s %s\ndigest: SHA-256=%s",
                    xDate, lowerMethod, uri, digest);
            String signature = calculateHMAC(DJI_SECRET_KEY, signatureContent);

            HttpGet request = new HttpGet(url);
            request.addHeader("Date", xDate);
            request.addHeader("Digest", "SHA-256=" + digest);
            request.addHeader("Authorization", String.format(
                    "hmac username=\"%s\", algorithm=\"hmac-sha256\", headers=\"date @request-target digest\", signature=\"%s\"",
                    DJI_APP_KEY, signature));
            request.addHeader("Content-Type", "application/json;charset=UTF-8");

            try (CloseableHttpClient httpClient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpClient.execute(request)) {
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                System.out.println("JOB " + uuid + " Response: " + responseBody);

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(responseBody);
                JsonNode dataNode = rootNode.path("data");
                String outputResourceUuid = dataNode.path("outputResourceUuid").asText();
                if (!outputResourceUuid.isEmpty() && !outputResourceUuid.isBlank()) {
                    getFileUuid(outputResourceUuid);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getGMTDate() {
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        dateFormatGmt.setTimeZone(java.util.TimeZone.getTimeZone("GMT"));
        return dateFormatGmt.format(new Date());
    }

    private String calculateHMAC(String key, String data) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        return Base64.getEncoder().encodeToString(sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8)));
    }

    private byte[] sha256(String data) throws Exception {
        return java.security.MessageDigest.getInstance("SHA-256").digest(data.getBytes(StandardCharsets.UTF_8));
    }

    private String encodeBase64(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    public void getFileUuid(String resourceUuid) {
        String uri = "/terra-rescon-be/v2/resources/" + resourceUuid;
        try {
            String url = HOST + uri;
            String xDate = getGMTDate();
            String lowerMethod = "get";
            String payload = "";  // GET请求没有payload，使用空字符串
            String digest = encodeBase64(sha256(payload));

            String signatureContent = String.format(
                    "date: %s\n@request-target: %s %s\ndigest: SHA-256=%s",
                    xDate, lowerMethod, uri, digest);
            String signature = calculateHMAC(DJI_SECRET_KEY, signatureContent);

            HttpGet request = new HttpGet(url);
            request.addHeader("Date", xDate);
            request.addHeader("Digest", "SHA-256=" + digest);
            request.addHeader("Authorization", String.format(
                    "hmac username=\"%s\", algorithm=\"hmac-sha256\", headers=\"date @request-target digest\", signature=\"%s\"",
                    DJI_APP_KEY, signature));
            request.addHeader("Content-Type", "application/json;charset=UTF-8");

            try (CloseableHttpClient httpClient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpClient.execute(request)) {
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                System.out.println("Get File UUid Response: " + responseBody);
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(responseBody);
                JsonNode fileUuids = rootNode.path("fileUuids");
                String uuid = fileUuids.path("uuid").asText();
                downloadImage(uuid);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void downloadImage(String uuid) {
        try {
            String xDate = getGMTDate();
            String payload = "";
            String digest = encodeBase64(sha256(payload));
            String imageUrl = HOST + "/terra-rescon-be/v2/files/:" + uuid;

            String requestSignature = calculateSignature(xDate, HTTP_METHOD, "/terra-rescon-be/v2/files/" + uuid, digest);

            String cmd = "curl -X " + HTTP_METHOD + " " + imageUrl +
                    " -H \"Date: " + xDate + "\"" +
                    " -H \"Digest: SHA-256=" + digest + "\"" +
                    " -H \"Authorization: hmac username=\\\"" + DJI_APP_KEY + "\\\", algorithm=\\\"hmac-sha256\\\", headers=\\\"date @request-target digest\\\", signature=\\\"" + requestSignature + "\\\"\"" +
                    " -H 'Content-Type: application/json;charset=UTF-8' | jq -r '.data.url'";

            Process process = Runtime.getRuntime().exec(cmd);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String url = reader.readLine(); // Read the URL from the output

            // Use the URL to download the image
            if (url != null && !url.isEmpty()) {
                String wgetCmd = "wget -O a.png " + url;
                Process wgetProcess = Runtime.getRuntime().exec(wgetCmd);
                int wgetResult = wgetProcess.waitFor();
                if (wgetResult == 0) {
                    System.out.println("Download successful.");
                } else {
                    System.out.println("Download failed.");
                }
            } else {
                System.out.println("URL not found in response.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String calculateSignature(String xDate, String method, String uri, String digest) throws Exception {
        String content = String.format("date: %s\n@request-target: %s %s\ndigest: SHA-256=%s", xDate, method, uri, digest);
        Mac hmacSha256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(DJI_SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        hmacSha256.init(secretKeySpec);
        byte[] signatureBytes = hmacSha256.doFinal(content.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(signatureBytes);
    }
}
