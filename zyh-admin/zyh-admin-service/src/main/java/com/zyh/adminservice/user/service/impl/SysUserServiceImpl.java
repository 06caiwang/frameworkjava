package com.zyh.adminservice.user.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.zyh.adminservice.user.domain.dto.PasswordLoginDTO;
import com.zyh.adminservice.user.domain.entity.SysUser;
import com.zyh.adminservice.user.mapper.SysUserMapper;
import com.zyh.adminservice.user.service.ISysUserService;
import com.zyh.commoncore.utils.AESUtil;
import com.zyh.commoncore.utils.VerifyUtil;
import com.zyh.commondomain.domain.ResultCode;
import com.zyh.commondomain.exception.ServiceException;
import com.zyh.commonsecurity.domain.dto.LoginUserDTO;
import com.zyh.commonsecurity.domain.dto.TokenDTO;
import com.zyh.commonsecurity.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhangyuheng
 */
@Service
public class SysUserServiceImpl implements ISysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private TokenService tokenService;

    @Override
    public TokenDTO login(PasswordLoginDTO passwordLoginDTO) {
        // 需要用户登录的生命周期LoginUserDTO
        LoginUserDTO loginUserDTO = new LoginUserDTO();

        // 校验参数：手机号是否合理
        if (!VerifyUtil.checkPhone(passwordLoginDTO.getPhone())) {
            throw new ServiceException("手机号不合理", ResultCode.INVALID_PARA.getCode());
        }

        // 接着判断手机号是否存在于mysql
        SysUser sysUser = sysUserMapper.selectByPhoneNumber(
                AESUtil.encryptHex(passwordLoginDTO.getPhone()));
        if (sysUser == null) {
            throw new ServiceException("手机号不存在", ResultCode.INVALID_PARA.getCode());
        }

        // 校验密码
        // 先解密
        String password = AESUtil.decryptHex(passwordLoginDTO.getPassword());
        if (StringUtils.isEmpty(password)) {
            throw new ServiceException("密码解析为空", ResultCode.INVALID_PARA.getCode());
        }
        String passwordEncrypt = DigestUtil.sha256Hex(password);

        // 拿加密后的密码与数据库字段比较
        if (!passwordEncrypt.equals(sysUser.getPassword())) {
            throw new ServiceException("密码不正确", ResultCode.INVALID_PARA.getCode());
        }

        // 校验用户状态
        if (sysUser.getStatus().equals("disable")) {
            throw new ServiceException(ResultCode.USER_DISABLE);
        }

        // 设置登录信息
        loginUserDTO.setUserId(sysUser.getId());
        loginUserDTO.setUserName(sysUser.getNickName());
        loginUserDTO.setUserFrom("sys");

        return tokenService.createToken(loginUserDTO);
    }

    public static void main(String[] args) {
        String enPsw = AESUtil.encryptHex("123456");
        String psw = AESUtil.decryptHex(enPsw);
        System.out.println(enPsw + "\n" + psw);
    }
}
