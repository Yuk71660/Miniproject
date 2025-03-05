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
      $(function() {
       
         let status = '${param.status}';
         if (status == 'notallowed') {
            $('.modal-body').html('본인이 작성한 글만 수정/삭제 가능합니다!');
            $('#myModal').show(500);
         }

         $('.modalClose').click(function() {
            $('#myModal').hide();
         });

         getAllReplies();


      });

      // 현재 게시글의 모든 댓글을 가져옴
      function getAllReplies() {
         $.ajax({
                url: '/replies/' + ${param.boardNo},
                type: 'get', // 이진 데이터를 보낼 때는 post
                dataType: 'json', // 수신받을 데이터 타입
                async: false,
                success: function (data) {
                  console.log(data);
                  if(data.code == "200") {
                     outputAllReplies(data);
                     cancelSave();
                  }
                },
                error: function (err) {
                  // ResponseEntity객체의 HttpStatus(통신상태)를 받아 에러가 발생하면 아래의 함수가 호출됨
                  console.log(err.responseJSON);
                },
              });
      }

      // 댓글을 출력하는 함수
      function outputAllReplies(data) {
         const replyList = data.data.boardList;
         let output = `<ul class="list-group list-group-flush">`;
         $.each(replyList, function(i, elt) {
            output += `<li class="list-group-item reply">`;
            output += `<div class='replyAndLike'>`;
            output += `<div style='font-size : 0.8em; font-weight : bold;'>\${elt.replyer}</div>`;
            output += `<div><img src='/resources/images/like48.png' width="24px" /></div>`;
            output += `</div>`;
            output += `<div>\${elt.replyContent}</div>`;
            let postDate = new Date(elt.postDate).toLocaleString();
            output += `<div style='float : right; margin-right : 3px;'>\${postDate}</div>`;
            output += `</li>`;
         });
         output += `</ul>`;

         $('.replyList').html(output);
      }
      
      function showSaveReply() {  
         let output = `<textarea rows="5" class="form-control" id="replyContent"></textarea>`;
         output += `<div><button type="button" class="btn btn-secondary" onclick='cancelSave();'>취소</button><button type="button" class="btn btn-success" onclick="saveReply();">저장</button></div>`
         
         $('.saveReplyArea').html(output);
         if (sessionStorage.getItem("reply")) {
          $('#replyContent').val(sessionStorage.getItem("reply"));
          sessionStorage.clear();
          } 
      }
      
      const cancelSave = function () {
         $('#replyContent').val('');
         $('.saveReplyArea').empty();
      };
      
      function isAuthentication() {
         /*
         아래와 같은 방법으로 뷰단에서 로그인 했는지 안했는지 검사 할 수 있지만...추천 하지 않는다.
         인증과 같은 critical한 상황은 반드시 백엔드에서 처리하는 것이 좋다
         
         let userId = '${sessionScope.loginMember.userId}';
         if (userId == '' || userId == null) {
            alert('로그인 하지 않은 유저!');
         }
         */
         
         let result = false;
         
         $.ajax({
              url: '/member/isAuth',
              type: 'get', // 이진 데이터를 보낼 때는 post
              dataType: 'text', // 수신받을 데이터 타입
              async : false,
              success: function (data) {
                console.log(data);
                if (data == 'true') {
                   result = true;
                }
              },
              error: function (err) {
                // ResponseEntity객체의 HttpStatus(통신상태)를 받아 에러가 발생하면 아래의 함수가 호출됨
                console.log(err.responseJSON);
              },
            });
         
         return result;
         
      }
      
      function saveReply() {
         let result = isAuthentication();
         
         if (result == true) {
            let replyContent = $('#replyContent').val();
             let replyer = '${sessionScope.loginMember.userId}';
             const replyDTO = {
              "replyContent" : replyContent,
              "replyer" : replyer
             };
             
             console.log(JSON.stringify(replyDTO));
             
             $.ajax({
                  url: '/replies/' + ${param.boardNo},
                  type: 'post', // 이진 데이터를 보낼 때는 post
                  dataType: 'json', // 수신받을 데이터 타입
                  async: false,
                  data : JSON.stringify(replyDTO)  , // 송신하는 데이터
                  headers : {
                     "Content-Type" : "application/json"
                  },
                  success: function (data) {
                    console.log(data);
                    if(data.code == "200") {
                       getAllReplies();
                    }
                  },
                  error: function (err) {
                    // ResponseEntity객체의 HttpStatus(통신상태)를 받아 에러가 발생하면 아래의 함수가 호출됨
                    console.log(err.responseJSON);
                  },
                });
         } else {
            // 유저가 작성하던 글이 있다면... 로컬 스토리지에 글 저장
            if ($('#replyContent').val() != '') {
               sessionStorage.setItem("reply", $('#replyContent').val());
            }
            
            location.href='/member/login?redirectUrl=replyView&boardNo=' +  ${param.boardNo};
         } 
      }
    </script>
<style>
.replyList, .saveReplyArea {
   margin-top: 15px;
   margin-bottom: 15px;
}

.reply {
   padding: 10px;
   border-bottom: 1px solid #595959;
}

.replyAndLike {
   display: flex;
   justify-content: space-between;
}

.reply div {
   margin-bottom: 5px;
}
</style>
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

      <div class="btns">
         <button type="button" class="btn btn-secondary"
            onclick="location.href='/rboard/modifyBoard?boardNo=${boardDetailInfo.boardNo}';">
            글 수정</button>
         <button type="button" class="btn btn-success"
            onclick="location.href='/rboard/removeBoard?boardNo=${boardDetailInfo.boardNo}';">
            글 삭제</button>
         <button type="button" class="btn btn-warning"
            onclick="showSaveReply();">댓글추가</button>
         <button type="button" class="btn btn-info"
            onclick="location.href='./listAll';">리스트페이지</button>
      </div>
      <div class="saveReplyArea"></div>
      <div class="replyList"></div>
      <div class="replyPagination"></div>
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
