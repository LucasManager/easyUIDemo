package com.bmm.service;

import java.util.List;
import java.util.Vector;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.bmm.dao.Dao;
import com.bmm.entity.Email;
import com.bmm.traffic.util.PublicTools;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service("EmailService")
public class EmailService extends SqlService {

	@Resource(name = "dao")
	private Dao dao;

	/**
	 * ͳ��Email��������ͼ���
	 * 
	 * @param email
	 *            ��ѯʵ����
	 * @return result JSONArray���Ͳ�ѯ���
	 */
	public JSONArray getStatistics(Email email) {

		// ��ʼ��SQL���
		String sql = S_SQL_EMAIL_STATISTICS;
		// ��ʼ����ز�ѯ����
		String mailType = email.getMailType();
		String address = email.getAddress();
		String status = email.getStatus();
		String startDate = email.getStartDate();
		String stopDate = email.getStopDate();

		// ������ز���SQL
		String[] param = null;
		// �ж��Ƿ���ɸѡ,����ж�Ӧ�Ĳ�ѯ���,�򽫽���ѯ���ƴ��SQL����WHERE�����
		if (!"".equals(mailType) && !"all".equals(mailType)) {
			sql += " AND MAIL_TYPE = '" + mailType + "'";
		}
		if (!"".equals(address)) {
			sql += " AND address like '%" + address + "%'";
		}
		if (!"".equals(status) && !"all".equals(status)) {
			sql += " AND STATUS = '" + status + "'";
		}
		if (!"".equals(startDate)) {
			sql += " AND to_char(CREATEDATE,'yyyy-MM-dd') >= '" + startDate + "'";
		}
		if (!"".equals(stopDate)) {
			sql += " AND to_char(CREATEDATE,'yyyy-MM-dd') <= '" + stopDate + "'";
		}
		sql += " group by STATUS ";

		// ��ѯ
		List list = dao.selectList(sql, param);
		// ����ѯ���Ľ��ת��Ϊ JSONArray
		JSONArray result = JSONArray.fromObject(list);
		return result;
	}

	/**
	 * ��ѯ�ʼ���Ϣ
	 * 
	 * @param email
	 *            ��ѯʵ����
	 * @return result JSONArray���Ͳ�ѯ���
	 * @throws Exception
	 */
	public JSONObject getEmailList(Email email, int rows, int page) throws Exception {

		// ��ʼ��SQL���
		String sql = S_SQL_EMAIL_SEARCH;
		// ��ʼ����ز�ѯ����
		String mailType = email.getMailType();
		String address = email.getAddress();
		String status = email.getStatus();
		String startDate = email.getStartDate();
		String stopDate = email.getStopDate();

		// ������ز���SQL
		String[] param = null;
		// �ж��Ƿ���ɸѡ,����ж�Ӧ�Ĳ�ѯ���,�򽫽���ѯ���ƴ��SQL����WHERE�����
		if (!"all".equals(mailType) && !"".equals(mailType)) {
			sql += " AND MAIL_TYPE = '" + mailType + "'";
		}
		if (!"".equals(address)) {
			sql += " AND lower(address) like '%" + address.toLowerCase() + "%'";
		}
		if (!"all".equals(status) && !"".equals(status)) {
			sql += " AND STATUS = '" + status + "'";
		}
		if (!"".equals(startDate)) {
			sql += " AND to_char(CREATEDATE,'yyyy-MM-dd') >= '" + startDate + "'";
		}
		if (!"".equals(stopDate)) {
			sql += " AND to_char(CREATEDATE,'yyyy-MM-dd') <= '" + stopDate + "'";
		}

		// ��ѯ��䰴�����ʱ�䵹������
		sql += " ORDER BY createdate DESC ";

		// ��ѯ
		JSONObject queryResult = dao.selectListPage(sql, param, rows, page);
		//List list = (List) queryResult.get("List");
		// ����ѯ���Ľ��ת��Ϊ JSONArray
		//JSONObject result = JSONArray.fromObject(list);
		return queryResult;
	}

	/**
	 * ��ʱˢ��ҳ�����
	 * 
	 * @param ids
	 *            ����ǰ��ҳ����ʼ���Ϣ���id��","ƴ��
	 * @return result JSONArray���Ͳ�ѯ���
	 * @throws Exception
	 */
	public JSONArray refreshStatus(String ids) throws Exception {

		// ��ʼ��SQL���
		String sql = S_SQL_EMAIL_REFRESHSTATUS;
		// ������ز���SQL
		if (ids != null && !"".equals(ids)) {
			sql += " WHERE ID IN (%s) ORDER BY createdate DESC";
			sql = sql.replaceAll("%s", ids);
		}
		String[] param = null;

		// ��ѯ
		List list = dao.selectList(sql, param);
		// ����ѯ���Ľ��ת��Ϊ JSONArray
		JSONArray result = JSONArray.fromObject(list);
		return result;
	}

	/**
	 * �����ʼ�״̬
	 * 
	 * @param ids
	 *            ��","ƴ�ӵ�����ʼ�id���
	 * 
	 */
	public void updateEmailStatus(String ids) {

		// ��ʼ��SQL���
		String sql = S_SQL_EMAIL_UPDATE_STATUS;
		// ������ز���SQL
		if (ids != null && "".equals(ids)) {
			sql = sql.replaceAll("%s", ids);
		}
		String[] param = new String[1];
		// ���µ�״̬��Ϊ"wait"
		param[0] = "wait";

		// ����
		dao.update(sql, param);
	}
}
