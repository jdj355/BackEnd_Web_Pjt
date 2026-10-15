
package com.ssafy.diet.service;

import com.ssafy.diet.dto.User;
import com.ssafy.diet.exception.LoginException;

public interface UserService {

    // 회원가입
    boolean add(User user);

    // 회원조회
    User getUser(String userId);

    // 회원수정
    boolean update(User user);

    // 회원삭제
    boolean delete(String userId);

    // 로그인
    boolean login(String userId, String password)
            throws LoginException;

    // 로그아웃
    boolean logout(String userId);
}
