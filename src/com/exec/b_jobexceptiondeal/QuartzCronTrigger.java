package com.exec.b_jobexceptiondeal;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuartzCronTrigger {


	private static Logger log = LoggerFactory.getLogger(QuartzCronTrigger.class);


	public static void main(String[] args) {

		Scheduler scheduler= null;

		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();

			scheduler.start();

//-------------------------------  异常停止调度 -------------------------
//			JobDetail jobDetail = JobBuilder.newJob(JobExStop.class)
//											.withIdentity("job_ex", "job_ex_group")
//											.build();

//-------------------------------  异常 修改参数继续执行 ---------------------
			JobDetail jobDetail = JobBuilder.newJob(JobExContinue.class)
											.withIdentity("job_ex", "job_ex_group")
											.build();


			Trigger trigger = TriggerBuilder.newTrigger()
											.withSchedule(CronScheduleBuilder.cronSchedule("0/2 * * * * ?"))
											.withIdentity("job_ex_trigger","job_ex_group")
											.build();
			scheduler.scheduleJob(jobDetail, trigger);

			Thread.currentThread().sleep(10000);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(scheduler != null) {
					scheduler.shutdown(true);
				}
			} catch (SchedulerException e) {
				e.printStackTrace();
			}
		}


	}
}
