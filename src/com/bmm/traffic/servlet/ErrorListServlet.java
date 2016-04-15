package com.bmm.traffic.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bmm.traffic.bean.ErrorListBean;
import com.bmm.traffic.util.Logger;

import net.sf.json.JSONArray;

/**
 * Servlet-Bean架构，日志处理，已废弃
 * @author Administrator
 *
 */
public class ErrorListServlet extends HttpServlet {

	private static Logger logger = Logger.getLogger(ErrorListServlet.class);

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
		}
		response.setContentType("text/html;charset=UTF-8");

		// 初始化相关结果集
		Vector<?> jsonV = null;
		JSONArray jal = null;
		JSONArray statistics = null;

		// 获取对应请求参数
		String filename = request.getParameter("filename");
		String startDate = request.getParameter("startdate");
		String stopDate = request.getParameter("stopdate");
		String modules = request.getParameter("MODULES");
		
		int page = Integer.parseInt(request.getParameter("page"));
		int rows = Integer.parseInt(request.getParameter("rows"));
		
		try {
			// 查询统计数据
			jsonV = ErrorListBean.getStatistics(modules, startDate, stopDate);
			statistics = (JSONArray) jsonV.get(2);
			// 查询出所有的ErrorList
			jsonV = ErrorListBean.getErrorList(filename, startDate, stopDate, modules, page, rows);
			PrintWriter pw = response.getWriter();
			if (jsonV != null && jsonV.size() > 0) {
				// 最终处理：传回AJAX 结果集
				jal = (JSONArray) jsonV.get(2);
				pw.print("{\"total\":" + jsonV.get(0) + ",\"rows\":" + jal.toString() + ",\"Statistics\":"
						+ statistics.get(0).toString() + "}");
			} else {
				pw.print("{\"total\":0,\"rows\":[],\"Statistics\":" + statistics.get(0).toString() + "}");
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
	}

}
