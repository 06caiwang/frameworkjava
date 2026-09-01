package com.zyh.adminservice.map.controller;

import com.zyh.adminapi.feign.map.domain.vo.RegionVO;
import com.zyh.adminapi.feign.map.feigen.MapFeignClient;
import com.zyh.adminservice.map.domain.dto.SysRegionDTO;
import com.zyh.adminservice.map.service.IMapService;
import com.zyh.commoncore.utils.BeanCopyUtil;
import com.zyh.commondomain.domain.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.ls.LSInput;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * 获取城市的拼音排序列表
     * @return 排序好的城市列表
     */
    @Override
    public R<Map<String, List<RegionVO>>> getCityListByPingYin() {
        // 1. 打印日志
        log.info("getCityListByPingYin: NoArgs");

        // 2. 调用service
        Map<String, List<SysRegionDTO>> map = mapService.getCityListByPingYin();

        // 3. 转换VO
        Map<String, List<RegionVO>> result = new LinkedHashMap<>();
        for (Map.Entry<String, List<SysRegionDTO>> region : map.entrySet()) {
            result.put(
                    region.getKey(),
                    BeanCopyUtil.copyListProperties(region.getValue(), RegionVO::new)
            );
        }

        //4. 返回结果
        return R.ok(result);
    }
}
