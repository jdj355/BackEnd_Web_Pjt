
package com.ssafy.diet.controller;

import java.io.IOException;
import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;


import com.ssafy.diet.dto.User;
import com.ssafy.diet.exception.LoginException;
import com.ssafy.diet.service.UserService;
import com.ssafy.diet.service.UserServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/user")
public class UserController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private UserService service =
            UserServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        process(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        process(request, response);
    }

    private void process(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) {
            action = "loginForm";
        }

        switch (action) {

        case "signupForm":
            forward(request, response, "signup.jsp");
            break;

        case "signup":
            signup(request, response);
            break;

        case "loginForm":
            forward(request, response, "login.jsp");
            break;

        case "login":
            login(request, response);
            break;

        case "logout":
            logout(request, response);
            break;

        case "mypage":
            mypage(request, response, "mypage.jsp");
            break;

        case "update":
            update(request, response);
            break;

        case "delete":
            delete(request, response);
            break;

        default:
            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND);
        }
    }

    // JSP로 이동
    private void forward(HttpServletRequest request,
            HttpServletResponse response, String jsp)
            throws ServletException, IOException {

        request.getRequestDispatcher(
                "/WEB-INF/views/user/" + jsp)
                .forward(request, response);
    }

    // 회원가입
    private void signup(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        if (!isPost(request)) {
            response.sendError(405);
            return;
        }

        try {
            String userId = request.getParameter("userId");
            String password = request.getParameter("password");
            // 비밀번호 8글자 이상 검사
//            if (password == null || password.length() < 8) {
//                request.setAttribute("error", "비밀번호는 8글자 이상이어야 합니다.");
//                forward(request, response, "signup.jsp");
//                return;
//            }
            String name = request.getParameter("name");

            int age = Integer.parseInt(
                    request.getParameter("age"));

            long height = Long.parseLong(
                    request.getParameter("height"));

            long weight = Long.parseLong(
                    request.getParameter("weight"));

            String smoking = request.getParameter("smoking");
            String drinking = request.getParameter("drinking");

            String[] diseases =
                    request.getParameterValues("deseaseList");

            String phoneNumber =
                    request.getParameter("phoneNumber");

            String email =
                    request.getParameter("email");

            String adress =
                    request.getParameter("adress");

            User user = new User();

            user.setUserId(userId);
            user.setPassword(password);
            user.setName(name);
            user.setAge(age);
            user.setHeight(height);
            user.setWeight(weight);
            user.setSmoking(smoking);
            user.setDrinking(drinking);

            user.setDeseaseList(
                    diseases == null
                        ? new ArrayList<>()
                        : Arrays.asList(diseases)
            );

            user.setPhoneNumber(phoneNumber);
            user.setEmail(email);
            user.setAdress(adress);

            // 시스템 관리 필드
            user.setDelYn("N");
            user.setRegDate(new Date());
            user.setModDate(null);
            user.setLoginStatus("N");

            boolean result = service.add(user);

            if (result) {
                response.sendRedirect(
                        request.getContextPath()
                        + "/user?action=loginForm");
            } else {
                request.setAttribute(
                        "error", "회원가입에 실패했습니다.");

                forward(request, response, "signup.jsp");
            }
            
        } catch (NumberFormatException e) {

            request.setAttribute(
                    "error", "나이, 키, 몸무게는 숫자로 입력해주세요.");

            forward(request, response, "signup.jsp");
        }
    }

    // 로그인
    private void login(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        if (!isPost(request)) {
            response.sendError(405);
            return;
        }

        String userId = request.getParameter("userId");
        String password = request.getParameter("password");

        try {
            boolean result = service.login(userId, password);

            if (result) {
                // 세션 고정 공격 방지를 위해 기존 세션 ID 변경
                HttpSession session = request.getSession();
                request.changeSessionId();

                session.setAttribute("loginUserId", userId);

                response.sendRedirect(
                        request.getContextPath()
                        + "/user?action=mypage");
            }

        } catch (LoginException e) {
            request.setAttribute("error",
                    "아이디 또는 비밀번호가 올바르지 않습니다.");
            forward(request, response, "login.jsp");
        }
    }

    // 로그아웃
    private void logout(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        if (!isPost(request)) {
            response.sendError(405);
            return;
        }

        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        response.sendRedirect(
                request.getContextPath()
                + "/user?action=loginForm");
    }

    // 마이페이지
    private void mypage(
            HttpServletRequest request,
            HttpServletResponse response,
            String jsp)
            throws ServletException, IOException {

        String userId = getLoginUserId(request);

        // 로그인하지 않은 경우
        if (userId == null) {
            redirectLogin(request, response);
            return;
        }

        // 회원 정보 조회
        User user = service.getUser(userId);

        if (user == null) {
            response.sendError(404);
            return;
        }

        request.setAttribute("userId", user.getUserId());

        // mypage.jsp로 이동
        forward(request, response, jsp);
    }

    // 회원정보 수정
    private void update(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        if (!isPost(request)) {
            response.sendError(405);
            return;
        }

        String userId = getLoginUserId(request);

        if (userId == null) {
            redirectLogin(request, response);
            return;
        }

        String password = request.getParameter("password");

        if (password == null || password.isEmpty()) {
            request.setAttribute("error", "비밀번호를 입력해주세요.");

            mypage(request, response, "mypage.jsp");
            return;
        }

        User user = service.getUser(userId);

        if (user == null) {
            response.sendError(404);
            return;
        }

        user.setPassword(password);

        boolean result = service.update(user);

        if (result) {
            response.sendRedirect(
                request.getContextPath() + "/user?action=mypage"
            );
        } else {
            request.setAttribute("error", "회원정보 수정에 실패했습니다.");

            mypage(request, response, "mypage.jsp");
        }
    }

    // 회원탈퇴
    private void delete(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        if (!isPost(request)) {
            response.sendError(405);
            return;
        }

        String userId = getLoginUserId(request);

        if (userId == null) {
            redirectLogin(request, response);
            return;
        }

        boolean result = service.delete(userId);

        if (result) {
            HttpSession session = request.getSession(false);

            if (session != null) {
                session.invalidate();
            }

            response.sendRedirect(
                    request.getContextPath() + "/");
        } else {
            response.sendError(404);
        }
    }

    private String getLoginUserId(
            HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session == null) {
            return null;
        }

        return (String) session.getAttribute("loginUserId");
    }

    private boolean isPost(HttpServletRequest request) {
        return "POST".equalsIgnoreCase(request.getMethod());
    }

    private void redirectLogin(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        response.sendRedirect(
                request.getContextPath()
                + "/user?action=loginForm");
    }
}
