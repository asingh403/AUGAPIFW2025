package com.qa.api.gorest.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.qa.api.base.BaseTest;
import com.qa.api.constants.AuthType;
import com.qa.api.pojo.User;
import com.qa.api.utils.StringUtils;

import io.qameta.allure.Epic;
import io.qameta.allure.Issue;
import io.qameta.allure.Link;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.TmsLink;
import io.qameta.allure.testng.Tag;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class UpdateUserTest extends BaseTest {

	@Epic("Update Customer based on User id")
	@Severity(SeverityLevel.CRITICAL)
	@Owner("Ashutosh Singh")
	@Tag("Regression")
	@Tag("API")
	@Link(name = "Wiki Doc", url = "https://learn-asingh.atlassian.net/browse/DAL-3")
	@Issue("JIRA-DAL-3")
	@TmsLink("TC-456")
	@Test
	public void updateUserTest() {
		String name = StringUtils.getRandomName();

		User user = new User(null, name, StringUtils.getRandomEmailId(), "Female", "Active");
		User.builder()
			.name(name)
			.email(StringUtils.getRandomEmailId())
			.status("active")
			.gender("Female")
			.build();

		Response responsePost = restClient.post(BASE_URL_GOREST, GOREST_USERS_ENDPOINT, user, null, null,
				AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertEquals(responsePost.jsonPath().getString("name"), name);
		Assert.assertNotNull(responsePost.jsonPath().getString("id"));
		
		//fetch the userId
		String userId = responsePost.jsonPath().getString("id");
		System.out.println("user id === " + userId + "\nuser name === "+ name);
		
		//2. Get call fetch the same user id
		
		Response responseGet = restClient.get(BASE_URL_GOREST, GOREST_USERS_ENDPOINT+"/"+userId, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertTrue(responseGet.statusLine().contains("OK"));
		Assert.assertEquals(responseGet.jsonPath().getString("id"), userId);
		System.out.println("user id === " + userId + "\nuser name === "+ name);
		
		//3. Update the userId
		String putName = StringUtils.getRandomName();
		user.setName(putName);
		user.setStatus("inactive");
		
		Response responsePut = restClient.put(BASE_URL_GOREST, GOREST_USERS_ENDPOINT+"/"+userId, user, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertTrue(responsePut.statusLine().contains("OK"));
		Assert.assertEquals(responsePut.jsonPath().getString("id"), userId);
		Assert.assertTrue(responsePut.statusLine().contains("OK"));
		Assert.assertEquals(responsePut.jsonPath().getString("name"), putName);
		Assert.assertEquals(responsePut.jsonPath().getString("status"), "inactive");
		
		
		//4. Get response after updating 
		responseGet = restClient.get(BASE_URL_GOREST, GOREST_USERS_ENDPOINT+"/"+userId, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertTrue(responseGet.statusLine().contains("OK"));
		Assert.assertEquals(responseGet.jsonPath().getString("id"), userId);
		Assert.assertEquals(responsePut.jsonPath().getString("name"), putName);
		System.out.println("user id === " + userId + "\nuser name === "+ name);
		
		
	}

}
