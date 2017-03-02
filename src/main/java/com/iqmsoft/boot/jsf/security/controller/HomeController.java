package com.iqmsoft.boot.jsf.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
	
	@RequestMapping("/login")
    public String getLoginPage() {
        return "login.xhtml";
    }
	
	@RequestMapping("/")
    public String getfirstPage() {
        return "home.xhtml";
    }
    

}
