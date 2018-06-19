package com.exec.c_disallowconcurrentexecutiontest;

import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuartzCronTrigger {

    private static Logger log = LoggerFactory.getLogger(QuartzCronTrigger.class);


    public static void main(String[] args) throws Exception{

        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        scheduler.start();

        JobDetail detail = new JobDetailImpl();
        // 创建job详情类
        JobDetail jobDetail = JobBuilder.newJob(DisallowConcurrentJob.class)
                                        .withIdentity("db_backup_job", "back_up")
                                        .usingJobData("dataBase","loacl_db")
                                        .build();
        // 创建Trigger 触发器类
        Trigger trigger = TriggerBuilder.newTrigger()
                                        .withIdentity("back_up_trigger","back_up")
                                        .withSchedule(CronScheduleBuilder.cronSchedule("0/1 * * * * ?"))
                                        .build();

        scheduler.scheduleJob(jobDetail, trigger);

        Thread.currentThread().sleep(8000);

        scheduler.shutdown();
    }

}
