package com.exec.a_twokindsoftrigger;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AMailJob implements Job {
    private static SimpleDateFormat sdf = new SimpleDateFormat("hh:MM:ss");

    private static Logger log = LoggerFactory.getLogger(AMailJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        String address = (String)jobDetail.getJobDataMap().get("email_address");
        Trigger trigger = jobExecutionContext.getTrigger();

        log.info(sdf.format(new Date(System.currentTimeMillis())) + "send email to address "+ address);
    }
}


