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
    <form action="<%=basePath%>news/updateNews" method="post">
    	<input type="hidden" name="news.newsId" value="13"/>
		<div>新闻标题：<input type="text" name="news.title" /></div>
		<div>新闻概要：<input type="text" name="news.briefContent" /></div>
		<div>新闻内容：<input type="text" name="news.content" /></div>
		<div>新闻图片：<input type="text" name="news.pic" /></div>
		<div>新闻视频：<input type="text" name="news.video" /></div>
		<div>新闻类型：
			<input type="checkbox" id="type" name="tags" value="14130451497570000ed4f8f09de38fea"/>HR
			<input type="checkbox" id="type" name="tags" value="1413045149759000109e082de7fb6c65"/>FIN
			<input type="checkbox" id="type" name="tags" value="141312011437600015aca4264bb5973c"/>INFO
			<input type="checkbox" id="type" name="tags" value="14131201143730000fb7a67c31353cd1"/>COMM
		</div> 
		<div>新闻状态：<input type="text" name="news.status" /></div>
		<div>新闻作者：<input type="text" name="news.authorUser" /></div>
		<input type="submit" value="提交">
    </form>
  </body>
</html>
