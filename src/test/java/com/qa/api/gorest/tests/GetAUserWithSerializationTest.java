package com.qa.api.gorest.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.qa.api.base.BaseTest;
import com.qa.api.constants.AuthType;
import com.qa.api.manager.ConfigManger;
import com.qa.api.pojo.User;
import com.qa.api.utils.JsonUtils;
import com.qa.api.utils.StringUtils;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class GetAUserWithSerializationTest extends BaseTest {

	public String tokenId;

	@BeforeClass
	public void setupToken() {
		tokenId = "e424198cc7e42c0e5fb9e5bcbf90d727d30ffbf00a56ca1ccb683809817d8304";
		ConfigManger.set("bearertoken", tokenId);

	}

	@Test
	public void createUserTest() {
		String expectedName = StringUtils.getRandomName();
		String expectedEmailId = StringUtils.getRandomEmailId();
		User user = new User(null, expectedName, expectedEmailId, "Female", "Active");

		Response responsePost = restClient.post(BASE_URL_GOREST, GOREST_USERS_ENDPOINT, user, null, null,AuthType.BEARER_TOKEN, ContentType.JSON);
		String actualName = responsePost.jsonPath().getString("name");
		String actualEmaillId = responsePost.jsonPath().getString("email");

		Assert.assertEquals(expectedName, actualName);
		Assert.assertEquals(expectedEmailId, actualEmaillId);

		String expectedUserId = responsePost.jsonPath().getString("id");

		Response responseGet = restClient.get(BASE_URL_GOREST, GOREST_USERS_ENDPOINT + "/" + expectedUserId, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		actualName = responseGet.jsonPath().getString("name");
		actualEmaillId = responseGet.jsonPath().getString("email");

		Assert.assertEquals(expectedName, actualName);
		Assert.assertEquals(expectedEmailId, actualEmaillId);
		Assert.assertTrue(responseGet.statusLine().contains("OK"));

		User userResponse = JsonUtils.deserialize(responseGet, User.class);

		Assert.assertEquals(userResponse.getName(), user.getName());
	}

}
