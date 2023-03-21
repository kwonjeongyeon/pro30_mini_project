<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>

<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%
	request.setCharacterEncoding("UTF-8");
%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>header</title>
</head>
<body>
	<table border="0" width="100%">
		<tr>
			<td><a href="${contextPath}/main.do"> <img
					src="${contextPath}/resources/image/rabbit.jpg" />
			</a></td>
			<td>
				<h1>
					<font size="30">스프링실습 홈페이지</font>
				</h1>
			</td>

			<td><c:choose>
					<c:when test="${isLogOn == true && member != null}">
						<h3>환영합니다. ${member.name}님!</h3>
						<a href="${contextPath}/member/logout.do"><h3>로그아웃</h3></a>
					</c:when>
					<c:otherwise>
						<a href="${contextPath}/member/loginForm.do"><h3>로그인</h3></a>
					</c:otherwise>
				</c:choose></td>
		</tr>
	</table>
</body>
</html>