package com.qa.reqres.tests;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.qa.api.base.BaseTest;
import com.qa.api.constants.AuthType;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class ReqResTest extends BaseTest{
	
	@Test
	public void getUserTest() {
		Map<String, String> queryMap = new HashMap<>();
		queryMap.put("page", "2");
		
		Response response = restClient.get(BASE_RESREQ_URL, RESREQ_ENDPOINT, queryMap, null, AuthType.NO_AUTH, ContentType.ANY);
		Assert.assertEquals(response.statusCode(),200);
	}

}
