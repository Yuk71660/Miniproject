<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>Insert title here</title>
<script
   src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script>
      $(function () {
        if ("${empty hboardList}" == "true") {
          // 게시물 데이터를 가져올 때 예외(비어있거나)
          $("#myModal").show(500);
        } else {
          timeDiffPostDate();
        }

        let status = "${param.status}";
        if (status == "fail") {
          $(".modal-body").html(
            "게시글 저장에 실패했습니다. 잠시 후 다시 이용해보세요. 문제가 지속되면 관리자에게 연락하세요"
          );

          $("#myModal").show(500);
        }

        $(".modalClose").click(function () {
          $("#myModal").hide();
        });
      });

      // 게시글의 작성시간과 현재시간의 차이를 구하여 2시간 이내 글이라면 "new.png"이미지를 제목뒤에 붙여 출력하도록..
      function timeDiffPostDate() {
        $(".postDate").each((i, e) => {
          let postDate = new Date($(e).html());
          let now = new Date();

          let diff = (now - postDate) / 1000 / 60 / 60; // 시간 차이
          console.log(postDate, now, diff);

          let tmpTitle = $(e).prev().prev().html();
          if (diff < 2) {
            let output = `\${tmpTitle}<span><img src='/resources/images/new.png' /></span>`;
            $(e).prev().prev().html(output);
          }
        });
      }
    </script>
    <style>
       .deleteBoard td{
          color : #eee;
          opacity: 0.3;
       }
    </style>
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
               <c:choose>
                  <c:when test="${board.isDelete == 'N'}">
                     <tr
                        onclick="location.href='./viewBoard?boardNo=${board.boardNo }';">
                        <td>${board.boardNo }</td>
                        <td><c:forEach var="i" begin="1" end="${board.step }"
                              step="1">
                              <img src="/resources/images/reply.png" width="16px" />
                           </c:forEach> ${board.title }</td>
                        <td>${board.writer }</td>
                        <td class="postDate">${board.postDate }</td>
                        <td>${board.readCount }</td>
                     </tr>
                  </c:when>
                  <c:when test="${board.isDelete == 'Y' }">
                     <tr class="deleteBoard">
                        <td></td>
                        <td>${board.title }</td>
                        <td>${board.writer }</td>
                        <td class="postDate"></td>
                        <td></td>
                     </tr>
                  </c:when>
               </c:choose>

            </c:forEach>
         </tbody>
      </table>

      <div style="float: right; margin-right: 10px">
         <button type="button" class="btn btn-primary"
            onclick="location.href='/hboard/write';">글 작성</button>
      </div>
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
