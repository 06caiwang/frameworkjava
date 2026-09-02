package com.zyh.adminservice.map.service;

import com.zyh.adminservice.map.domain.dto.SysRegionDTO;

import java.util.List;
import java.util.Map;

/**
 * @author zhangyuheng
 */
public interface IMapService {

    /**
     * 获取区域信息
     * @return 区域列表
     */
    List<SysRegionDTO> getRegionList();

    /**
     * 获取城市的拼音排序列表
     * @return 排序好的城市列表
     */
    Map<String, List<SysRegionDTO>> getCityListByPingYin();

    /**
     * 获取城市的区信息
     * @param parentId 城市id
     * @return 城市市区信息
     */
    List<SysRegionDTO> getChildrenCityList(Long parentId);

    /**
     * 获取热门城市列表
     * @return 城市列表
     */
    List<SysRegionDTO> getHotCityList();
}
