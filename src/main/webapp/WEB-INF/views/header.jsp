<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link
   href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
   rel="stylesheet">
<script
   src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<title>Insert title here</title>
</head>
<body>
   <nav class="navbar navbar-expand-sm bg-dark navbar-dark">
      <div class="container-fluid">
         <a class="navbar-brand" href="#">Logo</a>
         <button class="navbar-toggler" type="button"
            data-bs-toggle="collapse" data-bs-target="#collapsibleNavbar">
            <span class="navbar-toggler-icon"></span>
         </button>
         <div class="collapse navbar-collapse" id="collapsibleNavbar">
            <ul class="navbar-nav">
               <li class="nav-item dropdown"><a
                  class="nav-link dropdown-toggle" href="#" role="button"
                  data-bs-toggle="dropdown">게시판</a>
                  <ul class="dropdown-menu">
                     <li><a class="dropdown-item" href="/hboard/listAll">계층형게시판</a></li>
                     <li><a class="dropdown-item" href="#">댓글형게시판</a></li>
                  </ul></li>
               <c:choose>
                  <c:when test="${sessionScope.loginMember == null}">
                     <li class="nav-item"><a class="nav-link" href="/member/login">로그인</a></li>
                  </c:when>
                  <c:when test="${sessionScope.loginMember != null }">
                     <li class="nav-item">
                        <img alt="" src="/resources/${sessionScope.loginMember.userImg}" width="40px">
                        <span style="color : gray">${sessionScope.loginMember.userId }</span>
                     </li>
                     <li class="nav-item"><a class="nav-link" href="/member/logout">로그아웃</a></li>
                  </c:when>
               </c:choose>
               
               <li class="nav-item"><a class="nav-link" href="/member/register">회원가입</a></li>
               <li class="nav-item"><a class="nav-link" href="#">Link</a></li>
               
            </ul>
         </div>
      </div>
   </nav>
</body>
</html>