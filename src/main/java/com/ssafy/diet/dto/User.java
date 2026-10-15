package com.ssafy.diet.dto;

import java.util.Date;
import java.util.List;

public class User {
	private String userId;
	private String password;
	private String name;
	private int age;
	private long height;
	private long weight;
	private String smoking;
	private String drinking;
	private List<String> deseaseList;
	private String phoneNumber;
	private String email;
	private String adress;
	private String delYn;
	private Date regDate;
	private Date modDate;
	private String loginStatus;
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public User(String userId, String password, String name, int age, long height, long weight, String smoking,
			String drinking, List<String> deseaseList, String phoneNumber, String email, String adress, String delYn,
			Date regDate, Date modDate, String loginStatus) {
		super();
		this.userId = userId;
		this.password = password;
		this.name = name;
		this.age = age;
		this.height = height;
		this.weight = weight;
		this.smoking = smoking;
		this.drinking = drinking;
		this.deseaseList = deseaseList;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.adress = adress;
		this.delYn = delYn;
		this.regDate = regDate;
		this.modDate = modDate;
		this.loginStatus = loginStatus;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public long getHeight() {
		return height;
	}
	public void setHeight(long height) {
		this.height = height;
	}
	public long getWeight() {
		return weight;
	}
	public void setWeight(long weight) {
		this.weight = weight;
	}
	public String getSmoking() {
		return smoking;
	}
	public void setSmoking(String smoking) {
		this.smoking = smoking;
	}
	public String getDrinking() {
		return drinking;
	}
	public void setDrinking(String drinking) {
		this.drinking = drinking;
	}
	public List<String> getDeseaseList() {
		return deseaseList;
	}
	public void setDeseaseList(List<String> deseaseList) {
		this.deseaseList = deseaseList;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAdress() {
		return adress;
	}
	public void setAdress(String adress) {
		this.adress = adress;
	}
	public String getDelYn() {
		return delYn;
	}
	public void setDelYn(String delYn) {
		this.delYn = delYn;
	}
	public Date getRegDate() {
		return regDate;
	}
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
	public Date getModDate() {
		return modDate;
	}
	public void setModDate(Date modDate) {
		this.modDate = modDate;
	}
	public String getLoginStatus() {
		return loginStatus;
	}
	public void setLoginStatus(String loginStatus) {
		this.loginStatus = loginStatus;
	}
	
	@Override
	public String toString() {
		return "User [userId=" + userId + ", password=" + password + ", name=" + name + ", age=" + age + ", height="
				+ height + ", weight=" + weight + ", smoking=" + smoking + ", drinking=" + drinking + ", deseaseList="
				+ deseaseList + ", phoneNumber=" + phoneNumber + ", email=" + email + ", adress=" + adress + ", delYn="
				+ delYn + ", regDate=" + regDate + ", modDate=" + modDate + ", loginStatus=" + loginStatus + "]";
	}
	
	
}
