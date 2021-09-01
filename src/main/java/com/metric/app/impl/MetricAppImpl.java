package com.metric.app.impl;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.metric.app.datastore.MetricStore;
import com.metric.app.utils.MetricUtils;

public class MetricAppImpl {

	private static MetricAppImpl instance=null;

	private static final int SUCCESS=200;

	private static final int ERROR=400;

	private MetricAppImpl() {

	}

	public static MetricAppImpl getInstance() {
		if(Objects.isNull(instance)) {
			instance=new MetricAppImpl();
		}
		return instance;
	}

	public int storeRequest(HttpServletRequest request, HttpServletResponse response) {
		String jsonData=MetricUtils.getInstance().getRequestBody(request);
		if(Objects.isNull(jsonData)) {
			return ERROR;
		}
		JSONObject value=MetricUtils.getInstance().checkJson(jsonData);
		if(Objects.nonNull(value)) {
			Timestamp timestamp=new Timestamp(Long.valueOf(value.get("timestamp").toString()));
			MetricStore.getInstance().store(timestamp, value);
			return SUCCESS;	
		}
		return ERROR;
	}

	public void destroyServlet() {
		MetricStore.getInstance().clearData();
	}

	public Integer getStoreCount() {
		return MetricStore.getInstance().getStoreCount();

	}

	public void getRecords(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String queryString = request.getQueryString();
		HashMap<String,String> queryMap=MetricUtils.getInstance().getQueryMap(queryString);
		if(Objects.isNull(queryMap)) {
			response.getWriter().append("Served at: ").append(request.getContextPath()).append(" Status : "+HttpServletResponse.SC_BAD_REQUEST);
		}else {
			String resultList=new String();
			try {
				Timestamp from=new Timestamp(Long.valueOf(queryMap.get("t1")));
				Timestamp to=new Timestamp(Long.valueOf(queryMap.get("t2")));
				LinkedList<JSONObject> jsonList=MetricStore.getInstance().getRecordswithTimestamp(from, to);
				resultList=JSONObject.valueToString(jsonList);
				response.getWriter().append("Records between "+queryMap.get("t1") +" and "+queryMap.get("t2")+"\n"+resultList);
			}catch(Exception ex) {
				ex.printStackTrace();
				response.getWriter().append("Served at: ").append(request.getContextPath()).append(" Status : "+HttpServletResponse.SC_BAD_REQUEST);
			}
		}
	}




}
