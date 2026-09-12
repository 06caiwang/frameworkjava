package com.zyh.commondomain.constants;

import lombok.Data;

/**
 * @author zhangyuheng
 */
@Data
public class CacheConstants {
    /**
     * 缓存分割符
     */
    public final static String CACHE_SPLIT_COLON = ":";

    /**
     * 缓存有效期，默认7（天）
     */
    public final static long EXPIRATION = 7; // todo 修改缓存有效期

    /**
     * 缓存刷新时间，默认120（分钟）
     */
    public final static long REFRESH_TIME = 120;
}
