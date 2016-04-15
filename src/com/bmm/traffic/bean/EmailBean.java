package com.bmm.traffic.bean;

import java.sql.SQLException;
import java.util.Vector;

import com.bmm.bean.common.BaseBean;
import com.bmm.traffic.util.PublicTools;

public class EmailBean extends BaseBean {

	/* 查询邮件列表sql */
	private static final String S_SQL_SEARCH = "SELECT ID, ADDRESS, TITLE, STATUS, MAIL_TYPE,CONTENT as CONTENTCLOB, to_char(CREATEDATE,'yyyy-MM-dd  hh24:mi:ss') as CREATEDATE,SENDINFO from T_NOTIFICATION_EMAIL WHERE 1=1 ";

	/* 定时异步查询最新的邮件信息sql */
	private static final String S_SQL_REFRESHSTATUS = "SELECT ID, ADDRESS, TITLE, STATUS,CONTENT as CONTENTCLOB, MAIL_TYPE,to_char(CREATEDATE,'yyyy-MM-dd  hh24:mi:ss') as CREATEDATE from T_NOTIFICATION_EMAIL";

	/* 更新收件人地址及状态sql */
	private static final String S_SQL_UPDATE_ADDRESS = "INSERT INTO T_NOTIFICATION_EMAIL(ID,  STATUS, TITLE,ADDRESS,CONTENT, MAIL_TYPE,CREATEDATE) "
			+ "SELECT SEQ_T_NOTIFICATION_EMAIL.nextval as ID,? as status,TITLE,? as address,CONTENT,MAIL_TYPE,SYSDATE as CREATEDATE from T_NOTIFICATION_EMAIL where ID=?";

	/* 更新邮件状态sql */
	private static final String S_SQL_UPDATE_STATUS = "INSERT INTO T_NOTIFICATION_EMAIL(ID,  STATUS, TITLE,ADDRESS,CONTENT, MAIL_TYPE,CREATEDATE) "
			+ "SELECT SEQ_T_NOTIFICATION_EMAIL.nextval as ID,? as status,TITLE,ADDRESS,CONTENT,MAIL_TYPE,SYSDATE as CREATEDATE from T_NOTIFICATION_EMAIL WHERE ID IN (%s)";

	/* 统计邮件总数的sql */
	private static final String S_SQL_STATISTICS = "SELECT STATUS,count(ID) as count FROM T_NOTIFICATION_EMAIL WHERE 1=1 ";

	/**
	 * 邮件列表查询方法
	 * 
	 * @param type		邮件类型
	 * @param address	收件人地址
	 * @param status	邮件状态
	 * @param startDate	邮件生成时间查询起始时间
	 * @param stopdDate	邮件生成时间查询终止时间
	 * @param page
	 * @param rows
	 * @return vec 		结果集
	 * @throws SQLException
	 * @throws Exception
	 */
	public static Vector<?> getEmailList(String type, String address, String status, String startDate, String stopDate,
			int page, int rows) throws SQLException, Exception {
		// 结果集
		Vector<?> vec = null;
		String sql = S_SQL_SEARCH;
		String[] parameter = null;

		// 判断是否做筛选,如果有对应的查询条件,则将将查询条件拼进SQL语句的WHERE条件中
		if (!("all".equals(type))) {
			sql += " AND MAIL_TYPE = '" + type + "'";
		}
		if (!"".equals(address)) {
			sql += " AND lower(address) like '%" + address.toLowerCase() + "%'";
		}
		if (!"all".equals(status)) {
			sql += " AND STATUS = '" + status + "'";
		}
		if (!"".equals(startDate)) {
			sql += " AND to_char(CREATEDATE,'yyyy-MM-dd') >= '" + startDate + "'";
		}
		if (!"".equals(stopDate)) {
			sql += " AND to_char(CREATEDATE,'yyyy-MM-dd') <= '" + stopDate + "'";
		}
		// 查询语句按照生成时间倒叙排列
		sql += " ORDER BY createdate DESC ";
		vec = extractJSONArray(sql, parameter, page, rows);
		return vec;
	}

	/**
	 * 定时异步查询每个邮件的最新状态并更新到页面
	 * 
	 * @param ID	邮件的ID主键
	 * @return 		邮件的最新状态结果集
	 * @throws SQLException
	 * @throws Exception
	 */
	public static Vector<?> getRefreshStatus(String[] ID) throws SQLException, Exception {
		Vector<?> vec = null; // 结果集
		String sql = S_SQL_REFRESHSTATUS;
		sql += String.format(" WHERE ID IN (%s) ORDER BY createdate DESC", PublicTools.ArrayToString(ID));
		String[] parameter = null;
		vec = BaseBean.extractJSONArray(sql, parameter, 0, 0);
		return vec;
	}

	/**
	 * 修改收件人地址并将该邮件状态改为未发送
	 * 
	 * @param status	邮件状态
	 * @param address	邮件收件人地址
	 * @param id		邮件的id主键
	 * @throws SQLException
	 * @throws Exception
	 */
	public static void updateAddress(String status, String address, String id) throws SQLException, Exception {
		String sql = S_SQL_UPDATE_ADDRESS;
		String[] parameter = new String[3];
		parameter[0] = status;
		parameter[1] = address;
		parameter[2] = id;
		update(sql, parameter);
	}

	/**
	 * 将邮件状态更改为未发送
	 * 
	 * @param status	邮件状态
	 * @param id		邮件的id主键
	 * @throws SQLException
	 * @throws Exception
	 */
	public static void updateStatus(String status, String[] id) throws SQLException, Exception {
		String sql = S_SQL_UPDATE_STATUS;
		sql = String.format(sql, PublicTools.ArrayToString(id));
		String[] parameter = new String[1];
		parameter[0] = status;
		update(sql, parameter);
	}

	/**
	 * 统计发送情况
	 * 
	 * @param type		邮件类型
	 * @param address	邮件收件人地址
	 * @param status	邮件状态
	 * @param startDate	邮件生成时间查询起始日期
	 * @param stopDate	邮件生成时间查询终止日期
	 * @return vec 		统计情况的结果集
	 * @throws SQLException
	 * @throws Exception
	 */
	public static Vector<?> getStatistics(String type, String address, String status, String startDate,
			String stopDate) throws SQLException, Exception {
		Vector<?> vec = null; // 结果集
		String sql = S_SQL_STATISTICS;
		String[] parameter = null;
		if (!"all".equals(type)) {
			sql += " AND MAIL_TYPE = '" + type + "'";
		}
		if (!"".equals(address)) {
			sql += " AND address like '%" + address + "%'";
		}
		if (!"all".equals(status)) {
			sql += " AND STATUS = '" + status + "'";
		}
		if (!"".equals(startDate)) {
			sql += " AND to_char(CREATEDATE,'yyyy-MM-dd') >= '" + startDate + "'";
		}
		if (!"".equals(stopDate)) {
			sql += " AND to_char(CREATEDATE,'yyyy-MM-dd') <= '" + stopDate + "'";
		}
		sql += " group by STATUS ";
		vec = extractJSONArray(sql, parameter, 0, 0);
		return vec;
	}
}
