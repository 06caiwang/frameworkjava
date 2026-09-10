package com.zyh.adminapi.config.feign;

import com.zyh.adminapi.config.domain.dto.DictionaryDataDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author zhangyuheng
 */
@FeignClient(contextId = "dictionaryFeignClient", value = "zyh-admin")
public interface DictionaryFeignClient {

    /**
     * 获取某个字典类型下的所有字典数据
     * @param typeKey 字典类型键
     * @return 字典数据列表
     */
    @GetMapping("/dictionary_data/type")
    List<DictionaryDataDTO> selectDictDataByType(@RequestParam String typeKey);


}
