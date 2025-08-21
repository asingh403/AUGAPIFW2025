package com.qa.api.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import com.qa.api.client.RestClient;

public class BaseTest {
	
	private static int testCount = 0;
	protected RestClient restClient;
	protected static final Logger log = LogManager.getLogger();

	
	// ***** API Base URL ****
	protected final static String BASE_URL_GOREST = "https://gorest.co.in";
	protected final static String BASE_URL_CONTACT = "https://thinking-tester-contact-list.herokuapp.com";
	protected final static String BASE_RESREQ_URL = "https://reqres.in";
	protected final static String BASE_URL_BASIC_AUTH = "https://the-internet.herokuapp.com";
	protected final static String BASE_URL_PRODUCTS = "https://fakestoreapi.com";
	
	protected final static String BASE_URL_OAUTH2_AMADEUS = "https://test.api.amadeus.com";
	protected final static String BASE_URL_AMADEUS_FLIGHT_DETAILS = "https://test.api.amadeus.com";
	
	protected final static String BASE_URL_CIRCUIT="https://api.jolpi.ca";
	

	// ***** API End URL ****
	protected final static String GOREST_USERS_ENDPOINT = "/public/v2/users";
	protected final static String LOGIN_USERS_ENDPOINT = "/users/login";
	protected final static String CONTACTS_USERS_ENDPOINT = "/contacts";
	protected final static String RESREQ_ENDPOINT = "api/users";
	protected final static String BASE_AUTH_ENDPOINT = "/basic_auth";
	protected final static String PRODUCTS_ENDPOINT = "/products";
	protected final static String OAUTH2_AMADEUS_ENDPOINT = "/v1/security/oauth2/token";

	
	protected final static String AMADEUS_FLIGHT_DETAILS_ENDPOINT = "/v1/shopping/flight-destinations";
	
	protected final static String CIRCUIT_ENDPOINT="/ergast/f1/circuits/";

	
	
	
	@BeforeClass
	public void setup() throws InterruptedException {
	    System.out.println("=== SETUP STARTING for: " + this.getClass().getSimpleName() + " ===");
	    try {
	        Thread.sleep(1000);
	        restClient = new RestClient();
	        log.info("=== SETUP SUCCESS for: " + this.getClass().getSimpleName() + " ===");
	    } catch (Exception e) {
	    	log.info("=== SETUP FAILED for: " + this.getClass().getSimpleName() + ": " + e.getMessage() + " ===");
	        throw e;
	    }
	}
	
	@AfterMethod
	public void cleanup() throws InterruptedException {
		testCount++;
	    int delay = 2000 + (testCount * 500);
	    Thread.sleep(delay);
	    System.out.println("Delay after test #" + testCount + ": " + delay + "ms");
	}
}