package com.itxin.web.scheduletask;

import com.itxin.dao.StatisticMapper;
import com.itxin.utils.MailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//定时任务管理
@Component
public class ScheduleTask {
    @Autowired
    private StatisticMapper statisticMapper;
    @Autowired
    private MailUtils mailUtils;
    @Value("${spring.mail.username}")
    private String mailto;
    //定时邮件发送任务，每小时0分发送邮件
    @Scheduled(cron = "0 0 * * * ?")
    public void sendEmail(){
        //定制邮件内容
        long totalComment=statisticMapper.getTotalComment();
        long totalVisit = statisticMapper.getTotalVisit();
        StringBuffer content = new StringBuffer();
        content.append("博客系统总访问量为："+totalVisit+"人次").append("\n");
        content.append("博客系统总评论量为："+totalComment+"人次").append("\n");
        mailUtils.sendSimpleEmail(mailto,"个人博客系统流量统计情况",content.toString());
    }
}
