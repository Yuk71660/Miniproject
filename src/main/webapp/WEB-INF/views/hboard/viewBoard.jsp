<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

	<jsp:include page="./../header.jsp"></jsp:include>

	<div class="container">
		<h1>게시판 상세 조회 페이지</h1>



		<div class="board">
			<div class="mb-3 mt-3">
				<label for="boardNo" class="form-label">boardNo:</label> <input
					type="text" class="form-control" id="boardNo"
					value="${boardDetailInfo.boardNo }" readonly />
			</div>

			<div class="mb-3 mt-3">
				<label for="title" class="form-label">title:</label> <input
					type="text" class="form-control" id="title"
					value="${boardDetailInfo.title }" readonly />
			</div>

			<div class="mb-3 mt-3">
				<label for="writer" class="form-label">writer:</label> <input
					type="text" class="form-control" id="writer"
					value="${boardDetailInfo.writer }(${boardDetailInfo.email})"
					readonly />
			</div>

			<div class="mb-3 mt-3">
				<label for="postDate" class="form-label">postDate:</label> <input
					type="text" class="form-control" id="postDate"
					value="${boardDetailInfo.postDate }" readonly />
			</div>

			<div class="mb-3 mt-3">
				<label for="readCount" class="form-label">readCount:</label> <input
					type="text" class="form-control" id="readCount"
					value="${boardDetailInfo.readCount }" readonly />
			</div>

			<div class="mb-3 mt-3">
				<label for="content" class="form-label">content:</label>
				<div id="content"
					style="border: 1px solid #dee2e6; border-radius: 0.375rem; padding: 0.375rem 0.75rem;">
					${boardDetailInfo.content }</div>
			</div>
		</div>

		<div class="uploadFileList">
			<table class="table table-hover">
				<thead>
					<tr>
						<th>thumbNail</th>
						<th>fileName</th>
						<th>size</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="file" items="${boardDetailInfo.fileList }">
						<c:choose>
							<c:when test="${file.originalFileName != null }">
								<tr>
									<c:choose>
										<c:when test="${file.base64Image != null }">
											<!-- 이미지 -->
											<td><img
												src="data:${file.fileType };base64,${file.base64Image}" /></td>
										</c:when>
										<c:otherwise>
											<!-- 이미지가 아닌 -->
											<td><a
												href='/resources/boardUpFiles/${file.newFileName }'><img
													src="/resources/images/noimage.png" /></a></td>
										</c:otherwise>
									</c:choose>
									<td>${file.originalFileName }</td>
									<td>${file.size / 1024 }KBytes</td>
								</tr>
							</c:when>
						</c:choose>
					</c:forEach>
				</tbody>
			</table>
		</div>

		<div class="btns">
			<button type="button" class="btn btn-primary"
				onclick="location.href='/hboard/showReplyForm?boardNo=${boardDetailInfo.boardNo}&ref=${boardDetailInfo.ref}&step=${boardDetailInfo.step}&refOrder=${boardDetailInfo.refOrder}';">
				답글달기</button>
			<button type="button" class="btn btn-secondary">글 수정</button>
			<button type="button" class="btn btn-success">글 삭제</button>
			<button type="button" class="btn btn-info"
				onclick="location.href='./listAll';">리스트페이지</button>
		</div>
	</div>
</body>
</html>