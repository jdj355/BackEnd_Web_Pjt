<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<c:set var="root" value="${pageContext.request.contextPath}" />
<c:set var="mealCls" value="${diet.type eq '아침' ? 'meal-am' : diet.type eq '점심' ? 'meal-lunch' : diet.type eq '저녁' ? 'meal-dinner' : 'meal-snack'}" />
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>식단 상세 · 냠냠코치</title>
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
		<a class="page-sub d-inline-block mb-3" href="${root}/diet?action=list">← 목록으로</a>

		<div class="d-flex justify-content-between align-items-start flex-wrap gap-3 mb-4">
			<div>
				<span class="meal ${mealCls}">${diet.type}</span>
				<h1 class="page-title mt-2">${diet.date} 식단</h1>
			</div>
			<div class="d-flex gap-2">
				<a class="btn btn-line" href="${root}/diet?action=updateForm&id=${diet.id}">수정</a>
				<form action="${root}/diet" method="post" onsubmit="return confirm('이 식단을 삭제할까요?');">
					<input type="hidden" name="action" value="delete">
					<input type="hidden" name="id" value="${diet.id}">
					<button class="btn btn-danger-line">삭제</button>
				</form>
			</div>
		</div>

		<%-- F405 식단 분석 --%>
		<div class="row g-3 mb-3">
			<!-- 점수 -->
			<div class="col-md-4">
				<div class="box h-100 text-center">
					<div class="box-title text-start">영양 점수</div>
					<div class="score-ring" style="--score: ${analysis.score};">
						<div class="inner">
							<div>
								<div class="num">${analysis.score}</div>
								<div class="unit">/ 100점</div>
							</div>
						</div>
					</div>
					<p class="mt-3 mb-0 fw-semibold">${analysis.comment}</p>
				</div>
			</div>

			<!-- 영양소 -->
			<div class="col-md-8">
				<div class="box h-100">
					<div class="box-title">영양소 구성</div>
					<div class="row g-2 mb-4">
						<div class="col-6 col-lg-3">
							<div class="stat">
								<div class="label">칼로리</div>
								<div class="value"><fmt:formatNumber value="${analysis.totalCalories}" maxFractionDigits="0" /></div>
								<div class="pct text-muted">kcal</div>
							</div>
						</div>
						<div class="col-6 col-lg-3">
							<div class="stat">
								<div class="label"><span class="dot" style="background: var(--carb)"></span>탄수화물</div>
								<div class="value">${analysis.totalCarbs}g</div>
								<div class="pct" style="color: #b07d12">${analysis.carbsRatio}%</div>
							</div>
						</div>
						<div class="col-6 col-lg-3">
							<div class="stat">
								<div class="label"><span class="dot" style="background: var(--prot)"></span>단백질</div>
								<div class="value">${analysis.totalProtein}g</div>
								<div class="pct" style="color: var(--green-dark)">${analysis.proteinRatio}%</div>
							</div>
						</div>
						<div class="col-6 col-lg-3">
							<div class="stat">
								<div class="label"><span class="dot" style="background: var(--fat)"></span>지방</div>
								<div class="value">${analysis.totalFat}g</div>
								<div class="pct" style="color: #c4492d">${analysis.fatRatio}%</div>
							</div>
						</div>
					</div>

					<div class="d-flex justify-content-between small text-muted mb-1">
						<span>내 식단 (열량 기준)</span>
					</div>
					<div class="macro-bar mb-3">
						<div style="width: ${analysis.carbsRatio}%; background: var(--carb);"></div>
						<div style="width: ${analysis.proteinRatio}%; background: var(--prot);"></div>
						<div style="width: ${analysis.fatRatio}%; background: var(--fat);"></div>
					</div>
					<div class="d-flex justify-content-between small text-muted mb-1">
						<span>권장 비율</span>
						<span>탄 55 : 단 20 : 지 25</span>
					</div>
					<div class="macro-bar" style="opacity: .45;">
						<div style="width: 55%; background: var(--carb);"></div>
						<div style="width: 20%; background: var(--prot);"></div>
						<div style="width: 25%; background: var(--fat);"></div>
					</div>
				</div>
			</div>
		</div>

		<%-- 음식 목록 --%>
		<div class="box">
			<div class="box-title">먹은 음식 <span class="text-muted fw-normal">${fn:length(diet.foods)}개</span></div>
			<div class="table-responsive">
				<table class="table food-table mb-0">
					<thead>
						<tr><th>음식</th><th class="text-end">칼로리</th><th class="text-end">탄수화물</th><th class="text-end">단백질</th><th class="text-end">지방</th></tr>
					</thead>
					<tbody>
						<c:forEach items="${diet.foods}" var="f">
							<tr>
								<td class="fw-semibold">${f.name}</td>
								<td class="text-end"><fmt:formatNumber value="${f.calories}" maxFractionDigits="0" /> kcal</td>
								<td class="text-end"><fmt:formatNumber value="${f.carbs}" maxFractionDigits="1" />g</td>
								<td class="text-end"><fmt:formatNumber value="${f.protein}" maxFractionDigits="1" />g</td>
								<td class="text-end"><fmt:formatNumber value="${f.fat}" maxFractionDigits="1" />g</td>
							</tr>
						</c:forEach>
					</tbody>
					<tfoot>
						<tr>
							<td>합계</td>
							<td class="text-end"><fmt:formatNumber value="${analysis.totalCalories}" maxFractionDigits="0" /> kcal</td>
							<td class="text-end">${analysis.totalCarbs}g</td>
							<td class="text-end">${analysis.totalProtein}g</td>
							<td class="text-end">${analysis.totalFat}g</td>
						</tr>
					</tfoot>
				</table>
			</div>
		</div>
	</main>

</body>
</html>