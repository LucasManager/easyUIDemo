package com.bmm.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.bmm.service.OracleConnectionService;

public class OpenConnectListener implements ServletContextListener {

	/**
	 * �˳����ʱ�Ͽ�l��
	 * 
	 */
	public void contextDestroyed(ServletContextEvent event) {
		try {
			OracleConnectionService.Finalize();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ʼ����ݿ�l��
	 * 
	 */
	public void contextInitialized(ServletContextEvent event) {
		try {
			OracleConnectionService.Initialize();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
