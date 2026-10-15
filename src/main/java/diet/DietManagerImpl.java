package diet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.ssafy.diet.dto.Diet;
import com.ssafy.diet.dto.Food;

public class DietManagerImpl implements DietManager {

	// TreeMap: id 순서대로 정렬돼서 목록이 깔끔하게 나온다
	private Map<Integer, Diet> dietMap = new TreeMap<>();
	private List<Food> foodList = new ArrayList<>();

	// [변경] 경로를 고정하지 않고 init() 에서 받는다
	//  - 콘솔: new File("db/...") 는 프로젝트 폴더 기준이라 OK
	//  - Tomcat: 실행 위치가 달라서 못 찾음 → WEB-INF/data 의 실제 경로를 받아서 사용
	private File file = new File("db/diet-data.json");
	private File foodFile = new File("db/foods.json");

	private final Gson gson = new GsonBuilder()
			.setPrettyPrinting()
			.create();

	// 싱글턴 객체 생성
	private static final DietManagerImpl instance = new DietManagerImpl();

	private DietManagerImpl() {
	}

	public static DietManagerImpl getInstance() {
		return instance;
	}

	@Override
	public synchronized void init(String dataDir) {
		file = new File(dataDir, "diet-data.json");
		foodFile = new File(dataDir, "foods.json");
		fileInput();
		loadFoods();
	}

