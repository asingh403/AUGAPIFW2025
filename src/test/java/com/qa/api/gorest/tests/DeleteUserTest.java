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

public class DeleteUserTest extends BaseTest {
	@Epic("Customer Deletion using user id")
	@Severity(SeverityLevel.CRITICAL)
	@Owner("Ashutosh Singh")
	@Tag("Regression")
	@Tag("API")
	@Link(name = "Wiki Doc", url = "https://learn-asingh.atlassian.net/browse/DAL-3")
	@Issue("JIRA-DAL-3")
	@TmsLink("TC-345")
	@Test
	public void deleteUserTest() {
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
		
		//3. Delete the same user id
		Response responseDelete = restClient.delete(BASE_URL_GOREST, GOREST_USERS_ENDPOINT+"/"+userId, user, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		
		System.out.println("Response Delete : "+responseDelete.statusLine());
		Assert.assertTrue(responseDelete.statusLine().contains("No Content"));
				
		//4. Get for deleted resource
		responseGet = restClient.get(BASE_URL_GOREST, GOREST_USERS_ENDPOINT+"/"+userId, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertEquals(responseGet.jsonPath().getString("message"), "Resource not found");
	}


}
