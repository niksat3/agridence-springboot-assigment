package com.agridence.microservice.Assignment.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController	
public class SampleController {
	
	@GetMapping("/")
		public String index() {
			return "Welcome to spring boot assignment!";
		}

}
