package com.ssafy.diet.dto;

import java.util.List;

public class Diet {

	private int id;
	private String date;
	private String type;
	private List<Food> foods;

	public Diet() {
	}

	public Diet(int id, String date, String type, List<Food> foods) {
		this.id = id;
		this.date = date;
		this.type = type;
		this.foods = foods;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Food> getFoods() {
		return foods;
	}

	public void setFoods(List<Food> foods) {
		this.foods = foods;
	}

	@Override
	public String toString() {
		return "Diet [id=" + id
				+ ", date=" + date
				+ ", type=" + type
				+ ", foods=" + foods + "]";
	}
}