package com.kh.spring.demo.model.vo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Dev implements Serializable {

	/**
	 * 파라미터생성자 / getter / setter 등 따로 작성X >> pom.xml(122번) - lombok dependency 추가해서 사용
	 */
	private static final long serialVersionUID = 1L;
	
	private int no; // pk
	private String name;
	private int career;
	private String email;
	private String gender;
	private String[] lang;
}
