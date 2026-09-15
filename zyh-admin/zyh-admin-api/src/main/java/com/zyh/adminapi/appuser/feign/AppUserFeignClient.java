package com.zyh.adminapi.appuser.feign;

import com.zyh.adminapi.appuser.domain.vo.AppUserVO;
import com.zyh.commondomain.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zhangyuheng
 */
@FeignClient(contextId = "appUserFeignClient", value = "zyh-admin", path = "/app_user")
public interface AppUserFeignClient {
    /**
     * 根据微信注册用户
     * @param openId 用户微信ID
     * @return C端用户VO
     */
    @PostMapping("/register/openid")
    R<AppUserVO> registerByOpenId(@RequestParam String openId);

    /**
     * 根据openId查询用户信息
     * @param openId 用户微信ID
     * @return C端用户VO
     */
    @GetMapping("/open_id_find")
    R<AppUserVO> findByOpenId(@RequestParam String openId);

    /**
     * 根据邮箱注册用户
     * @param mail 邮箱
     * @return C端用户VO
     */
    @PostMapping("/register/mail")
    R<AppUserVO> registerByMail(@RequestParam String mail);

    /**
     * 根据邮箱查询用户信息
     * @param mail 邮箱
     * @return C端用户VO
     */
    @GetMapping("/mail_find")
    R<AppUserVO> findByMail(@RequestParam String mail);
}
