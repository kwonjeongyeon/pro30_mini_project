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
<title>하단 부분</title>
<style type="text/css">
p {
	font-size: 20px;
	text-align: center;
}
</style>
</head>
<body>
	<p>e-mail : admin@test.com</p>
	<p>회사 주소 : 서울시 강동구</p>
	<p>
		찾아오는 길 : <a href="#<">약도</a>
	</p>
</body>
</html>