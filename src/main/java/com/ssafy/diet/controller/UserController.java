
package com.ssafy.diet.controller;

import java.io.IOException;

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
    private void signup(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        if (!isPost(request)) {
            response.sendError(405);
            return;
        }

        String userId = request.getParameter("userId");
        String password = request.getParameter("password");

        // User 생성자 구조를 아직 모르므로
        // 기본 생성자와 setter 사용을 가정
        User user = new User();
        user.setUserId(userId);
        user.setPassword(password);

        boolean result = service.add(user);

        if (result) {
            response.sendRedirect(
                    request.getContextPath()
                    + "/user?action=loginForm");
        } else {
            request.setAttribute("error",
                    "회원가입에 실패했습니다.");
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
