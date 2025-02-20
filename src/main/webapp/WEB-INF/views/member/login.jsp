<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
   <jsp:include page="../header.jsp"></jsp:include>
   <div class="container">
      <h1>로그인 페이지</h1>
      
      <form action="/member/login" method="post">
         <div class="mb-3 mt-3">
            <label for="userId" class="form-label">UserId:</label> <input
               type="text" class="form-control" id="userId"
               placeholder="Enter userId" name="userId">
         </div>
         <div class="mb-3">
            <label for="userPwd" class="form-label">Password:</label> <input
               type="password" class="form-control" id="userPwd"
               placeholder="Enter password" name="userPwd">
         </div>
         <div class="form-check mb-3">
            <label class="form-check-label"> <input
               class="form-check-input" type="checkbox" name="remember">
               Remember me
            </label>
         </div>
         <button type="reset" class="btn btn-danger" onclick="location.href='../';">취소</button>
         <button type="submit" class="btn btn-primary">로그인</button>
      </form>
      <a href="/member/register">아직 회원이 아니신게롱~ 회원가입함세</a>
   </div>
   
   

</body>
</html>