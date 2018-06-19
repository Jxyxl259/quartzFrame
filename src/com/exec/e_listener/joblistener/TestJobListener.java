package com.exec.e_listener.joblistener;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.KeyMatcher;
import org.quartz.impl.triggers.SimpleTriggerImpl;

public class TestJobListener {

	public static void main(String[] args) throws Exception{

		// 创建任务调度
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

		// 开始任务调度
		scheduler.start();

		// 创建具体的 Job任务
		JobDetail detail = JobBuilder.newJob(MailJob.class)
				.withIdentity("mail_job","mail_group")
				.usingJobData("mail_address","123@312.com")
				.build();


		// 创建触发器
		SimpleTriggerImpl trigger = (SimpleTriggerImpl)TriggerBuilder.newTrigger()
				.withIdentity("mail_trigger","mail_group")
				.withSchedule(SimpleScheduleBuilder.simpleSchedule()
						.withIntervalInSeconds(2)
						.withRepeatCount(10))
				.build();

		// 创建任务监听器
		MailJobListener mailJobListener = new MailJobListener();
		// 创建 keyMatcher 用于关联监听器，并注册到监听管理器中
		KeyMatcher<JobKey> jobKeyKeyMatcher = KeyMatcher.keyEquals(detail.getKey());

		// 通过jobKeyMatcher将监听器与jobDetail关联， 并注册到监听管理器中
		scheduler.getListenerManager().addJobListener(mailJobListener, jobKeyKeyMatcher);

		scheduler.scheduleJob(detail, trigger);

		Thread.currentThread().sleep(10000);

		scheduler.shutdown(true);

	}

}
