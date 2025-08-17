package com.qa.api.base;

import org.testng.annotations.BeforeTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.qa.api.client.RestClient;

public class BaseTest {

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
	

	// ***** API End URL ****
	protected final static String GOREST_USERS_ENDPOINT = "/public/v2/users";
	protected final static String LOGIN_USERS_ENDPOINT = "/users/login";
	protected final static String CONTACTS_USERS_ENDPOINT = "/contacts";
	protected final static String RESREQ_ENDPOINT = "api/users";
	protected final static String BASE_AUTH_ENDPOINT = "/basic_auth";
	protected final static String PRODUCTS_ENDPOINT = "/products";
	protected final static String OAUTH2_AMADEUS_ENDPOINT = "/v1/security/oauth2/token";
//	protected final static String AMADEUS_FLIGHT_DETAILS_ENDPOINT = "/v1/shopping/flight-destinations?origin=PAR&maxPrice=700";
	
	protected final static String AMADEUS_FLIGHT_DETAILS_ENDPOINT = "/v1/shopping/flight-destinations";
	
//	https://test.api.amadeus.com/v1/security/oauth2/token
	
	
	
	@BeforeTest
	public void setup() {
		restClient = new RestClient();
		log.info("RestClient initialized in @BeforeTest: " + restClient);
	}
}