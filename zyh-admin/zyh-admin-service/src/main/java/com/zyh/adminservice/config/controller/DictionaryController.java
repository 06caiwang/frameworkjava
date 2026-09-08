package com.zyh.adminservice.config.controller;

import com.zyh.adminapi.config.domain.dto.DictionaryTypeWriteReqDTO;
import com.zyh.adminapi.config.feign.DictionaryFeignClient;
import com.zyh.adminservice.config.service.ISysDictionaryService;
import com.zyh.commoncore.utils.JsonUtil;
import com.zyh.commondomain.domain.R;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangyuheng
 */
@Slf4j
@RestController
public class DictionaryController extends DictionaryFeignClient {

    @Resource(name = "sysDictionaryServiceImpl")
    private ISysDictionaryService iSysDictionaryService;

    /**
     * 新增字典类型
     * @param dictionaryTypeWriteReqDTO 新增字典类型DTO
     * @return Long
     */
    @PostMapping("/dictionary_type/add")
    public R<Long> addType(@RequestBody @Validated DictionaryTypeWriteReqDTO dictionaryTypeWriteReqDTO) {
        // 打印日志
        log.info("addType DictionaryTypeWriteReqDTO: {}", JsonUtil.obj2String(dictionaryTypeWriteReqDTO));
        // 调用service
        Long response = iSysDictionaryService.addType(dictionaryTypeWriteReqDTO);
        // 返回结果
        return R.ok(response);
    }


}
