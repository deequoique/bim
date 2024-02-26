package edu.hitsz.bim.controller;

import edu.hitsz.bim.common.BaseController;
import edu.hitsz.bim.common.Response;
import edu.hitsz.bim.domain.dto.CreateRecordReq;
import edu.hitsz.bim.domain.dto.CreateUserReq;
import edu.hitsz.bim.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * Table for storing user information 前端控制器
 * </p>
 *
 * @author lane
 * @since 2024-02-02 01:32:10
 */
@Slf4j
@RestController
@RequestMapping("")
public class UserController extends BaseController {

    @Resource
    private UserService userService;
    @Override
    protected Logger getLog() {
        return log;
    }
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Response<Boolean> create(@RequestBody CreateUserReq req) {
        return dealWithException(req, userService::create, "RecordController");
    }


}
