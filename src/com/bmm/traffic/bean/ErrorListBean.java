package com.bmm.traffic.bean;

import java.sql.SQLException;
import java.util.Vector;

import com.bmm.bean.common.BaseBean;

public class ErrorListBean extends BaseBean {

	/* 查询错误日志sql */
	private static final String S_SQL_SEARCH = "SELECT ID,ERROR_DESC,ERROR_FILE_NAME,to_char(CREATE_DATE,'yyyy-MM-dd  hh24:mi:ss') as CREATE_DATE FROM T_TRAFFIC_CONVERT_ERROR_LOG WHERE MODULES = ? and lower(ERROR_FILE_NAME) LIKE ? ";

	/* 统计错误日志数量sql */
	private static final String S_SQL_STATISTICS = "SELECT SUM(FILE_QUANTITY) as fileQty , SUM(ERR_FILE_QUANTITY) as errQty , SUM(WBP_FILE_QUANTITY) as wbpQty ,SUM(Q2C_FILE_QUANTITY) as q2cQty FROM T_TRAFFIC_CONVERT_DAILY_LOG WHERE MODULES = ?";

	/**
	 * 错误日志列表查询方法
	 * 
	 * @param filename		错误日志对应的的文件名
	 * @param startDate		错误日志生成时间查询起始日期
	 * @param stopDate		错误日志生成时间查询终止日期
	 * @param modules		对应的模块名称
	 * @param page
	 * @param rows
	 * @return vec			查询到的结果集
	 * @throws SQLException
	 * @throws Exception
	 */
	public static Vector<?> getErrorList(String filename, String startDate, String stopDate, String modules, int page,
			int rows) throws SQLException, Exception {
		// 结果集
		Vector<?> vec = null;
		String sql = S_SQL_SEARCH;
		String[] parameter = null;
		parameter = new String[2];
		parameter[0] = modules;
		//filename忽略大小写,统一用小写查询
		parameter[1] = "%" + filename.toLowerCase() + "%";
		
		// 判断是否做筛选,如果有对应的查询条件,则将将查询条件拼进SQL语句的WHERE条件中
		if (!"".equals(startDate)) {
			sql += " AND to_char(CREATE_DATE,'yyyy-MM-dd') >= '" + startDate + "'";
		}
		if (!"".equals(stopDate)) {
			sql += " AND to_char(CREATE_DATE,'yyyy-MM-dd') <= '" + stopDate + "'";
		}
		sql += " order by CREATE_DATE desc ";
		vec = extractJSONArray(sql, parameter, page, rows);
		return vec;
	}

	/**
	 * 根据时间区间统计错误日志总数
	 * 
	 * @param modules		对应的模块名称
	 * @param startDate		错误日志生成时间查询起始日期
	 * @param stopDate		错误日志生成时间查询终止日期
	 * @return vec			查询到的结果集
	 * @throws SQLException
	 * @throws Exception
	 */
	public static Vector<?> getStatistics(String modules, String startDate, String stopDate)
			throws SQLException, Exception {
		// 结果集
		Vector<?> vec = null;
		String sql = S_SQL_STATISTICS;
		String[] parameter = null;
		parameter = new String[1];
		parameter[0] = modules;
		if (!"".equals(startDate)) {
			sql += " AND to_char(LOG_DATE,'yyyy-MM-dd') >= '" + startDate + "'";
		}
		if (!"".equals(stopDate)) {
			sql += " AND to_char(LOG_DATE,'yyyy-MM-dd') <= '" + stopDate + "'";
		}
		vec = extractJSONArray(sql, parameter, 0, 0);
		return vec;
	}

}
