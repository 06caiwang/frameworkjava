package com.zyh.adminservice.map.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.benmanes.caffeine.cache.Cache;
import com.zyh.adminapi.feign.map.constants.MapConstants;
import com.zyh.adminapi.feign.map.domain.vo.RegionVO;
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
import java.util.Map;
import java.util.TreeMap;
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

        // 3. 在服务启动期间，缓存城市拼音归类的列表
        loadCityPinyinList(list);
    }

    /**
     * 缓存城市拼音归类的列表
     * @param list 所有城市列表
     */
    private void loadCityPinyinList(List<SysRegion> list) {
        // 1. 声明一个 map来保存结果
        Map<String, List<SysRegionDTO>> map = new TreeMap<>();

        // 2. 遍历数据库中的结果，对结果进行处理
        for (SysRegion region : list) {
            if (region.getLevel().equals(MapConstants.CITY_LEVEL)) {
                // 类型转换
                SysRegionDTO sysRegionDTO = new SysRegionDTO();
                BeanUtils.copyProperties(region, sysRegionDTO);

                // 获取该城市拼音的首字母
                String firstChar = sysRegionDTO.getPinyin().toUpperCase().substring(0, 1);

                // 将符合首字母的城市进行归类
                // 如果有这个首字母直接加入
                // 否则就添加新的 键值对
                // {"A": [], "B":[], ...}
                if (map.containsKey(firstChar)) {
                    map.get(firstChar).add(sysRegionDTO);
                } else {
                    List<SysRegionDTO> regionDTOS = new ArrayList<>();
                    regionDTOS.add(sysRegionDTO);
                    map.put(firstChar, regionDTOS);
                }
            }
        }

        // 3. 设置缓存
        CacheUtil.setL2Cache(
                redisService,
                MapConstants.CACHE_MAP_CITY_PINYIN_KEY,
                map,
                caffeineCache,
                MapConstants.CACHE_TIMEOUT,
                TimeUnit.MINUTES
        );
    }

    /**
     * 缓存城市列表
     * @param list 所有城市列表
     */
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

    /**
     * 获取城市的拼音排序列表
     * @return 排序好的城市列表
     */
    @Override
    public Map<String, List<SysRegionDTO>> getCityListByPingYin() {
        // 从缓存中获取城市列表
        Map<String, List<SysRegionDTO>> map = CacheUtil.getL2Cache(
                redisService,
                MapConstants.CACHE_MAP_CITY_PINYIN_KEY,
                new TypeReference<Map<String, List<SysRegionDTO>>>() {},
                caffeineCache
        );

        // 返回结果
        return map;
    }

    /**
     * 根据城市id获取城市的区信息
     * @param parentId 城市id
     * @return 城市市区信息
     */
    @Override
    public List<SysRegionDTO> getChildrenCityList(Long parentId) {
        // 1. 设置缓存的key
        String key = MapConstants.CACHE_MAP_CITY_CHILDREN_KEY + parentId;

        // 2. 查询缓存
        List<SysRegionDTO> cache = CacheUtil.getL2Cache(
                redisService,
                key,
                new TypeReference<List<SysRegionDTO>>() {},
                caffeineCache
        );

        // 3. 存在
        if (CollectionUtils.isNotEmpty(cache)) {
            return cache;
        }

        // 4. 不存在，查询数据库
        List<SysRegion> list = regionMapper.selectAllRegion();
        List<SysRegionDTO> result = new ArrayList<>();
        for (SysRegion sysRegion : list) {
            Long id = sysRegion.getParentId();
            if (id != null && id.equals(parentId)) {
                SysRegionDTO sysRegionDTO = new SysRegionDTO();
                BeanUtils.copyProperties(sysRegion, sysRegionDTO);
                result.add(sysRegionDTO);
            }
        }

        // 5. 设置缓存
        CacheUtil.setL2Cache(
                redisService,
                key,
                result,
                caffeineCache,
                MapConstants.CACHE_TIMEOUT,
                TimeUnit.MINUTES
        );

        // 6. 返回
        return result;
    }

    @Override
    public List<SysRegionDTO> getHotCityList() {
        // 1. 查询缓存
        CacheUtil.getL2Cache(
                redisService,
                MapConstants.CACHE_MAP_HOT_CITY,
                new TypeReference<SysRegionDTO>() {},
                caffeineCache
        );

        // 2. 设置六个热门城市
        // todo Mock 6个假数据，后期修改
        List<Long> idList = List.of(1L,2L,3L,4L,5L,6L);

        // 3 查询热门城市结果
        List<SysRegionDTO> list = new ArrayList<>();
        for (SysRegion sysRegion : regionMapper.selectBatchIds(idList)) {
            SysRegionDTO sysRegionDTO = new SysRegionDTO();
            BeanUtils.copyProperties(sysRegion, sysRegionDTO);
            list.add(sysRegionDTO);
        }

        // 4 设置缓存
        CacheUtil.setL2Cache(
                redisService,
                MapConstants.CACHE_MAP_HOT_CITY,
                list, caffeineCache,
                MapConstants.CACHE_TIMEOUT,
                TimeUnit.MINUTES
        );

        // 5. 返回结果
        return list;
    }
}
