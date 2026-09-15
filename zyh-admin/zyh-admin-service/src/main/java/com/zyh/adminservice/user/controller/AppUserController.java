package com.zyh.adminservice.user.controller;

import com.zyh.adminapi.appuser.domain.dto.AppUserDTO;
import com.zyh.adminapi.appuser.domain.vo.AppUserVO;
import com.zyh.adminapi.appuser.feign.AppUserFeignClient;
import com.zyh.adminservice.user.service.IAppUserService;
import com.zyh.commondomain.domain.R;
import com.zyh.commondomain.exception.ServiceException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangyuheng
 */
@Slf4j
@RestController
@RequestMapping("/app_user")
public class AppUserController implements AppUserFeignClient {

    @Resource(name = "appUserServiceImpl")
    private IAppUserService appUserService;

    /**
     * 根据微信注册用户
     * @param openId 用户微信ID
     * @return C端用户VO
     */
    @Override
    public R<AppUserVO> registerByOpenId(String openId) {
        return R.ok(appUserService.registerByOpenId(openId).convertToVO());
    }

    /**
     * 根据openId查询用户信息
     * @param openId 用户微信ID
     * @return C端用户VO
     */
    @Override
    public R<AppUserVO> findByOpenId(String openId) {
        AppUserDTO appUserDTO = appUserService.findByOpenId(openId);
        if (appUserDTO == null) {
            return R.ok();
        }
        return R.ok(appUserDTO.convertToVO());
    }

    /**
     * 根据邮箱查询用户信息
     * @param mail 邮箱
     * @return C端用户VO
     */
    @Override
    public R<AppUserVO> findByMail(String mail) {
        AppUserDTO appUserDTO = appUserService.findByMail(mail);
        if (appUserDTO == null) {
            return R.ok();
        }
        return R.ok(appUserDTO.convertToVO());
    }

    /**
     * 根据邮箱注册用户
     * @param mail 邮箱
     * @return C端用户VO
     */
    @Override
    public R<AppUserVO> registerByMail(String mail) {
        AppUserDTO appUserDTO = appUserService.registerByMail(mail);
        if (appUserDTO == null) {
            throw new ServiceException("注册失败");
        }
        return R.ok(appUserDTO.convertToVO());
    }
}
