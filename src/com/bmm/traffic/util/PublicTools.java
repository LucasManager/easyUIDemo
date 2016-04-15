package com.bmm.traffic.util;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bmm.bean.common.BaseBean;

public class PublicTools extends BaseBean {

	/**
	 * 验证页面是否登入
	 * 
	 * @param request	请求对象
	 * @param response	相应对象
	 * @throws IOException
	 */
	public static void validate(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String userid = (String) (request.getSession().getAttribute("userid") == null ? ""
				: request.getSession().getAttribute("userid"));
		if ("".equals(userid)) {
			String loginurl = request.getSession().getServletContext().getInitParameter("loginurl");
			response.sendRedirect(loginurl);
		}
	}

	/**
	 * 将数组转成string用逗号分割
	 * 
	 * @param pArray	需要转换的数组
	 * @return
	 */
	public static final String ArrayToString(String[] pArray) {
		if (pArray == null) {
			return "";
		}
		String valuelist = "";
		for (String value : pArray) {
			if (valuelist != "") {
				valuelist += ",";
			}
			valuelist += value;
		}
		return valuelist;
	}

	/**
	 * 将ArrayList转为String用","分割
	 * 
	 * @param pArray
	 *            需要转换的ArrayList
	 * @return
	 */
	public static final String ArrayToString(ArrayList<String> pArray) {
		if (pArray == null) {
			return "";
		}
		String valuelist = "";
		for (String value : pArray) {
			if (valuelist != "") {
				valuelist += ",";
			}
			valuelist += value;
		}
		return valuelist;
	}
}
