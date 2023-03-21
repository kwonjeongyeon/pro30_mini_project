<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>

<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
	request.setCharacterEncoding("UTF-8");
%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>사이드 메뉴</title>
<style type="text/css">
.no-underline {
	text-decoration: none;
}
</style>
</head>
<body>
	<h1>사이드 메뉴</h1>
	<h1>
		<a href="${contextPath}/member/listMembers.do" class="no-underline">회원관리</a><br>
		<a href="${contextPath}/board/listArticles.do" class="no-underline">게시판관리</a><br>
		<a href="#" class="no-underline">상품관리</a><br>
	</h1>
</body>
</html>