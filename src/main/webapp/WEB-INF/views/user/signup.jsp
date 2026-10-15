
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         session="false" %>

<%!
    // HTML 특수문자 처리
    private String escapeHtml(Object value) {
        if (value == null) return "";

        return String.valueOf(value)
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
%>

<%
    String ctx = request.getContextPath();

    jakarta.servlet.http.HttpSession currentSession =
            request.getSession(false);

    Object loginUserId = currentSession == null
            ? null
            : currentSession.getAttribute("loginUserId");
%>

<!DOCTYPE html>
<html lang="ko">

<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1">

    <title>회원가입 | 냠냠코치</title>

    <style>
        /* 기본 설정 */
        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            background: #f7faf7;
            color: #203b2c;
            font-family: Arial, sans-serif;
        }

        a {
            color: #287b4b;
            text-decoration: none;
        }

        /* 상단 메뉴 */
        header {
            padding: 22px 6%;
            background: white;
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 16px;
            flex-wrap: wrap;
        }

        nav {
            display: flex;
            align-items: center;
            gap: 18px;
        }

        nav form {
            margin: 0;
        }

        nav button {
            padding: 8px 12px;
        }

        /* 회원가입 영역 */
        main {
            max-width: 540px;
            margin: 60px auto;
            padding: 30px;
            background: white;
            border-radius: 18px;
            box-shadow: 0 8px 30px #203b2c0d;
        }

        h1 {
            margin-top: 0;
        }

        p {
            line-height: 1.7;
        }

        /* 입력 폼 */
        .form-group {
            margin-bottom: 20px;
        }

        .form-group > label,
        .form-label {
            display: block;
            margin-bottom: 8px;
            font-weight: 500;
        }

        input:not([type="checkbox"]),
        select {
            width: 100%;
            padding: 13px;
            border: 1px solid #cddace;
            border-radius: 8px;
            font: inherit;
            background: white;
        }

        input:focus,
        select:focus {
            outline: none;
            border-color: #287b4b;
        }

        /* 체크박스 */
        .checkbox-group {
            display: flex;
            flex-wrap: wrap;
            gap: 16px;
        }

        .checkbox-item {
            display: flex;
            align-items: center;
            gap: 6px;
            cursor: pointer;
        }

        .checkbox-item input {
            width: auto;
            margin: 0;
        }

        /* 버튼 */
        button {
            display: inline-block;
            border: 0;
            border-radius: 8px;
            padding: 13px 20px;
            background: #287b4b;
            color: white;
            font: inherit;
            cursor: pointer;
        }

        .submit-btn {
            width: 100%;
            margin-top: 12px;
        }

        /* 안내 및 오류 메시지 */
        .error {
            color: #ac2525;
            background: #fff0f0;
            padding: 12px;
            border-radius: 8px;
        }

        .password-warning {
            color: #ac2525;
            font-size: 13px;
            margin: 6px 0 0;
        }

        .bottom-link {
            text-align: center;
            margin-top: 24px;
        }

        /* 모바일 */
        @media (max-width: 600px) {
            main {
                margin: 28px 16px;
                padding: 24px;
            }
        }
    </style>
</head>

