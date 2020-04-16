package com.autotest.controller;

import static org.springframework.http.HttpStatus.OK;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.autotest.exception.KellyException;
import com.autotest.model.TestSuitNumbers;
import com.autotest.service.TestExecutorService;

@RestController
public class AutoTestController {
	
	@Autowired
	private TestExecutorService testExecutorService;
	
	@GetMapping("/hello")
	public ResponseEntity<Object> getHello(){
		return new ResponseEntity<Object>("Hello Mukesh ", HttpStatus.OK);
	}
	
	@PostMapping("/execute")
	public ResponseEntity<Object> executeTest(@RequestBody TestSuitNumbers tetsSuitNumbers) throws IOException, KellyException{
		testExecutorService.testExecute(tetsSuitNumbers);
		
		return new ResponseEntity<Object>(tetsSuitNumbers.getTestSuitNumbers() + " Tests execution Completed!!", OK);	
	}

}
