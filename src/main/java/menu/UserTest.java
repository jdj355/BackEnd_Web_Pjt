package menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import user.LoginException;
import user.User;
import user.UserManagerImpl;

public class UserTest {

	// 프로그램 전체가 공유하는 Scanner / UserManager (중간에 닫지 않는다)
	static Scanner sc = new Scanner(System.in);
	static UserManagerImpl um = UserManagerImpl.getInstance();

	public static void main(String[] args) {

		um.loadUserData(); // 데이터는 시작 시 한 번만 로드

		while (true) {
			System.out.println();
			System.out.println("***************** 메 뉴 *****************");
			System.out.println("1. 회원 가입");
			System.out.println("2. 회원 조회");
			System.out.println("3. 회원 정보 수정");
			System.out.println("4. 회원 탈퇴");
			System.out.println("5. 로그인");
			System.out.println("6. 로그아웃");
			System.out.println("0. 종료");
			System.out.println("****************************************");
			System.out.print("메뉴를 선택하세요: ");

			String menu = sc.nextLine().trim();

			switch (menu) {
			case "1":
				userAdd();
				break;
			case "2":
				getUser();
				break;
			case "3":
				userUpdate();
				break;
			case "4":
				userDelete();
				break;
			case "5":
				login();
				break;
			case "6":
				logout();
				break;
			case "0":
				System.out.println("프로그램을 종료합니다.");
				sc.close(); // 종료할 때만 닫는다
				return;
			default:
				System.out.println("잘못된 입력입니다. 다시 선택하세요.");
			}
		}
	}

	// ================= 공통 입력 =================

	// 아이디 형식 검증 입력 (로그인/로그아웃용)
	static String inputUserId() {
		String userId;
		while (true) {
			System.out.println("아이디를 입력하세요: ");
			userId = sc.nextLine().trim();

			if (userId.isEmpty()) {
				System.out.println("아이디 입력해주세요.");
				continue;
			}
			if (!userId.matches("[a-zA-Z0-9]{4,12}")) {
				System.out.println("영문과 숫자로 4~12자 여야 합니다.");
				continue;
			}
			break;
		}
		return userId;
	}

	// 비밀번호 입력
	static String inputPassword() {
		String password;
		while (true) {
			System.out.println("비밀번호를 입력하세요: ");
			password = sc.nextLine().trim();

			if (password.isEmpty()) {
				System.out.println("비밀번호를 입력해주세요.");
				continue;
			}
			break;
		}
		return password;
	}

	// 양의 정수(long) 입력
	static long inputPositiveLong(String label) {
		while (true) {
			System.out.println(label + "를 입력하세요: ");
			String input = sc.nextLine().trim();
			try {
				long value = Long.parseLong(input);
				if (value <= 0) {
					System.out.println(label + "는 1 이상이어야 합니다.");
					continue;
				}
				return value;
			} catch (NumberFormatException e) {
				System.out.println("숫자만 입력하세요.");
			}
		}
	}

