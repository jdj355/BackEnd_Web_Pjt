package diet;

import java.util.List;
import java.util.Map;

import com.ssafy.diet.dto.Diet;
import com.ssafy.diet.dto.Food;

public interface DietManager {

	// 데이터 폴더 지정 + JSON 불러오기 (서버 시작 시 DietController.init() 에서 1번 호출)
	void init(String dataDir);

	// JSON 파일 불러오기
	void fileInput();

	// JSON 파일 저장하기
	void fileOutput();

	// 음식 DB 불러오기
	void loadFoods();

	// 식단 등록 (F401)
	boolean add(Diet diet);

	// 전체 식단 조회 (F402)
	Diet[] getDiets();

	// ID로 식단 상세 조회 (F402)
	Diet getDiet(int id);

	// 식단 수정 (F403)
	boolean updateDiet(Diet diet);

	// 식단 삭제 (F404)
	boolean deleteDiet(int id);

	// 다음 등록 ID 생성
	int getNextId();

	// 식단 분석 (F405)
	Map<String, Object> analyze(Diet diet);

	// 음식 DB 검색 (식단 작성 화면)
	List<Food> searchFoods(String keyword);

	// 음식 이름으로 음식 DB 조회
	Food getFood(String name);

	List<Food> recommendDiet(int targetCal, double targetProtein, int maxCount);
}