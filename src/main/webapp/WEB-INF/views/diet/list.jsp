<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<c:set var="root" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>내 식단 · 냠냠코치</title>
<%@ include file="_style.jspf"%>
</head>
<body>

	<header class="topbar">
		<div class="inner">
			<a class="brand" href="${root}/">🥗 냠냠코치</a>
			<nav>
				<a class="nav-link-soft active" href="${root}/diet?action=list">내 식단</a>
				<a class="nav-link-soft" href="${root}/diet?action=writeForm">식단 기록</a>
			</nav>
		</div>
	</header>

	<main class="page">
		<div class="d-flex justify-content-between align-items-end mb-4">
			<div>
				<h1 class="page-title">내 식단 기록</h1>
				<p class="page-sub mb-0">지금까지 <b>${fn:length(dietList)}</b>개의 식단을 기록했어요.</p>
			</div>
			<a class="btn btn-green" href="${root}/diet?action=writeForm">+ 식단 기록하기</a>
		</div>

		<c:choose>
			<%-- 기록이 없을 때 --%>
			<c:when test="${empty dietList}">
				<div class="box empty">
					<div class="icon">🍽️</div>
					<h5 class="fw-bold mt-3">아직 기록한 식단이 없어요</h5>
					<p class="page-sub">오늘 먹은 음식을 기록하고 영양 분석을 받아보세요.</p>
					<a class="btn btn-green mt-2" href="${root}/diet?action=writeForm">첫 식단 기록하기</a>
				</div>
			</c:when>

			<%-- 식단 카드 목록 --%>
			<c:otherwise>
				<div class="row g-3">
					<c:forEach items="${dietList}" var="d">
						<%-- 식단별 총 칼로리 계산 --%>
						<c:set var="kcal" value="0" />
						<c:forEach items="${d.foods}" var="f">
							<c:set var="kcal" value="${kcal + f.calories}" />
						</c:forEach>
						<c:set var="mealCls" value="${d.type eq '아침' ? 'meal-am' : d.type eq '점심' ? 'meal-lunch' : d.type eq '저녁' ? 'meal-dinner' : 'meal-snack'}" />

						<div class="col-12 col-md-6 col-lg-4">
							<a class="diet-card" href="${root}/diet?action=detail&id=${d.id}">
								<div class="d-flex justify-content-between align-items-center mb-3">
									<span class="date">${d.date}</span>
									<span class="meal ${mealCls}">${d.type}</span>
								</div>
								<div>
									<c:forEach items="${d.foods}" var="f">
										<span class="chip">${f.name}</span>
									</c:forEach>
								</div>
								<div class="kcal">
									총 <b><fmt:formatNumber value="${kcal}" maxFractionDigits="0" /></b> kcal
									· 음식 ${fn:length(d.foods)}개
								</div>
							</a>
						</div>
					</c:forEach>
				</div>
			</c:otherwise>
		</c:choose>
	</main>

</body>
</html>