package com.zyh.adminservice.config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyh.adminapi.config.domain.dto.DictionaryTypeWriteReqDTO;
import com.zyh.adminservice.config.domain.entity.SysDictionaryType;
import com.zyh.adminservice.config.mapper.SysDictionaryTypeMapper;
import com.zyh.adminservice.config.service.ISysDictionaryService;
import com.zyh.commondomain.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhangyuheng
 */
@Service
public class SysDictionaryServiceImpl implements ISysDictionaryService {

    @Autowired
    private SysDictionaryTypeMapper sysDictionaryTypeMapper;

    @Override
    public Long addType(DictionaryTypeWriteReqDTO dictionaryTypeWriteReqDTO) {
        // 构造新增语句 select id from db where value = ? or type = ?;
        LambdaQueryWrapper<SysDictionaryType> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(SysDictionaryType::getId)
                .eq(SysDictionaryType::getValue, dictionaryTypeWriteReqDTO.getValue())
                .or()
                .eq(SysDictionaryType::getTypeKey, dictionaryTypeWriteReqDTO.getTypeKey());
        // 查询
        SysDictionaryType sysDictionaryType = sysDictionaryTypeMapper.selectOne(wrapper);
        // 判断是否重复
        if (sysDictionaryType != null) {
            throw new ServiceException("字典类型键或者值已存在");
        }
        // 插入操作
        sysDictionaryType = new SysDictionaryType();
        sysDictionaryType.setTypeKey(dictionaryTypeWriteReqDTO.getTypeKey());
        sysDictionaryType.setValue(dictionaryTypeWriteReqDTO.getValue());
        if (StringUtils.isNoneBlank(dictionaryTypeWriteReqDTO.getRemark())) {
            sysDictionaryType.setRemark(dictionaryTypeWriteReqDTO.getRemark());
        }
        int insert = sysDictionaryTypeMapper.insert(sysDictionaryType);
        // 是否插入成功
        if (insert <= 0) {
            throw new ServiceException("字典类型键新增错误");
        }
        // 返回入库中的id
        return sysDictionaryType.getId();
    }
}
