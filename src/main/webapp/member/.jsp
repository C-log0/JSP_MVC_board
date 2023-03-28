<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%
// 세션아이디가 null 이거나 "admin" 이 아닐 경우 메인페이지로 이동
String sId = (String)session.getAttribute("sId");
if(sId == null || !sId.equals("admin")) {
	response.sendRedirect("index.jsp");
}
%> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<title>회원 수정</title>
<!-- 외부 CSS 가져오기 -->
<link href="css/default.css" rel="stylesheet" type="text/css">
<style type="text/css">
	#listForm {
		width: 900px;
		max-height: 610px;
		margin: auto;
	}
	
	h2 {
		text-align: center;
	}
	
	table {
		margin: auto;
		width: 900px;
	}
	
	#tr_top {
		background: #FAF082;
		text-align: center;
	}
	
	table td {
		background: #f3f3f3;
		text-align: center;
	}
	
	#subject {
		text-align: left;
		padding-left: 20px;
	}
	
	#pageList {
		margin: auto;
		width: 900px;
		text-align: center;
	}
	
	#emptyArea {
		margin: auto;
		width: 900px;
		text-align: center;
	}
	
	#buttonArea {
		margin: auto;
		width: 900x;
		text-align: right;
		margin-top: 10px;
	}
	
	a {
		text-decoration: none;
	}
</style>
</head>
<body>
	<header>
		<!-- Login, Join 링크 표시 영역(inc/top.jsp 페이지 삽입) -->
		<jsp:include page="/inc/top.jsp"></jsp:include>
	</header>
	<section id="listForm">
	<h1>member_modifyForm.jsp - 회원 수정</h1>
	
	<table border=1>
		<tr id="tr_top">
			<th width="100">이름</th>
			<th width="100">아이디</th>
			<th width="200">E-Mail</th>
			<th width="30">성별</th>
			<th width="100">가입일</th>
			<th width="100"></th>
		</tr>
	<!-- member 테이블의 모든 레코드 조회하여 테이블에 출력 -->
	<!-- 이름, 아이디, 이메일, 성별, 가입일 출력 -->
		<c:forEach var="member" items="${memberList }">
		<tr height="50">
			<td width="100">${member.name }</td>
			<td width="100">${member.id }</td>
			<td width="100">${member.email }</td>
			<td width="30">${member.gender }</td>
			<td><fmt:formatDate value="${member.date }" pattern="yy-MM-dd"/></td>
			
			<td>
				<input type="button" value="수정" onclick="location.href='MemberModifyForm.me?id=${member.id}'">
				<input type="button" value="삭제" onclick="location.href='MemberDelete.me?id=${member.id}'">
			</td>
		
		</tr>
		</c:forEach>
	</table>
	</section>
</body>
</html>