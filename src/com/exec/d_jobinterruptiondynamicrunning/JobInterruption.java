package com.exec.d_jobinterruptiondynamicrunning;

import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobInterruption implements InterruptableJob {

	private static Logger log = LoggerFactory.getLogger(JobInterruption.class);

	private static boolean stop = false;

	@Override
	public void interrupt() throws UnableToInterruptJobException {
		log.info("中断方法被调用了");
		stop = true;
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		if(!stop) {
			log.info("任务开始执行...");

			try {
				Thread.currentThread().sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			log.info("任务执行完毕...");
		}else{
			log.info("任务被打断...");
//			JobExecutionException je = new JobExecutionException("job has been called stopped ");
//			je.setUnscheduleAllTriggers(true);
//			throw new JobExecutionException();
		}

	}
}
