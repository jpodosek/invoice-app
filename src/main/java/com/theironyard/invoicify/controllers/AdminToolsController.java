package com.theironyard.invoicify.controllers;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.theironyard.invoicify.models.User;
import com.theironyard.invoicify.repositories.UserRepository;

@Controller
@RequestMapping("/admin/tools")
public class AdminToolsController {
	
	private UserRepository userRepo;
	
//	public AdminToolsController() {};
	
	public AdminToolsController(UserRepository userRepo) {
		this.userRepo = userRepo;
	}

	@GetMapping("")
	public ModelAndView listUsers() {
		//User user = (User) auth.getPrincipal();
		
		List<User> users = userRepo.findAll();
		//UserRole userRole = userRepo.find
		//List<User> users = userRepo.findAllAndRolesName(user);
		ModelAndView mv = new ModelAndView("admin/default");
		mv.addObject("users", users);
		return mv;
	}
}
