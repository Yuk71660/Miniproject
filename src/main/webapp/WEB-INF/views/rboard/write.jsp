<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>Insert title here</title>
<!-- include libraries(jQuery, bootstrap) 
<link
   href="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css"
   rel="stylesheet">
-->
<!-- 
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>

<
<script
   src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
 -->
 
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

   function isValid() {
      let result = false;
      let title = $('#title').val();
      if (title.length < 1 || title == null) {
         $('.modal-body').html('제목은 비어있으면 안됩니다!');
         $('#myModal').show(500);
      } else {
         result = true;
      }

      return result;
   }
</script>
<style>
.fileUploadArea {
   width: 100%;
   height: 300px;
   background-color: lightgray;
   text-align: center;
   line-height: 300px;
}
</style>
</head>
<body>
   <jsp:include page="./../header.jsp"></jsp:include>

   <div class="container">
      <h1>댓글형 게시판 글 작성 페이지</h1>

      <form action="./write" method="post">
         <div class="mb-3 mt-3">
            <label for="title" class="form-label">title:</label> <input
               type="text" class="form-control" id="title" name="title" />
         </div>

         <div class="mb-3 mt-3">
            <label for="writer" class="form-label">writer:</label> <input
               type="text" class="form-control" id="writer" name="writer"
               value="${sessionScope.loginMember.userId }" readonly />
         </div>

         <div class="mb-3 mt-3">
            <label for="content" class="form-label">content:</label>
            <textarea id="summernote" name="content"></textarea>
         </div>

         <button type="button" class="btn btn-warning"
            onclick="location.href='./listAll';">취소</button>
         <button type="submit" class="btn btn-success"
            onclick="return isValid();">저장</button>
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
