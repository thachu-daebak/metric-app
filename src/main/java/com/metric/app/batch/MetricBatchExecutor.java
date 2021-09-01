package com.metric.app.batch;

import java.util.Objects;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class MetricBatchExecutor {

	private static Scheduler metricScheduler=null; 

	//Job executes for ever 30 minutes
	private static String CRON_SCHEDULE="*/30 */30 * * * ?";


	private MetricBatchExecutor() {
		// TODO Auto-generated constructor stub
	}

	public static void scheduleMetricBatch() {
		if(Objects.isNull(metricScheduler)) {
			System.out.println("Executing Schedule Job");
			JobDetail metricJob = JobBuilder.newJob(MetricBatch.class)
					.withIdentity("job1", "group1").build();

			Trigger trigger1 = TriggerBuilder.newTrigger()
					.withIdentity("cronTrigger1", "group1")
					.withSchedule(CronScheduleBuilder.cronSchedule(CRON_SCHEDULE))
					.build();
			try{
				metricScheduler = new StdSchedulerFactory().getScheduler();
				metricScheduler.start();
				metricScheduler.scheduleJob(metricJob, trigger1);
				System.out.println("Job Scheduling finished");
			}catch(Exception exception) {
				System.out.println(exception);
			}
		}
	}

	public static void shutdownScheduler() {
		if(Objects.nonNull(metricScheduler)) {
			try {
				metricScheduler.shutdown();
			}catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
