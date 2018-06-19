package com.exec.e_listener.joblistener;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@DisallowConcurrentExecution
public class MailJob implements Job {

	private static Logger logger = LoggerFactory.getLogger(MailJob.class);


	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		logger.info("任务开始执行---> 发送邮件给:" + dataMap.getString("mail_address"));
	}
}
