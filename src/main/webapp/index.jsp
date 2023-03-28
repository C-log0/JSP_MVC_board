<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<!-- 외부 CSS 가져오기 -->
<link href="css/default.css" rel="stylesheet" type="text/css">
<style>
	img { display: block; margin: 0px auto; }
</style>
</head>
<body>
	<header>
		<!-- Login, Join 링크 표시 영역 -->
		<jsp:include page="/inc/top.jsp"></jsp:include>
	</header>
	
	<article>
		<!-- 본문 표시 영역 -->
		<h1> (☞ﾟヮﾟ)☞ <b>MVC 게시판</b> ☜(ﾟヮﾟ☜) </h1>
		<h3><a href="BoardWriteForm.bo">글쓰기</a></h3>
		<h3><a href="BoardList.bo">글목록</a></h3>
	</article>
	
	<img src="images/IMG_4943.jpeg" alt="" width="300px" height="300px">
</body>
</html>
