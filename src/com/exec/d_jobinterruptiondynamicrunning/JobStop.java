package com.exec.d_jobinterruptiondynamicrunning;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@DisallowConcurrentExecution
public class JobStop implements Job {

	private static Logger log = LoggerFactory.getLogger(JobStop.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		log.info(Thread.currentThread().getName() + "任务开始执行...");
		long start = System.currentTimeMillis();
		try {
			Thread.currentThread().sleep(1000); // RAM jobStore默认的 misfire 时间阈值在配置文件中已调整为1000毫秒，以保证 misfire效果
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		log.info(Thread.currentThread().getName() + "任务执行耗时["+ (System.currentTimeMillis() - start)/1000 + "sec]...");



	}
}
