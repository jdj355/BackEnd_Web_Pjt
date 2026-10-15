package com.ssafy.diet.repository;

import com.ssafy.diet.dto.User;

public interface UserRepository {

    // 회원 추가
    boolean add(User user);

    // 회원 조회
    User getUser(String userId);

    // 회원 수정
    boolean update(User user);

    // 회원 삭제
    boolean delete(String userId);

    // JSON 파일 읽기
    void loadUserData();

    // JSON 파일 저장
    void saveUserData();
}
