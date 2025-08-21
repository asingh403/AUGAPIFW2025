package com.qa.api.schema.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.qa.api.base.BaseTest;
import com.qa.api.constants.AuthType;
import com.qa.api.manager.ConfigManger;
import com.qa.api.pojo.User;
import com.qa.api.utils.SchemaValidator;
import com.qa.api.utils.StringUtils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class GoRestUserAPISchemaTest extends BaseTest{
	
	@Test
	public void getUserAPISchemaTest() {
		RestAssured.baseURI = "https://gorest.co.in";
		
		ConfigManger.set("bearertoken", "e424198cc7e42c0e5fb9e5bcbf90d727d30ffbf00a56ca1ccb683809817d8304");
		
		Response response = restClient.get(BASE_URL_GOREST, GOREST_USERS_ENDPOINT, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		
		boolean isSchemaValidation = SchemaValidator.validateSchema(response, "schema/getUserschema.json");
		
		Assert.assertTrue(isSchemaValidation, "Schema Validation is Passed");
		
	}
	
	@Test
	public void createUserAPISchemaTest() {
		User user = User.builder()
				    .name(StringUtils.getRandomName())
				    .status("Active")
				    .email(StringUtils.getRandomEmailId())
				    .gender("Female")
				    .build();
		
		
		RestAssured.baseURI = "https://gorest.co.in";
		
		ConfigManger.set("bearertoken", "e424198cc7e42c0e5fb9e5bcbf90d727d30ffbf00a56ca1ccb683809817d8304");
		
		Response response = restClient.post(BASE_URL_GOREST, GOREST_USERS_ENDPOINT, user, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		
		boolean isSchemaValidation = SchemaValidator.validateSchema(response, "schema/createuserschema.json");
		
		Assert.assertTrue(isSchemaValidation, "Schema Validation is Passed");
		
	}

}
