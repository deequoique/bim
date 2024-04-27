package edu.hitsz.bim.controller;

import edu.hitsz.bim.common.BaseController;
import edu.hitsz.bim.common.Response;
import edu.hitsz.bim.domain.dto.CreateProjectReq;
import edu.hitsz.bim.domain.dto.SecurityReq;
import edu.hitsz.bim.domain.vo.ProjectVO;
import edu.hitsz.bim.entity.Project;
import edu.hitsz.bim.service.ProjectService;
import org.springframework.core.io.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * <p>
 * Table for storing project information 前端控制器
 * </p>
 *
 * @author lane
 * @since 2024-02-02 01:47:27
 */
@Slf4j
@RestController
@RequestMapping("/api/project")
public class ProjectController extends BaseController {
    @jakarta.annotation.Resource
    private ProjectService projectService;

    @Override
    protected Logger getLog() {
        return log;
    }

    @RequestMapping(value = "/user/{user_id}", method = RequestMethod.GET)
    public Response<List<Project>> findByUser(@PathVariable String user_id) {
        return dealWithException(user_id, projectService::findByUser, "ProjectController");
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Response<String> createProject(@RequestBody CreateProjectReq req) {
        return dealWithException(req, projectService::create, "ProjectController");
    }

    @RequestMapping(value = "/details/{project_id}", method = RequestMethod.GET)
    public Response<ProjectVO> getDetails(@PathVariable String project_id) {
        return dealWithException(project_id, projectService::details, "ProjectController");
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public Response<Boolean> delete(@PathVariable String id) {
        return dealWithException(id, projectService::delete, "ProjectController");
    }

    @PostMapping("/upload")
    public Response<String> uploadFile(@RequestParam("file") MultipartFile file) {
        return dealWithException(file, projectService::upload, "ProjectController");

    }

    @GetMapping("/download/report/{project_id}")
    public ResponseEntity<Resource> downloadReport(HttpServletResponse response, @PathVariable String project_id) {
        return projectService.downloadReport(response, project_id);
    }

    @PostMapping("/result")
    public Response<Boolean> securityResult(@RequestBody SecurityReq req) {
        return dealWithException(req,projectService::securityResult,"ProjectController");
    }

    @PostMapping("/update")
    public Response<Boolean> updateProject(@RequestBody Project project) {
        return dealWithException(project, projectService::updateById, "ProjectController");
    }

    @RequestMapping(value = "/delete/report/{id}", method = RequestMethod.GET)
    public Response<Boolean> deleteReport(@PathVariable String id) {
        return dealWithException(id, projectService::deleteReportById, "ProjectController");
    }
}