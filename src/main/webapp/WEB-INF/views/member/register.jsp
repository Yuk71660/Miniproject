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
      $('.inputTag').change(function(){
         clearErrorMsg($(this));

         
      });

      $('.modalClose').click(function() {
         $('#myModal').hide();
      });

      $('#userId').blur(function() {
         idIsValid();
      });

      $('#userPwdConfirm').blur(function() {
         pwdIsValid();
      });

      $('#userName').blur(function() {
         userNameIsValid();
      });

      $('#mobile').blur(function() {
         mobileIsValid();
      });

      $('#email').blur(function() {
         if ($('#emailAuth').val() != 'true') {  // 이메일 인증을 받지 않았을때만...
            emailIsValid();
         }
         
      });

      $('#email').change(function() {  // 이메일 인증 후 다시 이메일을 변경 하고자 한다면...
         $('#emailAuth').val('');
      });
   });

   function emailIsValid() {
      let result = false;
      let email = $('#email');
      const regEmail = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i

      if (!regEmail.test(email.val())) {
         showErrorMsg('이메일 형식에 따라 정확히 입력해주세요', email);
         return;
      } else {
         sendMailAuthCode(email.val()); // 메일주소로 인증코드 보내고 인증코드 확인

         if ($('#emailAuth').val() == 'true') {
            result = true;
         }

      }

      return result;
   }

   function sendMailAuthCode(email) {
      $.ajax({
         url : '/member/sendAuthCode',
         type : 'post', // 이진 데이터를 보낼 때는 post
         dataType : 'json', // 수신받을 데이터 타입
         data : {
            "emailAddr" : email
         }, // 송신할 데이터
         async: false,
         success : function(data) {
            console.log(data);
            if (data.code == '200') {
               showAuthArea();
            } else if (data.code == '500') {
               $('.modal-body').html('이메일 전송에 실패했습니다. 이메일 주소를 확인해 주세요');
               $('#myModal').show();
            }
         },
         error : function(err) {
            // ResponseEntity객체의 HttpStatus(통신상태)를 받아 에러가 발생하면 아래의 함수가 호출됨
            console.log(err.responseJSON);
         },
      });
   }

   function showAuthArea() {
      $('.modal-body').html(
            '입력하신 이메일 주소로 인증코드를 전송했습니다. 메일을 확인하시고 인증코드를 입력후 인증 버튼을 누르세요');
      $('#myModal').show();
      let output = `<div><input type='text' id='confirmCodeInput' /><button type="button" class="btn btn-info" onclick="confirmAuthCode();">인증</button> </div>`;
      $('.authArea').html(output);
   }

   function confirmAuthCode() {
      let confirmCodeInput = $('#confirmCodeInput').val();

      $.ajax({
         url : '/member/confirmAuthCode',
         type : 'post', // 이진 데이터를 보낼 때는 post
         dataType : 'json', // 수신받을 데이터 타입
         data : {
            "confirmCodeInput" : confirmCodeInput
         }, // 송신할 데이터
         async: false,
         success : function(data) {
            console.log(data);
            if (data.code == '200') {
               $('#emailAuth').val('true');
               $('.authArea').empty();
            } else if (data.code == '500') {
               $('#email').val('');
               clearErrorMsg($('#email'));
               $('.authArea').empty();
               $('.modal-body').html(
                  '이메일 인증 코드가 틀립니다. 인증을 다시 해주세요');
               $('#myModal').show();
               $('#email').focus();

            }
         },
         error : function(err) {
            // ResponseEntity객체의 HttpStatus(통신상태)를 받아 에러가 발생하면 아래의 함수가 호출됨
            console.log(err.responseJSON);
         },
      });
   }

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
      //let pwdRegExt = /^[a-z0-9]+[a-z0-9]{3,9}$/g; 

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

      let idRegExt = /^[a-z]+[a-z0-9]{3,7}$/g;

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

      if (idIsValid() && pwdIsValid() && userNameIsValid() && mobileIsValid() && emailIsValid()) {
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
               type="text" class="form-control inputTag" id="userId" name="userId">
         </div>

         <div class="mb-3 mt-3">
            <label for="userPwd" class="form-label">비밀번호:</label> <input
               type="password" class="form-control inputTag" id="userPwd" name="userPwd">
         </div>

         <div class="mb-3 mt-3">
            <label for="userPwdConfirm" class="form-label">비밀번호확인:</label> <input
               type="password" class="form-control inputTag" id="userPwdConfirm">
         </div>

         <div class="mb-3 mt-3">
            <label for="userName" class="form-label">이름:</label> <input
               type="text" class="form-control inputTag" id="userName" name="userName">
         </div>

         <div class="mb-3 mt-3">
            <label for="mobile" class="form-label">mobile:</label> <input
               type="text" class="form-control inputTag" id="mobile" name="mobile">
         </div>

         <div class="mb-3 mt-3">
            <label for="email" class="form-label">email:</label> <input
               type="text" class="form-control inputTag" id="email" name="email">
         </div>
         <div class="authArea"></div>

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
            <input type="text" class="form-control inputTag" id="postZip" name="postZip">
            <input type="text" class="form-control inputTag" id="tmpAddr"
               placeholder="주소 검색..." name="addr">
         </div>

         <div class="mb-3 mt-3">
            <input type="file" class="form-control" id="userProfile"
               name="userProfile">
         </div>

         <input type="hidden" id="idDuplicate" />
         <input type="hidden" id="emailAuth" />

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