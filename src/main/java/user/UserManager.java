package user;

public interface UserManager {
	// 회원 가입
	boolean add(User user);
	
	// 회원 조회
	User getUser(String userId);
	
	// 회원 수정
	boolean update(User user);
	
	// 회원 삭제
	boolean delete(String userId);
	
	// 로그인
	boolean login(String userId, String password) throws LoginException;
	
	// 로그아웃
	boolean logout(String userId);
	
	// 회원데이터 json 파일 입력
	void loadUserData();
}
