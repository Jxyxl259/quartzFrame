package com.exec.c_disallowconcurrentexecutiontest;

import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @DisallowConcurrentExecution
 * 注解的作用：不允许当前任务并发执行
 *      ex:
 *          设定的 任务调度schedule 的触发机制trigger 为 1sec 执行一次(0/1 * * * * ?)，
 *          但是当任务第一次执行时耗费了 5sec
 *          第二次任务要等第一次任务执行完毕后执行，而不是在第一次任务执行了 1sec 后开始执行
 *
 */
@DisallowConcurrentExecution
public class DisallowConcurrentJob implements Job {

    private static Logger log = LoggerFactory.getLogger(DisallowConcurrentJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        JobDetailImpl jobDetail = (JobDetailImpl)context.getJobDetail();

        JobDataMap dataMap = jobDetail.getJobDataMap();

        String dataBaseName = (String)dataMap.get("dataBase");

        log.info("任务：" + jobDetail.getName() + "\t开始备份数据库：" + dataBaseName );


        try {
            Thread.currentThread().sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("任务：" + jobDetail.getName() + "\t备份数据库完成：" + dataBaseName );

    }
}




