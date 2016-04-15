package com.bmm.traffic.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bmm.traffic.bean.EmailBean;
import com.bmm.traffic.util.Logger;

import net.sf.json.JSONArray;

/**
 * Servlet-Bean架构，处理Email,已废弃
 * @author Administrator
 *
 */
public class EmailServlet extends HttpServlet {

	private static Logger logger = Logger.getLogger(EmailServlet.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			logger.error(e1.toString());
			;
		}
		response.setContentType("text/html;charset=UTF-8");

		// 初始化相关结果集
		Vector<?> jsonV = null;
		JSONArray jal = null;
		JSONArray statistics = null;
		
		// 获取操作类型
		String action = request.getParameter("action");
		
		// 页面加载时, 加载DataGrid的请求
		if ("getlist".equals(action)) {
			// 获取对应请求参数
			String type = request.getParameter("type");
			String address = request.getParameter("address");
			String status = request.getParameter("status");
			String startDate = request.getParameter("startdate");
			String stopDate = request.getParameter("stopdate");

			int page = Integer.parseInt(request.getParameter("page"));
			int rows = Integer.parseInt(request.getParameter("rows"));
			
			try {
				// 查询统计数据
				jsonV = EmailBean.getStatistics(type, address, status, startDate, stopDate);
				statistics = (JSONArray) jsonV.get(2);
				// 查询出所有的EmailList
				jsonV = EmailBean.getEmailList(type, address, status, startDate, stopDate, page, rows);
				PrintWriter pw = response.getWriter();
				if (jsonV != null && jsonV.size() > 0) {
					// 最终处理：传回AJAX 结果集
					jal = (JSONArray) jsonV.get(2);
					pw.print("{\"total\":" + jsonV.get(0) + ",\"rows\":" + jal.toString() + ",\"Statistics\":"
							+ statistics.toString() + "}");
					// response.getWriter().write("{\"total\":" +jsonV.get(0) +
					// ",\"rows\":" + jal.toString() + "}");
				} else {
					pw.print("{\"total\":0,\"rows\":[],\"Statistics\":" + statistics.toString() + "}");
				}
			} catch (Exception e) {
				logger.error(e.toString());
			}
			
		// 页面定时调用ajax请求查询邮件的相关信息
		} else if ("refreshstatus".equals(action)) {
			// 获取对应请求参数
			String[] IDS = request.getParameterValues("IDS");
			String type = request.getParameter("type");
			String address = request.getParameter("address");
			String status = request.getParameter("status");
			String startDate = request.getParameter("startdate");
			String stopDate = request.getParameter("stopdate");
			
			try {
				// 查询统计数据
				jsonV = EmailBean.getStatistics(type, address, status, startDate, stopDate);
				statistics = (JSONArray) jsonV.get(2);
				// 查询邮件最新信息
				jsonV = EmailBean.getRefreshStatus(IDS);
				PrintWriter pw = response.getWriter();
				if (jsonV != null && jsonV.size() > 0) {
					// 最终处理：传回AJAX 结果集
					jal = (JSONArray) jsonV.get(2);
					pw.print("{\"rows\":" + jal.toString() + ",\"Statistics\":" + statistics.toString() + "}");
				} else {
					pw.print("");
				}
			} catch (Exception e) {
				logger.error(e.toString());
			}
			
		// 点击resend按钮时将邮件状态设为wait
		} else if ("resend".equals(action)) {
			// 获取对应请求参数
			String[] IDS = request.getParameterValues("IDS");
			
			try {
				// 更新邮件状态
				EmailBean.updateStatus("wait", IDS);
			} catch (Exception e) {
				logger.error(e.toString());
			}
			
		// 当修改了邮件收件人地址时, 将复制原来的邮件并更新收件人地址, 同时将状态设为"wait"
		} else if ("edit".equals(action)) {
			// 获取对应请求参数
			String ID = request.getParameter("emailid");
			String address = request.getParameter("editaddress");
			
			try {
				// 更新邮件收件人地址
				EmailBean.updateAddress("wait", address, ID);
			} catch (Exception e) {
				logger.error(e.toString());
			}
		}
	}

}
