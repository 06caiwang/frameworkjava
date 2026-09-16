package com.zyh.adminservice.user.service.impl;

import com.zyh.adminapi.appuser.domain.dto.AppUserDTO;
import com.zyh.adminapi.appuser.domain.dto.UserEditReqDTO;
import com.zyh.adminservice.user.config.RabbitConfig;
import com.zyh.adminservice.user.domain.entity.AppUser;
import com.zyh.adminservice.user.mapper.AppUserMapper;
import com.zyh.adminservice.user.service.IAppUserService;
import com.zyh.commoncore.utils.AESUtil;
import com.zyh.commondomain.domain.ResultCode;
import com.zyh.commondomain.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author zhangyuheng
 */
@Slf4j
@Service
public class AppUserServiceImpl implements IAppUserService {

    @Autowired
    private AppUserMapper appUserMapper;

    @Value("${appuser.info.defaultAvatar}")
    private String defaultAvatar;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 根据微信ID注册用户
     * @param openId 微信ID
     * @return C端用户DTO
     */
    @Override
    public AppUserDTO registerByOpenId(String openId) {
        if (StringUtils.isEmpty(openId)) {
            throw new ServiceException("微信ID不能为空", ResultCode.INVALID_PARA.getCode());
        }
        AppUser appUser = new AppUser();
        appUser.setOpenId(openId);
        appUser.setNickName("用户"+ (int) (Math.random() * 9000) + 1000);
        appUser.setAvatar(defaultAvatar);
        appUserMapper.insert(appUser);
        AppUserDTO appUserDTO = new AppUserDTO();
        BeanUtils.copyProperties(appUser, appUserDTO);
        appUserDTO.setUserId(appUser.getId());
        
        return appUserDTO;
    }

    /**
     * 根据openId查询用户信息
     * @param openId 用户微信ID
     * @return C端用户DTO
     */
    @Override
    public AppUserDTO findByOpenId(String openId) {
        if (StringUtils.isEmpty(openId)) {
            return null;
        }

        // 1 查询appUser实体类
        AppUser appUser = appUserMapper.selectByOpenId(openId);

        // 2 对查出来的结果进行判断
        if (appUser == null) {
            return null;
        }
        
        AppUserDTO appUserDTO = new AppUserDTO();
        BeanUtils.copyProperties(appUser, appUserDTO);
        
        // 3 处理邮箱号
        appUserDTO.setPhoneNumber(AESUtil.decryptHex(appUser.getPhoneNumber()));
        return appUserDTO;
    }

    /**
     * 根据邮箱号查询用户信息
     * @param mail 邮箱号
     * @return C端用户DTO
     */
    @Override
    public AppUserDTO findByMail(String mail) {
        // 1 对入参判空
        if (StringUtils.isEmpty(mail)) {
            return null;
        }
        
        // 2 执行查询语句
        AppUser appUser = appUserMapper.selectByMail(mail);
        if (appUser == null) {
            return null;
        }
        
        // 3 对查出来的结果进行类型转换
        AppUserDTO appUserDTO = new AppUserDTO();
        BeanUtils.copyProperties(appUser, appUserDTO);
        appUserDTO.setUserId(appUser.getId());
        return appUserDTO;
    }

    /**
     * 根据邮箱注册用户
     * @param mail 邮箱
     * @return C端用户DTO
     */
    @Override
    public AppUserDTO registerByMail(String mail) {
        // 1 对邮箱进行判断
        if (StringUtils.isEmpty(mail)) {
            throw new ServiceException("要注册邮箱是空的", ResultCode.INVALID_PARA.getCode());
        }

        // 2. 查询是否存存在该邮箱
        AppUser cache = appUserMapper.selectByMail(mail);
        if (cache != null) {
            throw new ServiceException("该邮箱已注册，请勿重复注册", ResultCode.MAIL_EXISTS.getCode());
        }

        // 3. 生成用户对象
        AppUser appUser = new AppUser();
        appUser.setNickName("用户"+ (int) (Math.random() * 9000) + 1000);
        appUser.setMail(mail);
        appUser.setAvatar(defaultAvatar);
        appUserMapper.insert(appUser);
        AppUserDTO appUserDTO = new AppUserDTO();
        BeanUtils.copyProperties(appUser, appUserDTO);
        appUserDTO.setUserId(appUser.getId());
        return appUserDTO;
    }

    @Override
    public void edit(UserEditReqDTO userEditReqDTO) {
        // 1 根据ID查询要编辑的用户
        AppUser appUser = appUserMapper.selectById(userEditReqDTO.getUserId());
        if (appUser == null) {
            throw new ServiceException("用户不存在", ResultCode.INVALID_PARA.getCode());
        }
        // 2 查到用户，进行编辑操作
        appUser.setNickName(userEditReqDTO.getNickName());
        appUser.setAvatar(userEditReqDTO.getAvtar());
        appUserMapper.updateById(appUser);
        // 3 发送广播消息
        AppUserDTO appUserDTO = new AppUserDTO();
        BeanUtils.copyProperties(appUser, appUserDTO);
        appUserDTO.setUserId(appUser.getId());
        try {
            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, "", appUserDTO);
        } catch (Exception exception) {
            log.error("编辑用户发送消息失败", exception);
        }
    }
}
