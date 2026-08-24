package com.zyh.adminservice.map.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.benmanes.caffeine.cache.Cache;
import com.zyh.adminapi.feign.map.constants.MapConstants;
import com.zyh.adminservice.map.domain.dto.SysRegionDTO;
import com.zyh.adminservice.map.domain.entity.SysRegion;
import com.zyh.adminservice.map.mapper.RegionMapper;
import com.zyh.adminservice.map.service.IMapService;
import com.zyh.commmoncache.utils.CacheUtil;
import com.zyh.commonredis.service.RedisService;
import jakarta.annotation.PostConstruct;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangyuheng
 */
@Service
public class MapServiceImpl implements IMapService {

    @Autowired
    private RegionMapper regionMapper;

    /**
     * redis服务
     */
    @Autowired
    private RedisService redisService;

    /**
     * 本地缓存服务
     */
    @Autowired
    private Cache<String, Object> caffeineCache;

    @PostConstruct
    public void initCityList() {
        // 1. 直接查询数据库
        List<SysRegion> list = regionMapper.selectAllRegion();

        // 2. 在服务启动期间，缓存城市列表
        loadCityList(list);
    }

    private void loadCityList(List<SysRegion> list) {
        // 声明对象
        List<SysRegionDTO> sysRegionDTOS = new ArrayList<>();

        // 对象转换
        for (SysRegion sysRegion:list) {
            if (sysRegion.getLevel().equals(MapConstants.CITY_LEVEL)) {
                SysRegionDTO sysRegionDTO = new SysRegionDTO();
                BeanUtils.copyProperties(sysRegion, sysRegionDTO);
                sysRegionDTOS.add(sysRegionDTO);
            }
        }

        // 设置缓存
        CacheUtil.setL2Cache(
                redisService,
                MapConstants.CACHE_MAP_CITY_KEY,
                list, caffeineCache,
                MapConstants.CACHE_TIMEOUT,
                TimeUnit.MINUTES
        );
    }

    /**
     * 获取区域信息 v1 -> 直接访问数据库
     * @return 区域列表
     */
    public List<SysRegionDTO> getRegionListV1() {
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

    /**
     * 获取区域信息 v2 -> 访问redis
     * @return 区域列表
     */
    public List<SysRegionDTO> getRegionListV2() {
        // 1. 声明空列表
        List<SysRegionDTO> list = new ArrayList<>();

        // 2. 查询redis中的数据
        List<SysRegionDTO> cache = redisService.getCacheList(MapConstants.CACHE_MAP_CITY_KEY, new TypeReference<List<SysRegionDTO>>() {});

        // 3.如果存在，直接返回
        //   如果不存在，访问数据库，然后存入redis
        if (CollectionUtils.isNotEmpty(cache)) {
            return cache;
        }

        List<SysRegion> sysRegions = regionMapper.selectAllRegion();

        for (SysRegion sysRegion:sysRegions) {
            if (sysRegion.getLevel().equals(MapConstants.CITY_LEVEL)) {
                SysRegionDTO sysRegionDTO = new SysRegionDTO();
                BeanUtils.copyProperties(sysRegion, sysRegionDTO);
                list.add(sysRegionDTO);
            }
        }

        redisService.setCacheList(MapConstants.CACHE_MAP_CITY_KEY, list);

        return list;
    }

    /**
     * 获取区域信息 v3 -> 访问本地缓存，二级缓存方案
     * @return 区域列表
     */
    public List<SysRegionDTO> getRegionListV3() {
        // 1. 先进行缓存查询
        List<SysRegionDTO> cache = CacheUtil.getL2Cache(redisService, MapConstants.CACHE_MAP_CITY_KEY, new TypeReference<List<SysRegionDTO>>() {
        }, caffeineCache);

        // 2.如果存在，直接返回
        //   如果不存在，访问数据库，然后存入redis
        if (CollectionUtils.isNotEmpty(cache)) {
            return cache;
        }

        List<SysRegion> sysRegions = regionMapper.selectAllRegion();
        List<SysRegionDTO> list = new ArrayList<>();

        for (SysRegion sysRegion:sysRegions) {
            if (sysRegion.getLevel().equals(MapConstants.CITY_LEVEL)) {
                SysRegionDTO sysRegionDTO = new SysRegionDTO();
                BeanUtils.copyProperties(sysRegion, sysRegionDTO);
                list.add(sysRegionDTO);
            }
        }

        CacheUtil.setL2Cache(
                redisService,
                MapConstants.CACHE_MAP_CITY_KEY,
                list, caffeineCache,
                MapConstants.CACHE_TIMEOUT,
                TimeUnit.MINUTES
        );

        return list;
    }

    /**
     * 获取区域信息 v4 -> 缓存预热方案
     * @return 区域列表
     */
    @Override
    public List<SysRegionDTO> getRegionList() {
        List<SysRegionDTO> cache = CacheUtil.getL2Cache(
                redisService,
                MapConstants.CACHE_MAP_CITY_KEY,
                new TypeReference<List<SysRegionDTO>>() {},
                caffeineCache);
        return cache;
    }
}
