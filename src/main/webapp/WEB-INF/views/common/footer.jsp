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
<title>�ϴ� �κ�</title>
<style type="text/css">
p {
	font-size: 20px;
	text-align: center;
}
</style>
</head>
<body>
	<p>e-mail : admin@test.com</p>
	<p>ȸ�� �ּ� : ����� ������</p>
	<p>
		ã�ƿ��� �� : <a href="#<">�൵</a>
	</p>
</body>
</html>