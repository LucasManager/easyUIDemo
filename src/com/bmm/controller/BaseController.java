package com.bmm.controller;

import org.springframework.web.servlet.ModelAndView;

import com.bmm.traffic.util.Logger;

public class BaseController {
	
	protected Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * µÃµ½ModelAndView
	 */
	public ModelAndView getModelAndView() {
		return new ModelAndView();
	}
	
}
