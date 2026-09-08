package com.zyh.adminservice.config.service;

import com.zyh.adminapi.config.domain.dto.DictionaryTypeListReqDTO;
import com.zyh.adminapi.config.domain.dto.DictionaryTypeWriteReqDTO;
import com.zyh.adminapi.config.domain.vo.DictionaryTypeVO;
import com.zyh.commondomain.domain.vo.BasePageVO;

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

    /**
     * 字典类型列表
     * @param dictionaryTypeListReqDTO 字典类型列表DTO
     * @return BasePageVO
     */
    BasePageVO<DictionaryTypeVO> listType(DictionaryTypeListReqDTO dictionaryTypeListReqDTO);
}
