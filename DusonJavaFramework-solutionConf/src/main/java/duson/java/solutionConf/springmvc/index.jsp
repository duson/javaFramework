<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false"%>
<%
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%> 
<!doctype html>
<html>
<head>
<meta charset="utf-8" />
<body>
	<h2>Hello World!</h2>
	${name}
</body>
</html>
