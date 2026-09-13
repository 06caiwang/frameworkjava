package com.zyh.adminservice.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyh.adminservice.user.domain.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author zhangyuheng
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUserMapper> {
    /**
     * B端用户登录
     *
     * @param phoneNumber 手机号码
     * @return token信息
     */
    SysUser selectByPhoneNumber(@Param("phoneNumber") String phoneNumber);
}
