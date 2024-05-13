
package edu.hitsz.bim.service;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author lane
 * @description
 * @since 2024/2/26 23:24
 */
@SpringBootTest
class DJIHandlerTest {

    @Resource
    private DJIHandler djiHandler;

    @Resource
    private DynamicScheduler scheduler;

    @Resource
    DynamicPollingService service;
    @Test
    void getToken() {
        djiHandler.createResource("test");
    }

    @Test
    void run() {
        String path = "/Users/deequoique/Downloads/Three-Dimension-3D/3cm-3D-orthomosaic-70pic";

        djiHandler.run(path);
    }

    @Test
    void test() {
        scheduler.startPolling(" 000190ed-873d-441e-85c2-e3f08540a497");

    }

    @Test
    void download() {
        service.getFileUuid("1b43fd59-bd3c-4fb5-8bf1-ae981e4164c5");
    }

    @Test
    void getResource() {
        djiHandler.getResourseDetails("3c66adea-b64d-4f72-92ac-460a9104eb0a");
    }
}