package com.metric.app.utils;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;




public class MetricUtils {
	private static MetricUtils instance=null;

	private MetricUtils() {

	}

	public static MetricUtils getInstance() {
		if(Objects.isNull(instance)) {
			instance=new MetricUtils();
		}
		return instance;
	}

	public String getRequestBody(HttpServletRequest request) {
		AtomicReference<StringBuilder> req=new AtomicReference<>(new StringBuilder(""));
		try {
			request.getReader().lines().forEach(action->{
				req.get().append(action);
			});
			System.out.println(req.get());
			return req.get().toString();
		}catch(Exception exception) {
			System.out.print(exception);
			return null;
		}
	}

	public JSONObject checkJson(String jsonData) {
		try {
			return new JSONObject(jsonData);
		}catch(Exception es) {
			System.out.println(es);
			return null;
		}
	}

	public HashMap<String,String> getQueryMap(String queryString){
		if(StringUtils.isEmpty(queryString)) {
			return null;
		}
		queryString=StringUtils.trim(queryString);
		if(!StringUtils.contains(queryString, "&")) {
			return null;
		}else {
			String parArray[]=StringUtils.split(queryString, "&");
			HashMap<String,String> paramMap=new HashMap<String, String>();
			for(int i=0;i<parArray.length;i++) {
				if(StringUtils.contains(parArray[i], "=")) {
					String timeArray[]=StringUtils.split(parArray[i], "=");
					paramMap.put(timeArray[0], StringUtils.isEmpty(timeArray[1])?"":timeArray[1]);
				}
			}
			if(Objects.nonNull(paramMap) && paramMap.keySet().size() >=2 && paramMap.containsKey("t1") && paramMap.containsKey("t2")) {
				return paramMap;
			}

		}
		return null;

	}

}
