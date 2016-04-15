package com.bmm.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.bmm.service.ManageService;

import net.sf.json.JSONObject;

@Controller
public class ManageController extends BaseController {

	@Resource(name = "ManageService")
	private ManageService service;
	
	/**
	 * 登录验证
	 * 根据userId查询出用户的location
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/login/{id}")
	public ModelAndView validateUser(@PathVariable("id") String id,HttpServletRequest request){
		
		ModelAndView mv = this.getModelAndView();
		String userId = id;
		JSONObject user = null;
		try {
			user = service.getUserloc(userId);
		} catch (Exception e) {
			mv.setViewName("privs");
			return mv;
		}
		request.getSession().setAttribute("local", user.getString("OFFICE"));
		mv.setViewName("main");
		return mv;
	}

}
