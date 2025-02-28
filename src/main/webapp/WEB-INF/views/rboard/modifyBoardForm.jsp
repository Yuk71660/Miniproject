<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script
   src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>

<!-- include summernote css/js -->
<link
   href="https://cdn.jsdelivr.net/npm/summernote@0.9.0/dist/summernote.min.css"
   rel="stylesheet">
<script
   src="https://cdn.jsdelivr.net/npm/summernote@0.9.0/dist/summernote.min.js"></script>   
   
<script>
$(document).ready(function() {
   $('#summernote').summernote();
});
</script>
<style>
.removedFile>td {
   color: #333;
   background-color: #eee;
}
</style>
</head>
<body>

   <jsp:include page="./../header.jsp"></jsp:include>

   <div class="container">
      <h1>게시판 수정 페이지</h1>

      <form action="./modifyBoard" method="post">
         <div class="board">
            <div class="mb-3 mt-3">
               <label for="boardNo" class="form-label">boardNo:</label> <input
                  type="text" class="form-control" id="boardNo"
                  value="${boardDetailInfo.boardNo }" name="boardNo" readonly />
            </div>

            <div class="mb-3 mt-3">
               <label for="title" class="form-label">title:</label> <input
                  type="text" class="form-control" id="title" name="title"
                  value="${boardDetailInfo.title }" />
            </div>

            <div class="mb-3 mt-3">
               <label for="writer" class="form-label">writer:</label> <input
                  type="text" class="form-control" id="writer"
                  value="${boardDetailInfo.writer }(${boardDetailInfo.email})"
                  readonly />
            </div>

            <div class="mb-3 mt-3">
               <label for="content" class="form-label">content:</label>
               <textarea class="form-control" rows="10" id="summernote"
                  name="content">${boardDetailInfo.content }</textarea>
            </div>
         </div>

         <div class="btns" style="clear: right;">
            <button type="submit" class="btn btn-secondary">수정완료</button>
            <button type="button" class="btn btn-info" onclick="location.href='./listAll';">취소</button>
            
         </div>
      </form>
   </div>


   <!-- The Modal -->
   <div class="modal" id="myModal">
      <div class="modal-dialog">
         <div class="modal-content">
            <!-- Modal Header -->
            <div class="modal-header">
               <h4 class="modal-title">Miniproject.com</h4>
               <button type="button" class="btn-close modalClose"
                  data-bs-dismiss="modal"></button>
            </div>

            <!-- Modal body -->
            <div class="modal-body">일시적인 장애가 발생하였습니다. 잠시 후 다시 이용해 주세요.</div>

            <!-- Modal footer -->
            <div class="modal-footer">
               <button type="button" class="btn btn-danger modalClose"
                  data-bs-dismiss="modal">Close</button>
            </div>
         </div>
      </div>
   </div>
</body>
</html>