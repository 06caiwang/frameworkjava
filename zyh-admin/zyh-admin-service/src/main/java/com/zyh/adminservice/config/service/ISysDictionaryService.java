package com.zyh.adminservice.config.service;

import com.zyh.adminapi.config.domain.dto.DictionaryTypeWriteReqDTO;

/**
 * @author zhangyuheng
 */

public interface ISysDictionaryService {
    /**
     * 新增字典类型
     * @param dictionaryTypeWriteReqDTO 新增字典类型DTO
     * @return Long
     */
    Long addType(DictionaryTypeWriteReqDTO dictionaryTypeWriteReqDTO);
}
