package edu.hitsz.bim.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONArray;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import edu.hitsz.bim.domain.dto.FileUploadResult;
import jakarta.annotation.Resource;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.apache.commons.codec.digest.DigestUtils.sha256;

/**
 * @author lane
 * @description
 * @since 2024/2/26 22:53
 */
@Service
public class DJIHandler {

    public static final String DJI_APP_KEY = "aOou_zudnHlhlb8HVh1WG";
    public static final String DJI_SECRET_KEY = "uVhNyNaUkvW2dBR8w0tnXyIRqRJF7KK1";
    private static final String URI_TOKEN = "/terra-rescon-be/v2/store/obtain_token";
    public static final String HTTP_METHOD = "POST";
    private static final String HOST = "https://openapi-cn.dji.com";

    private static final String URI = "/terra-rescon-be/v2/resources";
    private static final String URI_CREATE_JOB = "/terra-rescon-be/v2/jobs";

    private static final String URI_CALLBACK = "/terra-rescon-be/v2/store/upload_callback";

    private static String RESOURCE_UUID;

    private static String CALLBACK_PARAM;
    private static String JOB_UUID;

    @Resource
    private DynamicScheduler scheduler;
    public void run(String filePath) {
        String payload = "{}"; // Adjust payload as needed


        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        String xDate = dateFormatGmt.format(new Date());

        String lowerMethod = HTTP_METHOD.toLowerCase();
        String digest = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, DJI_SECRET_KEY).hmacHex(payload);

