<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>

<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<style>
#container {
	width: 100%;
	margin: 0px auto;
	text-align: center;
	border: 0px solid #bcbcbc;
}

#header {
	padding: 5px;
	margin-bottom: 5px;
	border: 0px solid #bcbcbc;
	background-color: lightgreen;
}

#sidebar-left {
	width: 15%;
	height: 700px;
	padding: 5px;
	margin-right: 5px;
	margin-bottom: 5px;
	float: left;
	background-color: yellow;
	border: 0px solid #bcbcbc;
	font-size: 10px;
}

#content {
	width: 75%;
	padding: 5px;
	margin-right: 5px;
	float: left;
	border: 0px solid #bcbcbc;
}

#footer {
	clear: both;
	padding: 5px;
	border: 0px solid #bcbcbc;
	background-color: lightblue;
}
</style>
<title><tiles:insertAttribute name="title" />
</head>
<body id="container">
	<div id="header">
		<!-- tiles_member.xml의 <definition>의 하위 태그인 <put-attribute>태그의 name이 title인 값(value)을 표시 -->
		<tiles:insertAttribute name="header"></tiles:insertAttribute>
	</div>
	<div id="sidebar-left">
		<tiles:insertAttribute name="side"></tiles:insertAttribute>
	</div>
	<div id="content">
		<tiles:insertAttribute name="body" ignore="true"></tiles:insertAttribute>
	</div>
	<div id="footer">
		<tiles:insertAttribute name="footer"></tiles:insertAttribute>
	</div>

</body>
</html>