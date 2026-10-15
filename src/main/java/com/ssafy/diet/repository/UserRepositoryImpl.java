
package com.ssafy.diet.repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.ssafy.diet.dto.User;

public class UserRepositoryImpl implements UserRepository {

    private final Map<String, User> userMap = new HashMap<>();

    private final File file = new File("db/userData.json");

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    // 싱글턴
    private static final UserRepositoryImpl instance =
            new UserRepositoryImpl();

    private UserRepositoryImpl() {
        loadUserData();
    }

    public static UserRepositoryImpl getInstance() {
        return instance;
    }

    // 회원 데이터 JSON 읽기
    @Override
    public synchronized void loadUserData() {

        if (!file.exists()) {
            System.err.println("회원 파일이 없습니다: "
                    + file.getAbsolutePath());
            return;
        }

        try (BufferedReader br = Files.newBufferedReader(
                file.toPath(), StandardCharsets.UTF_8)) {

            Type type =
                new TypeToken<ArrayList<User>>() {}.getType();

            ArrayList<User> users = gson.fromJson(br, type);

            Map<String, User> loadedMap = new HashMap<>();

            if (users != null) {
                for (User user : users) {
                    loadedMap.put(user.getUserId(), user);
                }
            }

            userMap.clear();
            userMap.putAll(loadedMap);

        } catch (IOException | JsonParseException e) {
            throw new IllegalStateException(
                    "회원 데이터 읽기 실패", e);
        }
    }

    // 회원 데이터 JSON 저장
    @Override
    public synchronized void saveUserData() {

        File parent = file.getParentFile();

        if (parent != null && !parent.exists()
                && !parent.mkdirs()) {
            throw new IllegalStateException(
                    "회원 데이터 폴더 생성 실패");
        }

        try (BufferedWriter bw = Files.newBufferedWriter(
                file.toPath(), StandardCharsets.UTF_8)) {

            gson.toJson(userMap.values(), bw);

        } catch (IOException e) {
            throw new IllegalStateException(
                    "회원 데이터 저장 실패", e);
        }
    }

    // 회원 추가
    @Override
    public synchronized boolean add(User user) {

        if (userMap.containsKey(user.getUserId())) {
            return false;
        }

        userMap.put(user.getUserId(), user);
        saveUserData();

        return true;
    }

    // 회원 조회
    @Override
    public synchronized User getUser(String userId) {
        return userMap.get(userId);
    }

    // 회원 수정
    @Override
    public synchronized boolean update(User user) {

        if (!userMap.containsKey(user.getUserId())) {
            return false;
        }

        userMap.put(user.getUserId(), user);
        saveUserData();

        return true;
    }

    // 회원 삭제
    @Override
    public synchronized boolean delete(String userId) {

        if (!userMap.containsKey(userId)) {
            return false;
        }

        userMap.remove(userId);
        saveUserData();

        return true;
    }
}
