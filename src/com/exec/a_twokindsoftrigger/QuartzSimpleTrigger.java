package com.exec.a_twokindsoftrigger;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 简单任务调度
 *      jobDetail : 任务详情
 *          JobBuilder.newJob().ofType().withIdentity().usingJobData().build();
 *          JobBuilder.newJob(Real_job.class).withIdentity().usingJobData().build();
 *
 *      trigger : 触发器
 *          TriggerBuilder.newTrigger()
 *                          .withIdentity()
 *                          .withSchedule(SimpleScheduleBuilder的静态方法simpleSchedule()获取 ScheduleBuilder对象并设置次数和间隔时间)
 *                          .build();
 */
public class QuartzSimpleTrigger {

    private static SimpleDateFormat sdf = new SimpleDateFormat("hh:MM:ss");

    private static Logger log = LoggerFactory.getLogger(QuartzSimpleTrigger.class);

    public static void main(String[] args) {

        Scheduler scheduler = null;
        Trigger trigger = null;

        try {
            // 创建任务调度器
            scheduler = StdSchedulerFactory.getDefaultScheduler();

            // 创建任务详情类
            JobDetail jobDetail = JobBuilder.newJob(AMailJob.class) // 使用反射创 job对象
                                            .withIdentity("email","group_01") // job标识
                                            .usingJobData("email_address","123@321.com") // 可以在创建job对象的时候往jobDataMap 设置键值
                                            .build();

            // 创建触发器类
            log.info("触发器创建时间："+sdf.format(new Date(System.currentTimeMillis())));

            trigger = TriggerBuilder.newTrigger().startNow()
                                    .withIdentity("email_job_trigger","group_01")
                                    .withSchedule(/*需要一个ScheduleBuilder对象*/SimpleScheduleBuilder.simpleSchedule() // static method 返回一个简单任务调度建造器对象 simpleScheduleBuilder
                                                    .withIntervalInSeconds(2) // 建造者模式，对建造器对象进行配置
                                                    .withRepeatCount(10))
                                    .build();



            // 将任务详情jobDetail 以及触发器trigger 添加到任务调度中
            Date date = scheduler.scheduleJob(jobDetail, trigger);
            log.info("任务详情加入到调度表中时间" + sdf.format(date));
            log.info("任务详情加入到调度表中时间" + sdf.format(new Date(System.currentTimeMillis())));

            Thread.currentThread().sleep(4000);

            scheduler.start();
            log.info("调度表开始运行时间" + sdf.format(new Date(System.currentTimeMillis())));
            Thread.currentThread().sleep(22000);

        }catch (Exception e){
            e.printStackTrace();

        }finally{
            try {
                scheduler.shutdown();
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }

    }



}
