<%@ page import="org.neodatis.knowledger.core.*"%>

<%

	String baseName = request.getParameter("base.name");
	String language = request.getParameter("language");
	String width = request.getParameter("applet.witdh");
	String height = request.getParameter("applet.height");
	String withToolBar = request.getParameter("with.toolbar");
	String interactionManager = request.getParameter("interaction.manager.class");
	String stepDuration = request.getParameter("animation.step.duration");
	
	if(baseName==null){
		baseName = "knowledger";
	}
	if(width==null){
		width = "800";
	}
	if(height==null){
		height = "600";
	}

	System.out.println("Base name = " + baseName + " - " + width + " - " + height + " - with tool bar " + withToolBar + " language=" + language);
	

	String release = Release.RELEASE_DATE;

%>


<html>
<!-- <link href="../css/knowledger.css" rel="stylesheet" type="text/css"> -->
<body>

<table align="center">
<!-- 
	<tr>
		<td><H1 align="center">NeoDatis Knowledger : <%=baseName%></H1></td>
	</tr>
 -->
	<tr>
		<td>
			<applet 
				code="org.neodatis.knowledger.gui.graph.panel.KnowledgerEditorApplet" 
				codebase="."
			    archive="lib/knowledger.jar,lib/prefuse.jar,lib/neodatis-odb.jar,lib/antlr-2.7.5.jar,lib/log4j-1.2.8.jar"
			    width="<%=width%>" 
			    height="<%=height%>">
				    <param name="base.name" value="<%=baseName%>">
				    <param name="language" value="<%=language%>">
				    <param name="with.toolbar" value="<%=withToolBar%>">
				    <!--<param name="interaction.manager.class" value="<%="o nome da classe com pacote"%>">-->
				    <param name="animation.step.duration" value="100">
			
			Your browser is completely ignoring the &lt;APPLET&gt; tag!
			</applet>
		</td>
	</tr>
	<tr>
		<td>
			<table width="100%">
				<tr>
					<td align="left">
						<a href="../index.jsp" class="myfont">Open another base</a>					</td>
					<td align="right" class="myfont">build (<%=release%>)</td>
				</tr>				
		  </table>		
		</td>
	</tr>
</table>

<p align="right" class="myfont">

</body>
</html>