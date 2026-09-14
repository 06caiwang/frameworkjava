package com.zyh.adminservice.user.service.impl;

import com.zyh.adminservice.user.mapper.AppUserMapper;
import com.zyh.adminservice.user.service.IAppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhangyuheng
 */
@Service
public class AppUserServiceImpl implements IAppUserService {

    @Autowired
    private AppUserMapper appUserMapper;

}
