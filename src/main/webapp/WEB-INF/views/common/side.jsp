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
<title>���̵� �޴�</title>
<style type="text/css">
.no-underline {
	text-decoration: none;
}
</style>
</head>
<body>
	<h1>���̵� �޴�</h1>
	<h1>
		<a href="${contextPath}/member/listMembers.do" class="no-underline">ȸ������</a><br>
		<a href="${contextPath}/board/listArticles.do" class="no-underline">�Խ��ǰ���</a><br>
		<a href="#" class="no-underline">��ǰ����</a><br>
	</h1>
</body>
</html>