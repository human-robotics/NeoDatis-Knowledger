<%@ page import="org.neodatis.knowledger.web.*"%>
<% 
String baseName = request.getParameter("base.name");
String sId = request.getParameter("parameter");

String result = null;
if(baseName!=null && sId !=null){
	KnowledgerObjectPageBuilder builder = new KnowledgerObjectPageBuilder(baseName,sId);	
	result = builder.buildHtml();
}else{
	result = "click on a node to get details";
}

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Knowledger Object detail</title>
</head>

<body>
<p>
<%=result%>
</p>
</body>
</html>