<body>

    <!-- 상단 헤더 -->
    <header>
        <a href="<%= ctx %>/index.jsp">
            <strong>🥗 냠냠코치</strong>
        </a>

        <nav>
            <% if (loginUserId == null) { %>

                <a href="<%= ctx %>/user?action=loginForm">
                    로그인
                </a>

                <a href="<%= ctx %>/user?action=signupForm">
                    회원가입
                </a>

            <% } else { %>

                <a href="<%= ctx %>/user?action=mypage">
                    마이페이지
                </a>

                <form method="post"
                      action="<%= ctx %>/user">

                    <input type="hidden"
                           name="action"
                           value="logout">

                    <button type="submit">로그아웃</button>
                </form>

            <% } %>
        </nav>
    </header>

    <!-- 회원가입 페이지 -->
    <main>

        <h1>회원가입</h1>
        <p>회원 정보를 입력해주세요.</p>

        <!-- 서버 오류 메시지 -->
        <% if (request.getAttribute("error") != null) { %>

            <p class="error" role="alert">
                <%= escapeHtml(request.getAttribute("error")) %>
            </p>

        <% } %>

        <!-- 회원가입 폼 -->
        <form id="signupForm"
              action="<%= ctx %>/user"
              method="post">

            <input type="hidden"
                   name="action"
                   value="signup">

            <!-- 아이디 -->
            <div class="form-group">
                <label for="userId">아이디</label>

                <input type="text"
                       id="userId"
                       name="userId"
                       required>
            </div>

            <!-- 비밀번호 -->
            <div class="form-group">
                <label for="password">비밀번호</label>

                <input type="password"
                       id="password"
                       name="password"
                       minlength="8"
                       required>

                <p id="passwordWarning"
                   class="password-warning"
                   role="status"
                   hidden>
                    비밀번호는 8글자 이상이어야 합니다.
                </p>
            </div>

            <!-- 이름 -->
            <div class="form-group">
                <label for="name">이름</label>

                <input type="text"
                       id="name"
                       name="name"
                       required>
            </div>

            <!-- 나이 -->
            <div class="form-group">
                <label for="age">나이</label>

                <input type="number"
                       id="age"
                       name="age"
                       min="1"
                       required>
            </div>

            <!-- 키 -->
            <div class="form-group">
                <label for="height">키(cm)</label>

                <input type="number"
                       id="height"
                       name="height"
                       min="1"
                       step="1"
                       required>
            </div>

            <!-- 몸무게 -->
            <div class="form-group">
                <label for="weight">몸무게(kg)</label>

                <input type="number"
                       id="weight"
                       name="weight"
                       min="1"
                       step="1"
                       required>
            </div>

            <!-- 흡연 여부 -->
            <div class="form-group">
                <label for="smoking">흡연 여부</label>

                <select id="smoking" name="smoking">
                    <option value="N">비흡연</option>
                    <option value="Y">흡연</option>
                </select>
            </div>

            <!-- 음주 여부 -->
            <div class="form-group">
                <label for="drinking">음주 여부</label>

                <select id="drinking" name="drinking">
                    <option value="N">비음주</option>
                    <option value="Y">음주</option>
                </select>
            </div>

            <!-- 질병 목록 -->
            <div class="form-group">
                <span class="form-label">질병</span>

                <div class="checkbox-group">

                    <label class="checkbox-item">
                        <input type="checkbox"
                               name="deseaseList"
                               value="고혈압">
                        고혈압
                    </label>

                    <label class="checkbox-item">
                        <input type="checkbox"
                               name="deseaseList"
                               value="당뇨">
                        당뇨
                    </label>

                    <label class="checkbox-item">
                        <input type="checkbox"
                               name="deseaseList"
                               value="천식">
                        천식
                    </label>

                </div>
            </div>

            <!-- 전화번호 -->
            <div class="form-group">
                <label for="phoneNumber">전화번호</label>

                <input type="tel"
                       id="phoneNumber"
                       name="phoneNumber">
            </div>

            <!-- 이메일 -->
            <div class="form-group">
                <label for="email">이메일</label>

                <input type="email"
                       id="email"
                       name="email">
            </div>

            <!-- 주소 -->
            <div class="form-group">
                <label for="adress">주소</label>

                <input type="text"
                       id="adress"
                       name="adress">
            </div>

            <!-- 가입 버튼 -->
            <button type="submit" class="submit-btn">
                회원가입
            </button>

        </form>

        <p class="bottom-link">
            이미 회원이신가요?
            <a href="<%= ctx %>/user?action=loginForm">
                로그인
            </a>
        </p>

    </main>

    <!-- 비밀번호 실시간 검사 -->
    <script>
        const passwordInput =
            document.getElementById("password");

        const passwordWarning =
            document.getElementById("passwordWarning");

        passwordInput.addEventListener("input", function () {
            const password = passwordInput.value;

            passwordWarning.hidden =
                password.length === 0 || password.length >= 8;
        });
    </script>

</body>
</html>