        String content = "date: " + xDate + "\n@request-target: " + lowerMethod + " " + URI_TOKEN + "\ndigest: SHA-256=" + digest;
        byte[] hmac = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, DJI_SECRET_KEY).hmac(content);
        String signature = Base64.getEncoder().encodeToString(hmac);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(HOST + URI_TOKEN);
            request.setHeader("Date", xDate);
            request.setHeader("Digest", "SHA-256=" + digest);
            request.setHeader("Authorization", "hmac username=\"" + DJI_APP_KEY + "\", algorithm=\"hmac-sha256\", headers=\"date @request-target digest\", signature=\"" + signature + "\"");
            request.setHeader("Content-Type", "application/json;charset=UTF-8");
            request.setEntity(new StringEntity(payload, StandardCharsets.UTF_8));

            System.out.println("Sending request to DJI...");

            //2.upload OSS
            uploadOSS(request, filePath);

            //3.create DJI resource
            RESOURCE_UUID = createResource(filePath);

            //4.关联文件
            connectFiles();

            //5.创建Job
            createJob();

            //6.启动Job
            startJob();

            //7.开始轮询
            scheduler.startPolling(JOB_UUID);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void uploadOSS(HttpPost request, String filePath) throws IOException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            String response = EntityUtils.toString(client.execute(request).getEntity(), StandardCharsets.UTF_8);
            System.out.println("GET TOKEN : " + response);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode dataNode = rootNode.path("data");
            CALLBACK_PARAM = dataNode.path("callbackParam").asText();

            String accessKeyId = dataNode.path("accessKeyID").asText();
            String accessKeySecret = dataNode.path("secretAccessKey").asText();
            String bucketName = dataNode.path("cloudBucketName").asText();
            String token = dataNode.path("sessionToken").asText();
            String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";

            String storePath = dataNode.path("storePath").asText();

            System.out.println("Upload OSS Response: " + response);
            // 创建OSS客户端
            List<FileUploadResult> uploadedFiles = new ArrayList<>();
            try {
                Files.walk(Paths.get(filePath))
                        .filter(Files::isRegularFile)
                        .forEach(path -> {
                            String relativePath = path.getParent().relativize(path).toString();
                            String ossFilePath = storePath + relativePath.replace("\\", "/");
                            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret, token);
                            try {
                                PutObjectResult putResult = ossClient.putObject(bucketName, ossFilePath, path.toFile());
                                String etag = putResult.getETag();
                                System.out.println("Uploaded: " + path + " -> " + ossFilePath + ", etag: " + etag);
                                System.out.println("PutResult" + putResult.toString());
                                uploadedFiles.add(
                                        FileUploadResult.builder()
                                                .name(relativePath.replace("\\", "/"))
                                                .etag(etag)
                                                .checksum(etag).build());
                            }  catch (OSSException oe) {
                                System.out.println("Caught an OSSException, which means your request made it to OSS, "
                                        + "but was rejected with an error response for some reason.");
                                System.out.println("Error Message:" + oe.getErrorMessage());
                                System.out.println("Error Code:" + oe.getErrorCode());
                                System.out.println("Request ID:" + oe.getRequestId());
                                System.out.println("Host ID:" + oe.getHostId());
                            } catch (ClientException ce) {
                                System.out.println("Caught an ClientException, which means the client encountered "
                                        + "a serious internal problem while trying to communicate with OSS, "
                                        + "such as not being able to access the network.");
                                System.out.println("Error Message:" + ce.getMessage());
                            } finally {
                                if (ossClient != null) {
                                    ossClient.shutdown();
                                }
                            }
                        });

                if (!uploadedFiles.isEmpty()) {
                    // 保存上传结果为JSON文件
                    Path resultPath = Paths.get("./uploaded_files.json");
                    Files.write(resultPath, new JSONArray(uploadedFiles).toString().getBytes());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public String createResource(String resourceName) {
        String payload = String.format("{\"name\": \"%s\", \"type\": \"map\"}", resourceName);
        String url = HOST + URI;
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String xDate = dateFormat.format(new Date());
        String lowerMethod = HTTP_METHOD.toLowerCase();
        String digest = Base64.getEncoder().encodeToString(sha256(payload));

        String requestSignature = null;
        try {
            requestSignature = calculateSignature("date: " + xDate + "\n@request-target: " + lowerMethod + " " + URI + "\ndigest: SHA-256=" + digest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            connection.setRequestMethod(HTTP_METHOD);
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        }
        connection.setRequestProperty("Date", xDate);
        connection.setRequestProperty("Digest", "SHA-256=" + digest);
        connection.setRequestProperty("Authorization", "hmac username=\"" + DJI_APP_KEY + "\", algorithm=\"hmac-sha256\", headers=\"date @request-target digest\", signature=\"" + requestSignature + "\"");
        connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = payload.getBytes("utf-8");
            os.write(input, 0, input.length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String uuid = "";
        try {
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 判断响应码是否为200
                StringBuilder response = new StringBuilder();
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line);
                    }
                }

                JSONObject jsonResponse = new JSONObject(response.toString());
                System.out.println("Create Resource :" + jsonResponse.toString());
                uuid = jsonResponse.getJSONObject("data").getString("uuid"); // 获取uuid
                System.out.println("Resource ID: " + uuid);
            } else {
                System.out.println("Request failed");
            }
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
        return uuid;
    }

    private String calculateSignature(String content) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(DJI_SECRET_KEY.getBytes(), "HmacSHA256");
        mac.init(secretKeySpec);
        return Base64.getEncoder().encodeToString(mac.doFinal(content.getBytes()));
    }

    public void connectFiles(){
        try {
            Type listType = new TypeToken<List<FileUploadResult>>(){}.getType();
            List<FileUploadResult> uploadedFiles = new Gson().fromJson(new FileReader("./uploaded_files.json"), listType);

            List<FileUploadResult> miniBatch = new ArrayList<>();
            for (FileUploadResult item : uploadedFiles) {
                miniBatch.add(item);
                if (miniBatch.size() >= 50) {
                    bindBatchFiles(miniBatch);
                    miniBatch.clear();
                }
            }
            if (!miniBatch.isEmpty()) {
                bindBatchFiles(miniBatch);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void bindBatchFiles(List<FileUploadResult> files) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("resourceUUID", RESOURCE_UUID);
        payload.put("callbackParam", CALLBACK_PARAM);

        List<Map<String, String>> filesList = new ArrayList<>();
        for (FileUploadResult file : files) {
            Map<String, String> fileMap = new HashMap<>();
            fileMap.put("checksum", file.getChecksum());
            fileMap.put("etag", file.getEtag());
            fileMap.put("name", file.getName());
            filesList.add(fileMap);
        }
        payload.put("files", filesList);

        Gson gson = new Gson();
        String payloadJson = gson.toJson(payload);
        System.out.println("payloadJson" + payloadJson);
        String digest = encodeSHA256(payloadJson);
        String gmtTime = getGMTTime();
        String signingString = String.format("date: %s\n@request-target: post %s\ndigest: SHA-256=%s", gmtTime, URI_CALLBACK, digest);
        String signature = hmacSHA256(DJI_SECRET_KEY, signingString);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Date", gmtTime);
        headers.put("Digest", "SHA-256=" + digest);
        headers.put("Authorization", String.format("hmac username=\"%s\", algorithm=\"hmac-sha256\", headers=\"date @request-target digest\", signature=\"%s\"", DJI_APP_KEY, signature));

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(HOST + URI_CALLBACK);
            headers.forEach(request::addHeader);
            request.setEntity(new StringEntity(payloadJson, StandardCharsets.UTF_8));

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                System.out.println("bindBatchFiles Response content: " + EntityUtils.toString(response.getEntity()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void getResourseDetails(String uuid) {
        String xDate = getGMTDate();
        String payload = "";
        String digest = encodeSHA256(payload);

        String url = HOST + "/v2/resources/" + uuid;



        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String requestSignature = calculateSignature(xDate, "GET", url, digest);
            HttpGet request = new HttpGet(url);
            request.setHeader("Date", xDate);
            request.setHeader("Digest", "SHA-256=" + digest);
            request.setHeader("Authorization", String.format("hmac username=\"%s\", algorithm=\"hmac-sha256\", headers=\"date @request-target digest\", signature=\"%s\"", DJI_APP_KEY, requestSignature));
            request.setHeader("Content-Type", "application/json;charset=UTF-8");

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String responseString = EntityUtils.toString(response.getEntity());
                System.out.println( responseString);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    private String getGMTTime() {
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormatGmt.format(new Date());
    }

    private String encodeSHA256(String data) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(DJI_SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            return Base64.getEncoder().encodeToString(sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new RuntimeException("Error while encoding SHA256", e);
        }
    }

    private String hmacSHA256(String secret, String data) {
        try {
            Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            hmacSHA256.init(secretKeySpec);
            return Base64.getEncoder().encodeToString(hmacSHA256.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new RuntimeException("Error while calculating HMAC SHA256", e);
        }
    }

    private String getGMTDate() {
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormatGmt.format(new Date());
    }
    private void createJob() {
        try {
            String payload = "{\"name\": \"test_name\"}";

            String xDate = getGMTDate();
            String lowerMethod = HTTP_METHOD.toLowerCase();
            String digest = Base64.getEncoder().encodeToString(sha256(payload));
            String requestSignature = calculateSignature(xDate, lowerMethod, URI_CREATE_JOB, digest);

            String url = HOST + URI_CREATE_JOB;

            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpPost request = new HttpPost(url);
                request.setHeader("Date", xDate);
                request.setHeader("Digest", "SHA-256=" + digest);
                request.setHeader("Authorization", String.format("hmac username=\"%s\", algorithm=\"hmac-sha256\", headers=\"date @request-target digest\", signature=\"%s\"", DJI_APP_KEY, requestSignature));
                request.setHeader("Content-Type", "application/json;charset=UTF-8");
                request.setEntity(new StringEntity(payload, StandardCharsets.UTF_8));

                try (CloseableHttpResponse response = httpClient.execute(request)) {
                    String responseString = EntityUtils.toString(response.getEntity());
                    System.out.println("Create Job Response: " + responseString);

                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode rootNode = objectMapper.readTree(responseString);
                    JsonNode dataNode = rootNode.path("data");
                    JOB_UUID = dataNode.path("uuid").asText(); // 确保响应中包含uuid字段
                    System.out.println("JOB UUID: " + JOB_UUID);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    private void startJob() {
        try {
            String payload = "{\"type\": 14, \"resourceUuid\": \"" + RESOURCE_UUID +
                    "\", \"parameters\": \"{\\\"parameter\\\":{\\\"map_mode\\\":1,\\\"quality_level\\\":1,\\\"output_geo_desc\\\":{\\\"cs_type\\\":\\\"GEO_CS\\\",\\\"geo_cs\\\":\\\"EPSG:32650\\\",\\\"geo_cs_wkt\\\":\\\"\\\",\\\"override_vertical_cs\\\":\\\"\\\"}}}\"}";

            String URI_START_JOB = "/terra-rescon-be/v2/jobs/" + JOB_UUID + "/start";
            String xDate = getGMTDate();
            String lowerMethod = HTTP_METHOD.toLowerCase();
            String digest = Base64.getEncoder().encodeToString(sha256(payload));
            String requestSignature = calculateSignature(xDate, lowerMethod, URI_START_JOB, digest);

            String url = "https://openapi-cn.dji.com" + URI_START_JOB;

            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpPost request = new HttpPost(url);
                request.setHeader("Date", xDate);
                request.setHeader("Digest", "SHA-256=" + digest);
                request.setHeader("Authorization", String.format("hmac username=\"%s\", algorithm=\"hmac-sha256\", headers=\"date @request-target digest\", signature=\"%s\"", DJI_APP_KEY, requestSignature));
                request.setHeader("Content-Type", "application/json;charset=UTF-8");
                request.setEntity(new StringEntity(payload, StandardCharsets.UTF_8));

                try (CloseableHttpResponse response = httpClient.execute(request)) {
                    String responseString = EntityUtils.toString(response.getEntity());
                    System.out.println("Start Job Response: " + responseString);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
