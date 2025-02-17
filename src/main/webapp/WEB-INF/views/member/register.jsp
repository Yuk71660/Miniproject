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
      $('#userId').blur(function() {
         idIsValid();
      });

      $('#userPwdConfirm').blur(function() {
         pwdIsValid();
      });

      $('#userName').blur(function(){
         userNameIsValid();
      });

      $('#mobile').blur(function(){
         mobileIsValid();
      });
   });

   function mobileIsValid() {
      let result = true;
      let mobile = $('#mobile');

      if (mobile.val().length > 0) {
         let mobileRegExp = /^\d{3}-\d{3,4}-\d{4}$/;
         if (!mobileRegExp.test(mobile.val())) {
            showErrorMsg("핸드폰번호 형식이 아닙니다!", mobile);
         } else {
            clearErrorMsg(mobile);
         }
      } else {
         clearErrorMsg(mobile);
      }

      return result;
   }

   function userNameIsValid() {
      let result = true;
      let userName = $('#userName');

      if (userName.val().length > 0) {
         if (userName.val().length > 4) {
            showErrorMsg("이름은 4자 이내로 입력해 주세요!", userName);
            result = false;
         } else {
            clearErrorMsg(userName);
         }
      } else {
         clearErrorMsg(userName);
      }

      return result;
   }

   function pwdIsValid() {
      let result = false;
      let pwd = $('#userPwd');
      let pwdConfirm = $('#userPwdConfirm');
      //let pwdRegExt = /^[a-z0-9]+[a-z0-9]{4,10}$/g; 

      // if (!pwdRegExt.test(pwd.val())) {
      //    showErrorMsg("패스워드는 영문자+숫자 포함 4~10자로 입력해 주세요!", pwd);
      // } else {
      //    if (pwd.val() != pwdConfirm.val()) {
      //       showErrorMsg("패스워드가 일치 하지 않습니다", pwd);
      //    } else {
      //       result = true;
      //       clearErrorMsg(pwd);
      //    }
      // }

      if (pwd.val().length < 4 || pwd.val().length > 10) {
         showErrorMsg("패스워드는 4~10자 입니다", pwd);
      } else {
         if (pwd.val() != pwdConfirm.val()) {
            showErrorMsg("패스워드가 일치 하지 않습니다", pwd);
         } else {
            result = true;
            clearErrorMsg(pwd);
         }
      }
      
      return result;
   }

   function idIsValid() {
      let result = false;
      let obj = $('#userId');

      let idRegExt = /^[a-z]+[a-z0-9]{4,8}$/g;

      if (!idRegExt.test(obj.val())) {
         showErrorMsg("아이디는 영문자(소문자)숫자 포함 4~8자로 입력해 주세요!", obj);
      } else {
         userIdDuplicate(obj);
         if ($('#idDuplicate').val() == 'true') {
            result = true;
            clearErrorMsg(obj);
         }
      }

      return result;
   }

   function userIdDuplicate(obj) {
      let result = false;
      $.ajax({
         url : '/member/duplicateId',
         type : 'post', // 이진 데이터를 보낼 때는 post
         dataType : 'json', // 수신받을 데이터 타입
         data : {
            "userId" : obj.val()
         }, // 송신할 데이터
         success : function(data) {
            console.log(data);
            if (data.message == 'Duplicate') {
               showErrorMsg('아이디를 사용할 수 없습니다', obj);
            } else if (data.message == 'Available') {
               clearErrorMsg(obj);
               $('#idDuplicate').val('true'); // 히든 태그 이용
            }
         },
         error : function(err) {
            // ResponseEntity객체의 HttpStatus(통신상태)를 받아 에러가 발생하면 아래의 함수가 호출됨
            console.log(err.responseJSON);
         },
      });
   }

   function clearErrorMsg(obj) {
      let id = $(obj).attr('id') + 'Msg';
      $(`#\${id}`).remove(); // 기존의 에러 메시지
   }

   function showErrorMsg(msg, obj) {
      let id = $(obj).attr('id') + 'Msg';
      $(`#\${id}`).remove(); // 기존의 에러 메시지

      let errorMsg = `<div class='errorMsg' id='\${id}'>\${msg}</div>`;
      $(errorMsg).insertAfter(obj);
      $(obj).focus();
   }

   function isValid() {
      let result = false;

      if (idIsValid() && pwdIsValid() && userNameIsValid() && mobileIsValid()) {
         result = true;
      }

      return result;
   }
</script>
<style>
.errorMsg {
   font-weight: bold;
   font-size: 0.8em;
   color: red;
   padding: 8px;
}
</style>
</head>
<body>
   <jsp:include page="./../header.jsp" />
   <div class="container">
      <h1>회원 가입 페이지</h1>

      <form action="./saveMember" method="post"
         enctype="multipart/form-data">
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
                  name="gender" value="M"> <label
                  class="form-check-label" for="genderM">남성</label>
            </div>
         </div>

         <div class="mb-3 mt-3">
            <label for="job" class="form-label">직업:</label> <select name="job"
               id="job" class="form-select">
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
            <input type="text" class="form-control" id="postZip" name="postZip">
            <input type="text" class="form-control" id="tmpAddr"
               placeholder="주소 검색..." name="addr">
         </div>

         <div class="mb-3 mt-3">
            <input type="file" class="form-control" id="userProfile"
               name="userProfile">
         </div>

         <input type="hidden" id="idDuplicate" />

         <div class="mb-3 mt-3">
            <input class="form-check-input" type="checkbox" id="checkAgree">
            회원가입에 동의합니다
         </div>

         <div class="mb-3 mt-3">
            <button type="reset" class="btn btn-danger">취소</button>
            <button type="submit" class="btn btn-dark"
               onclick="return isValid();">가입</button>
         </div>
      </form>
   </div>

</body>
</html>