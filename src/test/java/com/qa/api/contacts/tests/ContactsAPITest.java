package com.qa.api.contacts.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.qa.api.base.BaseTest;
import com.qa.api.constants.AuthType;
import com.qa.api.manager.ConfigManger;
import com.qa.api.pojo.ContactCredentials;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class ContactsAPITest extends BaseTest {
	private String tokenId;
	private boolean tokenInitialized = false;

//	@BeforeMethod
//	public void ensureTokenSetup() {
//		if (!tokenInitialized && restClient != null) {
//			getToken();
//			tokenInitialized = true;
//		} else if (restClient == null) {
//			throw new RuntimeException("RestClient is null in ContactsAPITest");
//		}
//	}

	@BeforeMethod(alwaysRun = true) 
	public void getToken() {
		ContactCredentials creds = ContactCredentials.builder().email("asingh.tic@gmail.com").password("1108noki@")
				.build();
		Response response = restClient.post(BASE_URL_CONTACT, LOGIN_USERS_ENDPOINT, creds, null, null, AuthType.NO_AUTH,
				ContentType.JSON);

		Assert.assertEquals(response.statusCode(), 200);
		tokenId = response.jsonPath().getString("token");
		ConfigManger.set("bearertoken", tokenId);
		System.out.println("token id :: " + tokenId);
	}

	@Test
	public void getAllContactsTest() {
		Response response = restClient.get(BASE_URL_CONTACT, CONTACTS_USERS_ENDPOINT, null, null, AuthType.BEARER_TOKEN,
				ContentType.JSON);
		Assert.assertEquals(response.statusCode(), 200);
	}

}
