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
 * Servlet-Bean�ܹ�������Email,�ѷ���
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

		// ��ʼ����ؽ����
		Vector<?> jsonV = null;
		JSONArray jal = null;
		JSONArray statistics = null;
		
		// ��ȡ��������
		String action = request.getParameter("action");
		
		// ҳ�����ʱ, ����DataGrid������
		if ("getlist".equals(action)) {
			// ��ȡ��Ӧ�������
			String type = request.getParameter("type");
			String address = request.getParameter("address");
			String status = request.getParameter("status");
			String startDate = request.getParameter("startdate");
			String stopDate = request.getParameter("stopdate");

			int page = Integer.parseInt(request.getParameter("page"));
			int rows = Integer.parseInt(request.getParameter("rows"));
			
			try {
				// ��ѯͳ������
				jsonV = EmailBean.getStatistics(type, address, status, startDate, stopDate);
				statistics = (JSONArray) jsonV.get(2);
				// ��ѯ�����е�EmailList
				jsonV = EmailBean.getEmailList(type, address, status, startDate, stopDate, page, rows);
				PrintWriter pw = response.getWriter();
				if (jsonV != null && jsonV.size() > 0) {
					// ���մ���������AJAX �����
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
			
		// ҳ�涨ʱ����ajax�����ѯ�ʼ��������Ϣ
		} else if ("refreshstatus".equals(action)) {
			// ��ȡ��Ӧ�������
			String[] IDS = request.getParameterValues("IDS");
			String type = request.getParameter("type");
			String address = request.getParameter("address");
			String status = request.getParameter("status");
			String startDate = request.getParameter("startdate");
			String stopDate = request.getParameter("stopdate");
			
			try {
				// ��ѯͳ������
				jsonV = EmailBean.getStatistics(type, address, status, startDate, stopDate);
				statistics = (JSONArray) jsonV.get(2);
				// ��ѯ�ʼ�������Ϣ
				jsonV = EmailBean.getRefreshStatus(IDS);
				PrintWriter pw = response.getWriter();
				if (jsonV != null && jsonV.size() > 0) {
					// ���մ���������AJAX �����
					jal = (JSONArray) jsonV.get(2);
					pw.print("{\"rows\":" + jal.toString() + ",\"Statistics\":" + statistics.toString() + "}");
				} else {
					pw.print("");
				}
			} catch (Exception e) {
				logger.error(e.toString());
			}
			
		// ���resend��ťʱ���ʼ�״̬��Ϊwait
		} else if ("resend".equals(action)) {
			// ��ȡ��Ӧ�������
			String[] IDS = request.getParameterValues("IDS");
			
			try {
				// �����ʼ�״̬
				EmailBean.updateStatus("wait", IDS);
			} catch (Exception e) {
				logger.error(e.toString());
			}
			
		// ���޸����ʼ��ռ��˵�ַʱ, ������ԭ�����ʼ��������ռ��˵�ַ, ͬʱ��״̬��Ϊ"wait"
		} else if ("edit".equals(action)) {
			// ��ȡ��Ӧ�������
			String ID = request.getParameter("emailid");
			String address = request.getParameter("editaddress");
			
			try {
				// �����ʼ��ռ��˵�ַ
				EmailBean.updateAddress("wait", address, ID);
			} catch (Exception e) {
				logger.error(e.toString());
			}
		}
	}

}