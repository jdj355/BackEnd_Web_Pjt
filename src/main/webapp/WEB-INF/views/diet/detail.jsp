<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="root" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>식단 상세</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>s
<body class="container py-4">
	<h3>${diet.date} · ${diet.type}</h3>

	<table class="table mt-3">
		<thead><tr><th>음식</th><th>칼로리</th><th>단백질</th><th>탄수화물</th><th>지방</th></tr></thead>
		<tbody>
			<c:forEach items="${diet.foods}" var="f">
				<tr>
					<td>${f.name}</td>
					<td>${f.calories}kcal</td>
					<td>${f.protein}g</td>
					<td>${f.carbs}g</td>
					<td>${f.fat}g</td>
				</tr>
			</c:forEach>
		</tbody>
		<tfoot class="fw-bold">
			<tr>
				<td>합계</td>
				<td>${analysis.totalCalories}kcal</td>
				<td>${analysis.totalProtein}g</td>
				<td>${analysis.totalCarbs}g</td>
				<td>${analysis.totalFat}g</td>
			</tr>
		</tfoot>
	</table>

	<%-- F405 식단 분석 --%>
	<div class="card mb-4">
		<div class="card-body">
			<h5 class="card-title">식단 분석 · ${analysis.score}점</h5>
			<p>${analysis.comment}</p>
			<div class="progress" style="height: 24px;">
				<div class="progress-bar bg-warning" style="width:${analysis.carbsRatio}%">탄 ${analysis.carbsRatio}%</div>
				<div class="progress-bar bg-success" style="width:${analysis.proteinRatio}%">단 ${analysis.proteinRatio}%</div>
				<div class="progress-bar bg-danger" style="width:${analysis.fatRatio}%">지 ${analysis.fatRatio}%</div>
			</div>
			<small class="text-muted">권장 비율 탄 55 : 단 20 : 지 25 (열량 기준)</small>
		</div>
	</div>

	<a class="btn btn-outline-secondary" href="${root}/diet?action=list">목록</a>
	<a class="btn btn-primary" href="${root}/diet?action=updateForm&id=${diet.id}">수정</a>
	<form action="${root}/diet" method="post" class="d-inline" onsubmit="return confirm('삭제하시겠습니까?');">
		<input type="hidden" name="action" value="delete">
		<input type="hidden" name="id" value="${diet.id}">
		<button class="btn btn-outline-danger">삭제</button>
	</form>
</body>
</html>