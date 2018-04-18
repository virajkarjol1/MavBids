package org.web.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.web.beans.Response;
import org.web.beans.User;
import org.web.dao.DBMgr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class SessionController {

	final static Logger logger = Logger.getLogger(SessionController.class);

	@Autowired
	DBMgr dbMgr;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	@ResponseBody
	public String login(HttpServletRequest request, @RequestParam("emailId") String emailId,
			@RequestParam("password") String password){
		Response r = new Response();
		ObjectMapper mapper = new ObjectMapper();

		User user = dbMgr.getUser(emailId);

		if(user != null){
			boolean status;
			status = user.verify(password);

			if(status == true){
				HttpSession session = request.getSession();
				session.setAttribute("hasAccess", "true");
				r.setResult(user);
			} else {
				r.setType("failed");
				r.setMessage("Invalid password");
			}
		}

		try {
			return mapper.writeValueAsString(r);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "{}";
		}
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	@ResponseBody
	public String logout(HttpSession session,String userName, String passwd){
		Response r = new Response();
		ObjectMapper mapper = new ObjectMapper();
		session.removeAttribute("hasAccess");

		try {
			r.setMessage("Successfully logged out");
			return mapper.writeValueAsString(r);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "{}";
		}
	}

	@RequestMapping(value = "/registerUser", method = RequestMethod.POST)
	public @ResponseBody String registerUser(User user){
		Response r = new Response();
		ObjectMapper mapper = new ObjectMapper();

		Boolean status = dbMgr.saveUser(user);

		try {
			r.setResult(status);
			return mapper.writeValueAsString(r);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "{}";
		}
	}

	@RequestMapping(value = "/ping", method = RequestMethod.GET)
	public @ResponseBody String ping(){
		Response r = new Response();
		ObjectMapper mapper = new ObjectMapper();

		try {
			r.setResult("Hello");
			return mapper.writeValueAsString(r);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "{}";
		}
	}

}
