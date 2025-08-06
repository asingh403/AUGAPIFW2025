package com.qa.api.gorest.tests;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.qa.api.base.BaseTest;
import com.qa.api.constants.AuthType;
import com.qa.api.manager.ConfigManger;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class GetUserTest extends BaseTest{
	
	@Test
	public void getAllUsersTest() {
		System.out.println("Bearer Token from Config: " + ConfigManger.get("token"));
		Response response = restClient.get(BASE_URL_GOREST, GOREST_USERS_ENDPOINT, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertTrue(response.statusLine().contains("OK"));
	}
	
	
	@Test(enabled=false)
	public void getAllUsersWithQueryParamTest() {
		Map<String, String> queryParams = Map.of("name", "Ashutosh", "Status", "Active"); 
		Response response = restClient.get(BASE_URL_GOREST, GOREST_USERS_ENDPOINT, queryParams, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertTrue(response.statusLine().contains("OK"));
	}
	
	@Test(enabled=false)
	public void getSingleUsersTest() {
		String userId="8053360";
		Response response = restClient.get(BASE_URL_GOREST, GOREST_USERS_ENDPOINT+"/"+userId, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertTrue(response.statusLine().contains("OK"));
		Assert.assertEquals(response.jsonPath().getString("id"), userId);
	}

}
