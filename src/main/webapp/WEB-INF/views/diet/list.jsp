<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="root" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>식단 목록</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="container py-4">
	<div class="d-flex justify-content-between align-items-center mb-3">
		<h3>식단 목록</h3>
		<a class="btn btn-success" href="${root}/diet?action=writeForm">+ 식단 기록</a>
	</div>

	<c:choose>
		<c:when test="${empty dietList}">
			<p class="text-muted">등록된 식단이 없습니다.</p>
		</c:when>
		<c:otherwise>
			<table class="table table-hover">
				<thead><tr><th>ID</th><th>날짜</th><th>식사</th><th>음식</th></tr></thead>
				<tbody>
					<c:forEach items="${dietList}" var="d">
						<tr>
							<td>${d.id}</td>
							<td><a href="${root}/diet?action=detail&id=${d.id}">${d.date}</a></td>
							<td>${d.type}</td>
							<td>
								<c:forEach items="${d.foods}" var="f" varStatus="s">${f.name}${s.last ? '' : ', '}</c:forEach>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:otherwise>
	</c:choose>
</body>
</html>