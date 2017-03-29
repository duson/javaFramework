package jobs;

import java.util.Date;
import java.util.UUID;

import org.quartz.DateBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

 */
@Service
public class JobService {
	@Autowired
	private Scheduler scheduler;
	
	private static final int[] RETRY_INTERVAL_SECOND = { 30, 60, 180 }; // 重试间隔，单位：秒
	
	private int retryCount = RETRY_INTERVAL_SECOND.length;
	private int retry = 0;

	public void startJob(Class<? extends Job> clazz) {
		this.startJob(clazz, null, null);
	}
	public void startJob(Class<? extends Job> clazz, Object params) {
		this.startJob(clazz, null, params);
	}
	private void startJob(Class<? extends Job> clazz, Date startTime, Object params) {
		Date jobStartTime = DateBuilder.futureDate(10, DateBuilder.IntervalUnit.SECOND);
		if(startTime != null)
			jobStartTime = startTime;
		
		try {
			JobDetail jobDetail = JobBuilder.newJob(clazz).build();
			
			SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger().withIdentity(UUID.randomUUID().toString()).startAt(jobStartTime).build();  
			
            //在指定cron表达式的时间点触发  
            // Trigger trigger = TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule(String cron)).build();
			
			JobDataMap jobDataMap = trigger.getJobDataMap();
			jobDataMap.put("retry", String.valueOf(retry));
			jobDataMap.put("over", "false");
			if(params != null)
				jobDataMap.put("params", JSON.toJSONString(params));
            scheduler.scheduleJob(jobDetail, trigger);  
            scheduler.start(); 
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	public void retryJob(String jobName) {
		retry++;
		if(retry > retryCount) return;
		
		try {
			Trigger oldTrigger = scheduler.getTrigger(TriggerKey.triggerKey(jobName));
			JobDataMap jobDataMap = oldTrigger.getJobDataMap();
			LogUtils.LOGGER_ERROR.info("-----------job-DataMap" + JSON.toJSONString(jobDataMap));
			
			int retrySecond = 10;
			if(retry <= RETRY_INTERVAL_SECOND.length)
				retrySecond = RETRY_INTERVAL_SECOND[retry-1];
			Date startTime = DateBuilder.futureDate(retrySecond, DateBuilder.IntervalUnit.SECOND);
			Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName).startAt(startTime).build();
				/*.withSchedule(SimpleScheduleBuilder.simpleSchedule()
					.withIntervalInSeconds(10)
					.withRepeatCount(cnt))*/
			trigger.getJobDataMap().put("retry", String.valueOf(retry));
			trigger.getJobDataMap().put("params", jobDataMap.getString("params"));
			trigger.getJobDataMap().put("over", String.valueOf(retry == retryCount));
			
			scheduler.rescheduleJob(TriggerKey.triggerKey(jobName), trigger);
		} catch (SchedulerException e) {
			LogUtils.LOGGER_ERROR.error("retryJob异常-" + e);
		}
	}
	
}
