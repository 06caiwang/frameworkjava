package com.zyh.adminservice.user.controller;

import com.zyh.adminservice.user.service.IAppUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangyuheng
 */
@Slf4j
@RestController
@RequestMapping("/app_user")
public class AppUserController {

    @Resource(name = "appUserServiceImpl")
    private IAppUserService appUserService;


}
