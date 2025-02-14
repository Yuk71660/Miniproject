<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script
   src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script>
   $(function() {
      $('.modalClose').click(function() {
         $('#myModal').hide();
      });
   });

   //title이 null인지 유효성 검사하는 함수
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
</head>
<body>

   <jsp:include page="./../header.jsp"></jsp:include>

   <div class="container">
      <h1>게시판 답글 입력 페이지</h1>

      <form action="/hboard/saveReply" method="post">
         <div class="mb-3 mt-3">
            <label for="title" class="form-label">title:</label> <input
               type="text" class="form-control" id="title" name="title" />
         </div>

         <div class="mb-3 mt-3">
            <label for="writer" class="form-label">writer:</label> <input
               type="text" class="form-control" id="writer" name="writer" />
         </div>

         <div class="mb-3 mt-3">
            <label for="content" class="form-label">content:</label>
            <textarea class="form-control" rows="10" id="content" name="content"></textarea>
         </div>
         
         <div>
            <input type="hidden" name="ref" value="${param.ref }" />
            <input type="hidden" name="step" value="${param.step }" />
            <input type="hidden" name="refOrder" value="${param.refOrder }" /> 
         </div>

         <button type="button" class="btn btn-warning"
            onclick="location.href='/hboard/viewBoard?boardNo=${param.boardNo}';">취소</button>
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