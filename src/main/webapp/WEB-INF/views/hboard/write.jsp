<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>Insert title here</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <script>
      let upfiles = new Array();

      $(function () {
        // 업로드 파일 영역에 drag&drop과 관련된 이벤트(파일이 웹 브라우저에 드래그 드랍되었을 때 그 파일을 웹브라우저가 실행시킴)을 방지 해야 한다.
        $(".fileUploadArea").on("dragenter dragover", function (evt) {
          evt.preventDefault();
        });

        $(".fileUploadArea").on("drop", function (evt) {
          evt.preventDefault();
          for (let file of evt.originalEvent.dataTransfer.files) {
            if (file.size == 0 && file.type == "") {
              $(".modal-body").html("폴더는 업로드 할 수 없습니다!");
              $("#myModal").show(500);
              return;
            }
            if (file.size > 10485760) {
              $(".modal-body").html("파일 사이즈가 너무 큽니다!");
              $("#myModal").show(500);
            } else {
              upfiles.push(file);
              fileUpload(file);
              // 이미지 파일이면 미리보기
              showPreview(file);
            }
          }
        });

        $(".modalClose").click(function () {
          $("#myModal").hide();
        });
      });

      // 실제 파일을 업로드 하는 함수
      function fileUpload(file) {
        const fd = new FormData(); // FormData : form태그와 같은 역활을 하는 자바스크립트 객체
        fd.append("file", file);

        $.ajax({
          url: "/hboard/upfiles",
          type: "post", // 이진 데이터를 보낼 때는 post
          dataType: "json", // 수신받을 데이터 타입
          data: fd, // 송신할 데이터
          // processData : false -> 데이터를 쿼리스트링 형태로 보내지 않는다
          // contentType : false -> 인코딩 방식을 "application/x-www-form-urlencoded(default)"로 하지 않는다.
          processData: false,
          contentType: false, // 인코딩 방식을 "multipart/form-data"로 한다.
          success: function (data) {
            console.log(data);
          },
          error: function (err) {
        	console.log(err.responseJSON);
        	console.log("err");
          }
        });
      }

      // 넘겨준 file이 이미지 파일이면 미리보기 하여 출력
      function showPreview(file) {
        // let fileType = file.type.toLowerCase();
        let fileTypeSpl = file.type.toLowerCase().split("/");
        if (fileTypeSpl[0] == "image") {
          const fileReader = new FileReader();
          fileReader.readAsDataURL(file); // 파일을 읽음
          fileReader.onload = function () {
            // 파일을 모두 읽으면
            let imageBase64data = fileReader.result; // base64로 인코딩된 파일
            outputPreview(imageBase64data, file);
          };
        } else {
          let image = "'/resources/images/noimage.png'";
          outputPreview(image, file);
        }
      }

      function outputPreview(image, file) {
        let output = `<div><img  src=\${image} width='60px' /><span>\${file.name}</span>`;
        output += `<span><img src='/resources/images/remove.png' onclick='remFile(this);'/></span></div>`;
        $(".preview").append(output);
      }

      // 업로드된 파일을 삭제하는 함수
      function remFile(obj) {
        // 배열에서 파일 삭제, 화면에서 삭제
        let removedFileName = $(obj).parent().prev().html();
        for (let i = 0; i < upfiles.length; i++) {
          if (upfiles[i].name == removedFileName) {
            // 배열에서 삭제
            upfiles.splice(i, 1);
            console.log(upfiles);
            // 화면에서 삭제
          }
        }
        $(obj).parent().parent().remove();
      }

      function isValid() {
        let result = false;
        let title = $("#title").val();
        if (title.length < 1 || title == null) {
          $(".modal-body").html("제목은 비어있으면 안됩니다!");
          $("#myModal").show(500);
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
      <h1>계층형 게시판 글 작성 페이지</h1>

      <form action="./write" method="post">
        <div class="mb-3 mt-3">
          <label for="title" class="form-label">title:</label>
          <input type="text" class="form-control" id="title" name="title" />
        </div>

        <div class="mb-3 mt-3">
          <label for="writer" class="form-label">writer:</label>
          <input type="text" class="form-control" id="writer" name="writer" />
        </div>

        <div class="mb-3 mt-3">
          <label for="content" class="form-label">content:</label>
          <textarea
            class="form-control"
            rows="10"
            id="content"
            name="content"
          ></textarea>
        </div>

        <div class="preview"></div>

        <div class="fileUploadArea">
          <p>업로드 파일 여기 드래그 드랍</p>
        </div>
        <br />

        <button
          type="button"
          class="btn btn-warning"
          onclick="location.href='./listAll';"
        >
          취소
        </button>
        <button
          type="submit"
          class="btn btn-success"
          onclick="return isValid();"
        >
          저장
        </button>
      </form>
    </div>

    <!-- The Modal -->
    <div class="modal" id="myModal">
      <div class="modal-dialog">
        <div class="modal-content">
          <!-- Modal Header -->
          <div class="modal-header">
            <h4 class="modal-title">Miniproject.com</h4>
            <button
              type="button"
              class="btn-close modalClose"
              data-bs-dismiss="modal"
            ></button>
          </div>

          <!-- Modal body -->
          <div class="modal-body">
            일시적인 장애가 발생하였습니다. 잠시 후 다시 이용해 주세요.
          </div>

          <!-- Modal footer -->
          <div class="modal-footer">
            <button
              type="button"
              class="btn btn-danger modalClose"
              data-bs-dismiss="modal"
            >
              Close
            </button>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>
