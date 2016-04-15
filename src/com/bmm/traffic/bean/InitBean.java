package com.bmm.traffic.bean;

import java.sql.SQLException;
import java.util.Vector;

import com.bmm.bean.common.BaseBean;

public class InitBean extends BaseBean {

	/* 查询用户相关权限信息的sql */
	private static final String S_SQL_SEARCH = "SELECT OFFICE, GROUP_ID, USERID FROM WEBEDIVIEW where USERID=?";

	
	/**
	 * 获取用户相关权限信息的方法
	 * 
	 * @param userid	WBP传入的用户id
	 * @return vec 		查询到的结果集
	 * @throws SQLException
	 * @throws Exception
	 */
	public static Vector<?> getUserInfo(String userid) throws SQLException, Exception {
		if ("".equals(userid)) {
			return null;
		}
		// 结果集
		Vector<?> vec = null;
		String sql = S_SQL_SEARCH;
		String[] parameter = new String[1];
		parameter[0] = userid;
		vec = extractJSONArray(sql, parameter, 0, 0);
		return vec;
	}
}
