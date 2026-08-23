package com.zyh.adminservice.map.service.impl;

import com.zyh.adminapi.feign.map.constants.MapConstants;
import com.zyh.adminservice.map.domain.dto.SysRegionDTO;
import com.zyh.adminservice.map.domain.entity.SysRegion;
import com.zyh.adminservice.map.mapper.RegionMapper;
import com.zyh.adminservice.map.service.IMapService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangyuheng
 */
@Service
public class MapServiceImpl implements IMapService {

    @Autowired
    private RegionMapper regionMapper;

    @Override
    public List<SysRegionDTO> getRegionList() {
        // 1. 获取列表
        List<SysRegion> sysRegions = regionMapper.selectAllRegion();
        // 2. 转换成 DTO
        List<SysRegionDTO> sysRegionDTOS = new ArrayList<>();

        for (SysRegion sysRegion:sysRegions) {
            if (sysRegion.getLevel().equals(MapConstants.CITY_LEVEL)) {
                SysRegionDTO sysRegionDTO = new SysRegionDTO();
                BeanUtils.copyProperties(sysRegion, sysRegionDTO);
                sysRegionDTOS.add(sysRegionDTO);
            }
        }

        return sysRegionDTOS;
    }
}
