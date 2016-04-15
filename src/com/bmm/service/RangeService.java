package com.bmm.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.bmm.dao.Dao;
import com.bmm.entity.Range;
import com.bmm.traffic.util.PublicTools;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service("RangeService")
public class RangeService extends SqlService {

	@Resource(name = "dao")
	private Dao dao;

	/**
	 * 查询范围数据
	 * 
	 * @param range
	 *            查询实体类
	 * @return result JSONObject类型查询结果集
	 * @throws Exception
	 */
	public JSONObject getRangeList(Range range, int rows, int page) throws Exception {

		// 初始化SQL语句
		String sql = S_SQL_SEARCHLIST;
		// 初始化相关查询参数
		String level = range.getLevel();
		String code = range.getCode();
		String local = range.getLocal();
		// 处理相关参数及SQL
		String[] param = new String[5];
		param[0] = level;
		param[1] = "%" + code.toLowerCase() + "%";
		param[2] = "%" + code.toLowerCase() + "%";
		if("Editable".equals(range.getEditable())){
			param[3] = local.toLowerCase();
			param[4] = local.toLowerCase();
		}else{
			param[3] = "%%";
			param[4] = "%%";
		}
		// 查询父级信息
		JSONObject queryResult = dao.selectListPage(sql, param, rows, page);
		List list = (List) queryResult.get("List");

		// 初始化相关结果集
		JSONObject result = new JSONObject();
		JSONArray jsonArr = new JSONArray();

		if (list != null && list.size() > 0) {
			// 检查是否要查询出所有日志
			String isDelete = range.getIsDelete();
			// 循环每个父级信息
			for (int i = 0; i < list.size(); i++) {
				
				JSONObject json = JSONObject.fromObject(list.get(i));
				// 查询当前父级信息下所有的子集，根据parentId
				String parentId = json.getString("ID");
				// 初始化子集查询条件
				range = new Range();
				range.setParentId(parentId);
				range.setCode("");
				range.setIsDelete(isDelete);
				// 查询
				JSONObject subJson = getSubRangeList(range, 0, 0);
				JSONArray subJsonArr = JSONArray.fromObject(subJson.get("List"));
				// 如果存在子集，则将子集添加到父级内
				if (subJsonArr != null && subJsonArr.size() > 0) {
					json.put("children", subJsonArr);
					json.put("state", "closed");
				}
				jsonArr.add(json);
			}
			// 将条数统计和主体数据添加到JSONObject中
			result.accumulate("Count", queryResult.get("Count"));
			result.accumulate("Data", jsonArr);
		} else {
			result.accumulate("Count", 0);
			result.accumulate("Data", "[]");
		}
		return result;
	}

	/**
	 * 查询二级范围数据
	 * 
	 * @param range
	 *            查询实体类
	 * @return result JSONArray类型查询结果集
	 * @throws Exception
	 */
	public JSONObject getSubRangeList(Range range, int rows, int page) throws Exception {

		// 初始化SQL语句
		String sql = S_SQL_SEARCHCHILDLIST;

		// 初始化相关查询参数
		String parentId = range.getParentId();
		//System.out.println(parentId);
		String code = range.getCode();
		String isDelete = range.getIsDelete();

		// 处理相关参数及SQL
		String[] param = new String[3];
		param[0] = parentId;
		param[1] = "%" + code.toLowerCase() + "%";
		param[2] = "%" + code.toLowerCase() + "%";
		// 判断是否做筛选,如果有对应的查询条件,则将将查询条件拼进SQL语句的WHERE条件中
		if ("Active".equals(isDelete)) {
			sql += " and ISDELETE is null ";
		}
		sql += " ORDER BY CODE,ISDELETE DESC,a.ID DESC";

		// 查询
		JSONObject queryResult = dao.selectListPage(sql, param, rows, page);
//		List list = (List) queryResult.get("List");
//		// 将查询到的结果集转换为 JSONArray
//		JSONArray result = JSONArray.fromObject(list);
		return queryResult;
	}

	/**
	 * 根据APL_OFFICE的值加载BusinessPartner下拉框
	 * 
	 * @param code
	 *            APL_OFFICE值
	 * @return result JSONArray类型查询结果集
	 */
	public JSONArray getBpCombo(String code) {

		// 初始化SQL语句
		String sql = S_SQL_BPCODE;

		// 处理相关参数及SQL
		String[] param = new String[1];
		param[0] = code;

		// 查询
		List list = dao.selectList(sql, param);
		// 将查询到的结果集转换为 JSONArray
		JSONArray result = JSONArray.fromObject(list);
		return result;
	}

	/**
	 * 加载Origin下拉框
	 * 
	 * @return result JSONArray类型查询结果集
	 */
	public JSONArray getOriginCombo() {

		// 初始化SQL语句
		String sql = S_SQL_ORIGIN;

		// 处理相关参数及SQL
		String[] param = null;

		// 查询
		List list = dao.selectList(sql, param);
		// 将查询到的结果集转换为 JSONArray
		JSONArray result = JSONArray.fromObject(list);
		return result;
	}

	/**
	 * 根据Code值加载单条BusinessPartner的范围
	 * 
	 * @param code
	 *            BusinessPartner的Code值
	 * @return result JSONArray类型查询结果集,BusinessPartner的详细范围
	 */
	public JSONArray getBusinessPartner(Range range) {

		// 初始化SQL语句
		String sql = S_SQL_SEARCHBP;
		// 初始化相关查询参数
		String code = range.getCode();

		// 处理相关参数及SQL
		String[] param = new String[1];
		param[0] = code;

		// 查询
		List list = dao.selectList(sql, param);
		// 将查询到的结果集转换为 JSONArray
		return JSONArray.fromObject(list);
	}

