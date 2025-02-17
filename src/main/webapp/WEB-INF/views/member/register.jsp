<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script
   src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
</head>
<body>
   <jsp:include page="./../header.jsp" />
   <div class="container">
      <h1>회원 가입 페이지</h1>

      <form action="" method="post" enctype="multipart/form-data">
         <div class="mb-3 mt-3">
            <label for="userId" class="form-label">아이디:</label> <input
               type="text" class="form-control" id="userId" name="userId">
         </div>

         <div class="mb-3 mt-3">
            <label for="userPwd" class="form-label">비밀번호:</label> <input
               type="password" class="form-control" id="userPwd" name="userPwd">
         </div>

         <div class="mb-3 mt-3">
            <label for="userPwdConfirm" class="form-label">비밀번호확인:</label> <input
               type="password" class="form-control" id="userPwdConfirm">
         </div>

         <div class="mb-3 mt-3">
            <label for="userName" class="form-label">이름:</label> <input
               type="text" class="form-control" id="userName" name="userName">
         </div>

         <div class="mb-3 mt-3">
            <label for="mobile" class="form-label">mobile:</label> <input
               type="text" class="form-control" id="mobile" name="mobile">
         </div>

         <div class="mb-3 mt-3">
            <label for="email" class="form-label">email:</label> <input
               type="text" class="form-control" id="email" name="email">
         </div>

         <div class="mb-3 mt-3">
            <div class="form-check">
               <input type="radio" class="form-check-input" id="genderF"
                  name="gender" value="F" checked> <label
                  class="form-check-label" for="genderF">여성</label>

            </div>
            <div class="form-check">
               <input type="radio" class="form-check-input" id="genderM"
                  name="gender" value="M" checked> <label
                  class="form-check-label" for="genderM">남성</label>
            </div>
         </div>

         <div class="mb-3 mt-3">
            <label for="email" class="form-label">직업:</label> <select
               class="form-select">
               <option value="-1">-- 직업을 선택하세요 --</option>
               <option value="student">학생</option>
               <option value="owner">자영업</option>
               <option value="officer">공무원/회사원</option>
            </select>
         </div>

         <div class="mb-3 mt-3">
            <input class="form-check-input" type="checkbox" id="check1"
               name="hobby" value="운동"> <label class="form-check-label"
               for="check1">운동</label> <input class="form-check-input"
               type="checkbox" id="check2" name="hobby" value="게임"> <label
               class="form-check-label" for="check2">게임</label> <input
               class="form-check-input" type="checkbox" id="check3" name="hobby"
               value="쇼핑"> <label class="form-check-label" for="check3">쇼핑</label>

            <input class="form-check-input" type="checkbox" id="check4"
               name="hobby" value="독서"> <label class="form-check-label"
               for="check4">독서</label>
         </div>

         <div class="mb-3 mt-3">
            <input type="text" class="form-control" id="tmpAddr"
               placeholder="주소 검색...">
         </div>

         <div class="mb-3 mt-3">
            <input type="file" class="form-control" id="userImg" name="userImg">
         </div>

         <div class="mb-3 mt-3">
            <input class="form-check-input" type="checkbox" id="checkAgree">
            회원가입에 동의합니다
         </div>

         <div class="mb-3 mt-3">
            <button type="reset" class="btn btn-danger">취소</button>
            <button type="submit" class="btn btn-dark">가입</button>
         </div>
      </form>
   </div>

</body>
</html>