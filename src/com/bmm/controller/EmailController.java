package com.bmm.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bmm.entity.Email;
import com.bmm.service.EmailService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
public class EmailController extends BaseController {

	@Resource(name = "EmailService")
	private EmailService service;

	/**
	 * 获取邮件列表
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/Email", method = RequestMethod.GET)
	public void getEmailList(HttpServletRequest request, HttpServletResponse response) {
		// 统一request与response编码
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.toString());
		}
		response.setContentType("text/html;charset=UTF-8");

		// 获取对应请求参数
		String type = request.getParameter("type");
		String address = request.getParameter("address");
		String status = request.getParameter("status");
		String startDate = request.getParameter("startdate");
		String stopDate = request.getParameter("stopdate");
		
		int rows = Integer.parseInt(request.getParameter("rows") == null ? "0" : request.getParameter("rows"));
		int page = Integer.parseInt(request.getParameter("page") == null ? "0" : request.getParameter("page"));

		// 将查询参数初始化到entity中
		Email email = new Email();
		email.setMailType(type);
		email.setAddress(address);
		email.setStatus(status);
		email.setStartDate(startDate);
		email.setStopDate(stopDate);

		// 初始化结果集
		JSONArray statistics = null;
		JSONObject result = null;
		try {
			// 获取邮件信息列表
			result = service.getEmailList(email, rows, page);
			// 获取邮件数量统计
			statistics = service.getStatistics(email);
			logger.info("aaaaaaaaaa");
		} catch (Exception e) {
			logger.error(e.toString());
		}
		// 输出到页面
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
		} catch (IOException e) {
			logger.error(e.toString());
		}
		if (result != null && result.size() > 0) {
			pw.print("{\"total\":" + result.get("Count") + ",\"rows\":" + result.get("List") + ",\"Statistics\":"
					+ statistics.toString() + "}");
		} else {
			pw.print("{\"total\":0,\"rows\":[],\"Statistics\":" + statistics.toString() + "}");
		}
	}

	/**
	 * 重新发送邮件，根据选中的主键id将原来的邮件内容复制一份新的出来，状态设为wait
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/Email", method = RequestMethod.POST)
	public void resend(HttpServletRequest request) throws Exception {

		// 获取对应请求参数
		String ids = request.getParameter("ids");
		try {
			// 根据ids复制并重新插入新的邮件，状态设为wait
			service.updateEmailStatus(ids);
		} catch (Exception e) {
			logger.error(e.toString());
		}
	}

	/**
	 * 定时刷新界面邮件内容
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/EmailStatus", method = RequestMethod.POST)
	public void refreshStatus(HttpServletRequest request, HttpServletResponse response) {
		// 统一request与response编码
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.toString());
		}
		response.setContentType("text/html;charset=UTF-8");

		// 获取对应请求参数
		String type = request.getParameter("type");
		String address = request.getParameter("address");
		String status = request.getParameter("status");
		String startDate = request.getParameter("startdate");
		String stopDate = request.getParameter("stopdate");
		String ids = request.getParameter("ids");

		// 将查询参数初始化到entity中
		Email email = new Email();
		email.setMailType(type);
		email.setAddress(address);
		email.setStatus(status);
		email.setStartDate(startDate);
		email.setStopDate(stopDate);

		// 初始化结果集
		JSONArray statistics = null;
		JSONArray result = null;

		try {
			// 获取邮件信息列表
			result = service.refreshStatus(ids);
			// 获取邮件数量统计
			statistics = service.getStatistics(email);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		// 输出到页面
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
		} catch (IOException e) {
			logger.error(e.toString());
		}
		if (result != null && result.size() > 0) {
			pw.print("{\"total\":" + result.size() + ",\"rows\":" + result.toString() + ",\"Statistics\":"
					+ statistics.toString() + "}");
		} else {
			pw.print("{\"total\":0,\"rows\":[],\"Statistics\":" + statistics.toString() + "}");
		}
	}
}
