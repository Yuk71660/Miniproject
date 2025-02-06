<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
   <jsp:include page="./../header.jsp"></jsp:include>

   <div class="container">
      <h1>계층형 게시판 글 작성 페이지</h1>

      <form action="./write" method="post">
         <div class="mb-3 mt-3">
            <label for="title" class="form-label">title:</label> <input
               type="text" class="form-control" id="title" name="title">
         </div>

         <div class="mb-3 mt-3">
            <label for="writer" class="form-label">writer:</label> <input
               type="text" class="form-control" id="writer" name="writer" value="admin">
         </div>

         <div class="mb-3 mt-3">
            <label for="content" class="form-label">content:</label>
            <textarea class="form-control" rows="10" id="content" name="content"></textarea>
         </div>

         <button type="button" class="btn btn-warning"
            onclick="location.href='./listAll';">취소</button>
         <button type="submit" class="btn btn-success">저장</button>
      </form>
   </div>

</body>
</html>