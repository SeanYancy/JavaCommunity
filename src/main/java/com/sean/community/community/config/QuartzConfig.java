package com.sean.community.community.config;

import com.sean.community.community.quartz.AlphaJob;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

@Configuration
public class QuartzConfig {

    // FactoryBean 可简化Bean的实例化过程：
    // 1. 通过FactoryBean封装了Bean的实例化过程
    // 2. 将FactoryBean装配到Spring容器中
    // 3. 将FactoryBean注入到其他Bean
    // 4， 该Bean得到的是FactoryBean所管理的对象实例


    // 配置JobDetail
    @Bean
    public JobDetailFactoryBean alphaJobDetail() {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(AlphaJob.class);
        factoryBean.setName("alphaJob");
        factoryBean.setGroup("alphaJobGroup");
        factoryBean.setDurability(true); // 长久保存
        factoryBean.setRequestsRecovery(true);
        return factoryBean;
    }


    // 配置Trigger（SimpleTriggerFactoryBean间隔多少时间，CronTriggerFactoryBean指定日期）
    @Bean
    public SimpleTriggerFactoryBean alphaTrigger(JobDetail alphaJobDetail) { // 类型相同会找同名注入
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(alphaJobDetail);
        factoryBean.setName("alphaTrigger");
        factoryBean.setGroup("alphaTriggerGroup");
        factoryBean.setRepeatInterval(3000);
        factoryBean.setJobDataMap(new JobDataMap()); // 存Job状态的对象

        return factoryBean;
    }
}
