package com.zyh.adminservice.map.service;

import com.zyh.adminservice.map.domain.dto.SysRegionDTO;

import java.util.List;

/**
 * @author zhangyuheng
 */
public interface IMapService {

    /**
     * 获取区域信息
     * @return 区域列表
     */
    List<SysRegionDTO> getRegionList();
}
