package com.zyh.adminservice.user.service.impl;

import com.zyh.adminservice.user.mapper.SysUserMapper;
import com.zyh.adminservice.user.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhangyuheng
 */
@Service
public class SysUserServiceImpl implements ISysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;


}
