package com.qa.api.gorest.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.qa.api.base.BaseTest;
import com.qa.api.constants.AuthType;
import com.qa.api.pojo.User;
import com.qa.api.utils.StringUtils;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class UpdateUserTest extends BaseTest {

	@Test
	public void updateUserTest() {
		String name = StringUtils.getRandomName();

		User user = new User(name, StringUtils.getRandomEmailId(), "Female", "Active");
		User.builder().name(name).email(StringUtils.getRandomEmailId()).status("active").gender("Female").build();

		Response response = restClient.post(BASE_URL_GOREST, GOREST_USERS_ENDPOINT, user, null, null,
				AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertEquals(response.jsonPath().getString("name"), name);
		Assert.assertNotNull(response.jsonPath().getString("id"));
	}

}
