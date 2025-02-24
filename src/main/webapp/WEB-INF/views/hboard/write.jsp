<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>Insert title here</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <script>
      let upfiles = new Array(); // 업로드 되는 파일들을 저장하는 배열
      $(function () {
        $('.modalClose').click(function () {
          $('#myModal').hide();
        });

        // 업로드 파일 영역에 drag&drop과 관련된 이벤트(파일이 웹 브라우저에 드래그 드랍되었을 때 그 파일을 웹브라우저가 실행시킴)을 방지 해야 한다.
        $('.fileUploadArea').on('dragenter dragover', function (evt) {
          evt.preventDefault();
        });

        $('.fileUploadArea').on('drop', function (evt) {
          evt.preventDefault();
          console.log(evt.originalEvent.dataTransfer.files);
          for (let file of evt.originalEvent.dataTransfer.files) {
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
            }
          }
        });
      });

      // 실제 파일을 업로드 하는 함수
      function fileUpload(file) {
        const fd = new FormData(); // FormData : form태그와 같은 역활을 하는 자바스크립트 객체
        fd.append('file', file);
        fd.append('status','write');

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

      function outputFailure(file) {
        $('.modal-body').html(file.name + '파일 업로드에 실패했습니다. ');
        $('#myModal').show(500);
        $('.uploadFileName').each(function (i, e) {
          if (file.name == $(e).html()) {
            $(e).parent().remove();
          }
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

      // 넘겨준 file이 이미지 파일이면 미리보기 하여 출력
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

      function cancelWrite() {
        $.ajax({
              url: '/hboard/removeAllFile',
              type: 'post', // 이진 데이터를 보낼 때는 post
              dataType: 'json', // 수신받을 데이터 타입
              success: function (data) {
                console.log(data);
                if (data.message == 'success') {
                  location.href='./listAll';
                }
              },
              error: function (err) {
                // ResponseEntity객체의 HttpStatus(통신상태)를 받아 에러가 발생하면 아래의 함수가 호출됨
                console.log(err.responseJSON);
              },
            });
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
      <h1>계층형 게시판 글 작성 페이지</h1>

      <form action="./write" method="post" enctype="multipart/form-data">
        <div class="mb-3 mt-3">
          <label for="title" class="form-label">title:</label>
          <input type="text" class="form-control" id="title" name="title" />
        </div>

        <div class="mb-3 mt-3">
          <label for="writer" class="form-label">writer:</label>
          <input type="text" class="form-control" id="writer" name="writer"  value="${sessionScope.loginMember.userId }" readonly />
        </div>

        <div class="mb-3 mt-3">
          <label for="content" class="form-label">content:</label>
          <textarea class="form-control" rows="10" id="content" name="content"></textarea>
        </div>

        <div class="fileUploadArea">
          <p>업로드 할 파일을 요기에 드래그 드랍 하세요</p>
        </div>
        <div class="previewArea">
          <table class="table table-hover">
            <tbody class="preview"></tbody>
          </table>
        </div>

        <button type="button" class="btn btn-warning" onclick="cancelWrite();">
          취소
        </button>
        <button type="submit" class="btn btn-success" onclick="return isValid();">저장</button>
      </form>
    </div>

    <!-- The Modal -->
    <div class="modal" id="myModal">
      <div class="modal-dialog">
        <div class="modal-content">
          <!-- Modal Header -->
          <div class="modal-header">
            <h4 class="modal-title">Miniproject.com</h4>
            <button type="button" class="btn-close modalClose" data-bs-dismiss="modal"></button>
          </div>

          <!-- Modal body -->
          <div class="modal-body">일시적인 장애가 발생하였습니다. 잠시 후 다시 이용해 주세요.</div>

          <!-- Modal footer -->
          <div class="modal-footer">
            <button type="button" class="btn btn-danger modalClose" data-bs-dismiss="modal">
              Close
            </button>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>
