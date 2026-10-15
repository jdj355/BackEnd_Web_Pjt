package diet;

import java.util.List;

public interface DietManager {

	// JSON 파일 불러오기
	void fileInput();

	// JSON 파일 저장하기
	void fileOutput();
	
	// 식단 정보
	void loadFoods();

	// 식단 등록
	boolean add(Diet diet);

	// 전체 식단 조회
	Diet[] getDiets();

	// ID로 식단 상세 조회
	Diet getDiet(int id);

	// 식단 수정
	boolean updateDiet(Diet diet);

	// 식단 삭제
	boolean deleteDiet(int id);

	// 다음 등록 ID 생성
	int getNextId();
	
	List<Food> recommendDiet(int targetCal, double targetProtein, int maxCount);
}