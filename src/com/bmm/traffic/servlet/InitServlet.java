package com.bmm.traffic.servlet;

import java.io.IOException;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.bmm.traffic.bean.InitBean;
import com.bmm.traffic.util.Logger;

import net.sf.json.JSONArray;

/**
 * Servlet-Bean�ܹ����û���¼���ѷ���
 * @author Administrator
 *
 */
public class InitServlet extends HttpServlet {
	
	private static Logger logger = Logger.getLogger(InitServlet.class);
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("123");
		// ҳ�浯��������
		response.getWriter().write("loading....");
		
		// ��ʼ����ؽ����
		Vector<?> jsonV = null;
		JSONArray jal = null;
		
		// ��ȡ�����е�userId
		String userid = request.getParameter("userid") == null ? "13" : request.getParameter("userid");
		
		try {
			// ��ѯ���û��������Ϣ
			//jsonV = InitBean.getUserInfo(userid);
			jsonV = null;
			// ���û���Ϣ�����session��, �������ҳ�洦�����Ȩ�޵����Ʋ���
			if (jsonV != null && ((JSONArray) jsonV.get(2)).size() > 0) {
				jal = (JSONArray) jsonV.get(2);
				request.getSession().setAttribute("userid", userid);
				request.getSession().setAttribute("groupid", jal.getJSONObject(0).getString("GROUP_ID"));
				request.getSession().setAttribute("local", jal.getJSONObject(0).getString("OFFICE"));
				// ����ɹ���ת��̨ҳ��
				RequestDispatcher dispatcher = request.getRequestDispatcher("/main.jsp");
				dispatcher.forward(request, response);
			} else {
				request.getSession().setAttribute("userid", userid);
				request.getSession().setAttribute("groupid", "");
				request.getSession().setAttribute("local", "SHANGHAI");
				RequestDispatcher dispatcher = request.getRequestDispatcher("/main.jsp");// ����ɹ���ת��̨ҳ��
				dispatcher.forward(request, response);
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
	}
	@Override
	public void init() throws ServletException {
		System.out.println("servlet init");
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(); 
		ctx.scan("org"); 
		ctx.scan("com");
		ctx.refresh(); 
		super.init();
	}
}
