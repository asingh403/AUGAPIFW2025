package com.qa.api.base;

import org.testng.annotations.BeforeTest;
import com.qa.api.client.RestClient;

public class BaseTest {

	protected RestClient restClient;

	// ***** API Base URL ****
	protected final static String BASE_URL_GOREST = "https://gorest.co.in";

	// ***** API End URL ****
	protected final static String GOREST_USERS_ENDPOINT = "/public/v2/users";

	@BeforeTest
	public void setup() {
		restClient = new RestClient();
		System.out.println("RestClient initialized in @BeforeTest: " + restClient);
	}
}

