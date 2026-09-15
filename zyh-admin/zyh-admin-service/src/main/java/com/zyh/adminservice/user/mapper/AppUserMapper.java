package com.zyh.adminservice.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyh.adminservice.user.domain.entity.AppUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author zhangyuheng
 */
@Mapper
public interface AppUserMapper extends BaseMapper<AppUser> {
    /**
     * 根据openId查询用户信息
     * @param openId 用户微信ID
     * @return C端用户
     */
    AppUser selectByOpenId(@Param("openId") String openId);

    /**
     * 根据邮箱查询用户信息
     * @param mail 邮箱
     * @return C端用户
     */
    AppUser selectByMail(@Param("mail") String mail);
}
