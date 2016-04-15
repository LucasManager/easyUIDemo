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
 * Servlet-Bean架构，用户登录，已废弃
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
		// 页面弹出进度条
		response.getWriter().write("loading....");
		
		// 初始化相关结果集
		Vector<?> jsonV = null;
		JSONArray jal = null;
		
		// 获取请求中的userId
		String userid = request.getParameter("userid") == null ? "13" : request.getParameter("userid");
		
		try {
			// 查询出用户的相关信息
			//jsonV = InitBean.getUserInfo(userid);
			jsonV = null;
			// 将用户信息存放在session中, 方便后续页面处理相关权限的限制操作
			if (jsonV != null && ((JSONArray) jsonV.get(2)).size() > 0) {
				jal = (JSONArray) jsonV.get(2);
				request.getSession().setAttribute("userid", userid);
				request.getSession().setAttribute("groupid", jal.getJSONObject(0).getString("GROUP_ID"));
				request.getSession().setAttribute("local", jal.getJSONObject(0).getString("OFFICE"));
				// 登入成功跳转后台页面
				RequestDispatcher dispatcher = request.getRequestDispatcher("/main.jsp");
				dispatcher.forward(request, response);
			} else {
				request.getSession().setAttribute("userid", userid);
				request.getSession().setAttribute("groupid", "");
				request.getSession().setAttribute("local", "SHANGHAI");
				RequestDispatcher dispatcher = request.getRequestDispatcher("/main.jsp");// 登入成功跳转后台页面
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

