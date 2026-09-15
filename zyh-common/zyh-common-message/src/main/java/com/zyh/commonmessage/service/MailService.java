package com.zyh.commonmessage.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangyuheng
 */
@Slf4j
@Component
public class MailService {

    /**
     * 官方名称
     */
    @Value(value = "${zyh.mail.from:}")
    private String from;

    /**
     * 是否发送线上短信
     */
    @Value("${zyh.mail.send-message:false}")
    private boolean sendMessage;

    @Resource
    private JavaMailSender mailSender;


    /**
     * 发邮件模版
     *
     * @param to:  目标邮箱地址
     * @param subject： 标题
     * @param context： 正文
     * @return 是否发送成功
     */
    public Boolean sendMessage(String to, String subject, String context) {
        // 创建邮件发送请求
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(context);
        try {
            mailSender.send(message);
        } catch (Exception e) {
            log.error("向{}发送邮件失败！", to, e);
            return false;
        }
        return true;
    }
}
