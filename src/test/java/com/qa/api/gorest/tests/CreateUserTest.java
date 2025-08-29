package com.qa.api.gorest.tests;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.qa.api.base.BaseTest;
import com.qa.api.constants.AppConstants;
import com.qa.api.constants.AuthType;
import com.qa.api.pojo.User;
import com.qa.api.utils.ExcelUtils;
import com.qa.api.utils.StringUtils;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Link;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.TmsLink;
import io.qameta.allure.testng.Tag;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

@Feature("Customer Onboarding")
public class CreateUserTest extends BaseTest{
	
	static {
        System.out.println("=== CreateUserTest STATIC BLOCK EXECUTING ===");
    }
	
	@Epic("Customer Creation")
	@Severity(SeverityLevel.CRITICAL)
	@Owner("Ashutosh Singh")
	@Tag("Regression")
	@Tag("API")
	@Link(name = "Wiki Doc", url = "https://learn-asingh.atlassian.net/browse/DAL-2")
	@Issue("JIRA-DAL-2")
	@TmsLink("TC-456")
	@Test
	public void createUserTest() {
		User user = new User(null, "Ashutosh", StringUtils.getRandomEmailId(), "Male", "Active");
		Response response = restClient.post(BASE_URL_GOREST, GOREST_USERS_ENDPOINT, user, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertEquals(response.jsonPath().getString("name"), "Ashutosh");
		Assert.assertNotNull(response.jsonPath().getString("id"));
		LOG.info("Get userId : "+response.jsonPath().getString("id"));
		
	}
	@Epic("Single Customer Creation")
	@Severity(SeverityLevel.CRITICAL)
	@Owner("Ashutosh Singh")
	@Tag("Regression")
	@Tag("API")
	@Link(name = "Wiki Doc", url = "https://learn-asingh.atlassian.net/browse/DAL-3")
	@Issue("JIRA-DAL-3")
	@TmsLink("TC-456")
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
	
	@Epic("Single Customer Creation using JSON")
	@Severity(SeverityLevel.CRITICAL)
	@Owner("Ashutosh Singh")
	@Tag("Regression")
	@Tag("API")
	@Link(name = "Wiki Doc", url = "https://learn-asingh.atlassian.net/browse/DAL-3")
	@Issue("JIRA-DAL-3")
	@TmsLink("TC-456")
	@Test	
	public void createUserWithJsonTest() throws IOException {
		
		String jsonContent = Files.readString(Paths.get("./src/test/resources/jsons/user.json"));
	    
	    String dynamicEmail = StringUtils.getRandomEmailId();
	    String userJson = jsonContent.replace("abhi.pk@test.com", dynamicEmail);
		
		Response response = restClient.post(BASE_URL_GOREST, GOREST_USERS_ENDPOINT, userJson, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertEquals(response.jsonPath().getString("name"), "Abhi p");
		Assert.assertNotNull(response.jsonPath().getString("id"));
		
		
		
		LOG.info(User.class.getProtectionDomain().getCodeSource());
	    java.util.Arrays.stream(User.class.getDeclaredConstructors()).forEach(System.out::println);

	}

	@DataProvider
	public Object [][] getUserData() {
		return new Object[][] {
			{"Priyanka", "Female", "active"},
			{"Abhishek", "Male", "active"},
			{"Faraz", "Male", "inactive"}
		};
	}
	
	@DataProvider
	public Object[][] getExcelData() {
		return ExcelUtils.readData(AppConstants.CREATE_USER_SHEET_NAME);
	}
	
	@Epic("Multiple Customer Creation using DataProvider")
	@Severity(SeverityLevel.CRITICAL)
	@Owner("Ashutosh Singh")
	@Tag("Regression")
	@Tag("API")
	@Link(name = "Wiki Doc", url = "https://learn-asingh.atlassian.net/browse/DAL-3")
	@Issue("JIRA-DAL-3")
	@TmsLink("TC-458")
	@Test(dataProvider = "getExcelData")
	public void createMultipleUserWithStringTest(String name, String gender, String status) {
//		String name = StringUtils.getRandomName();
		String userJson = "{\n"
				+ "\"name\": \"" + name + "\",\n"
				+ "\"gender\": \"" + gender + "\",\n"
				+ "\"email\": \"" + StringUtils.getRandomEmailId() + "\",\n"
				+ "\"status\": \"" + status + "\",\n"
				+ "}";
		Response response = restClient.post(BASE_URL_GOREST, GOREST_USERS_ENDPOINT, userJson, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertEquals(response.jsonPath().getString("name"), name);
		Assert.assertEquals(response.jsonPath().getString("gender"), gender);
		Assert.assertEquals(response.jsonPath().getString("status"), status);
		Assert.assertNotNull(response.jsonPath().getString("id"));
		
	}
	
}
