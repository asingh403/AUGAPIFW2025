package com.qa.api.client;

import static io.restassured.RestAssured.expect;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.util.Map;

import com.qa.api.constants.AuthType;
import com.qa.api.exceptions.APIException;
import com.qa.api.manager.ConfigManger;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class RestClient {

	// define all the response
	private ResponseSpecification responseSpec200 = expect().statusCode(200);
	private ResponseSpecification responseSpec201 = expect().statusCode(201);
	private ResponseSpecification responseSpec400 = expect().statusCode(400);
	private ResponseSpecification responseSpec204 = expect().statusCode(204);
	private ResponseSpecification responseSpec200or201 = expect().statusCode(anyOf(equalTo(200), equalTo(201)));
	private ResponseSpecification responseSpec200or404 = expect().statusCode(anyOf(equalTo(200), equalTo(404)));

	private RequestSpecification setupRequest(String baseUrl, AuthType authType, ContentType contentType) {

		RequestSpecification request = RestAssured.given().log().all().baseUri(baseUrl).contentType(contentType)
				.accept(contentType);

		switch (authType) {
		case BEARER_TOKEN:
			request.header("Authorization", "Bearer " + ConfigManger.get("bearertoken"));
			break;

		case OAUTH2:
			request.header("Authorization", "Bearer " + "oauth2 token");
			break;

		case BASIC_AUTH:
			request.header("Authorization", "Basic " + "=== Basicauth token ===");
			break;

		case API_KEY:
			request.header("x-api-key", "Basic " + "===api key===");
			break;

		case NO_AUTH:
			System.out.println("Auth is not required...");
			break;

		default:
			System.out.println("This Auth is not supported...Pls pass correct AuthType");
			throw new APIException("=== Invalid Auth ==");
		}
		return request;
	}

	public void applyParams(RequestSpecification request, Map<String, String> queryParams,
			Map<String, String> pathParams) {

		if (queryParams != null) {
			request.queryParams(queryParams);
		}
		if (pathParams != null) {
			request.pathParams(pathParams);
		}
	}

	/**
	 * 
	 * @param url
	 * @param endPoint
	 * @param queryParams
	 * @param pathParams
	 * @param authType
	 * @param contentType
	 * @return get ResponseSpecification
	 */

	public Response get(String url, String endPoint, Map<String, String> queryParams, Map<String, String> pathParams,
			AuthType authType, ContentType contentType) {

		RequestSpecification request = setupRequest(url, authType, contentType);
		applyParams(request, queryParams, pathParams);
		Response response = request.get(endPoint).then().spec(responseSpec200or404).extract().response();
		response.prettyPrint();
		return response;

	}

	public <T> Response post(String url, String endPoint, T body, Map<String, String> queryParams,
			Map<String, String> pathParams, AuthType authType, ContentType contentType) {

		RequestSpecification request = setupRequest(url, authType, contentType);
		applyParams(request, queryParams, pathParams);

		Response response = request.body(body).post(endPoint).then().spec(responseSpec201).extract().response();

		response.prettyPrint();

		return response;

	}
	
	public <T> Response put(String url, String endPoint, T body, Map<String, String> queryParams,
			Map<String, String> pathParams, AuthType authType, ContentType contentType) {

		RequestSpecification request = setupRequest(url, authType, contentType);
		applyParams(request, queryParams, pathParams);

		Response response = request.body(body).put(endPoint).then().spec(responseSpec200).extract().response();

		response.prettyPrint();

		return response;

	}
	
	public <T> Response patch(String url, String endPoint, T body, Map<String, String> queryParams,
			Map<String, String> pathParams, AuthType authType, ContentType contentType) {

		RequestSpecification request = setupRequest(url, authType, contentType);
		applyParams(request, queryParams, pathParams);

		Response response = request.body(body).patch(endPoint).then().spec(responseSpec200).extract().response();

		response.prettyPrint();

		return response;

	}
	
	public <T> Response delete(String url, String endPoint, T body, Map<String, String> queryParams,
			Map<String, String> pathParams, AuthType authType, ContentType contentType) {

		RequestSpecification request = setupRequest(url, authType, contentType);
		applyParams(request, queryParams, pathParams);

		Response response = request.delete(endPoint).then().spec(responseSpec204).extract().response();

		response.prettyPrint();

		return response;

	}
	
	public Response post(String url, String endPoint, File file, Map<String, String> queryParams,
			Map<String, String> pathParams, AuthType authType, ContentType contentType) {

		RequestSpecification request = setupRequest(url, authType, contentType);
		applyParams(request, queryParams, pathParams);

		Response response = request.body(file).post(endPoint).then().spec(responseSpec201).extract().response();

		response.prettyPrint();

		return response;

	}
}
