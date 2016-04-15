package com.bmm.traffic.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bmm.traffic.bean.ManagerBean;
import com.bmm.traffic.util.Logger;

import net.sf.json.JSONArray;

/**
 * 处理bl#范围编辑操作
 * 
 * @author Administrator
 *
 */
public class ManagerServlet extends HttpServlet {

	protected Logger logger = Logger.getLogger(this.getClass());

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e1) {

			e1.printStackTrace();
		}
		response.setContentType("text/html;charset=UTF-8");

		// 初始化相关结果集
		Vector<?> jsonV = null;
		JSONArray jal = null;

		// 获取操作类型
		String action = request.getParameter("action");
		ManagerBean bean = new ManagerBean();
		// 导出前台传入参数
		loaddata(bean, request);

		// 加载树状表格信息
		if ("loadlist".equals(action)) {
			int page = Integer.parseInt(request.getParameter("page") == null ? "0" : request.getParameter("page"));// 当前页数
			int rows = Integer.parseInt(request.getParameter("rows") == null ? "0" : request.getParameter("rows"));// 行数
			try {
				// 获取分页数据
				jsonV = bean.getLoadList(page, rows);
				PrintWriter pw = response.getWriter();
				if (jsonV != null && jsonV.size() > 0) {
					// 最终处理：传回AJAX 结果集
					jal = (JSONArray) jsonV.get(2);
					for (int i = 0; i < jal.size(); i++) {
						Vector<?> subjsonV = null;
						// 返回的json对象
						JSONArray subjal = null;
						// 获取父节点id
						String PARENTID = jal.getJSONObject(i).getString("ID");
						bean.setType(request.getParameter("type"));
						bean.setParentid(PARENTID);
						// 根据父节点id获取子节点数据
						subjsonV = bean.getLoadChildList(0, 0);
						if (subjsonV != null && subjsonV.size() > 0) {
							subjal = (JSONArray) subjsonV.get(2);
							// 拼接出树状表格数据
							jal.getJSONObject(i).put("children", subjal);
							jal.getJSONObject(i).put("state", "closed");
						}
					}
					pw.print("{\"total\":" + jsonV.get(0) + ",\"rows\":" + jal.toString() + "}");// 返回前台
				} else {
					pw.print("{\"total\":0,\"rows\":[]}");
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
			// 加载编辑表格信息
		} else if ("loaddefaultlist".equals(action)) {
			int page = Integer.parseInt(request.getParameter("page") == null ? "0" : request.getParameter("page"));
			int rows = Integer.parseInt(request.getParameter("rows") == null ? "0" : request.getParameter("rows"));
			bean.setType(request.getParameter("type"));
			try {
				jsonV = bean.getLoadChildList(page, rows);
				PrintWriter pw = response.getWriter();
				if (jsonV != null && jsonV.size() > 0) {
					// 最终处理：传回AJAX 结果集
					jal = (JSONArray) jsonV.get(2);
					pw.print("{\"total\":" + jsonV.get(0) + ",\"rows\":" + jal.toString() + "}");
				} else {
					pw.print("{\"total\":0,\"rows\":[]}");
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
			// 加载local详情
		} else if ("loadlocation".equals(action)) {
			int page = Integer.parseInt(request.getParameter("page") == null ? "0" : request.getParameter("page"));
			int rows = Integer.parseInt(request.getParameter("rows") == null ? "0" : request.getParameter("rows"));

			try {
				jsonV = bean.getLoadlocation(page, rows);
				PrintWriter pw = response.getWriter();
				if (jsonV != null && jsonV.size() > 0) {
					// 最终处理：传回AJAX 结果集
					jal = (JSONArray) jsonV.get(2);
					pw.print("{\"total\":" + jsonV.get(0) + ",\"rows\":" + jal.toString() + "}");
				} else {
					pw.print("{\"total\":0,\"rows\":[]}");
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
			// 加载bp#详情
		} else if ("loadbp".equals(action)) {
			int page = Integer.parseInt(request.getParameter("page") == null ? "0" : request.getParameter("page"));
			int rows = Integer.parseInt(request.getParameter("rows") == null ? "0" : request.getParameter("rows"));

			try {
				jsonV = bean.loadBusinessPartner(page, rows);
				PrintWriter pw = response.getWriter();
				if (jsonV != null && jsonV.size() > 0) {
					// 最终处理：传回AJAX 结果集
					jal = (JSONArray) jsonV.get(2);
					pw.print("{\"total\":" + jsonV.get(0) + ",\"rows\":" + jal.toString() + "}");
				} else {
					pw.print("{\"total\":0,\"rows\":[]}");
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
			// 加载orign详情
		} else if ("loadeditorign".equals(action)) {
			int page = Integer.parseInt(request.getParameter("page") == null ? "0" : request.getParameter("page"));
			int rows = Integer.parseInt(request.getParameter("rows") == null ? "0" : request.getParameter("rows"));

			try {
				jsonV = bean.loadOrigin(page, rows);
				PrintWriter pw = response.getWriter();
				if (jsonV != null && jsonV.size() > 0) {
					// 最终处理：传回AJAX 结果集
					jal = (JSONArray) jsonV.get(2);
					pw.print("{\"total\":" + jsonV.get(0) + ",\"rows\":" + jal.toString() + "}");
				} else {
					pw.print("{\"total\":0,\"rows\":[]}");
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
			// 加载location下拉框
		} else if ("getlocation".equals(action)) {
			try {
				jsonV = bean.getLocationCombo();
				PrintWriter pw = response.getWriter();
				if (jsonV != null && jsonV.size() > 0) {
					// 最终处理：传回AJAX 结果集
					jal = (JSONArray) jsonV.get(0);
					pw.print(jal.toString());
				} else {
					pw.print("");
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
			// 加载bp#下拉框
		} else if ("getbpcode".equals(action)) {
			try {
				jsonV = bean.getBpCombo();
				PrintWriter pw = response.getWriter();
				if (jsonV != null && jsonV.size() > 0) {
					// 最终处理：传回AJAX 结果集
					jal = (JSONArray) jsonV.get(0);
					pw.print(jal.toString());
				} else {
					pw.print("");
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
			// 加载orign下拉框
		} else if ("getorign".equals(action)) {
			try {
				jsonV = bean.getOriginCombo();
				PrintWriter pw = response.getWriter();
				if (jsonV != null && jsonV.size() > 0) {
					// 最终处理：传回AJAX 结果集
					jal = (JSONArray) jsonV.get(0);
					pw.print(jal.toString());
				} else {
					pw.print("");
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
			// 添加修改location信息
		} else if ("editlocation".equals(action)) {
			String[] codes = request.getParameterValues("codes");
			// 循环遍历前台传入参数
			for (int i = 0; i < codes.length; i++) {
				String code = codes[i];
				bean.setCode(code);
				bean.setWavemax(request.getParameter(code + "wavemax"));
				bean.setWavemin(request.getParameter(code + "wavemin"));
				bean.setNowavemax(request.getParameter(code + "nowavemax"));
				bean.setNowavemin(request.getParameter(code + "nowavemin"));
				bean.setWavelast(request.getParameter(code + "wavelast"));
				bean.setNowavelast(request.getParameter(code + "nowavelast"));
				String editid = request.getParameter(code + "editid");
				if ("".equals(editid)) {
					// 执行insert操作
					try {
						bean.setLevel("3");
						bean.addLocation("");
						bean.setLevel("4");
						// 保存location的默认信息
						bean.addLocation("default");
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
				} else {
					// 保存location信息
					try {
						bean.setLevel("3");
						if ("default".equals(code)) {
							bean.updateDefault(request.getParameter(code + "editid"));
						} else {
							bean.updateLocation(request.getParameter(code + "editid"));
						}
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
				}
			}
			// 添加修改bp#信息
		} else if ("editbp".equals(action)) {
			String[] codes = request.getParameterValues("codes");
			// 循环遍历前台传入参数
			for (int i = 0; i < codes.length; i++) {
				String code = codes[i];
				bean.setCode(code);
				bean.setWavemax(request.getParameter(code + "wavemax"));
				bean.setWavemin(request.getParameter(code + "wavemin"));
				bean.setNowavemax(request.getParameter(code + "nowavemax"));
				bean.setNowavemin(request.getParameter(code + "nowavemin"));
				bean.setWavelast(request.getParameter(code + "wavelast"));
				bean.setNowavelast(request.getParameter(code + "nowavelast"));
				String editid = request.getParameter(code + "editid");
				// 执行insert操作
				if ("".equals(editid)) {
					try {
						// 添加bussiness party信息，等级为4
						bean.setLevel("4");
						bean.addbp("");
						// 添加bussiness party下的default信息，等级为5
						bean.setLevel("5");
						bean.addbp("default");
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
					// 修改
				} else {
					try {
						bean.setLevel("4");
						if ("default".equals(code)) {
							bean.updateDefault(request.getParameter(code + "editid"));
						} else {
							bean.updatebp(editid);
						}

					} catch (Exception e) {
						logger.error(e.getMessage());
					}
				}
			}
			// 添加修改orign信息
		} else if ("editorign".equals(action)) {
			String[] codes = request.getParameterValues("codes");
			bean.setLevel("5");
			// 循环遍历前台传入参数
			for (int i = 0; i < codes.length; i++) {
				String code = codes[i];
				bean.setCode(code);
				bean.setWavemax(request.getParameter(code + "wavemax"));
				bean.setWavemin(request.getParameter(code + "wavemin"));
				bean.setNowavemax(request.getParameter(code + "nowavemax"));
				bean.setNowavemin(request.getParameter(code + "nowavemin"));
				bean.setWavelast(request.getParameter(code + "wavelast"));
				bean.setNowavelast(request.getParameter(code + "nowavelast"));
				String editid = request.getParameter(code + "editid");
				// 执行insert操作
				if ("".equals(editid)) {
					try {
						bean.addorign();
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
				} else {
					try {
						bean.setLevel("5");
						if ("default".equals(code)) {
							bean.updateDefault(request.getParameter(code + "editid"));
						} else {
							bean.updateorign(editid);
						}
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
				}
			}
			// 删除信息
		} else if ("remove".equals(action)) {
			String[] IDS = request.getParameterValues("listid");
			try {
				bean.remove(IDS);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
	}

	/**
	 * 获取请求中相关参数并传入到bean中
	 * 
	 * @param bean
	 *            ManagerBean对象
	 * @param request
	 *            请求对象
	 */
	public void loaddata(ManagerBean bean, HttpServletRequest request) {
		bean.setLocationcode(request.getParameter("locationcode"));
		bean.setCode(request.getParameter("code"));
		bean.setBpcode(request.getParameter("bpcode"));
		bean.setLevel(request.getParameter("level"));
		bean.setParentid(request.getParameter("parentid"));
		bean.setParentcode(request.getParameter("parentcode"));
		bean.setWavemax(request.getParameter("wavemax"));
		bean.setWavemin(request.getParameter("wavemin"));
		bean.setNowavemax(request.getParameter("nowavemax"));
		bean.setNowavemin(request.getParameter("nowavemin"));
	}
}
