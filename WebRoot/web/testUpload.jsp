<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>My JSP 'test.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    

  </head>
  
  <body>
	<form action="<%=basePath%>userApp/importAPPUserExcel" method="post" enctype="multipart/form-data">
	    <input type="file"  name="file" value="上传文件"/>
	    <input id="submit_form" type="submit" class="btn btn-success save" value="保存"/>
	</form>
  </body>
</html>
