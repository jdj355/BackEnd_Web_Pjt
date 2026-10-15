package menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import diet.Diet;
import diet.DietManager;
import diet.DietManagerImpl;
import diet.Food;

public class dietCrud {

	private final static Scanner sc = new Scanner(System.in);

	private final DietManager manager =
			DietManagerImpl.getInstance();

	public static void main(String[] args) {

		dietCrud program = new dietCrud();
		program.run();
	}

	public void run() {

		// 프로그램 시작 시 JSON 데이터 불러오기
		manager.fileInput();
		manager.loadFoods();   
		
		while (true) {

			printMenu();

			int menuNumber = readInt("메뉴 번호를 입력하세요: ");

			switch (menuNumber) {

			case 1:
				addDiet();
				break;

			case 2:
				printAllDiets();
				break;

			case 3:
				printDietById();
				break;

			case 4:
				updateDiet();
				break;

			case 5:
				deleteDiet();
				break;
			case 6:
				recommendDiet();
				break;

			case 0:
				System.out.println("프로그램을 종료합니다.");
				sc.close();
				return;

			default:
				System.out.println("올바른 메뉴 번호를 입력하세요.");
			}
		}
	}

	private void printMenu() {

		System.out.println();
		System.out.println("====================================");
		System.out.println("           식단 관리 프로그램");
		System.out.println("====================================");
		System.out.println("1. 식단 등록");
		System.out.println("2. 전체 식단 조회");
		System.out.println("3. 식단 상세 조회");
		System.out.println("4. 식단 수정");
		System.out.println("5. 식단 삭제");
		System.out.println("6. 식단 추천");
		System.out.println("0. 프로그램 종료");
		System.out.println("====================================");
	}

	// CREATE
	private void addDiet() {

		System.out.println();
		System.out.println("[식단 등록]");

		int id = manager.getNextId();

		String date =
				readRequiredString("날짜를 입력하세요(예: 2025-05-15): ");

		String type =
				readRequiredString("식사 유형을 입력하세요(아침/점심/저녁): ");

		List<Food> foods = inputFoods();

		Diet diet = new Diet(id, date, type, foods);

		boolean result = manager.add(diet);

		if (result) {
			System.out.println("식단이 등록되었습니다.");
			System.out.println("등록된 식단 ID: " + id);
		} else {
			System.out.println("식단 등록에 실패했습니다.");
		}
	}

	// READ 전체 조회
	private void printAllDiets() {

		System.out.println();
		System.out.println("[전체 식단 조회]");

		Diet[] diets = manager.getDiets();

		if (diets.length == 0) {
			System.out.println("등록된 식단이 없습니다.");
			return;
		}

		for (Diet diet : diets) {
			printDiet(diet);
		}
	}

	// READ 상세 조회
	private void printDietById() {

		System.out.println();
		System.out.println("[식단 상세 조회]");

		int id = readInt("조회할 식단 ID를 입력하세요: ");

		Diet diet = manager.getDiet(id);

		if (diet == null) {
			System.out.println(
					id + "번 식단을 찾을 수 없습니다."
			);
			return;
		}

		printDiet(diet);
	}

	// UPDATE
	private void updateDiet() {

		System.out.println();
		System.out.println("[식단 수정]");

		int id = readInt("수정할 식단 ID를 입력하세요: ");

		Diet savedDiet = manager.getDiet(id);

		if (savedDiet == null) {
			System.out.println(
					id + "번 식단을 찾을 수 없습니다."
			);
			return;
		}

		System.out.println("현재 저장된 식단입니다.");
		printDiet(savedDiet);

		System.out.println("수정할 내용을 새로 입력하세요.");

		String date =
				readRequiredString("날짜를 입력하세요: ");

		String type =
				readRequiredString("식사 유형을 입력하세요: ");

		List<Food> foods = inputFoods();

		Diet newDiet = new Diet(id, date, type, foods);

		boolean result = manager.updateDiet(newDiet);

		if (result) {
			System.out.println("식단이 수정되었습니다.");
		} else {
			System.out.println("식단 수정에 실패했습니다.");
		}
	}

	// DELETE
	private void deleteDiet() {

		System.out.println();
		System.out.println("[식단 삭제]");

		int id = readInt("삭제할 식단 ID를 입력하세요: ");

		Diet diet = manager.getDiet(id);

		if (diet == null) {
			System.out.println(
					id + "번 식단을 찾을 수 없습니다."
			);
			return;
		}

		System.out.println("삭제할 식단입니다.");
		printDiet(diet);

		System.out.print("정말 삭제하시겠습니까? (y/n): ");

		String answer = sc.nextLine().trim();

		if (!answer.equalsIgnoreCase("y")) {
			System.out.println("삭제를 취소했습니다.");
			return;
		}

		boolean result = manager.deleteDiet(id);

		if (result) {
			System.out.println("식단이 삭제되었습니다.");
		} else {
			System.out.println("식단 삭제에 실패했습니다.");
		}
	}

