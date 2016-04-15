package com.bmm.bean.common;

import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.bmm.service.OracleConnectionService;
import com.bmm.traffic.util.Logger;

public class BaseBean {

	private static Logger logger = Logger.getLogger(BaseBean.class);

	/**
	 * 直接对数据库进行查询操作
	 * 
	 * @param pSqlString	执行的sql语句
	 * @param pParameter	参数数组
	 * @return dataset 		ArrayList结果集
	 * @throws Exception
	 */
	public static ArrayList<ArrayList<String>> query(String pSqlString, String[] pParameter) throws Exception {
		// 初始化结果集
		ArrayList<ArrayList<String>> dataset = new ArrayList<ArrayList<String>>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			// 从OracleConnectionService连接池中获取连接
			conn = OracleConnectionService.GetInstance().GetConnection();
			pstmt = conn.prepareStatement(pSqlString, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			// 导入查询参数
			if (pParameter != null) {
				for (int i = 1; i <= pParameter.length; i++) {
					pstmt.setString(i, pParameter[i - 1]);
				}
			}
			// 执行查询
			rs = pstmt.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columns = rsmd.getColumnCount();
			if (columns != 0) {
				// 遍历结果集
				while (rs.next()) {
					ArrayList<String> row = new ArrayList<String>();
					for (int i = 0; i < columns; i++) {// 遍历每一行的列
						row.add(rs.getString(i + 1));
					}
					dataset.add(row);// 添加每行数据
				}
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
		} finally {
			try {
				// 关闭连接
				rs.close();
				pstmt.close();
				conn.close();
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		return dataset;
	}

	/**
	 * 将结果集分页转很成JSON,返回JSONArray
	 * 
	 * @param pSqlString		执行的sql语句
	 * @param pParameter		参数数组
	 * @param page
	 * @param rows
	 * @return returnV 			Vector分页结果集
	 * @throws SQLException
	 * @throws Exception
	 */
	protected static Vector extractJSONArray(String pSqlString, String[] pParameter, int page, int rows)
			throws SQLException, Exception {
		// 封装sql语句防止因为版本问题导致ordey by出错
		pSqlString = "select * from (" + pSqlString + ")";
		// 初始化分页结果集
		Vector returnV = new Vector();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			// 从OracleConnectionService连接池中获取连接
			conn = OracleConnectionService.GetInstance().GetConnection();
			pstmt = conn.prepareStatement(pSqlString, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			// 导入查询参数
			if (pParameter != null) {
				for (int i = 1; i <= pParameter.length; i++) {
					pstmt.setString(i, pParameter[i - 1]);// 放入参数
				}
			}
			// 执行查询
			rs = pstmt.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			// 获取列数
			int columns = rsmd.getColumnCount();
			int iRow = 0;
			rs.last();
			// 获取总行数
			int row = rs.getRow();

			rs.beforeFirst();
			if (rows != 0 && page != 1) {
				rs.absolute((page - 1) * rows);
			}
			JSONArray array = new JSONArray();
			JSONObject mapOfColValues = new JSONObject();

			// 遍历结果集
			if (columns != 0) {
				while (rs.next() && (iRow < rows || rows == 0)) {
					for (int i = 1; i <= columns; i++) {
						// 处理clob类型的列
						if ("CONTENTCLOB".equals(rsmd.getColumnLabel(i))) {
							Clob clob = rs.getClob("CONTENTCLOB");
							Reader inStream = clob.getCharacterStream();
							char[] c = new char[(int) clob.length()];
							try {
								inStream.read(c);
								String clobstr = new String(c);
								mapOfColValues.put(rsmd.getColumnLabel(i), clobstr);
								inStream.close();
							} catch (IOException e) {
								logger.error(e.getMessage());
							}
						} else {
							mapOfColValues.put(rsmd.getColumnLabel(i), rs.getObject(i));
						}
					}
					++iRow;
					array.add(mapOfColValues);
				}
			}
			// 总行数
			returnV.add(String.valueOf(row));
			returnV.add("rows");
			// 结果集
			returnV.add(array);
		} catch (SQLException e) {
			logger.error(e.getMessage());
		} finally {
			try {
				// 关闭连接
				rs.close();
				pstmt.close();
				conn.close();
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		return returnV;
	}

	/**
	 * 将结果集分页转换成JSON,返回JSONArray,用于下拉框
	 * 
	 * @param pSqlString	执行的sql语句
	 * @param pParameter	参数数组
	 * @param id			下拉框实际值
	 * @param name			下拉框显示值
	 * @return returnV 		结果集
	 * @throws SQLException
	 * @throws Exception
	 */
	protected static Vector extractComboArray(String pSqlString, String[] pParameter, String id, String name)
			throws SQLException, Exception {
		pSqlString = "select * from (" + pSqlString + ")";
		// 初始化结果集
		Vector returnV = new Vector();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			// 从OracleConnectionService连接池中获取连接
			conn = OracleConnectionService.GetInstance().GetConnection();
			pstmt = conn.prepareStatement(pSqlString, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			// 导入参数
			if (pParameter != null) {
				for (int i = 1; i <= pParameter.length; i++) {
					pstmt.setString(i, pParameter[i - 1]);
				}
			}
			// 执行sql
			rs = pstmt.executeQuery();
			JSONArray array = new JSONArray();
			JSONObject mapOfColValues = new JSONObject();
			while (rs.next()) {
				// 添加下拉框实际值
				mapOfColValues.put(id, rs.getObject(1));
				// 添加下拉框显示值
				mapOfColValues.put(name, rs.getObject(2));
				array.add(mapOfColValues);
			}
			// 保存下拉框数据
			returnV.add(array);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			try {
				rs.close();
				pstmt.close();
				conn.close();
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		return returnV;
	}

	/**
	 * 更新数据库
	 * 
	 * @param pSqlString	执行的sql语句
	 * @param pParameter	参数数组
	 * @return updateCount 	返回执行成功的条数
	 * @throws Exception
	 */
	public static int update(String pSqlString, String[] pParameter) throws Exception {
		int updateCount = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			// 从OracleConnectionService连接池中获取连接
			conn = OracleConnectionService.GetInstance().GetConnection();
			pstmt = conn.prepareStatement(pSqlString);
			// 导入参数
			if (pParameter != null) {
				for (int i = 1; i <= pParameter.length; i++) {
					pstmt.setString(i, pParameter[i - 1]);
				}
			}
			// 执行sql
			updateCount = pstmt.executeUpdate();
		} catch (SQLException e) {
			logger.error(e.getMessage());
		} finally {
			try {
				// 关闭连接
				pstmt.close();
				conn.close();
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		return updateCount;
	}
	
}
