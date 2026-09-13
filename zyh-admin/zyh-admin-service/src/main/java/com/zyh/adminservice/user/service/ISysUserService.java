package com.zyh.adminservice.user.service;

import com.zyh.adminservice.user.domain.dto.PasswordLoginDTO;
import com.zyh.commonsecurity.domain.dto.TokenDTO;

/**
 * @author zhangyuheng
 */
public interface ISysUserService {
    /**
     * B端用户账户密码登录
     *
     * @param passwordLoginDTO 用户登录DTO
     * @return tokenDTO token信息
     */
    TokenDTO login(PasswordLoginDTO passwordLoginDTO);

}