	/**
	 * 根据Code值加载单条Origin的范围
	 * 
	 * @param code
	 *            Origin的Code值
	 * @param parentId
	 *            父级BusinessPartner的id
	 * @return result JSONArray类型查询结果集，Origin的详细范围
	 */
	public JSONArray getOrigin(String code, String parentId) {

		// 初始化SQL语句
		String sql = S_SQL_SEARCHORIGIN;

		// 处理相关参数及SQL
		String[] param = new String[2];
		param[0] = code;
		param[1] = parentId;

		// 查询
		List list = dao.selectList(sql, param);
		// 将查询到的结果集转换为 JSONArray
		return JSONArray.fromObject(list);
	}

	/**
	 * 执行新增或更新
	 * 
	 * @param list
	 *            编辑页面中新增到表单中的Range
	 */
	public void executeRange(List<Range> list) {

		// 循环遍历Range List
		for (Range range : list) {
			// 当Id不存在时则为新增，否则为更新
			if ("".equals(range.getId()) || range.getId() == null) {
				addRange(range);
			} else {
				updateRange(range);
			}
		}
	}

	/**
	 * 删除range
	 * 
	 * @param ids
	 *            编辑页面选中的range数据的主键id，按照","拼接
	 */
	public void deleteRange(String ids) {

		// 初始化SQL语句
		String sql = S_SQL_REMOVE;

		// 处理相关参数及SQL
		sql = sql.replaceAll("%s", ids);

		// 执行SQL
		dao.update(sql, null);
	}

	/**
	 * 新增范围，首先新增一条范围，如果该范围的level不为5，则新增一条该Range的default信息
	 * 
	 * @param range
	 *            本次新增的Range实体
	 */
	private void addRange(Range range) {
		// 初始化SQL语句
		String sql = "";
		sql = S_SQL_INSERT;

		// 处理相关参数及SQL
		String[] param = new String[9];
		param[0] = range.getLevel();
		param[1] = range.getCode();
		param[2] = range.getParentId();
		param[3] = range.getNonWave1Min();
		param[4] = range.getNonWave1Max();
		param[5] = range.getWave1Min();
		param[6] = range.getWave1Max();
		param[7] = range.getNonWave1Last();
		param[8] = range.getWave1Last();
		// 执行
		dao.update(sql, param);
		String level = range.getLevel();

		// 如果当前的level不为5时，则新增该Range对应的default
		if (!"5".equals(level)) {
			// 初始化SQL语句
			sql = S_SQL_INSERT_DEFAULT;
			// 处理相关参数及SQL
			param = new String[8];
			param[0] = Integer.valueOf(range.getLevel()) + 1 + "";
			param[1] = range.getNonWave1Min();
			param[2] = range.getNonWave1Max();
			param[3] = range.getWave1Min();
			param[4] = range.getWave1Max();
			param[5] = range.getNonWave1Min();
			param[6] = range.getWave1Min();
			param[7] = range.getCode();
			// 执行
			dao.update(sql, param);
		}
	}

	/**
	 * 更新范围
	 * 
	 * @param range
	 *            本次更新的Range实体
	 */
	private void updateRange(Range range) {

		// 处理相关参数及SQL
		String code = range.getCode();
		// 当更新default时
		if ("default".equals(code)) {
			String sql = "";
			// 将default的isDelete改为1
			sql = S_SQL_UPDATESTATUS;
			String[] param = null;
			param = new String[2];
			param[0] = range.getId() + "";
			param[1] = range.getParentId() + "";
			// 执行
			dao.update(sql, param);
			// 添加default信息
			sql = S_SQL_INSERT;
			param = new String[9];
			param[0] = range.getLevel();
			param[1] = range.getCode();
			param[2] = range.getParentId();
			param[3] = range.getNonWave1Min();
			param[4] = range.getNonWave1Max();
			param[5] = range.getWave1Min();
			param[6] = range.getWave1Max();
			param[7] = range.getNonWave1Last();
			param[8] = range.getWave1Last();
			dao.update(sql, param);
		} else {
			// 将当前范围的状态改为删除状态
			String sql = S_SQL_UPDATESTATUS;
			String[] parameter = null;
			parameter = new String[2];
			parameter[0] = range.getId();
			parameter[1] = range.getId();
			
			dao.update(sql, parameter);
			addRange(range);
//			// 新增一条Range
//			sql = S_SQL_INSERT;
//			// 处理相关参数及SQL
//			parameter = new String[9];
//			parameter[0] = range.getLevel();
//			parameter[1] = range.getCode();
//			parameter[2] = range.getParentId();
//			parameter[3] = range.getNonWave1Min();
//			parameter[4] = range.getNonWave1Max();
//			parameter[5] = range.getWave1Min();
//			parameter[6] = range.getWave1Max();
//			parameter[7] = range.getNonWave1Last();
//			parameter[8] = range.getWave1Last();
//			// 执行
//			dao.update(sql, parameter);
			// 修改之前Range下的子节点的paretendId,将之前的子节点的parentId指定到新的Range上
			sql = S_SQL_UPDATEPARTENDID;
			parameter = new String[2];
			parameter[0] = code;
			parameter[1] = range.getId();
			// 执行
			dao.update(sql, parameter);
		}
	}

}
