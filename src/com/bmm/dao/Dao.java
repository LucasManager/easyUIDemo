package com.bmm.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import net.sf.json.JSONObject;

@Repository("dao")
public class Dao implements IDao {

	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	/**
	 * 更新数据
	 * 
	 * @param sql
	 * @param parameter
	 */
	public void update(String sql, String[] parameter) {
		jdbcTemplate.update(sql, parameter);
	}

	/**
	 * 批处理
	 * 
	 * @param sql
	 * @param parameter
	 */
	public void updateBatch(String sql, BatchPreparedStatementSetter parameter) {
		jdbcTemplate.batchUpdate(sql, parameter);
	}

	/**
	 * 返回一条查询结果
	 * 
	 * @param sql
	 * @param parameter
	 * @return
	 */
	public Map selectOne(String sql, String[] parameter) {
		return jdbcTemplate.queryForMap(sql, parameter);
	}

	/**
	 * 返回查询列表
	 * 
	 * @param sql
	 * @param parameter
	 * @return
	 */
	public List selectList(String sql, String[] parameter) {
		return jdbcTemplate.queryForList(sql, parameter);
	}

	/**
	 * 返回查询列表并分页
	 * 
	 * @param sql
	 * @param parameter
	 * @param rows
	 * @param page
	 * @return
	 */
	public JSONObject selectListPage(String sql, String[] parameter, int rows, int page) {
		JSONObject result = new JSONObject();
		List list = jdbcTemplate.queryForList(sql, parameter);
//		int count = list.size();
//		int fromIndex = (page - 1) * rows;
//		if(rows == 0){
//			rows = list.size();
//		}
//		int toIndex = fromIndex + rows;
//		if (toIndex > list.size()) {
//			toIndex = list.size();
//		}
		int count = list.size();
		int fromIndex = (page - 1) * rows;
		int toIndex = fromIndex + rows;
		if (toIndex > list.size() || rows == 0) {
			toIndex = list.size();
		}
		list = list.subList(fromIndex, toIndex);
		result.accumulate("Count", count);
		result.accumulate("List", list);
		return result;
	}

}
