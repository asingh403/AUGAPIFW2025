package com.qa.api.products.tests;

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.qa.api.base.BaseTest;
import com.qa.api.constants.AuthType;
import com.qa.api.utils.JsonPathValidatorUtil;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class ProductAPITestWithJsonPath extends BaseTest {

	@Test
	public void getProductTest() {
		Response getResponse = restClient.get(BASE_URL_PRODUCTS, PRODUCTS_ENDPOINT, null, null, AuthType.NO_AUTH,
				ContentType.ANY);

		Assert.assertEquals(getResponse.statusCode(), 200);
		Assert.assertEquals(getResponse.statusLine(), "HTTP/1.1 200 OK");

		List<Number> prices = JsonPathValidatorUtil.readList(getResponse, "$[?(@.price > 50)].price");
		System.out.println("Price Found :: " + prices);

		List<Number> ids = JsonPathValidatorUtil.readList(getResponse, "$[?(@.price >50)].id");
		System.out.println("ids Found :: " + ids);

		List<Number> ratings = JsonPathValidatorUtil.readList(getResponse, "$[?(@.*)].rating.rate");
		System.out.println("ratings Found :: " + ratings);

		List<Integer> counts = JsonPathValidatorUtil.readList(getResponse, "$[?(@.*)].rating.count");
		System.out.println("counts Found :: " + counts);

		List<Map<List<String>, Object>> idTitleList = JsonPathValidatorUtil.readListOfMaps(getResponse,"$.[*].['id', 'title']");

		System.out.println("Id Title List => " + idTitleList);

		List<Map<List<String>, Object>> idTitleCategoryList = JsonPathValidatorUtil.readListOfMaps(getResponse,"$.[*].['id', 'title', 'category']");

		System.out.println("Id Title Category List => " + idTitleCategoryList);
		
		List<Map<List<String>, Object>> idTitleCategoryRatingList = JsonPathValidatorUtil.readListOfMaps(getResponse,"$.[*].['id', 'title', 'category', 'rating']");
		System.out.println("Id Title Category Rating List => " + idTitleCategoryRatingList);
		
		Double minPrice = JsonPathValidatorUtil.read(getResponse, "min($[*].price)");
		System.out.println("min price :: "+ minPrice);
		
		
	}

}
