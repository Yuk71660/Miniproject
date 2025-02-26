<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>index</title>
<script
   src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script>
   $(function(){
      $('#searchBlog').click(function (){
         let keyWord = document.getElementsByName("keyWord")[0].value;
         $.ajax({
            url : '/blog',
            type : 'post', // 이진 데이터를 보낼 때는 post
            dataType : 'json', // 수신받을 데이터 타입
            data : {
               "keyWord" : keyWord
            }, // 송신할 데이터
            async : false,
            success : function(data) {
               console.log(data);
               
            },
            error : function(err) {
               // ResponseEntity객체의 HttpStatus(통신상태)를 받아 에러가 발생하면 아래의 함수가 호출됨
               console.log(err.responseJSON);
            },
         });
      });
   });
</script>
</head>
<body>
   <jsp:include page="header.jsp"></jsp:include>
   <div class="container">
      <h1>네이버 블로그 검색</h1>

      <div>

         <input type="text" name="keyWord" />
         <button type="button" id="searchBlog">검색</button>


      </div>

   </div>
</body>
</html>