	// ================= 회원 가입 =================
	static void userAdd() {
		System.out.println("***************** 회원 가입 *****************");

		String userId;
		while (true) {
			System.out.println("아이디를 입력하세요: ");
			userId = sc.nextLine().trim();

			if (userId.isEmpty()) {
				System.out.println("아이디 입력해주세요.");
				continue;
			}
			if (!userId.matches("[a-zA-Z0-9]{4,12}")) {
				System.out.println("영문과 숫자로 4~12자 여야 합니다.");
				continue;
			}
			if (um.getUser(userId) != null) {
				System.out.println("이미 사용중인 아이디입니다.");
				continue;
			}
			break;
		}

		String password = inputPassword();

		System.out.println("이름을 입력하세요: ");
		String name = sc.nextLine();

		int age;
		while (true) {
			System.out.println("나이를 입력하세요: ");
			String input = sc.nextLine().trim();
			try {
				age = Integer.parseInt(input);
				if (age <= 0) {
					System.out.println("나이는 1 이상이어야 합니다.");
					continue;
				}
				break;
			} catch (NumberFormatException e) {
				System.out.println("숫자만 입력하세요.");
			}
		}

		long height = inputPositiveLong("키");
		long weight = inputPositiveLong("몸무게");

		System.out.println("흡연 여부(y/n): ");
		String smoking = sc.nextLine();

		System.out.println("음주 여부(y/n): ");
		String drinking = sc.nextLine();

		System.out.println("질병을 하나씩 입력하세요. 다 입력했으면 '끝' 입력: ");
		List<String> deseaseList = new ArrayList<>();
		while (true) {
			String d = sc.nextLine();
			if (d.equals("끝")) {
				break;
			}
			deseaseList.add(d);
		}

		System.out.println("휴대폰 번호를 입력하세요: ");
		String phoneNumber = sc.nextLine();

		System.out.println("이메일을 입력하세요: ");
		String email = sc.nextLine();

		System.out.println("주소를 입력하세요: ");
		String adress = sc.nextLine();

		User user = new User();
		user.setUserId(userId);
		user.setPassword(password);
		user.setName(name);
		user.setAge(age);
		user.setHeight(height);
		user.setWeight(weight);
		user.setSmoking(smoking);
		user.setDrinking(drinking);
		user.setDeseaseList(deseaseList);
		user.setPhoneNumber(phoneNumber);
		user.setEmail(email);
		user.setAdress(adress);

		um.add(user);

		System.out.println(user.getUserId() + "님. 회원가입이 완료되었습니다..");
	}

	// ================= 회원 조회 =================
	static void getUser() {
		System.out.println("***************** 회원 조회 *****************");

		System.out.println("검색할 회원 아이디를 입력하세요: ");
		String search = sc.nextLine();

		System.out.println(um.getUser(search));
	}

	// ================= 회원 정보 수정 =================
	static void userUpdate() {
		System.out.println("***************** 회원 정보 수정 *****************");

		System.out.println("수정할 회원 아이디를 입력하세요: ");
		String userId = sc.nextLine();

		User user = new User();

		long height = inputPositiveLong("키");
		long weight = inputPositiveLong("몸무게");

		System.out.println("흡연 여부(y/n): ");
		String smoking = sc.nextLine();

		System.out.println("음주 여부(y/n): ");
		String drinking = sc.nextLine();

		System.out.println("질병을 하나씩 입력하세요. 다 입력했으면 '끝' 입력: ");
		List<String> deseaseList = new ArrayList<>();
		while (true) {
			String d = sc.nextLine();
			if (d.equals("끝")) {
				break;
			}
			deseaseList.add(d);
		}

		System.out.println("휴대폰 번호를 입력하세요: ");
		String phoneNumber = sc.nextLine();

		System.out.println("이메일을 입력하세요: ");
		String email = sc.nextLine();

		System.out.println("주소를 입력하세요: ");
		String adress = sc.nextLine();

		user.setUserId(userId);
		user.setHeight(height);
		user.setWeight(weight);
		user.setSmoking(smoking);
		user.setDrinking(drinking);
		user.setDeseaseList(deseaseList);
		user.setPhoneNumber(phoneNumber);
		user.setEmail(email);
		user.setAdress(adress);

		um.update(user);

		System.out.println(user.getUserId() + "님. 회원정보 수정이 완료되었습니다.");
		System.out.println(um.getUser(userId));
	}

	// ================= 회원 탈퇴 =================
	static void userDelete() {
		System.out.println("***************** 회원 탈퇴 *****************");

		System.out.println("탈퇴할 회원 아이디를 입력하세요: ");
		String search = sc.nextLine();

		um.delete(search);

		System.out.println("탈퇴가 완료되었습니다.");
	}

	// ================= 로그인 =================
	static void login() {
		System.out.println("***************** 로그인 *****************");

		String userId = inputUserId();
		String password = inputPassword();

		try {
			um.login(userId, password);
			System.out.println(userId + "님. 로그인 완료되었습니다.");
		} catch (LoginException e) {
			System.out.println(e.getMessage());
		}
	}

	// ================= 로그아웃 =================
	static void logout() {
		System.out.println("***************** 로그아웃 *****************");

		String userId = inputUserId();

		um.logout(userId);
		System.out.println(userId + "님. 로그아웃 완료되었습니다.");
	}
}