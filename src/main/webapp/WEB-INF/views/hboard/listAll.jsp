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
<script>
   $(function(){
      if(${empty hboardList}) {
         $('#myModal').show(500);
      }
      
      
      $('.modalClose').click(function(){
         $('#myModal').hide();   
      });
   });
</script>
</head>
<body>
   <jsp:include page="./../header.jsp"></jsp:include>

   <div class="container">
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
            <c:forEach var="board" items="${hboardList }">
               <tr>
                  <td>${board.boardNo }</td>
                  <td>${board.title }</td>
                  <td>${board.writer }</td>
                  <td>${board.postDate }</td>
                  <td>${board.readCount }</td>

               </tr>
            </c:forEach>
         </tbody>
      </table>

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