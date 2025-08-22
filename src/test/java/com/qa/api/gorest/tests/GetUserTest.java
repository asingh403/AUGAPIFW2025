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
	
	private static String userId;
	
	@Test(priority = 1)
	public void getAllUsersTest() {
		LOG.info("Bearer Token from Config: " + ConfigManger.get("bearertoken"));
		Response response = restClient.get(BASE_URL_GOREST, GOREST_USERS_ENDPOINT, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		userId = response.jsonPath().getString("id[0]");
		LOG.info("userid found : "+ userId);
		Assert.assertTrue(response.statusLine().contains("OK"));
	}
	
	
	@Test
	public void getAllUsersWithQueryParamTest() {
		Map<String, String> queryParams = Map.of("name", "Ashutosh", "Status", "Active"); 
		Response response = restClient.get(BASE_URL_GOREST, GOREST_USERS_ENDPOINT, queryParams, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertTrue(response.statusLine().contains("OK"));
	}
	
	@Test(priority = 2, dependsOnMethods = "getAllUsersTest")
	public void getSingleUsersTest() {
		LOG.info("=== Single user "+ BASE_URL_GOREST+GOREST_USERS_ENDPOINT+"/"+userId);
		Response response = restClient.get(BASE_URL_GOREST, GOREST_USERS_ENDPOINT+"/"+userId, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertTrue(response.statusLine().contains("OK"));
		Assert.assertEquals(response.jsonPath().getString("id"), userId);
	}

}
