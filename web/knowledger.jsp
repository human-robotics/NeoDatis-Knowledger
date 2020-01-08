
<%
	String baseName = request.getParameter("base.name");
	String language = request.getParameter("language");
	String width = request.getParameter("applet.witdh");
	String height = request.getParameter("applet.height");
	String withToolBar = request.getParameter("with.toolbar");

	String appletUrl = "knowledger-applet.jsp?base.name="+baseName+"&applet.width"+width+"&applet.height="+height+"&with.toolbar="+withToolBar+"&language="+language;

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>NeoDatis Knowledger - <%=baseName%></title>
</head>

<frameset rows="50%,*" cols="*" framespacing="1" frameborder="yes" border="1">
  <frame src="<%=appletUrl%>" name="top" scrolling="yes" id="top" title="top" />
  <frame src="knowledger-detail.jsp" name="detail" id="detail" title="detail" />
</frameset>
<noframes><body>
</body>
</noframes></html>
