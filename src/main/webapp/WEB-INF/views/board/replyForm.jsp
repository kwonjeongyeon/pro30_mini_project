<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
	request.setCharacterEncoding("utf-8");
%>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>답글 쓰기</title>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.3/jquery.min.js"></script>
<script type="text/javascript">
	function backToList(obj) {
		obj.action = "${contextPath}/board/listArticles.do";
		obj.submit();
	}

	function readURL(input) {
		if (input.files && input.files[0]) {
			var reader = new FileReader();
			reader.onload = function(e) {
				$('#preview').attr('src', e.target.result);
			}
			reader.readAsDataURL(input.files[0]);
		}
	}
</script>
</head>
<body>
	<h1 style="text-align: center">답글쓰기</h1>
	<form name="frmReply" method="post"
		action="${contextPath}/board/addReply.do"
		enctype="multipart/form-data" accept-charset="utf-8">
		<table align="center">
			<tr>
				<td align="right">작성자:&nbsp;</td>
				<td><input type="text" size="20" maxlength="100" name="writer"
					disabled /></td>
			</tr>
			<tr>
				<td align="right">제목:&nbsp;</td>
				<td><input type="text" size="67" maxlength="500" name="title" />
				</td>
			</tr>
			<tr>
				<td align="right" valign="top"><br>내용:&nbsp;</td>
				<td><textarea name="content" rows="10" cols="65"
						maxlength="4000" /></td>
			</tr>
			<tr>
				<td align="right">비밀번호:&nbsp;</td>
				<td><input type="password" name="pwd" size="10" maxlength="12" /></td>
			</tr>
			<tr>
				<td align="right">이미지파일 첨부:</td>
				<td><input type="file" name="imageFileName"
					onchange="readURL(this);" /></td>
				<td><img id="preview" src="#" width="200" height="200" /></td>
			</tr>
			<tr>
				<td align="right"></td>
				<td><input type="submit" value="답글쓰기" /> <input type="button"
					value="취소" onclick="backToList(this.form)" /></td>
			</tr>
		</table>
	</form>
</body>
</html>