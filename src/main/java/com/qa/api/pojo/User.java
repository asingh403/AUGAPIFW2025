package com.qa.api.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class User {
	
	private String name;
	private String email;
	private String gender;
	private String status;


}
