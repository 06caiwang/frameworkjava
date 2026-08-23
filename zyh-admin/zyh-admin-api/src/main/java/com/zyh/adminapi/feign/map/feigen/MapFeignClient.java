package com.zyh.adminapi.feign.map.feigen;

import com.zyh.adminapi.feign.map.domain.vo.RegionVO;
import com.zyh.commondomain.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author zhangyuheng
 * 地图服务远程调用
 */
@FeignClient(contextId = "mapFeignClient", value = "zyh-admin")
public interface MapFeignClient {

    /**
     * 获取所有区域信息
     * @return 区域列表
     */
    @GetMapping("/map/city_list")
    public R<List<RegionVO>> getRegionList();


}
