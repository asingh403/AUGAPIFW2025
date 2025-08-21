package com.qa.api.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

public class SchemaValidator {
	
	private static final Logger logger = LogManager.getLogger(SchemaValidator.class);
	
	public static boolean validateSchema(Response response, String schemaFileName) {
		
		try {
			response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaFileName));
			logger.info("=== Schema validation is Passed ===" + schemaFileName);
			
			return true;
		} catch (Exception e) {
			logger.error("Validation failed : "+ e.getMessage());
			
			return false;
		}
		
	}
	

}
