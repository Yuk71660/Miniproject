<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<jsp:include page="./../header.jsp"></jsp:include>

	<div class="container mt-3">
		<h1>계층형 게시판 전체 리스트 페이지</h1>

		<table class="table table-hover boardList">
			<thead>
				<tr>
					<th>#</th>
					<th>title</th>
					<th>writer</th>
					<th>postDate</th>
					<th>readCount</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="board" items="${hboardList}">
					<tr>
						<td>${board.boardNo}</td>
						<td>${board.title}</td>
						<td>${board.writer}</td>
						<td>${board.postDate}</td>
						<td>${board.readCount}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</body>
</html>