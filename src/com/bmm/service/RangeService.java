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
	 * ��ѯ��Χ����
	 * 
	 * @param range
	 *            ��ѯʵ����
	 * @return result JSONObject���Ͳ�ѯ�����
	 * @throws Exception
	 */
	public JSONObject getRangeList(Range range, int rows, int page) throws Exception {

		// ��ʼ��SQL���
		String sql = S_SQL_SEARCHLIST;
		// ��ʼ����ز�ѯ����
		String level = range.getLevel();
		String code = range.getCode();
		String local = range.getLocal();
		// ������ز�����SQL
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
		// ��ѯ������Ϣ
		JSONObject queryResult = dao.selectListPage(sql, param, rows, page);
		List list = (List) queryResult.get("List");

		// ��ʼ����ؽ����
		JSONObject result = new JSONObject();
		JSONArray jsonArr = new JSONArray();

		if (list != null && list.size() > 0) {
			// ����Ƿ�Ҫ��ѯ��������־
			String isDelete = range.getIsDelete();
			// ѭ��ÿ��������Ϣ
			for (int i = 0; i < list.size(); i++) {
				
				JSONObject json = JSONObject.fromObject(list.get(i));
				// ��ѯ��ǰ������Ϣ�����е��Ӽ�������parentId
				String parentId = json.getString("ID");
				// ��ʼ���Ӽ���ѯ����
				range = new Range();
				range.setParentId(parentId);
				range.setCode("");
				range.setIsDelete(isDelete);
				// ��ѯ
				JSONObject subJson = getSubRangeList(range, 0, 0);
				JSONArray subJsonArr = JSONArray.fromObject(subJson.get("List"));
				// ��������Ӽ������Ӽ����ӵ�������
				if (subJsonArr != null && subJsonArr.size() > 0) {
					json.put("children", subJsonArr);
					json.put("state", "closed");
				}
				jsonArr.add(json);
			}
			// ������ͳ�ƺ������������ӵ�JSONObject��
			result.accumulate("Count", queryResult.get("Count"));
			result.accumulate("Data", jsonArr);
		} else {
			result.accumulate("Count", 0);
			result.accumulate("Data", "[]");
		}
		return result;
	}

	/**
	 * ��ѯ������Χ����
	 * 
	 * @param range
	 *            ��ѯʵ����
	 * @return result JSONArray���Ͳ�ѯ�����
	 * @throws Exception
	 */
	public JSONObject getSubRangeList(Range range, int rows, int page) throws Exception {

		// ��ʼ��SQL���
		String sql = S_SQL_SEARCHCHILDLIST;

		// ��ʼ����ز�ѯ����
		String parentId = range.getParentId();
		//System.out.println(parentId);
		String code = range.getCode();
		String isDelete = range.getIsDelete();

		// ������ز�����SQL
		String[] param = new String[3];
		param[0] = parentId;
		param[1] = "%" + code.toLowerCase() + "%";
		param[2] = "%" + code.toLowerCase() + "%";
		// �ж��Ƿ���ɸѡ,����ж�Ӧ�Ĳ�ѯ����,�򽫽���ѯ����ƴ��SQL����WHERE������
		if ("Active".equals(isDelete)) {
			sql += " and ISDELETE is null ";
		}
		sql += " ORDER BY CODE,ISDELETE DESC,a.ID DESC";

		// ��ѯ
		JSONObject queryResult = dao.selectListPage(sql, param, rows, page);
//		List list = (List) queryResult.get("List");
//		// ����ѯ���Ľ����ת��Ϊ JSONArray
//		JSONArray result = JSONArray.fromObject(list);
		return queryResult;
	}

	/**
	 * ����APL_OFFICE��ֵ����BusinessPartner������
	 * 
	 * @param code
	 *            APL_OFFICEֵ
	 * @return result JSONArray���Ͳ�ѯ�����
	 */
	public JSONArray getBpCombo(String code) {

		// ��ʼ��SQL���
		String sql = S_SQL_BPCODE;

		// ������ز�����SQL
		String[] param = new String[1];
		param[0] = code;

		// ��ѯ
		List list = dao.selectList(sql, param);
		// ����ѯ���Ľ����ת��Ϊ JSONArray
		JSONArray result = JSONArray.fromObject(list);
		return result;
	}

	/**
	 * ����Origin������
	 * 
	 * @return result JSONArray���Ͳ�ѯ�����
	 */
	public JSONArray getOriginCombo() {

		// ��ʼ��SQL���
		String sql = S_SQL_ORIGIN;

		// ������ز�����SQL
		String[] param = null;

		// ��ѯ
		List list = dao.selectList(sql, param);
		// ����ѯ���Ľ����ת��Ϊ JSONArray
		JSONArray result = JSONArray.fromObject(list);
		return result;
	}

	/**
	 * ����Codeֵ���ص���BusinessPartner�ķ�Χ
	 * 
	 * @param code
	 *            BusinessPartner��Codeֵ
	 * @return result JSONArray���Ͳ�ѯ�����,BusinessPartner����ϸ��Χ
	 */
	public JSONArray getBusinessPartner(Range range) {

		// ��ʼ��SQL���
		String sql = S_SQL_SEARCHBP;
		// ��ʼ����ز�ѯ����
		String code = range.getCode();

		// ������ز�����SQL
		String[] param = new String[1];
		param[0] = code;

		// ��ѯ
		List list = dao.selectList(sql, param);
		// ����ѯ���Ľ����ת��Ϊ JSONArray
		return JSONArray.fromObject(list);
	}

	/**
	 * ����Codeֵ���ص���Origin�ķ�Χ
	 * 
	 * @param code
	 *            Origin��Codeֵ
	 * @param parentId
	 *            ����BusinessPartner��id
	 * @return result JSONArray���Ͳ�ѯ�������Origin����ϸ��Χ
	 */
	public JSONArray getOrigin(String code, String parentId) {

		// ��ʼ��SQL���
		String sql = S_SQL_SEARCHORIGIN;

		// ������ز�����SQL
		String[] param = new String[2];
		param[0] = code;
		param[1] = parentId;

		// ��ѯ
		List list = dao.selectList(sql, param);
		// ����ѯ���Ľ����ת��Ϊ JSONArray
		return JSONArray.fromObject(list);
	}

	/**
	 * ִ�����������
	 * 
	 * @param list
	 *            �༭ҳ���������������е�Range
	 */
	public void executeRange(List<Range> list) {

		// ѭ������Range List
		for (Range range : list) {
			// ��Id������ʱ��Ϊ����������Ϊ����
			if ("".equals(range.getId()) || range.getId() == null) {
				addRange(range);
			} else {
				updateRange(range);
			}
		}
	}

	/**
	 * ɾ��range
	 * 
	 * @param ids
	 *            �༭ҳ��ѡ�е�range���ݵ�����id������","ƴ��
	 */
	public void deleteRange(String ids) {

		// ��ʼ��SQL���
		String sql = S_SQL_REMOVE;

		// ������ز�����SQL
		sql = sql.replaceAll("%s", ids);

		// ִ��SQL
		dao.update(sql, null);
	}

	/**
	 * ������Χ����������һ����Χ������÷�Χ��level��Ϊ5��������һ����Range��default��Ϣ
	 * 
	 * @param range
	 *            ����������Rangeʵ��
	 */
	private void addRange(Range range) {
		// ��ʼ��SQL���
		String sql = "";
		sql = S_SQL_INSERT;

		// ������ز�����SQL
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
		// ִ��
		dao.update(sql, param);
		String level = range.getLevel();

		// �����ǰ��level��Ϊ5ʱ����������Range��Ӧ��default
		if (!"5".equals(level)) {
			// ��ʼ��SQL���
			sql = S_SQL_INSERT_DEFAULT;
			// ������ز�����SQL
			param = new String[8];
			param[0] = Integer.valueOf(range.getLevel()) + 1 + "";
			param[1] = range.getNonWave1Min();
			param[2] = range.getNonWave1Max();
			param[3] = range.getWave1Min();
			param[4] = range.getWave1Max();
			param[5] = range.getNonWave1Min();
			param[6] = range.getWave1Min();
			param[7] = range.getCode();
			// ִ��
			dao.update(sql, param);
		}
	}

	/**
	 * ���·�Χ
	 * 
	 * @param range
	 *            ���θ��µ�Rangeʵ��
	 */
	private void updateRange(Range range) {

		// ������ز�����SQL
		String code = range.getCode();
		// ������defaultʱ
		if ("default".equals(code)) {
			String sql = "";
			// ��default��isDelete��Ϊ1
			sql = S_SQL_UPDATESTATUS;
			String[] param = null;
			param = new String[2];
			param[0] = range.getId() + "";
			param[1] = range.getParentId() + "";
			// ִ��
			dao.update(sql, param);
			// ����default��Ϣ
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
			// ����ǰ��Χ��״̬��Ϊɾ��״̬
			String sql = S_SQL_UPDATESTATUS;
			String[] parameter = null;
			parameter = new String[2];
			parameter[0] = range.getId();
			parameter[1] = range.getId();
			
			dao.update(sql, parameter);
			addRange(range);
//			// ����һ��Range
//			sql = S_SQL_INSERT;
//			// ������ز�����SQL
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
//			// ִ��
//			dao.update(sql, parameter);
			// �޸�֮ǰRange�µ��ӽڵ��paretendId,��֮ǰ���ӽڵ��parentIdָ�����µ�Range��
			sql = S_SQL_UPDATEPARTENDID;
			parameter = new String[2];
			parameter[0] = code;
			parameter[1] = range.getId();
			// ִ��
			dao.update(sql, parameter);
		}
	}

}