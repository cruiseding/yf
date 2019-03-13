package com.wp.yf.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/index")
public class IndexController {

	@RequestMapping(value = {"/", "/welcome"})
	public ModelAndView index() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("msg", "Hello world!");
		mv.setViewName("/index");
		return mv;
	}
	
}
