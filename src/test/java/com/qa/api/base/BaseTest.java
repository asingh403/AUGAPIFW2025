package com.qa.api.base;

import org.testng.annotations.BeforeTest;
import com.qa.api.client.RestClient;

public class BaseTest {

	protected RestClient restClient;

	
	// ***** API Base URL ****
	protected final static String BASE_URL_GOREST = "https://gorest.co.in";
	protected final static String BASE_URL_CONTACT = "https://thinking-tester-contact-list.herokuapp.com";
	protected final static String BASE_RESREQ_URL = "https://reqres.in";
	protected final static String BASE_URL_BASIC_AUTH = "https://the-internet.herokuapp.com";

	// ***** API End URL ****
	protected final static String GOREST_USERS_ENDPOINT = "/public/v2/users";
	protected final static String LOGIN_USERS_ENDPOINT = "/users/login";
	protected final static String CONTACTS_USERS_ENDPOINT = "/contacts";
	protected final static String RESREQ_ENDPOINT = "api/users";
	protected final static String BASE_AUTH_ENDPOINT = "/basic_auth";

	@BeforeTest
	public void setup() {
		restClient = new RestClient();
		System.out.println("RestClient initialized in @BeforeTest: " + restClient);
	}
}