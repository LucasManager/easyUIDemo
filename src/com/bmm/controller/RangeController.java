package com.bmm.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bmm.entity.Range;
import com.bmm.service.RangeService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
public class RangeController extends BaseController {

	private Range range;
	@Resource(name = "RangeService")
	private RangeService service;

	/**
	 * 查询范围主页面数据
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/Range", method = RequestMethod.POST)
	public void getRangeList(HttpServletRequest request, HttpServletResponse response) {

		// 统一request与response编码
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.toString());
		}
		response.setContentType("text/html;charset=UTF-8");

		// 获取对应请求参数
		String type = request.getParameter("type");
		String code = request.getParameter("code");
		String level = request.getParameter("level");
		String editable = request.getParameter("editable");
		String local = (String) request.getSession().getAttribute("local");
		
		int rows = Integer.parseInt(request.getParameter("rows") == null ? "0" : request.getParameter("rows"));
		int page = Integer.parseInt(request.getParameter("page") == null ? "0" : request.getParameter("page"));

		// 将查询参数初始化到entity中
		range = new Range();
		range.setIsDelete(type);
		range.setCode(code);
		range.setLevel(level);
		range.setEditable(editable);
		range.setLocal(local);
		// 初始化结果集
		JSONObject result = null;
		try {
			// 获取范围数据
			result = service.getRangeList(range, rows, page);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		// 输出到页面
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
		} catch (IOException e) {
			logger.error(e.toString());
		}
		// 返回前台
		pw.print("{\"total\":" + result.getString("Count") + ",\"rows\":" + result.getString("Data") + "}");
	}

	/**
	 * 查询范围详细页面数据
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/RangeDetail", method = RequestMethod.GET)
	public void addRangeList(HttpServletRequest request, HttpServletResponse response){
		// 统一request与response编码
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.toString());
		}
		response.setContentType("text/html;charset=UTF-8");

		// 获取对应请求参数
		String parentId = request.getParameter("parentId");
		String code = "";
		try {
			code = new String(request.getParameter("code").getBytes("iso-8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.toString());
		}
		System.out.println(code);
		String type = request.getParameter("type");
		int rows = Integer.parseInt(request.getParameter("rows") == null ? "0" : request.getParameter("rows"));
		int page = Integer.parseInt(request.getParameter("page") == null ? "0" : request.getParameter("page"));

		// 将查询参数初始化到entity中
		range = new Range();
		range.setParentId(parentId);
		range.setCode(code);
		range.setIsDelete(type);

		// 初始化结果集
		JSONObject result = null;
		try {
			// 获取二级范围数据
			result = service.getSubRangeList(range, rows, page);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		// 输出到页面
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
		} catch (IOException e) {
			logger.error(e.toString());
		}
		// 返回前台
		pw.print("{\"total\":" + result.getString("Count") + ",\"rows\":" + result.getString("List") + "}");
	}

	/**
	 * 查询任意Location下的所有BusinessPartner返回成下拉框需要的格式
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/BpCombo", method = RequestMethod.GET)
	public void getBpCombo(HttpServletRequest request, HttpServletResponse response) {
		// 统一request与response编码
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.toString());
		}
		response.setContentType("text/html;charset=UTF-8");

		// 获取对应请求参数
		String code = request.getParameter("code");

		// 初始化结果集
		JSONArray result = null;
		// 查询
		result = service.getBpCombo(code);
		// 输出到页面
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
		} catch (IOException e) {
			logger.error(e.toString());
		}
		// 返回前台
		pw.print(result.toString());
	}

	/**
	 * 查询出所有Origin返回成下拉框需要的格式
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/OriginCombo", method = RequestMethod.GET)
	public void getOriginCombo(HttpServletRequest request, HttpServletResponse response) {
		// 统一request与response编码
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.toString());
		}
		response.setContentType("text/html;charset=UTF-8");

		// 初始化结果集
		JSONArray result = null;
		// 查询
		result = service.getOriginCombo();
		// 输出到页面
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
		} catch (IOException e) {
			logger.error(e.toString());
		}
		// 返回前台
		pw.print(result.toString());
	}

	/**
	 * 查询出BusinessPartner的详细范围，当在详情页面点击"+"或"edit"时
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/BusinessPartner", method = RequestMethod.GET)
	public void loadBusinessPartner(HttpServletRequest request, HttpServletResponse response) {
		// 统一request与response编码
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.toString());
		}
		response.setContentType("text/html;charset=UTF-8");

		// 获取对应请求参数
		String code = request.getParameter("code");

		// 将查询参数初始化到entity中
		range = new Range();
		range.setCode(code);

		// 初始化结果集
		JSONArray result = null;
		// 查询
		result = service.getBusinessPartner(range);
		// 输出到页面
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
		} catch (IOException e) {
			logger.error(e.toString());
		}
		// 返回前台
		pw.print("{\"rows\":" + result.toString() + "}");
	}

	/**
	 * 查询出Origin的详细范围，当在详情页面点击"+"或"edit"时
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/Origin", method = RequestMethod.GET)
	public void loadOrigin(HttpServletRequest request, HttpServletResponse response) {
		// 统一request与response编码
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.toString());
		}
		response.setContentType("text/html;charset=UTF-8");

		// 获取对应请求参数
		String code = request.getParameter("code");
		String parentId = request.getParameter("parentId");

		// 将查询参数初始化到entity中
		range = new Range();
		range.setCode(code);
		range.setParentId(parentId);

		// 初始化结果集
		JSONArray result = null;
		// 查询
		result = service.getOrigin(code, parentId);
		// 输出到页面
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 返回前台
		pw.print("{\"rows\":" + result.toString() + "}");
	}

	/**
	 * 新增或编辑范围
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/RangeDetail", method = RequestMethod.POST)
	public void updateRange(HttpServletRequest request, HttpServletResponse response) {
		// 统一request与response编码
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.toString());
		}
		response.setContentType("text/html;charset=UTF-8");

		// 获取对应请求参数
		String level = request.getParameter("level");
		// 页面上传过来的 level + 1 等于本次添加的范围level
		level = Integer.valueOf(level) + 1 + "";
		String[] codes = request.getParameterValues("codes");
		List<Range> list = new ArrayList<Range>();

		// 循环遍历传入的code值，根据每个CODE值获取对应范围参数
		for (String code : codes) {
			Range range = new Range();
			range.setId(request.getParameter(code + "editid"));
			range.setCode(code);
			range.setLevel(level);
			range.setParentId(request.getParameter("parentid"));
			range.setNonWave1Min(request.getParameter(code + "nowavemin"));
			range.setNonWave1Max(request.getParameter(code + "nowavemax"));
			range.setNonWave1Last(request.getParameter(code + "nowavelast"));
			range.setWave1Min(request.getParameter(code + "wavemin"));
			range.setWave1Max(request.getParameter(code + "wavemax"));
			range.setWave1Last(request.getParameter(code + "wavelast"));
			// 将Range添加到list中
			list.add(range);
		}
		// 执行
		service.executeRange(list);
	}

	/**
	 * 删除范围
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/RangeDetail", method = RequestMethod.DELETE)
	public void deleteRange(HttpServletRequest request, HttpServletResponse response) {

		// 统一request与response编码
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.toString());
		}

		// 获取对应请求参数
		String ids = request.getParameter("ids");
		// 删除
		service.deleteRange(ids);
		try {
			response.getWriter().write("success");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
