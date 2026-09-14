package com.zyh.adminservice.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyh.adminservice.user.domain.entity.AppUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zhangyuheng
 */
@Mapper
public interface AppUserMapper extends BaseMapper<AppUser> {

}
