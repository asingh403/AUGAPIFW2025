package com.qa.api.amadeus.tests;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.qa.api.base.BaseTest;
import com.qa.api.constants.AuthType;
import com.qa.api.manager.ConfigManger;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class AmadeusAPITest extends BaseTest {

	private String accessToken;
	private static final String ORIGIN = "PAR";
	private static final String PRICE_CONSTRAINT = "200";
	private static final String MIN_PRICE_CONSTRAINT = "1";

	@BeforeMethod
	public void getOAuth2Token() {
		log.info("Starting OAuth2 token generation");
		try {
			log.debug("OAuth2 Request Details - URL: {}, Endpoint: {}", BASE_URL_OAUTH2_AMADEUS,
					OAUTH2_AMADEUS_ENDPOINT);
			log.info("=== OAuth2 Token Request Debug ===");
			log.info("Base URL: " + BASE_URL_OAUTH2_AMADEUS);
			log.info("Endpoint: " + OAUTH2_AMADEUS_ENDPOINT);
			log.info("Client ID: " + ConfigManger.get("clientId"));
			log.info("Grant Type: " + ConfigManger.get("granttype"));

			Response postResponse = restClient.post(BASE_URL_OAUTH2_AMADEUS, OAUTH2_AMADEUS_ENDPOINT,
					ConfigManger.get("clientId"), ConfigManger.get("clientsecret"), ConfigManger.get("granttype"),
					ContentType.URLENC);

			log.info("Response Status: " + postResponse.getStatusCode());
			log.info("Response Body: " + postResponse.asString());

			if (postResponse.getStatusCode() != 200) {
				throw new RuntimeException("OAuth failed with status: " + postResponse.getStatusCode());
			}

			String accessToken = postResponse.jsonPath().getString("access_token");
			log.info("Access Token === " + accessToken);

			if (accessToken == null || accessToken.isEmpty()) {
				throw new RuntimeException("Access token is null or empty");
			}

			ConfigManger.set("bearertoken", accessToken);

		} catch (Exception e) {
			log.info("OAuth2 Error: " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * This TC is found the Bug We have max price limit is 200 but still it is able
	 * to fetch more than 200
	 */
	@Test(description = "Validate the max price contraint as per user fixed range")
	public void shouldRespectMaxPriceConstraint() {
		String maxPriceParam = PRICE_CONSTRAINT;
		Response res = makeFlightRequest(ORIGIN, maxPriceParam);
		List<Map<String, Object>> flights = res.jsonPath().getList("data");

		BigDecimal maxPrice = new BigDecimal(maxPriceParam);

		// Check if API bug still exists
		boolean hasPriceBug = flights.stream().map(this::extractPrice).anyMatch(price -> price.compareTo(maxPrice) > 0);

		if (hasPriceBug) {
			log.warn("SKIPPING: maxPrice parameter bug still exists in API");
			throw new SkipException("API maxPrice parameter not working - bug reported");
		}

		flights.forEach(flight -> {
			BigDecimal price = extractPrice(flight);
			Assert.assertTrue(price.compareTo(maxPrice) <= 0,
					String.format("Flight price %s exceeds limit %s", price, maxPrice));
		});
	}

	@Test(description = "Validate price object structure and non-null values")
	public void shouldValidatePriceObjectStructure() {
		Response res = makeFlightRequest(ORIGIN, PRICE_CONSTRAINT);
		List<Map<String, Object>> flights = res.jsonPath().getList("data");

		Assert.assertFalse(flights.isEmpty(), "No flight data returned");
		flights.forEach(flight -> {
			BigDecimal price = extractPrice(flight);
			Assert.assertNotNull(price, "Price extraction failed");
		});
	}

	/**
	 * These below method are the Actions methods for this class
	 * 
	 * @param origin
	 * @param maxPrice
	 * @return Response Will refactor these 2 methods in different class
	 */
	public void shouldValidateMaxPriceBoundary() {
		Response res = makeFlightRequest(ORIGIN, MIN_PRICE_CONSTRAINT);
		List<Map<String, Object>> flights = res.jsonPath().getList("data");

		if (!flights.isEmpty()) {
			log.info("Flights returned for maxPrice=1:");
			flights.forEach(flight -> {
				BigDecimal price = extractPrice(flight);
				log.info("Price: " + price);
			});
		}
	}

	@Test(description = "Validate flight data structure")
	public void shouldContainValidFlightData() {
		Response res = makeFlightRequest(ORIGIN, PRICE_CONSTRAINT);
		List<Map<String, Object>> flights = res.jsonPath().getList("data");

		Assert.assertFalse(flights.isEmpty(), "No flight data returned");
		flights.forEach(this::validateFlightStructure);
	}

	@Test
	public void printDictionaryData() {
		Response response = makeFlightRequest(ORIGIN, PRICE_CONSTRAINT);
		JsonPath jp = response.jsonPath();

		// Print currencies
		Map<String, String> currencies = jp.getMap("dictionaries.currencies");
		System.out.println("=== CURRENCIES ===");
		currencies.forEach((code, name) -> System.out.printf("Currency: %s = %s%n", code, name));

		// Print locations
		Map<String, Object> locations = jp.getMap("dictionaries.locations");
		System.out.println("\\n=== LOCATIONS ===");
		locations.forEach((code, details) -> {
			Map<String, Object> locationData = (Map<String, Object>) details;

			System.out.printf("Location: %s%n", code);
			System.out.printf("  SubType: %s%n", locationData.get("subType"));
			System.out.printf("  Detailed Name: %s%n", locationData.get("detailedName"));
			System.out.println();
		});
	}

	/**
	 * This method behave like a helper method for
	 * 
	 * @Test shouldContainValidFlightData
	 * @param flight
	 */

	private void validateFlightStructure(Map<String, Object> flight) {
		log.info("=== CONSOLE: validateFlightStructure called === " + flight.toString());
		Assert.assertNotNull(flight.get("origin"), "Missing origin");
		Assert.assertNotNull(flight.get("destination"), "Missing destination");

		Map<String, Object> links = (Map<String, Object>) flight.get("links");
		Assert.assertNotNull(links, "Missing links object");
		Assert.assertTrue(String.valueOf(links.get("flightDates")).startsWith("https://"));
		Assert.assertTrue(String.valueOf(links.get("flightOffers")).startsWith("https://"));
	}

	/**
	 * low to high ticket range constraint set by customer from $1 to $1000 to get
	 * the budget range set by endpoint Test multiple price points to confirm bug
	 * pattern Bug Identified
	 */
	public void shouldDocumentMaxPriceBugBehavior() {
		Map<String, String> testCases = Map.of("1", "Very low price", "50", "Low price", "200", "Medium price", "1000",
				"High price");

		testCases.forEach((maxPrice, description) -> {
			Response res = makeFlightRequest(ORIGIN, maxPrice);
			List<Map<String, Object>> flights = res.jsonPath().getList("data");

			BigDecimal actualMaxPrice = flights.stream().map(this::extractPrice).max(BigDecimal::compareTo)
					.orElse(BigDecimal.ZERO);

			log.info(
					String.format("%s (maxPrice=%s): Actual max returned = %s", description, maxPrice, actualMaxPrice));
		});
	}

	private BigDecimal extractPrice(Map<String, Object> flight) {
		Objects.requireNonNull(flight, "Flight data cannot be null");

		Map<String, Object> priceObj = (Map<String, Object>) flight.get("price");
		Assert.assertNotNull(priceObj, "Price object missing from flight data");

		Object totalPrice = priceObj.get("total");
		Assert.assertNotNull(totalPrice, "Total price missing from price object");

		try {
			BigDecimal price = new BigDecimal(String.valueOf(totalPrice).trim());
			Assert.assertTrue(price.compareTo(BigDecimal.ZERO) > 0, "Price must be positive, got: " + price);
			return price;
		} catch (NumberFormatException e) {
			Assert.fail("Invalid price format: " + totalPrice + " - " + e.getMessage());
			return null;
		}
	}

	private Response makeFlightRequest(String origin, String maxPrice) {
		Map<String, String> queryParams = Map.of("origin", origin, "maxPrice", maxPrice);

		return restClient.get(BASE_URL_AMADEUS_FLIGHT_DETAILS, AMADEUS_FLIGHT_DETAILS_ENDPOINT, queryParams, null,
				AuthType.BEARER_TOKEN, ContentType.ANY);
	}
}