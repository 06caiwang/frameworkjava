package com.zyh.adminservice.config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.adminapi.config.domain.dto.DictionaryTypeListReqDTO;
import com.zyh.adminapi.config.domain.dto.DictionaryTypeWriteReqDTO;
import com.zyh.adminapi.config.domain.vo.DictionaryTypeVO;
import com.zyh.adminservice.config.domain.entity.SysDictionaryType;
import com.zyh.adminservice.config.mapper.SysDictionaryTypeMapper;
import com.zyh.adminservice.config.service.ISysDictionaryService;
import com.zyh.commondomain.domain.vo.BasePageVO;
import com.zyh.commondomain.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangyuheng
 */
@Service
public class SysDictionaryServiceImpl implements ISysDictionaryService {

    @Autowired
    private SysDictionaryTypeMapper sysDictionaryTypeMapper;

    @Override
    public Long addType(DictionaryTypeWriteReqDTO dictionaryTypeWriteReqDTO) {
        // 构造新增语句 select id from db where value = ? or type_key = ?;
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

    @Override
    public BasePageVO<DictionaryTypeVO> listType(DictionaryTypeListReqDTO dictionaryTypeListReqDTO) {
        BasePageVO<DictionaryTypeVO> result = new BasePageVO<>();
        // 构建查询字典类型列表的 SQL
        // select * from db where type_key = ? and value like '...%'
        LambdaQueryWrapper<SysDictionaryType> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(dictionaryTypeListReqDTO.getTypeKey())) {
            wrapper.eq(SysDictionaryType::getTypeKey, dictionaryTypeListReqDTO.getTypeKey());
        }
        if (StringUtils.isNotBlank(dictionaryTypeListReqDTO.getValue())) {
            wrapper.likeRight(SysDictionaryType::getValue, dictionaryTypeListReqDTO.getValue());
        }
        // 分页查询
        Page<SysDictionaryType> page = sysDictionaryTypeMapper.selectPage(
                new Page<>(dictionaryTypeListReqDTO.getPageNo().longValue(), dictionaryTypeListReqDTO.getPageSize().longValue()),
                wrapper
        );
        // 设置总记录数，页数
        result.setTotals(Integer.parseInt(String.valueOf(page.getTotal())));
        result.setTotalPages(Integer.parseInt(String.valueOf(page.getPages())));
        // 转换查到的结果类型 do -> vo
        List<DictionaryTypeVO> list = new ArrayList<>();
        for (SysDictionaryType sysDictionaryType : page.getRecords()) {
            DictionaryTypeVO dictionaryTypeVO = new DictionaryTypeVO();
            BeanUtils.copyProperties(sysDictionaryType, dictionaryTypeVO);
            list.add(dictionaryTypeVO);
        }
        result.setList(list);

        // 返回结果
        return result;
    }
}
