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
   let upfiles = new Array(); // 업로드 되는 파일들을 저장하는 배열
   $(function() {
      $('.deletedFileCheck').change(
            function() {
               let deletedFileCnt = 0;

               $('.deletedFileCheck')
                     .each(
                           function(i, e) {
                              if ($(e).is(":checked")) {
                                 deletedFileCnt++;
                                 isDelete = true;
                                 //console.log($(e).parent().closest('.fileName').html());
                                 fileStatusTransfer($(e).parent().next().next().html(), 'delete');
                              } else {
                                 isDelete = false;
                                 fileStatusTransfer($(e).parent().next().next().html(), 'cancel');
                                 $(e).parent().parent().find(
                                       '.status').html('');
                                 $(e).parent().parent().css(
                                       'opacity', '1');
                                 $(e).parent().next().next().css(
                                       'text-decoration', 'none');
                              }
                           });
               console.log(deletedFileCnt);
               $('#fileDeleteBtn').find("span").html(deletedFileCnt);
               if (deletedFileCnt == 0) {
                  $('#fileDeleteBtn').attr('disabled', 'true');
               } else if (deletedFileCnt > 0) {
                  $('#fileDeleteBtn').removeAttr('disabled');
               }
            });

      $('#fileDeleteBtn').click(
            function() {
               $('.deletedFileCheck').each(
                     function(i, e) {

                        if ($(e).is(":checked")) {
                           $(e).parent().parent().find('.status')
                                 .html('delete');
                           $(e).parent().parent()
                                 .css('opacity', '0.3');
                           $(e).parent().next().next().css(
                                 'text-decoration', 'line-through');
                        }
                     });
            });
      
      $('#uploadFiles').change(function (evt){
           evt.preventDefault();
             for (let file of evt.originalEvent.target.files) {
               let isDuplicate = false;
               $.each(upfiles, function (i, e) {
                 // 업로드된 파일이 이미 있는지
                 if (file.name == e.name) {
                   isDuplicate = true;
                 }
               });

               if (file.size == 0 && file.type == '') {
                 $('.modal-body').html('폴더는 업로드 할 수 없습니다!');
                 $('#myModal').show(500);
                 return;
               }
               if (file.size > 10485760 || isDuplicate) {
                 if (isDuplicate) {
                   $('.modal-body').html('업로드된 같은 파일이 이미 있습니다!');
                 } else {
                   $('.modal-body').html('파일 사이즈가 너무 큽니다!');
                 }
                 $('#myModal').show(500);
               } else {              
                 upfiles.push(file);
                 // 업로드 파일 미리보기
                 showPreview(file);
                 fileUpload(file);
                 console.log(upfiles);
            }
         }
      });
   });
   
   // 업로드된 파일을 삭제하는 함수
    function remFile(removedFileName, obj) {
      for (let i = 0; i < upfiles.length; i++) {
        if (upfiles[i].name == removedFileName) {
          // 웹 서버에서도 파일이 삭제되도록
          $.ajax({
            url: '/hboard/remFile',
            type: 'post', // 이진 데이터를 보낼 때는 post
            dataType: 'json', // 수신받을 데이터 타입
            data: {
              'removeFileName': removedFileName,
              'status' : 'modify'
            }, // 송신할 데이터
            success: function (data) {
              console.log(data);
              if (data.message == 'success') {
                // 배열에서 삭제
                upfiles.splice(i, 1);
                console.log(upfiles);
                // 화면에서 삭제
                $(obj).parent().parent().remove();
              }
            },
            error: function (err) {
              // ResponseEntity객체의 HttpStatus(통신상태)를 받아 에러가 발생하면 아래의 함수가 호출됨
              console.log(err.responseJSON);
            },
          });
          
        }
      }
    }
   
   // 실제 파일을 업로드 하는 함수
    function fileUpload(file) {
      const fd = new FormData(); // FormData : form태그와 같은 역활을 하는 자바스크립트 객체
      fd.append('file', file);
     fd.append('status', 'modify');
     
      $.ajax({
        url: '/hboard/upfiles',
        type: 'post', // 이진 데이터를 보낼 때는 post
        dataType: 'json', // 수신받을 데이터 타입
        data: fd, // 송신할 데이터
        // processData : false -> 데이터를 쿼리스트링 형태로 보내지 않는다
        // contentType : false -> 인코딩 방식을 "application/x-www-form-urlencoded(default)"로 하지 않는다.
        processData: false,
        contentType: false, // 인코딩 방식을 "multipart/form-data"로 한다.
        success: function (data) {
          console.log(data);
          outputSuccess(file);
        },
        error: function (err) {
          // ResponseEntity객체의 HttpStatus(통신상태)를 받아 에러가 발생하면 아래의 함수가 호출됨
          console.log(err.responseJSON);
          outputFailure(file);
        },
      });
    }
   
    function outputSuccess(file) {
        $('.uploadFileName').each(function (i, e) {
          if (file.name == $(e).html()) {
            let output = `<td><img src='/resources/images/success.png' width='50%' /></td>`;
            $(output).insertAfter($(e));
          }
        });
      }

   function fileStatusTransfer(deletedFileName, status) {
      $.ajax({
         url : '/hboard/modifyUpdateStatus',
         type : 'post', // 이진 데이터를 보낼 때는 post
         dataType : 'json', // 수신받을 데이터 타입
         data : {
            'deletedFileName' : deletedFileName,
            'status' : status
         }, // 송신할 데이터
         success : function(data) {
            console.log(data);
            
         },
         error : function(err) {
            // ResponseEntity객체의 HttpStatus(통신상태)를 받아 에러가 발생하면 아래의 함수가 호출됨
            console.log(err.responseJSON);
         },
      });
   }

   function showPreview(file) {
        let imageType = ['image/png', 'image/jpeg', 'image/gif'];
        let fileType = file.type.toLowerCase();
        let output = '';
        if (imageType.indexOf(fileType) != -1) {
          const fileReader = new FileReader();

          fileReader.readAsDataURL(file); // 파일을 읽음
          fileReader.onload = function () {
            // 파일을 모두 읽으면
            let imageBase64data = fileReader.result; // base64로 인코딩된 파일
            outputPreview(imageBase64data, file);
            console.log(output);
          };
        } else {
          output += `<tr><td><img src='/resources/images/noimage.png' width='40px' /></td><td class='uploadFileName'>\${file.name}</td>`;
          output += `<td><img src='/resources/images/remove.png' width='25px' onclick='remFile("\${file.name}", this);'/></td></tr>`;
          $('.preview').append(output);
        }
      }

      function outputPreview(imageBase64data, file) {
        let output = ``;
        output += `<tr id='\${file.name}'><td><img  src='\${imageBase64data}' width='40px' /></td><td class='uploadFileName'>\${file.name}</td>`;
        output += `<td><img src='/resources/images/remove.png' width='25px' onclick='remFile("\${file.name}", this);'/></td></tr>`;
        $('.preview').append(output);
      }
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

      <form action="" method="post">
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
               <textarea class="form-control" rows="10" id="content"
                  name="content">${boardDetailInfo.content }</textarea>
            </div>
         </div>

         <!-- 게시글에 업로드된 파일들 -->
         <div class="uploadFileList">
            <table class="table table-hover">
               <thead>
                  <tr>
                     <th>#</th>
                     <th>thumbNail</th>
                     <th>fileName</th>
                     <th>size</th>
                     <th>status</th>
                  </tr>
               </thead>
               <tbody>
                  <c:forEach var="file" items="${boardDetailInfo.fileList }">
                     <c:choose>
                        <c:when test="${file.originalFileName != null }">
                           <tr>
                              <td><input class="form-check-input deletedFileCheck"
                                 type="checkbox"></td>
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
                              <td class='fileName'>${file.originalFileName }</td>
                              <td>${file.size / 1024 }KBytes</td>
                              <td class="status"></td>
                           </tr>
                        </c:when>
                     </c:choose>
                  </c:forEach>
               </tbody>
            </table>
            <div id="addNewFile">
               <input type="file" id="uploadFiles" multiple />
            </div>
            <div class="previewArea">
               <table class="table table-hover">
                  <tbody class="preview"></tbody>
               </table>
            </div>

            <div style="float: right; margin-right: 5px;">
               <button type="button" class="btn btn-danger" id="newFile">
                  add file</button>

               <button type="button" class="btn btn-danger" id="fileDeleteBtn">
                  <span></span>files delete
               </button>
            </div>
         </div>

         <div class="btns" style="clear: right;">
            <button type="button" class="btn btn-primary"
               onclick="location.href='/hboard/showReplyForm?boardNo=${boardDetailInfo.boardNo}&ref=${boardDetailInfo.ref}&step=${boardDetailInfo.step}&refOrder=${boardDetailInfo.refOrder }';">답글달기</button>
            <button type="button" class="btn btn-secondary"
               onclick="location.href='/hboard/modifyBoard?boardNo=${boardDetailInfo.boardNo}';">글
               수정</button>
            <button type="button" class="btn btn-success">글 삭제</button>

            <!--  아래의 방법으로도 로그인했고, 본인글일때만 수정 삭제가 되도록 처리 할 수도 있다
         <c:choose>
            <c:when test="${sessionScope.loginMember != null && sessionScope.loginMember.userId == boardDetailInfo.writer }">
               <button type="button" class="btn btn-secondary">글 수정</button>
               <button type="button" class="btn btn-success">글 삭제</button>
            </c:when>
            <c:otherwise>
               <button type="button" class="btn btn-secondary" disabled>글 수정</button>
               <button type="button" class="btn btn-success" disabled>글 삭제</button>
            </c:otherwise>
         </c:choose>
          -->

            <button type="button" class="btn btn-info"
               onclick="location.href='./listAll';">리스트페이지</button>
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