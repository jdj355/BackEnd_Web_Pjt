
package com.ssafy.diet.service;

import java.util.regex.Pattern;

import com.ssafy.diet.exception.LoginException;
import com.ssafy.diet.dto.User;
import com.ssafy.diet.repository.UserRepository;
import com.ssafy.diet.repository.UserRepositoryImpl;

public class UserServiceImpl implements UserService {

    private final UserRepository repository =
            UserRepositoryImpl.getInstance();

    // 싱글턴
    private static final UserServiceImpl instance =
            new UserServiceImpl();

    private UserServiceImpl() {
    }

    public static UserServiceImpl getInstance() {
        return instance;
    }

    // 회원가입
    @Override
    public boolean add(User user) {

        if (user == null
                || user.getUserId() == null
                || user.getPassword() == null
                || user.getName() == null) {
            return false;
        }

        if (!user.getUserId().matches("^[a-zA-Z0-9]{4,20}$")) {
            return false;
        }

        if (user.getPassword().length() < 8) {
            return false;
        }

        if (user.getName().trim().isEmpty()
                || user.getAge() <= 0
                || user.getHeight() <= 0
                || user.getWeight() <= 0) {
            return false;
        }

        return repository.add(user);
    }

    // 회원조회
    @Override
    public User getUser(String userId) {
        return repository.getUser(userId);
    }

    // 회원수정
    @Override
    public boolean update(User user) {

        if (user == null || user.getUserId() == null) {
            return false;
        }

        if (repository.getUser(user.getUserId()) == null) {
            return false;
        }

        return repository.update(user);
    }

    // 회원삭제
    @Override
    public boolean delete(String userId) {

        if (repository.getUser(userId) == null) {
            return false;
        }

        return repository.delete(userId);
    }

    // 로그인
    @Override
    public boolean login(String userId, String password)
            throws LoginException {

        User user = repository.getUser(userId);

        if (user == null
                || password == null
                || !password.equals(user.getPassword())) {

            throw new LoginException(userId, password);
        }

        return true;
    }

    // 로그아웃
    @Override
    public boolean logout(String userId) {

        User user = repository.getUser(userId);

        if (user == null) {
            return false;
        }

        // 기존 로그인 상태 확인
        if (!"Y".equals(user.getLoginStatus())) {
            return false;
        }

        user.setLoginStatus("N");

        return repository.update(user);
    }

    // 아이디 및 비밀번호 영문 검사
    private boolean isAlphabet(String str) {

        return str != null
                && Pattern.matches("^[a-zA-Z]*$", str);
    }
}
