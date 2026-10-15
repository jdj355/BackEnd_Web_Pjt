<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<c:set var="root" value="${pageContext.request.contextPath}" />
<%-- 작성/수정 공용 화면: diet.id 가 0 보다 크면 수정 --%>
<c:set var="isUpdate" value="${diet.id > 0}" />
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>${isUpdate ? '식단 수정' : '식단 기록'} · 냠냠코치</title>
<%@ include file="_style.jspf"%>
</head>
<body>

	<header class="topbar">
		<div class="inner">
			<a class="brand" href="${root}/">🥗 냠냠코치</a>
			<nav>
				<a class="nav-link-soft" href="${root}/diet?action=list">내 식단</a>
				<a class="nav-link-soft active" href="${root}/diet?action=writeForm">식단 기록</a>
			</nav>
		</div>
	</header>

	<main class="page">
		<h1 class="page-title">${isUpdate ? '식단 수정' : '오늘 뭐 드셨어요?'}</h1>
		<p class="page-sub mb-4">음식을 검색해서 체크하면 영양 분석을 해드려요.</p>

		<c:if test="${not empty errorMsg}">
			<div class="alert-soft mb-3">⚠️ ${errorMsg}</div>
		</c:if>

		<form action="${root}/diet" method="post">
			<c:if test="${isUpdate}"><input type="hidden" name="id" value="${diet.id}"></c:if>

			<%-- 1. 날짜 / 식사 유형 --%>
			<div class="box mb-3">
				<div class="box-title"><span class="step">1</span>언제 드셨나요?</div>
				<div class="row g-3">
					<div class="col-md-5">
						<label class="form-label small text-muted">날짜</label>
						<input type="date" name="date" value="${diet.date}" class="form-control">
					</div>
					<div class="col-md-7">
						<label class="form-label small text-muted">식사 유형</label>
						<div class="row g-2">
							<c:forEach items="아침,점심,저녁,간식" var="t" varStatus="s">
								<div class="col-3 pick type">
									<%-- 처음 작성할 때는 '점심'이 기본 선택 --%>
									<input type="radio" name="type" value="${t}" id="type${s.index}"
										${diet.type eq t or (empty diet.type and t eq '점심') ? 'checked' : ''}>
									<label for="type${s.index}">${t}</label>
								</div>
							</c:forEach>
						</div>
					</div>
				</div>
			</div>

			<%-- 2. 선택한 음식 --%>
			<div class="box mb-3">
				<c:set var="sumKcal" value="0" />
				<c:forEach items="${diet.foods}" var="f">
					<c:set var="sumKcal" value="${sumKcal + f.calories}" />
				</c:forEach>

				<div class="box-title d-flex justify-content-between">
					<span><span class="step">2</span>선택한 음식</span>
					<c:if test="${not empty diet.foods}">
						<span class="text-muted fw-normal small">합계 <b class="text-dark"><fmt:formatNumber value="${sumKcal}" maxFractionDigits="0" /></b> kcal</span>
					</c:if>
				</div>

				<c:choose>
					<c:when test="${empty diet.foods}">
						<p class="page-sub mb-0">아직 선택한 음식이 없어요. 아래에서 검색해 추가해 주세요.</p>
					</c:when>
					<c:otherwise>
						<div class="row g-2">
							<c:forEach items="${diet.foods}" var="f" varStatus="s">
								<div class="col-md-6 pick">
									<input type="checkbox" name="foodName" value="${f.name}" id="sel${s.index}" checked>
									<label for="sel${s.index}">
										<span class="check">✓</span>
										<span class="name">${f.name}</span>
										<div class="meta"><fmt:formatNumber value="${f.calories}" maxFractionDigits="0" /> kcal · 탄 ${f.carbs}g · 단 ${f.protein}g · 지 ${f.fat}g</div>
									</label>
								</div>
							</c:forEach>
						</div>
						<p class="small text-muted mt-2 mb-0">체크를 해제하고 저장하면 목록에서 빠져요.</p>
					</c:otherwise>
				</c:choose>
			</div>

			<%-- 3. 음식 검색 : 같은 폼을 GET 으로 다시 보내서 입력값/선택값을 유지한다 --%>
			<div class="box mb-3">
				<div class="box-title"><span class="step">3</span>음식 검색</div>
				<div class="d-flex gap-2">
					<input type="text" name="keyword" value="${param.keyword}" class="form-control" placeholder="예: 닭가슴살, 현미밥, 김치찌개">
					<button class="btn btn-line text-nowrap" formmethod="get" formnovalidate
						name="action" value="${isUpdate ? 'updateForm' : 'writeForm'}">🔍 검색</button>
				</div>

				<c:if test="${not empty param.keyword}">
					<div class="mt-4">
						<c:choose>
							<c:when test="${empty foodList}">
								<p class="page-sub mb-0">'<b>${param.keyword}</b>' 검색 결과가 없어요.</p>
							</c:when>
							<c:otherwise>
								<p class="small text-muted">'<b>${param.keyword}</b>' 검색 결과 ${fn:length(foodList)}개 · 먹은 음식을 체크하세요</p>
								<div class="row g-2">
									<c:forEach items="${foodList}" var="f" varStatus="s">
										<div class="col-md-6 pick">
											<input type="checkbox" name="foodName" value="${f.name}" id="res${s.index}">
											<label for="res${s.index}">
												<span class="check">✓</span>
												<span class="name">${f.name}</span>
												<div class="meta"><fmt:formatNumber value="${f.calories}" maxFractionDigits="0" /> kcal · 탄 ${f.carbs}g · 단 ${f.protein}g · 지 ${f.fat}g</div>
											</label>
										</div>
									</c:forEach>
								</div>
							</c:otherwise>
						</c:choose>
					</div>
				</c:if>
			</div>

			<%-- 저장 버튼 (화면 아래 고정) --%>
			<div class="action-bar d-flex justify-content-end gap-2">
				<c:choose>
					<c:when test="${isUpdate}"><a class="btn btn-line" href="${root}/diet?action=detail&id=${diet.id}">취소</a></c:when>
					<c:otherwise><a class="btn btn-line" href="${root}/diet?action=list">취소</a></c:otherwise>
				</c:choose>
				<button class="btn btn-green" name="action" value="${isUpdate ? 'update' : 'write'}">
					${isUpdate ? '수정 완료' : '저장하고 분석 보기'}
				</button>
			</div>
		</form>
	</main>

</body>
</html>