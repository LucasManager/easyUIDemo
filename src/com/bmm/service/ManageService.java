package com.bmm.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.bmm.dao.Dao;

import net.sf.json.JSONObject;

@Service("ManageService")
public class ManageService {

	@Resource(name = "dao")
	private Dao dao;

	public JSONObject getUserloc(String userId) throws Exception {
		JSONObject user = null;
		String sql = "SELECT OFFICE, GROUP_ID, USERID FROM WEBEDIVIEW where USERID=?";
		String[] param = new String[1];
		param[0] = userId;
		user = JSONObject.fromObject(dao.selectOne(sql, param));
		return user;
	}
}
