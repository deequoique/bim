package edu.hitsz.bim.controller;

import edu.hitsz.bim.common.BaseController;
import edu.hitsz.bim.common.Response;
import edu.hitsz.bim.domain.dto.CreateGlassReq;
import edu.hitsz.bim.domain.dto.CreateRecordReq;
import edu.hitsz.bim.entity.Glass;
import edu.hitsz.bim.service.GlassService;
import edu.hitsz.bim.service.RecordService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 * Table for storing glass information 前端控制器
 * </p>
 *
 * @author lane
 * @since 2024-02-02 01:47:27
 */
@Slf4j
@RestController
@RequestMapping("/api/glass")
public class GlassController extends BaseController {

    @Resource
    private GlassService glassService;

    @Override
    protected Logger getLog() {
        return log;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Response<Boolean> create(@RequestBody CreateGlassReq req) {
        return dealWithException(req, glassService::create, "RecordController");
    }

    @RequestMapping(value = "/list/{project_id}", method = RequestMethod.GET)
    public Response<List<Glass>> getList(@PathVariable String project_id) {
        return dealWithException(project_id, glassService::getList, "RecordController");
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public Response<Boolean> delete(@PathVariable String id) {
        return dealWithException(id, glassService::delete, "RecordController");
    }

}
