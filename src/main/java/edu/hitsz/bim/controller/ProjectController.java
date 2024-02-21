package edu.hitsz.bim.controller;

import edu.hitsz.bim.common.BaseController;
import edu.hitsz.bim.common.Response;
import edu.hitsz.bim.domain.dto.CreateProjectReq;
import edu.hitsz.bim.domain.vo.ProjectVO;
import edu.hitsz.bim.entity.Project;
import edu.hitsz.bim.service.ProjectService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
@Controller
@RequestMapping("/project")
public class ProjectController extends BaseController {
    @Resource
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
    public Response<Boolean> createProject(@RequestBody CreateProjectReq req) {
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
}
