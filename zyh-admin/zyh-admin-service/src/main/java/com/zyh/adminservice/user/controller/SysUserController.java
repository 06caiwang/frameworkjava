package com.zyh.adminservice.user.controller;

import com.zyh.adminservice.user.service.ISysUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangyuheng
 */
@Slf4j
@RestController
@RequestMapping("/sys_user")
public class SysUserController {

    @Resource(name = "sysUserServiceImpl")
    private ISysUserService sysUserService;

}
