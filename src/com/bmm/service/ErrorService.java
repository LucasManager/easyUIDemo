package com.bmm.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.bmm.dao.Dao;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service("ErrorService")
public class ErrorService extends SqlService {

	@Resource(name = "dao")
	private Dao dao;

	/**
	 * 统计Error的相关数量
	 * 
	 * @param error
	 *            查询实体类
	 * @return result JSONObject类型查询结果
	 */
	public JSONObject getStatistics(com.bmm.entity.Error error) {

		// 初始化SQL语句
		String sql = S_SQL_ERROR_STATISTICS;
		// 初始化相关查询参数
		String modules = error.getModules();
		String startDate = error.getStartDate();
		String stopDate = error.getStopDate();

		// 处理相关参数及SQL
		String[] param = null;
		param = new String[1];
		param[0] = modules;
		// 判断是否做筛选,如果有对应的查询条件,则将将查询条件拼进SQL语句的WHERE条件中
		if (!"".equals(startDate)) {
			sql += " AND to_char(LOG_DATE,'yyyy-MM-dd') >= '" + startDate + "'";
		}
		if (!"".equals(stopDate)) {
			sql += " AND to_char(LOG_DATE,'yyyy-MM-dd') <= '" + stopDate + "'";
		}

		// 查询
		List list = dao.selectList(sql, param);
		// 将查询到的结果集转换为 JSONObject
		JSONObject result = JSONObject.fromObject(list.get(0));
		return result;
	}

	/**
	 * 查询错误信息
	 * 
	 * @param error
	 *            查询实体类
	 * @return JSONArray类型查询结果集
	 * @throws Exception
	 */
	public JSONObject getErrorList(com.bmm.entity.Error error, int rows, int page) throws Exception {

		// 初始化SQL语句
		String sql = S_SQL_ERROR_SEARCH;
		// 初始化相关查询参数
		String fileName = error.getFilename();
		String modules = error.getModules();
		String startDate = error.getStartDate();
		String stopDate = error.getStopDate();

		// 处理相关参数及SQL
		String[] param = null;
		param = new String[2];
		param[0] = modules;
		// filename忽略大小写,统一用小写查询
		param[1] = "%" + fileName.toLowerCase() + "%";
		// 判断是否做筛选,如果有对应的查询条件,则将将查询条件拼进SQL语句的WHERE条件中
		if (!"".equals(startDate)) {
			sql += " AND to_char(CREATE_DATE,'yyyy-MM-dd') >= '" + startDate + "'";
		}
		if (!"".equals(stopDate)) {
			sql += " AND to_char(CREATE_DATE,'yyyy-MM-dd') <= '" + stopDate + "'";
		}
		// 查询语句按照生成时间倒叙排列
		sql += " order by CREATE_DATE desc ";

		// 查询
		JSONObject queryResult = dao.selectListPage(sql, param, rows, page);
		//List list = (List) queryResult.get("List");
		// 将查询到的结果集转换为 JSONArray
		//JSONArray result = JSONArray.fromObject(list);
		return queryResult;
	}
}
