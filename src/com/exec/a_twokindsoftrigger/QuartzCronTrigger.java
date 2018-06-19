package com.exec.a_twokindsoftrigger;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;


/**
 * Cron任务调度
 *      获取jobDetail对象
 *          jobBuilder.newJob(real_job.class).withIdentity().usingJobData().build()
 *
 *      获取CronTrigger对象
 *          TriggerBuilder.newTrigger()
 *                              .withIdentity()
 *                              .withSchedule( 需要一个ScheduleBuilder对象 通过CronScheduleBuilder类的静态方法cronSchedule("cron表达式")获取)
 *                              .build();
 *
 *
 */
public class QuartzCronTrigger {

    private static SimpleDateFormat sdf = new SimpleDateFormat("hh:MM:ss");

    private static Logger log = LoggerFactory.getLogger(QuartzCronTrigger .class);

    public static void main(String[] args) throws Exception{

        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        log.info("调度任务启动");
        scheduler.start();

        log.info("调度任务空闲5 sec...");
        Thread.currentThread().sleep(5000);
        log.info("调度任务空闲5 sec结束...");

        // 创建job详情类
        JobDetail jobDetail = JobBuilder.newJob().ofType(AMailJob.class)
                                    .withIdentity("my_mail_job","mail")
                                    .usingJobData("email_address","123@321.com")
                                    .build();

        // 创建Cron触发器
        CronTrigger trigger = TriggerBuilder.newTrigger()/*.startNow()*/
                                                .withIdentity("mail_trigger", "mail_trigger_group")
                                                .withSchedule(CronScheduleBuilder.cronSchedule("0/2 * * * * ?"))
                                                .build();
        log.info("job & trigger 都已创建完毕，暂停 2sec");
        Thread.currentThread().sleep(2000);

        log.info("调度任务开始执行");
        scheduler.scheduleJob(jobDetail, trigger);

        Thread.currentThread().sleep(22000);

        scheduler.shutdown(true);
    }

}
