package com.metric.app.batch;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.metric.app.datastore.MetricStore;

public class MetricBatch implements Job{

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("Executing Batch ");
		MetricStore.getInstance().clearDataStore();
	}

}
