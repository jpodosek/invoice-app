package com.theironyard.invoicify.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.theironyard.invoicify.models.User;
import com.theironyard.invoicify.models.UserRole;
import com.theironyard.invoicify.repositories.UserRepository;
import com.theironyard.invoicify.repositories.UserRoleRepository;

@Controller
@RequestMapping("/admin/tools")
public class AdminToolsController {
	
	private UserRepository userRepo;
	private UserRoleRepository userRoleRepo;
	private BCryptPasswordEncoder passwordEncoder;
	private List<UserRole> roles;
//	public AdminToolsController() {};
	
	public AdminToolsController(UserRepository userRepo, UserRoleRepository userRoleRepo) {
		this.userRepo = userRepo;
		this.userRoleRepo = userRoleRepo;
		passwordEncoder = new BCryptPasswordEncoder();
	}

	@GetMapping("")
	public ModelAndView listUsers() {
		ModelAndView mv = new ModelAndView();
	
//		List<User> users = userRepo.findAll();
//		List<UserRole> userRoles = userRoleRepo.findAll();
	
		mv.addObject("users", userRepo.findAll());
		mv.addObject("userRoles", userRoleRepo.findAll());
		mv.setViewName("admin/default");
		return mv;
	}
	
	@PostMapping("create")
	public ModelAndView createUser(User user, long userRoleId, String username, String password) {	
		ModelAndView mv = new ModelAndView();
		
		try {
		String hashedPassword = passwordEncoder.encode(password);
		UserRole userRole = userRoleRepo.findOne(userRoleId);
		String roleName = userRole.getName();
		
		user = new User();
		//userRole.setUser(user);
		user.setUsername(username);
		user.setPassword(hashedPassword);
		roles = new ArrayList<UserRole>();
		roles.add(new UserRole(roleName, user));
		user.setRoles(roles);
		//userRoleRepo.save(userRole);
		userRepo.save(user);
	
		
		mv.setViewName("redirect:/admin/tools");
		} catch (DataIntegrityViolationException dive)
		{
			System.err.println(dive);
//			List<User> users = userRepo.findAll();
//			List<UserRole> userRoles = userRoleRepo.findAll();
		
			mv.addObject("users", userRepo.findAll());
			mv.addObject("userRoles", userRoleRepo.findAll());
			mv.addObject("errorMessage", "That username already exists.");
			mv.setViewName("admin/default");
		}
		
		return mv;
		
	}
}
