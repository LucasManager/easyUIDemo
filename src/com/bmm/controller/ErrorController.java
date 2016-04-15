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
import com.bmm.service.ErrorService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
public class ErrorController extends BaseController {

	@Resource(name = "ErrorService")
	private ErrorService service;

	/**
	 * ��ȡError��־�б�
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/Error", method = RequestMethod.GET)
	public void getErrorList(HttpServletRequest request, HttpServletResponse response) {

		// ͳһrequest��response����
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.toString());
		}
		response.setContentType("text/html;charset=UTF-8");

		// ��ȡ��Ӧ�������
		String fileName = request.getParameter("filename");
		String startDate = request.getParameter("startdate");
		String stopDate = request.getParameter("stopdate");
		String modules = request.getParameter("modules");
		int rows = Integer.parseInt(request.getParameter("rows") == null ? "0" : request.getParameter("rows"));
		int page = Integer.parseInt(request.getParameter("page") == null ? "0" : request.getParameter("page"));

		// ����ѯ������ʼ����entity��
		com.bmm.entity.Error error = new com.bmm.entity.Error();
		error.setFileName(fileName);
		error.setStartDate(startDate);
		error.setStopDate(stopDate);
		error.setModules(modules);

		// ��ʼ�������
		JSONObject statistics = null;
		JSONObject result = null;

		try {
			// ��ȡ������־�б�
			result = service.getErrorList(error, rows, page);
			// ��ȡ������־����ͳ��
			statistics = service.getStatistics(error);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		// �����ҳ��
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
}