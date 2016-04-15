package com.bmm.dao;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import net.sf.json.JSONObject;

public interface IDao {
	
	/**
	 * 更新数据
	 * @param sql
	 * @param parameter
	 */
	public void update(String sql, String[] parameter);
	
	/**
	 * 批处理
	 * @param sql
	 * @param parameter
	 */
	public void updateBatch(String sql, BatchPreparedStatementSetter parameter);
	
	/**
	 * 返回一条查询结果
	 * @param sql
	 * @param parameter
	 * @return
	 */
	public Object selectOne(String sql, String[] parameter);
	
	/**
	 * 返回查询列表
	 * @param sql
	 * @param parameter
	 * @return
	 */
	public List selectList(String sql, String[] parameter);
	
	/**
	 * 返回查询列表并分页
	 * @param sql
	 * @param parameter
	 * @param rows
	 * @param page
	 * @return
	 */
	public JSONObject selectListPage(String sql, String[] parameter, int rows, int page);
}