	private List<Food> inputFoods() {

		List<Food> foods = new ArrayList<>();

		int foodCount;

		while (true) {

			foodCount = readInt("음식 개수를 입력하세요: ");

			if (foodCount > 0) {
				break;
			}

			System.out.println("음식 개수는 1개 이상이어야 합니다.");
		}

		for (int i = 0; i < foodCount; i++) {

			System.out.println();
			System.out.println(
					"[" + (i + 1) + "번째 음식 입력]"
			);

			String name =
					readRequiredString("음식 이름: ");

			int calories =
					readNonNegativeInt("칼로리: ");

			int protein =
					readNonNegativeInt("단백질: ");

			int carbs =
					readNonNegativeInt("탄수화물: ");

			int fat =
					readNonNegativeInt("지방: ");

			Food food = new Food(
					name,
					calories,
					protein,
					carbs,
					fat
			);

			foods.add(food);
		}

		return foods;
	}

	private void printDiet(Diet diet) {

		System.out.println();
		System.out.println("------------------------------------");
		System.out.println("식단 ID   : " + diet.getId());
		System.out.println("날짜      : " + diet.getDate());
		System.out.println("식사 유형 : " + diet.getType());
		System.out.println("음식 목록");
		System.out.println("------------------------------------");

		List<Food> foods = diet.getFoods();

		int totalCalories = 0;
		int totalProtein = 0;
		int totalCarbs = 0;
		int totalFat = 0;

		if (foods == null || foods.isEmpty()) {

			System.out.println("등록된 음식이 없습니다.");

		} else {

			for (int i = 0; i < foods.size(); i++) {

				Food food = foods.get(i);

				System.out.println(
						(i + 1) + ". " + food.getName()
				);

				System.out.println(
						"   칼로리: " + food.getCalories() + "kcal"
				);

				System.out.println(
						"   단백질: " + food.getProtein() + "g"
				);

				System.out.println(
						"   탄수화물: " + food.getCarbs() + "g"
				);

				System.out.println(
						"   지방: " + food.getFat() + "g"
				);

				totalCalories += food.getCalories();
				totalProtein += food.getProtein();
				totalCarbs += food.getCarbs();
				totalFat += food.getFat();
			}
		}

		System.out.println("------------------------------------");
		System.out.println("총 칼로리   : " + totalCalories + "kcal");
		System.out.println("총 단백질   : " + totalProtein + "g");
		System.out.println("총 탄수화물 : " + totalCarbs + "g");
		System.out.println("총 지방     : " + totalFat + "g");
		System.out.println("------------------------------------");
	}

	private int readInt(String message) {

		while (true) {

			System.out.print(message);

			String input = sc.nextLine().trim();

			try {
				return Integer.parseInt(input);

			} catch (NumberFormatException e) {
				System.out.println("숫자를 입력해야 합니다.");
			}
		}
	}

	private int readNonNegativeInt(String message) {

		while (true) {

			int number = readInt(message);

			if (number >= 0) {
				return number;
			}

			System.out.println("0 이상의 숫자를 입력하세요.");
		}
	}

	private String readRequiredString(String message) {

		while (true) {

			System.out.print(message);

			String input = sc.nextLine().trim();

			if (!input.isEmpty()) {
				return input;
			}

			System.out.println("내용을 입력해야 합니다.");
		}
	}
	
	public void recommendDiet() {
	    System.out.print("목표 칼로리(상한): ");
	    int c = sc.nextInt();

	    System.out.print("목표 단백질(하한): ");
	    double p = sc.nextDouble();

	    System.out.print("음식 개수 상한: ");
	    int k = sc.nextInt();

	    // 결과를 result에 받아서
	    List<Food> result = manager.recommendDiet(c, p, k);

	    // 출력
	    double totalCal = 0;
	    double totalProtein = 0;
	    System.out.println("\n[추천 식단]");
	    for (Food f : result) {
	        System.out.printf(" - %s (%.0fkcal, 단백질 %.1fg)%n",
	                f.getName(), f.getCalories(), f.getProtein());
	        totalCal += f.getCalories();
	        totalProtein += f.getProtein();
	    }
	    System.out.printf("총 %.0fkcal / 단백질 %.1fg%n", totalCal, totalProtein);
	    System.out.println(totalProtein >= p ? "목표 달성" : "목표 미달");
	}

}