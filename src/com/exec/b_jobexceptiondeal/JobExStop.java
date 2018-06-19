package com.exec.b_jobexceptiondeal;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobExStop implements Job {

	private static Logger log = LoggerFactory.getLogger(JobExStop.class);


	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		try {
			int i = 0;

			int error = 5/i;
			log.info("运行 job时发生异常");
		} catch (Exception e) {

			log.info("捕获到 job执行时发生的异常");
			JobExecutionException je = new JobExecutionException(e);

			log.info("停止与该 job关联的所有任务调度（不再执行该 Job）");

			//默认值是 false（任务没异常情况时调度一直起效）
			je.setUnscheduleAllTriggers(true);
			throw je;

		}

	}
}
