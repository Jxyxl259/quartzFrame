package com.exec.e_listener.joblistener;


import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 具体某一个任务的监听类
 */
public class MailJobListener implements JobListener {

	private static Logger logger = LoggerFactory.getLogger(MailJobListener.class);

	/**
	 * 返回值，用以说明JobListener的名称
	 * 对于注册于全局的监听器，getName()主要用于记录日志
	 * 注册在JobDetail上的监听器名称必须匹配此方法的返回值
	 * @return
	 */
	@Override
	public String getName() {
		return "mail_job_listener";
	}

	/**
	 * 任务将要被触发器执行时调用
	 * @param context
	 */
	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		JobDetail jobDetail = context.getJobDetail();
		JobDataMap dataMap = jobDetail.getJobDataMap();
		String address = (String)dataMap.get("mail_address");
		logger.info("给"+ address +"发送邮件任务开始执行...");
	}

	/**
	 * scheduler 在JobDetail即将被执行， 但又被TriggerListener否决时调用这个方法
	 * @param context
	 */
	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {

	}

	/**
	 * job执行完后调用此方法
	 * @param context
	 * @param jobException
	 */
	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		JobDetail jobDetail = context.getJobDetail();
		JobDataMap dataMap = jobDetail.getJobDataMap();
		String address = (String)dataMap.get("mail_address");
		logger.info("给"+ address +"发送邮件任务执行完毕...");
	}
}
