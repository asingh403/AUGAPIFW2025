package com.qa.api.utils;

import java.util.Random;
import java.util.stream.Collectors;

public class StringUtils {

	public static String getRandomEmailId() {
		return "api_automation_" + System.currentTimeMillis() + "@api-test.com";
	}

	public static String getRandomName() {
		return "TEST" + "_" + new Random().ints(8, 0, 26).mapToObj(i -> String.valueOf((char) ('a' + i)))
				.collect(Collectors.joining());
	}

}