	@Override
	public synchronized void fileInput() {

		dietMap = new TreeMap<>();

		if (!file.exists()) {
			System.out.println("diet-data.json 파일이 없습니다: " + file.getAbsolutePath());
			return;
		}

		try (BufferedReader br = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {

			Type dietListType = new TypeToken<ArrayList<Diet>>() {}.getType();
			List<Diet> loadedList = gson.fromJson(br, dietListType);

			if (loadedList != null) {
				for (Diet d : loadedList) {
					dietMap.put(d.getId(), d);
				}
			}
			System.out.println(dietMap.size() + "개의 식단을 불러왔습니다.");

		} catch (JsonParseException e) {
			System.out.println("diet-data.json의 JSON 형식이 올바르지 않습니다.");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("식단 파일을 읽는 중 오류가 발생했습니다.");
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void fileOutput() {

		File parentFolder = file.getParentFile();
		if (parentFolder != null && !parentFolder.exists()) {
			parentFolder.mkdirs();
		}

		try (BufferedWriter bw = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
			gson.toJson(dietMap.values(), bw);
		} catch (IOException e) {
			System.out.println("식단 파일을 저장하는 중 오류가 발생했습니다.");
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void loadFoods() {

		// 빈 문자열/잘못된 값을 0.0으로 처리하는 double 어댑터
		Gson safeGson = new GsonBuilder()
				.registerTypeAdapter(double.class, new JsonDeserializer<Double>() {
					public Double deserialize(JsonElement json, Type t, JsonDeserializationContext ctx) {
						try {
							String s = json.getAsString().trim();
							return s.isEmpty() ? 0.0 : Double.parseDouble(s);
						} catch (Exception e) {
							return 0.0;
						}
					}
				})
				.create();

		if (!foodFile.exists()) {
			System.out.println("foods.json 파일이 없습니다: " + foodFile.getAbsolutePath());
			foodList = new ArrayList<>();
			return;
		}

		try (BufferedReader br = Files.newBufferedReader(foodFile.toPath(), StandardCharsets.UTF_8)) {
			Type foodListType = new TypeToken<ArrayList<Food>>() {}.getType();
			List<Food> loaded = safeGson.fromJson(br, foodListType);
			foodList = (loaded == null) ? new ArrayList<>() : loaded;
			System.out.println(foodList.size() + "개의 음식을 불러왔습니다.");
		} catch (IOException | JsonParseException e) {
			System.out.println("foods.json을 읽는 중 오류가 발생했습니다.");
			foodList = new ArrayList<>();
		}
	}

	@Override
	public synchronized boolean add(Diet diet) {

		// 같은 ID가 이미 존재하는지 확인
		if (dietMap.containsKey(diet.getId())) {
			return false;
		}

		dietMap.put(diet.getId(), diet);
		fileOutput();
		return true;
	}

	@Override
	public synchronized Diet[] getDiets() {
		return dietMap.values().toArray(new Diet[0]);
	}

	@Override
	public synchronized Diet getDiet(int id) {
		return dietMap.get(id);
	}

	@Override
	public synchronized boolean updateDiet(Diet newDiet) {
		if (!dietMap.containsKey(newDiet.getId())) return false;
		dietMap.put(newDiet.getId(), newDiet);
		fileOutput();   // [버그 수정] 기존에는 저장이 빠져서 서버 재시작 시 수정 내용이 사라졌음
		return true;
	}

	@Override
	public synchronized boolean deleteDiet(int id) {
		if (!dietMap.containsKey(id)) return false;
		dietMap.remove(id);
		fileOutput();   // [버그 수정] 삭제도 동일
		return true;
	}

	@Override
	public synchronized int getNextId() {
		int maxId = 0;
		for (Diet diet : dietMap.values()) {
			if (diet.getId() > maxId) {
				maxId = diet.getId();
			}
		}
		return maxId + 1;
	}

	// ================= 식단 분석 (F405) =================

	@Override
	public Map<String, Object> analyze(Diet diet) {

		double cal = 0, protein = 0, carbs = 0, fat = 0;
		if (diet != null && diet.getFoods() != null) {
			for (Food f : diet.getFoods()) {
				cal += f.getCalories();
				protein += f.getProtein();
				carbs += f.getCarbs();
				fat += f.getFat();
			}
		}

		// 열량 기준 비율 (탄수화물 4kcal, 단백질 4kcal, 지방 9kcal / 1g)
		double energy = carbs * 4 + protein * 4 + fat * 9;
		double carbsRatio = 0, proteinRatio = 0, fatRatio = 0;
		if (energy > 0) {
			carbsRatio = round1(carbs * 4 / energy * 100);
			proteinRatio = round1(protein * 4 / energy * 100);
			fatRatio = round1(fat * 9 / energy * 100);
		}

		// TODO: 임시 점수 — 권장 비율(탄55 : 단20 : 지25)과의 차이만큼 감점
		double diff = Math.abs(carbsRatio - 55) + Math.abs(proteinRatio - 20) + Math.abs(fatRatio - 25);
		int score = energy > 0 ? (int) Math.max(0, Math.round(100 - diff)) : 0;

		String comment;
		if (energy == 0) comment = "분석할 음식이 없습니다.";
		else if (score >= 80) comment = "영양 균형이 좋은 식단입니다.";
		else if (proteinRatio < 15) comment = "단백질이 부족해요. 단백질 식품을 추가해 보세요.";
		else if (fatRatio > 35) comment = "지방 비율이 높아요.";
		else comment = "탄수화물/단백질/지방 비율을 조정해 보세요.";

		// JSP 에서 ${analysis.totalCalories} 처럼 꺼내 쓴다
		Map<String, Object> result = new LinkedHashMap<>();
		result.put("totalCalories", round1(cal));
		result.put("totalProtein", round1(protein));
		result.put("totalCarbs", round1(carbs));
		result.put("totalFat", round1(fat));
		result.put("carbsRatio", carbsRatio);
		result.put("proteinRatio", proteinRatio);
		result.put("fatRatio", fatRatio);
		result.put("score", score);
		result.put("comment", comment);
		return result;
	}

	private double round1(double v) {
		return Math.round(v * 10) / 10.0;
	}

	// ================= 음식 DB =================

	@Override
	public List<Food> searchFoods(String keyword) {
		List<Food> result = new ArrayList<>();
		if (keyword == null || keyword.isBlank()) return result;

		for (Food f : foodList) {
			if (f.getName() != null && f.getName().contains(keyword.trim())) {
				result.add(f);
				if (result.size() >= 30) break;   // 너무 많이 나오지 않게 30개까지
			}
		}
		return result;
	}

	@Override
	public Food getFood(String name) {
		for (Food f : foodList) {
			if (f.getName() != null && f.getName().equals(name)) {
				return f;
			}
		}
		return null;
	}

	@Override
	public List<Food> recommendDiet(int targetCal, double targetProtein, int maxCount) {
		List<Food> candidates = new ArrayList<>();
		for (Food f : foodList) {
			if (f.getCalories() > 0) {
				candidates.add(f);
			}
		}

		// [버그 수정] 칼로리 대비 단백질이 높은 순 (기존: a.cal / a.cal → 항상 1)
		candidates.sort((a, b) -> {
			double ratioA = a.getProtein() / a.getCalories();
			double ratioB = b.getProtein() / b.getCalories();
			return Double.compare(ratioB, ratioA);   // 내림차순
		});

		List<Food> picked = new ArrayList<>();
		double sumCal = 0;
		double sumProtein = 0;

		for (Food f : candidates) {
			if (picked.size() >= maxCount) break;

			if (sumCal + f.getCalories() <= targetCal) {
				picked.add(f);
				sumCal += f.getCalories();
				sumProtein += f.getProtein();
			}

			if (sumProtein >= targetProtein) break;
		}

		return picked;
	}
}