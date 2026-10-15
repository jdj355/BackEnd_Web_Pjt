<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false" %>
<%!
private String escapeHtml(Object value) {
    if (value == null) return "";
    return String.valueOf(value).replace("&", "&amp;").replace("<", "&lt;")
        .replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#39;");
}
%>
<%
String ctx = request.getContextPath();
jakarta.servlet.http.HttpSession currentSession = request.getSession(false);
Object loginUserId = currentSession == null ? null : currentSession.getAttribute("loginUserId");
%><%
if (loginUserId == null) {
    response.sendRedirect(ctx + "/user?action=loginForm");
    return;
}
response.setHeader("Cache-Control", "no-store");
%><!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>냠냠코치</title>
<style>
*{box-sizing:border-box}body{margin:0;background:#f7faf7;color:#203b2c;font-family:Arial,sans-serif}header{padding:22px 6%;background:white;display:flex;justify-content:space-between;align-items:center;gap:16px;flex-wrap:wrap}a{color:#287b4b;text-decoration:none}nav{display:flex;align-items:center;gap:18px}main{max-width:540px;margin:60px auto;padding:30px;background:white;border-radius:18px;box-shadow:0 8px 30px #203b2c0d}h1{margin-top:0}p{line-height:1.7}label{display:block;margin:20px 0 8px}input{width:100%;padding:13px;border:1px solid #cddace;border-radius:8px;font:inherit}button,.button{display:inline-block;border:0;border-radius:8px;padding:13px 20px;background:#287b4b;color:white;font:inherit;cursor:pointer}.primary{width:100%;margin-top:24px}.error{color:#ac2525;background:#fff0f0;padding:12px;border-radius:8px}.danger{background:#ac3535}hr{border:0;border-top:1px solid #e2e9e2;margin:30px 0}nav form{margin:0}nav button{padding:8px 12px}small{color:#647567}@media(max-width:600px){main{margin:28px 16px;padding:24px}}
</style>
</head>
<body>
<header>
<a href="<%= ctx %>/index.jsp"><strong>🥗 냠냠코치</strong></a>
<nav>
<% if (loginUserId == null) { %>
<a href="<%= ctx %>/user?action=loginForm">로그인</a>
<a href="<%= ctx %>/user?action=signupForm">회원가입</a>
<% } else { %>
<a href="<%= ctx %>/user?action=mypage">마이페이지</a>
<form method="post" action="<%= ctx %>/user">
<input type="hidden" name="action" value="logout">
<button type="submit">로그아웃</button>
</form>
<% } %>
</nav>
</header>
<main><h1>마이페이지</h1>
<p><strong><%= escapeHtml(request.getAttribute("userId")) %></strong>님의 회원정보</p><% if (request.getAttribute("error") != null) { %>
<p class="error" role="alert"><%= escapeHtml(request.getAttribute("error")) %></p>
<% } %><form method="post" action="<%= ctx %>/user">
<input type="hidden" name="action" value="update">
<label for="userId">아이디</label>
<input id="userId" value="<%= escapeHtml(request.getAttribute("userId")) %>" readonly>
<label for="password">새 비밀번호</label>
<input id="password" name="password" type="password" autocomplete="new-password" required>
<button class="primary" type="submit">비밀번호 수정</button>
</form>
<hr>
<form method="post" action="<%= ctx %>/user" onsubmit="return confirm('정말 탈퇴하시겠습니까? 이 작업은 되돌릴 수 없습니다.');">
<input type="hidden" name="action" value="delete">
<p>회원탈퇴 시 계정이 삭제됩니다.</p>
<button class="danger" type="submit">회원탈퇴</button>
</form></main>
</body>
</html>


