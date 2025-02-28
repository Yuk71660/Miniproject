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
      $(function () {
       let searchType = '${param.searchType}';
       
       
       if (searchType != "" || searchType != null) {
          $.each(document.getElementById('searchType').options, function(i, e) {
             if($(e).attr('value') == searchType) {
                $(e).prop('selected', 'true');   
             }
          });
       }
       
       
       //$(`select[name=seachType] option[value=\${searchType}]`).prop("selected", 'true');
       
              
       let rowCntPerPage = '${param.rowCntPerPage}';
       if (rowCntPerPage != "" || rowCntPerPage != null) {
          $.each(document.getElementById('rowCntPerPage').options, function(i, elt) {
             if ($(elt).attr('value') == rowCntPerPage) {
                $(elt).prop('selected', 'true');
             }
          });
       }
         
         
        if ("${empty pageResponseDTO}" == "true") {
          // 게시물 데이터를 가져올 때 예외(비어있거나)
          $("#myModal").show(500);
        } else {
          timeDiffPostDate();
        }

        let status = "${param.status}";
        if (status == "fail") {
          $(".modal-body").html(
            "게시글 저장에 실패했습니다. 잠시 후 다시 이용해보세요. 문제가 지속되면 관리자에게 연락하세요"
          );

          $("#myModal").show(500);
        }

        $(".modalClose").click(function () {
          $("#myModal").hide();
        });
      });
      
      function isValid() {
         let result = false;
         let searchType = $('#searchType').val();
         if (searchType == '-1') {
            $(".modal-body").html("검색 조건은 반드시 입력해 주세요");
           $("#myModal").show(500);
           
         } else {
            result = true;
         }
         
         return result;
      }

      // 게시글의 작성시간과 현재시간의 차이를 구하여 2시간 이내 글이라면 "new.png"이미지를 제목뒤에 붙여 출력하도록..
      function timeDiffPostDate() {
        $(".postDate").each((i, e) => {
          let postDate = new Date($(e).html());
          let now = new Date();

          let diff = (now - postDate) / 1000 / 60 / 60; // 시간 차이
          console.log(postDate, now, diff);

          let tmpTitle = $(e).prev().prev().html();
          if (diff < 2) {
            let output = `\${tmpTitle}<span><img src='/resources/images/new.png' /></span>`;
            $(e).prev().prev().html(output);
          }
        });
      }
    </script>
<style>
.deleteBoard td {
   color: #eee;
   opacity: 0.3;
}

.pagingControl {
   display: flex;
   flex-direction: row;
   justify-content: space-between;
   margin-top: 10px;
   margin-bottom: 10px;
   padding: 5px;
}
</style>
</head>
<body>
   <c:if
      test="${param.pageNo < 1 or param.pageNo > pageResponseDTO.totalPageCnt  }">
      <c:redirect url="./listAll?pageNo=1"></c:redirect>
   </c:if>
   <jsp:include page="./../header.jsp"></jsp:include>

   <div class="container">
      <h1>댓글형 게시판 전체 리스트 페이지</h1>

      <form class="pagingControl" action="./listAll" method="get">
         <div>
            <select name="searchType" id="searchType">
               <option value="-1">--검색조건--</option>
               <option value="title">제목</option>
               <option value="writer">작성자</option>
               <option value="content">내용</option>
            </select>
            <c:choose>
               <c:when test="${empty param.searchWord}">
                  <input type="text" name="searchWord" placeholder="검색어를 입력하세요" />
               </c:when>
               <c:otherwise>
                  <input type="text" name="searchWord" value="${param.searchWord }" />
               </c:otherwise>
            </c:choose>
            <input type="submit" value="검색" onclick="return isValid();">


         </div>
         <c:choose>
            <c:when test="empty ${param.pageNo }">
               <input type="hidden" name="pageNo" value="${param.pageNo }" />
            </c:when>
            <c:otherwise>
               <input type="hidden" name="pageNo" value="1" />
            </c:otherwise>
         </c:choose>


         <div>

            <select id="rowCntPerPage" name="rowCntPerPage">
               <option value="10">10</option>
               <option value="20">20</option>
               <option value="40">40</option>
               <option value="80">80</option>
            </select>개씩 보기

         </div>
      </form>

      <table class="table table-hover boardList">
         <thead>
            <tr>
               <th>#</th>
               <th>title</th>
               <th>writer</th>
               <th>postDate</th>
               <th>readCount</th>
            </tr>
         </thead>
         <tbody>
            <c:forEach var="board" items="${pageResponseDTO.boardList }">
               <tr
                  onclick="location.href='./viewBoard?boardNo=${board.boardNo }';">
                  <td>${board.boardNo }</td>
                  <td>${board.title }</td>
                  <td>${board.writer }</td>
                  <td class="postDate">${board.postDate }</td>
                  <td>${board.readCount }</td>
               </tr>
            </c:forEach>
         </tbody>
      </table>

      <div clas="pagination">
         <ul class="pagination justify-content-center" style="margin: 20px 0">
            <c:if test="${param.pageNo > 1}">
               <li class="page-item"><a class="page-link"
                  href="./listAll?pageNo=${param.pageNo - 1}&searchType=${pageResponseDTO.searchType}&searchWord=${param.searchWord}&rowCntPerPage=${pageResponseDTO.rowCntPerPage}">Previous</a></li>
            </c:if>


            <c:forEach var="i" begin="${pageResponseDTO.startPageNumPerBlock }"
               end="${pageResponseDTO.endPageNumPerBlock }">
               <c:choose>
                  <c:when test="${param.pageNo == i }">
                     <li class="page-item active"><a class="page-link"
                        href="./listAll?pageNo=${i }&searchType=${pageResponseDTO.searchType}&searchWord=${param.searchWord}&rowCntPerPage=${pageResponseDTO.rowCntPerPage}">${i }</a></li>
                  </c:when>
                  <c:otherwise>
                     <li class="page-item"><a class="page-link"
                        href="./listAll?pageNo=${i }&searchType=${pageResponseDTO.searchType}&searchWord=${param.searchWord}&rowCntPerPage=${pageResponseDTO.rowCntPerPage}">${i }</a></li>
                  </c:otherwise>
               </c:choose>

            </c:forEach>

            <c:if
               test="${param.pageNo < pageResponseDTO.totalPageCnt or pageResponseDTO.pageNo == 1}">
               <li class="page-item"><a class="page-link"
                  href="./listAll?pageNo=${param.pageNo + 1}&searchType=${pageResponseDTO.searchType}&searchWord=${param.searchWord}&rowCntPerPage=${pageResponseDTO.rowCntPerPage}">Next</a></li>
            </c:if>

         </ul>
      </div>

      <div style="float: right; margin-right: 10px">
         <button type="button" class="btn btn-primary"
            onclick="location.href='/rboard/write';">글 작성</button>
      </div>
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
