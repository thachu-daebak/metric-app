package com.metric.app.servlet;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.metric.app.batch.MetricBatchExecutor;
import com.metric.app.impl.MetricAppImpl;

/**
 * Servlet implementation class MetricServlet
 */
@WebServlet({ "/postMetric"})
public class MetricServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MetricServlet() {
		super();
		System.out.println("Init ----------");
		MetricBatchExecutor.scheduleMetricBatch();
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int status=MetricAppImpl.getInstance().storeRequest(request, response);
		response.getWriter().append("Served at: ").append(request.getContextPath()).append(" Status : "+status);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int status=MetricAppImpl.getInstance().storeRequest(request, response);
		response.getWriter().append("Served at: ").append(request.getContextPath()).append(" Status : "+status);
		if(status == 200) {
			response.setStatus(HttpServletResponse.SC_OK);
		}else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	@Override
	public void destroy() {
		System.out.println("destroy----------");
		MetricBatchExecutor.shutdownScheduler();
		MetricAppImpl.getInstance().destroyServlet();
		super.destroy();
	}

}
