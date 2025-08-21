package com.qa.api.gorest.tests;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.qa.api.base.BaseTest;
import com.qa.api.constants.AuthType;
import com.qa.api.pojo.User;
import com.qa.api.utils.StringUtils;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class CreateUserTest extends BaseTest{
	static {
        System.out.println("=== CreateUserTest STATIC BLOCK EXECUTING ===");
    }
	
	@Test
	public void createUserTest() {
		User user = new User(null, "Ashutosh", StringUtils.getRandomEmailId(), "Male", "Active");
		Response response = restClient.post(BASE_URL_GOREST, GOREST_USERS_ENDPOINT, user, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertEquals(response.jsonPath().getString("name"), "Ashutosh");
		Assert.assertNotNull(response.jsonPath().getString("id"));
		
	}
	
	@Test
	public void createUserWithStringTest() {
		String name = StringUtils.getRandomName();
		String userJson = "{\n"
				+ "\"name\": \"" + name + "\",\n"
				+ "\"gender\": \"Female\",\n"
				+ "\"email\": \"" + StringUtils.getRandomEmailId() + "\",\n"
				+ "\"status\": \"Active\"\n"
				+ "}";
		Response response = restClient.post(BASE_URL_GOREST, GOREST_USERS_ENDPOINT, userJson, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertEquals(response.jsonPath().getString("name"), name);
		Assert.assertNotNull(response.jsonPath().getString("id"));
		
	}
	
	@Test	
	public void createUserWithJsonTest() throws IOException {
		
		String jsonContent = Files.readString(Paths.get("./src/test/resources/jsons/user.json"));
	    
	    String dynamicEmail = StringUtils.getRandomEmailId();
	    String userJson = jsonContent.replace("abhi.pk@test.com", dynamicEmail);
		
		Response response = restClient.post(BASE_URL_GOREST, GOREST_USERS_ENDPOINT, userJson, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertEquals(response.jsonPath().getString("name"), "Abhi p");
		Assert.assertNotNull(response.jsonPath().getString("id"));
		
		
		
		log.info(User.class.getProtectionDomain().getCodeSource());
	    java.util.Arrays.stream(User.class.getDeclaredConstructors()).forEach(System.out::println);

	}

}
