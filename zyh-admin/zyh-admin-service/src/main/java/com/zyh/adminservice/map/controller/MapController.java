package com.zyh.adminservice.map.controller;

import com.zyh.adminapi.feign.map.domain.vo.RegionVO;
import com.zyh.adminapi.feign.map.feigen.MapFeignClient;
import com.zyh.adminservice.map.domain.dto.SysRegionDTO;
import com.zyh.adminservice.map.service.IMapService;
import com.zyh.commoncore.utils.BeanCopyUtil;
import com.zyh.commondomain.domain.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhangyuheng
 */

@RestController
@Slf4j
public class MapController implements MapFeignClient {

    @Autowired
    private IMapService mapService;

    /**
     * 获取区域信息
     * @return 区域列表
     */
    @Override
    public R<List<RegionVO>> getRegionList() {
        // 1. 调用service
        List<SysRegionDTO> list = mapService.getRegionList();

        // 2. 转换成 VO
        List<RegionVO> response = BeanCopyUtil.copyListProperties(list, RegionVO::new);

        // 3. 返回结果
        return R.ok(response);
    }
}
