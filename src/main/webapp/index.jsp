<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>냠냠코치</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="container py-5">

	<div class="p-5 mb-4 bg-light rounded-3">
		<h1 class="fw-bold">냠냠코치</h1>
		<p class="fs-5">먹은 음식을 기록하면 영양 비율과 점수를 분석해 드려요.</p>

		<a class="btn btn-success btn-lg" href="${pageContext.request.contextPath}/diet?action=writeForm">식단 기록하기</a>
		<a class="btn btn-outline-success btn-lg" href="${pageContext.request.contextPath}/diet?action=list">내 식단 보기</a>
	</div>

	<%-- TODO: 회원 담당 팀원 — 로그인/회원가입 링크 (UserController URL 정해지면 연결) --%>

</body>
</html>