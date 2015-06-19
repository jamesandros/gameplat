package com.outwit.gameplat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.outwit.das.permission.model.User;
import com.outwit.das.permission.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
 	@RequestMapping
	@ResponseBody
	public User test(){
		return new User();
	}
}
