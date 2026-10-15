package diet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

public class DietManagerImpl implements DietManager {

	private Map<Integer, Diet> dietMap = new HashMap<>();
	private List<Food> foodList = new ArrayList<>();	

	private final File file = new File("db/diet-data.json");

	private final Gson gson = new GsonBuilder()
			.setPrettyPrinting()
			.create();

	// 싱글턴 객체 생성
	private static final DietManagerImpl instance =
			new DietManagerImpl();

	private DietManagerImpl() {
	}

	public static DietManagerImpl getInstance() {
		return instance;
	}

	@Override
	public void fileInput() {

		if (!file.exists()) {
			System.out.println("diet-data.json 파일이 없습니다.");
			dietMap = new HashMap<>();
			return;
		}

		try (
			BufferedReader br = Files.newBufferedReader(
					file.toPath(),
					StandardCharsets.UTF_8
			)
		) {

			Type dietListType = new TypeToken<ArrayList<Diet>>(){}.getType();
			List<Diet> loadedList = gson.fromJson(br, dietListType);   // 리스트로 받고

			dietMap = new HashMap<>();
			if (loadedList != null) {
			    for (Diet d : loadedList) {
			        dietMap.put(d.getId(), d);   // id를 키로 Map에 채움
			    }
			}
			System.out.println(dietMap.size() + "개의 식단을 불러왔습니다.");

		} catch (JsonParseException e) {
			System.out.println(
					"diet-data.json의 JSON 형식이 올바르지 않습니다."
			);
			e.printStackTrace();

		} catch (IOException e) {
			System.out.println(
					"식단 파일을 읽는 중 오류가 발생했습니다."
			);
			e.printStackTrace();
		}
	}

	@Override
	public void fileOutput() {

		File parentFolder = file.getParentFile();

		if (parentFolder != null && !parentFolder.exists()) {
			parentFolder.mkdirs();
		}

		try (
			BufferedWriter bw = Files.newBufferedWriter(
					file.toPath(),
					StandardCharsets.UTF_8
			)
		) {

			gson.toJson(dietMap.values(), bw);

		} catch (IOException e) {
			System.out.println(
					"식단 파일을 저장하는 중 오류가 발생했습니다."
			);
			e.printStackTrace();
		}
	}
	
	// food 로딩 메서드
	public void loadFoods() {
	    File foodFile = new File("db/foods.json");

	    // 빈 문자열/잘못된 값을 0.0으로 처리하는 double 어댑터
	    Gson safeGson = new GsonBuilder()
	        .registerTypeAdapter(double.class, new JsonDeserializer<Double>() {
	            public Double deserialize(JsonElement json, Type t, JsonDeserializationContext ctx) {
	                try {
	                    String s = json.getAsString().trim();
	                    return s.isEmpty() ? 0.0 : Double.parseDouble(s);
	                } catch (Exception e) {
	                    return 0.0;   // 빈 값이나 이상한 값은 0으로
	                }
	            }
	        })
	        .create();

	    try (BufferedReader br = Files.newBufferedReader(foodFile.toPath(), StandardCharsets.UTF_8)) {
	        Type foodListType = new TypeToken<ArrayList<Food>>(){}.getType();
	        foodList = safeGson.fromJson(br, foodListType);
	        System.out.println(foodList.size() + "개의 음식을 불러왔습니다.");
	    } catch (IOException e) {
	        System.out.println("foods.json을 읽는 중 오류가 발생했습니다.");
	        foodList = new ArrayList<>();
	    }
	}

	@Override
	public boolean add(Diet diet) {

		// 같은 ID가 이미 존재하는지 확인
		if (getDiet(diet.getId()) != null) {
			return false;
		}

		dietMap.put(diet.getId(), diet);
		fileOutput();

		return true;
	}

	@Override
	public Diet[] getDiets() {
		return dietMap.values().toArray(new Diet[0]);
	}

	@Override
	public Diet getDiet(int id) {
		return dietMap.get(id);
	}

	@Override
	public boolean updateDiet(Diet newDiet) {
		if(!dietMap.containsKey(newDiet.getId())) return false;
		dietMap.put(newDiet.getId(), newDiet);
		return true;
	}

	@Override
	public boolean deleteDiet(int id) {

		if(!dietMap.containsKey(id)) return false;
		dietMap.remove(id);
		
		return true;
	}

	@Override
	public int getNextId() {

		int maxId = 0;
		
		for (Diet diet : dietMap.values()) {
		    if (diet.getId() > maxId) {
		        maxId = diet.getId();
		    }
		}
		return maxId + 1;
	}
	
	public List<Food> recommendDiet(int targetCal, double targetProtein, int maxCount) {
		List<Food> candidates = new ArrayList<>();
		for(Food f : foodList) {
			if(f.getCalories() > 0) {
				candidates.add(f);
			}
		}
		
		candidates.sort((a, b) -> {
			double ratioA = a.getCalories() / a.getCalories();
			double ratioB = b.getProtein() / b.getProtein();
			return Double.compare(ratioB, ratioA);   // b, a 순서 = 내림차순
		});
		
		List<Food> picked = new ArrayList<>();
		int sumCal = 0;
		double sumProtein = 0;
		
		for(Food f : candidates) {
			if(picked.size() >= maxCount) break;
			
			if(sumCal + f.getCalories() <= targetCal) {
				picked.add(f);
				sumCal += f.getCalories();
				sumProtein += f.getProtein();
			}
			
			if(sumProtein >= targetProtein) break;
		}
		
		return picked;
	}
}