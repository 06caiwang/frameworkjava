package com.zyh.adminapi.config.feign;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author zhangyuheng
 */
@FeignClient(contextId = "dictionaryFeignClient", value = "zyh-admin")
public class DictionaryFeignClient {

}
