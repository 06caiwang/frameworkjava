package com.zyh.adminservice.config.service;

import com.zyh.adminapi.config.domain.dto.DictionaryDataAddReqDTO;
import com.zyh.adminapi.config.domain.dto.DictionaryDataListReqDTO;
import com.zyh.adminapi.config.domain.dto.DictionaryTypeListReqDTO;
import com.zyh.adminapi.config.domain.dto.DictionaryTypeWriteReqDTO;
import com.zyh.adminapi.config.domain.vo.DictionaryDataVO;
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

    /**
     * 编辑字典类型
     * @param dictionaryTypeWriteReqDTO 编辑字典类型DTO
     * @return Long
     */
    Long editType(DictionaryTypeWriteReqDTO dictionaryTypeWriteReqDTO);

    /**
     * 添加字典数据
     * @param dictionaryDataAddReqDTO 添加字典数据DTO
     * @return Long
     */
    Long addData(DictionaryDataAddReqDTO dictionaryDataAddReqDTO);

    /**
     * 查看字典数据列表
     * @param dictionaryDataListReqDTO 字典数据列表DTO
     * @return BasePageVO
     */
    BasePageVO<DictionaryDataVO> listData(DictionaryDataListReqDTO dictionaryDataListReqDTO);
}
