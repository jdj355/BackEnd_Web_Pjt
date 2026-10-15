package com.ssafy.diet.exception;

public class LoginException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private String userId;
	private String password;
	
	public String getUserId() {
		return userId;
	}
	public String getPassword() {
		return password;
	}
	public LoginException(String userId, String password) {
		super("아이디 또는 비밀번호를 확인해주세요.");
		this.userId = userId;
		this.password = password;
	}
}
