package com.ssafy.diet.dto;

public class Food {

	private String name;
	private double calories;
	private double protein;
	private double carbs;
	private double fat;

	public Food() {
	}

	public Food(String name, double calories, double protein, double carbs, double fat) {
		this.name = name;
		this.calories = calories;
		this.protein = protein;
		this.carbs = carbs;
		this.fat = fat;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getCalories() {
		return calories;
	}

	public void setCalories(double calories) {
		this.calories = calories;
	}

	public double getProtein() {
		return protein;
	}

	public void setProtein(double protein) {
		this.protein = protein;
	}

	public double getCarbs() {
		return carbs;
	}

	public void setCarbs(double carbs) {
		this.carbs = carbs;
	}

	public double getFat() {
		return fat;
	}

	public void setFat(double fat) {
		this.fat = fat;
	}

	@Override
	public String toString() {
		return "Food [name=" + name + ", calories=" + calories + ", protein=" + protein + ", carbs=" + carbs + ", fat="
				+ fat + "]";
	}
}