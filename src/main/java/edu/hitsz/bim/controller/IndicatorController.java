package edu.hitsz.bim.controller;

import edu.hitsz.bim.common.BaseController;
import edu.hitsz.bim.common.Response;
import edu.hitsz.bim.domain.dto.CreateIndicatorReq;
import edu.hitsz.bim.domain.dto.ValueIndicatorReq;
import edu.hitsz.bim.entity.Indicator;
import edu.hitsz.bim.service.IndicatorService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * Table for storing various indicators related to projects 前端控制器
 * </p>
 *
 * @author lane
 * @since 2024-04-25 08:59:42
 */
@Slf4j
@RestController
@RequestMapping("/api/indicator")
public class IndicatorController extends BaseController {
    @Resource
    private IndicatorService indicatorService;
    @Override
    protected Logger getLog() {
        return log;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Response<Boolean> createIndicator(@RequestBody CreateIndicatorReq req) {
        return dealWithException(req, indicatorService::create, "IndicatorController");
    }

    @RequestMapping(value = "/list/{project_id}", method = RequestMethod.GET)
    public Response<List<Indicator>> getList(@PathVariable String project_id) {
        return dealWithException(project_id, indicatorService::getList, "IndicatorController");
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public Response<Boolean> delete(@PathVariable String id) {
        return dealWithException(id, indicatorService::delete, "IndicatorController");
    }

    @PostMapping("/value")
    public Response<Boolean> valueList(@RequestParam("req") List<ValueIndicatorReq> req) {
        return dealWithException(req,indicatorService::valueList, "ProjectController");

    }
    @PostMapping("/upload")
    public Response<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("project_id") String project_id) {
        return dealWithException(file, unused -> indicatorService.upload(file, project_id), "ProjectController");

    }
}