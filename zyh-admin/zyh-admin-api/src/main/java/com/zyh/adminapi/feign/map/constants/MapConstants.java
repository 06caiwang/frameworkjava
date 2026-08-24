package com.zyh.adminapi.feign.map.constants;

/**
 * @author zhangyuheng
 * 地图服务使用到的常量
 */
public class MapConstants {
    /**
     * 城市级别
     */
    public final static Integer CITY_LEVEL = 2;

    /**
     * 城市列表缓存key
     */
    public final static String CACHE_MAP_CITY_KEY = "map:city:id";

    /**
     * 本地缓存有效时间
     */
    public final static Long CACHE_TIMEOUT = 120L;
}
