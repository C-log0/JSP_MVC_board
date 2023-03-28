<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script>
  		function confirm_logout(){
  			// Confirm Dialog 를 활용하여 "로그아웃 하시겠습니까?" 질문 처리
  			let result = confirm("로그아웃 하시겠습니까?");
  			
  			// 선택된 결과값이 true일 경우 logout.jsp 페이지로 이동
  			if(result) {
  				let result = confirm("로그아웃 되었습니다!");
  				location.href = "MemberLogout.me";
  			}
  		}
  </script>


<div id="member_area">
	<a href="./">Home</a>
	 <!-- login 실패 / 성공 (admin일때, 아닐때) -->
<c:choose>
	<c:when test="${empty sessionScope.sId }">
		| <a href="MemberLoginForm.me">Login</a> 
		| <a href="MemberJoinForm.me">Join</a>
	</c:when>
	
	<c:otherwise>
	<c:choose>
		<%-- 만약, 로그인 된 아이디가 "admin"일 경우, 관리자페이지 링크(MemberList  --%>
		<c:when test="${sessionScope.sId == 'admin'}">
			| admin Page
			| <a href="MemberList.me">List</a>
		</c:when>
		<c:otherwise>
			| ${sessionScope.sId } 님
			
		</c:otherwise>
		</c:choose>
			| <a href="javascript:confirm_logout()">Logout</a>
	</c:otherwise>
</c:choose>	

</div>