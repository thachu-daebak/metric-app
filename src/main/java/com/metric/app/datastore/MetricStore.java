package com.metric.app.datastore;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONObject;


public class MetricStore {

	private static ConcurrentHashMap<Timestamp,LinkedList<JSONObject>> metricStoreMap=new ConcurrentHashMap<>();

	private static AtomicBoolean lockFlag=new AtomicBoolean(false);

	private static MetricStore instance=null;

	public static MetricStore getInstance() {
		if(Objects.isNull(instance)) {
			instance=new MetricStore();
		}
		return instance;
	}
	
	private void checkLockFlag() {
		if(lockFlag.get()) {
			try {
				System.out.println("Going to wait state");
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void store(Timestamp key,JSONObject value) {
		checkLockFlag();
		metricStoreMap.compute(key, (existingKey,existingObject)->{
			System.out.println("process");
			if(Objects.isNull(existingObject)) {
				LinkedList<JSONObject> list=new LinkedList<JSONObject>();
				list.add(value);
				return list;
			}else {
				existingObject.add(value);
				return existingObject;
			}
		});
	}

	public void clearDataStore() {
		lockFlag.set(true);
		metricStoreMap.clear();
		lockFlag.set(false);
		notifyAll();
		System.out.println("notify all wait state");
	}

	public void clearData() {
		metricStoreMap.clear();
	}

	public Integer getStoreCount() {
		AtomicInteger count=new AtomicInteger(0);
		metricStoreMap.values().stream().forEach(action->count.set(action.size()+count.get()));
		return count.get();
	}
	
	public LinkedList<JSONObject> getRecordswithTimestamp(Timestamp from,Timestamp to){
		checkLockFlag();
		LinkedList<JSONObject> result=new LinkedList<JSONObject>();
		metricStoreMap.entrySet().parallelStream()
		.filter(predicate->!(predicate.getKey().compareTo(from)<0) && !(predicate.getKey().compareTo(to)>0)).forEach(action->{
			result.addAll(action.getValue());
		});
		return result;
	}

}
