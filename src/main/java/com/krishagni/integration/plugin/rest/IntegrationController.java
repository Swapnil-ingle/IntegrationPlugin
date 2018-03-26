package com.krishagni.integration.plugin.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.krishagni.integration.plugin.core.InstituteImporter;

@Controller
@RequestMapping("/integration")
public class IntegrationController {
	
	@Autowired
	private InstituteImporter instituteImporter;
	
	@RequestMapping(method = RequestMethod.GET)
	public void importerMain() {
		instituteImporter.importObjects();
	}
}
