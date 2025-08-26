package com.qa.api.base;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import com.qa.api.client.RestClient;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;

public class BaseTest {
//	protected RestClient restClient;
	private static int testCount = 0;
	protected static final Logger LOG = LogManager.getLogger();
	protected static final RestClient restClient = new RestClient();

	// API Base URLs
	protected final static String BASE_URL_GOREST = "https://gorest.co.in";
	protected final static String BASE_URL_CONTACT = "https://thinking-tester-contact-list.herokuapp.com";
	protected final static String BASE_RESREQ_URL = "https://reqres.in";
	protected final static String BASE_URL_BASIC_AUTH = "https://the-internet.herokuapp.com";
	protected final static String BASE_URL_PRODUCTS = "https://fakestoreapi.com";
	protected final static String BASE_URL_OAUTH2_AMADEUS = "https://test.api.amadeus.com";
	protected final static String BASE_URL_AMADEUS_FLIGHT_DETAILS = "https://test.api.amadeus.com";
	protected final static String BASE_URL_CIRCUIT = "https://api.jolpi.ca";

	// API Endpoints
	protected final static String GOREST_USERS_ENDPOINT = "/public/v2/users";
	protected final static String LOGIN_USERS_ENDPOINT = "/users/login";
	protected final static String CONTACTS_USERS_ENDPOINT = "/contacts";
	protected final static String RESREQ_ENDPOINT = "api/users";
	protected final static String BASE_AUTH_ENDPOINT = "/basic_auth";
	protected final static String PRODUCTS_ENDPOINT = "/products";
	protected final static String OAUTH2_AMADEUS_ENDPOINT = "/v1/security/oauth2/token";
	protected final static String AMADEUS_FLIGHT_DETAILS_ENDPOINT = "/v1/shopping/flight-destinations";
	protected final static String CIRCUIT_ENDPOINT = "/ergast/f1/circuits/";

	@BeforeSuite
	public void setupAllureReport() {
		RestAssured.filters(new AllureRestAssured());
		Properties props = new Properties();
        props.setProperty("OS", System.getProperty("os.name"));
        props.setProperty("Java", System.getProperty("java.version"));
        props.setProperty("Environment", "QA");
        props.setProperty("Browser", "Chrome-126");
        props.setProperty("Build", "LocalRun-" + System.currentTimeMillis());

        try (FileOutputStream fos = new FileOutputStream("allure-results/environment.properties")) {
            props.store(fos, "Allure Environment Properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	@BeforeClass(alwaysRun = true)
	public void setup() throws InterruptedException {
		Reporter.log("SETUP START: " + this.getClass().getSimpleName());
		Thread.sleep(1000);
//		restClient = new RestClient();
		System.out.println("RestClient created: " + (restClient != null ? "SUCCESS" : "FAILED"));
	}

	@AfterMethod
	public void cleanup() throws InterruptedException {
		testCount++;
		int delay = 2000 + (testCount * 100);
		Thread.sleep(delay);
		System.out.println("Delay after test #" + testCount + ": " + delay + "ms");
	}
}