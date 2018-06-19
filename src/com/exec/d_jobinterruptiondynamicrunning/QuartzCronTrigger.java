package com.exec.d_jobinterruptiondynamicrunning;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class QuartzCronTrigger {


	private static Logger log = LoggerFactory.getLogger(JobInterruption.class);


	public static void main(String[] args) throws Exception{

		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		scheduler.start();


//------------------------------ 任务执行过程中被中断, 但是trigger对该任务依旧起作用 ----------------------------------
//		JobDetail jobDetail = JobBuilder.newJob(JobInterruption.class).withIdentity("interrupt_job").build();

//		Trigger trigger = TriggerBuilder.newTrigger()
//				.withSchedule(CronScheduleBuilder.cronSchedule("0/2 * * * * ?"))
//				.withIdentity("interrupt_trigger")
//				.build();



//		Thread.currentThread().sleep(4000);
//
//		scheduler.interrupt(jobDetail.getKey());
//
//		Thread.currentThread().sleep(8000);
//
//		scheduler.shutdown(true);



//------------------------------  停止任务，任务不再被 trigger触发  ------------------------------------------
// 这里需要注意，停止任务的触发之后， 如果是Crontrigger 会记录未执行的job，
// 待 Trigger重新触发之后，暂停的这段时间的 job会并发执行，
// 所以如果有暂停需求的一般要在 Job 接口实现类上加 @DisallowConcurrentExecution注解
		JobDetail jobDetail = JobBuilder.newJob(JobStop.class).withIdentity("stop_job").storeDurably(false).build();

		CronTriggerImpl trigger = (CronTriggerImpl)TriggerBuilder.newTrigger()
										.withSchedule(CronScheduleBuilder.cronSchedule("0/2 * * * * ?"))// 每2秒执行一次
										.withIdentity("stop_trigger")
										.build();

// 设置触发器的 misfire 时的指令
		// 补偿错过的任务 有个任务 每15sec 执行一次，结果第一次执行了5min 后续立马补偿(开多个线程去执行错过的 misfire的job)，不更新 Scheduler信息, job 是按计划好的时序执行的
		// 会并发执行 (严重时占满线程池)
		//trigger.setMisfireInstruction(Trigger.MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY);

		// 会及时更新 scheduler 错过的任务立马执行一次(默认值)
		//trigger.setMisfireInstruction(Trigger.MISFIRE_INSTRUCTION_SMART_POLICY);

		// 放弃超过 misfire的任务(可以解决 pause期间的 cronTrigger 在 resume之后并发执行的场景) 从下一个Cron触发时间点开始执行
		trigger.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);

		System.err.println("triggerKey --->" + trigger.getKey()+"\njobKey --->" + jobDetail.getKey());

		scheduler.scheduleJob(jobDetail, trigger);


		Thread.currentThread().sleep(2000);

/*
//--------------------------------- 通过移除触发器(remove trigger)的方式 停止 job运行 --------------------------------------
		CronTrigger stop_trigger = (CronTrigger)scheduler.getTrigger(new TriggerKey("stop_trigger"));

		if(stop_trigger != null){
			log.info("scheduler 任务调度 停止identity 为 stop_trigger 的 trigger触发 job");
			boolean delete = scheduler.unscheduleJob(stop_trigger.getKey());
			if(delete) {
				System.err.println("scheduler deleted ");
			}
		}

		Thread.currentThread().sleep(5000);

		// 将被移除的 trigger 与 jobDetail 重新结合并加入到 任务计划 schsdule中
		// 由于之前调用了 unscheduleJob()方法删除了 trigger对象，
		// 之前的jobDetail对象的持久化属性又设置为false，
		// 并且该jobDetail又没有其他关联的 trigger对象，（参见unscheduleJob() 方法文档注释）
		// 所以这里需要重新 创建 jobDetail对象和 trigger对象，并塞到 任务调度中
		log.info("scheduler 任务调度 加入 identity 为 stop_trigger 的 trigger触发 identity 为 stop_job 的 job");
		JobDetail jobDetail_2 = JobBuilder.newJob(JobStop.class).withIdentity("stop_job").storeDurably(false).build();
		CronTriggerImpl trigger_2 = (CronTriggerImpl)TriggerBuilder.newTrigger()
				.withSchedule(CronScheduleBuilder.cronSchedule("0/2 * * * * ?"))// 每2秒执行一次
				.withIdentity("stop_trigger")
				.build();
		//scheduler.scheduleJob(jobDetail_2, trigger_2);
		Date date = scheduler.rescheduleJob(new TriggerKey("stop_trigger"), TriggerBuilder.newTrigger().withIdentity("stop_job").withSchedule(CronScheduleBuilder.cronSchedule("0/2 * * * * ?")).build());
		//System.err.println(date);
//---------------------------------------------------------------------------------------------------------------------
*/


//--------------------------------- 通过给定的 jobKey 暂停当前任务 ------------------------------------------
		log.info("scheduler 任务调度 暂停 identity 为 stop_trigger 的 trigger触发 job");
		scheduler.pauseJob(jobDetail.getKey());

		Thread.currentThread().sleep(8000);


		// 暂停5秒后再度触发 trigger
		log.info("scheduler 任务调度 恢复 identity 为 stop_trigger 的 trigger触发 job");
		scheduler.resumeJob(jobDetail.getKey());

		// pause和 rescheduleJob可以搭配使用实现任务的 暂停 与 恢复执行
		Date date = scheduler.rescheduleJob(new TriggerKey("stop_trigger"), TriggerBuilder.newTrigger().withIdentity("stop_job").withSchedule(CronScheduleBuilder.cronSchedule("0/2 * * * * ?")).build());
		System.err.println(date);
//----------------------------------------------------------------------------------------------------------


		Thread.currentThread().sleep(6000);

		scheduler.shutdown(true);

	}

}
