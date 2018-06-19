package com.exec.b_jobexceptiondeal;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobExContinue implements Job {

	private static Logger log = LoggerFactory.getLogger(JobExContinue.class);

	private static int i = 0;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		try {
			int error = 5/i;
			log.info("正常运行 job");
		} catch (Exception e) {

			log.info("捕获到 job执行时发生的异常");
			JobExecutionException je = new JobExecutionException(e);

			log.info("修改参数后再次执行该 Job");
			i = 1;
			je.setRefireImmediately(true);
			throw je;

		}

	}
}
