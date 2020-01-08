<%@ page import="org.neodatis.knowledger.core.*"%>

<%String release = Release.RELEASE_DATE;
%>

<html>
<link href="css/knowledger.css" rel="stylesheet" type="text/css">
<body >



<p>&nbsp;</p>
<table align="center" width="30%" >
  <tr><td><H1 align="center">NeoDatis Knowledger</H1></td></tr>
  <tr>
    <td class="mybody">
    <form action="knowledger.jsp" method="post">
    <table align="center" width="60%">
      <tr><td>&nbsp;</td></tr>
	  <tr>
        <td class="myfont">Base name</td>
        <td ><input name="base.name" type="text" class="mynput" value="knowledger" size="15"></td>
      </tr>
      <tr>
        <td class="myfont">Window Width</td>
        <td ><input type="text" name="applet.width" size="15" value="400" class="mynput"></td>
      </tr>
      <tr>
        <td class="myfont">Window Height</td>
        <td><input type="text" name="applet.height" size="15" value="400" class="mynput"></td>
      </tr>
      <tr>
        <td class="myfont">With ToolBar</td>
        <td><p>
          <select name="with.toolbar" size="1" class="mynput">
            <option value="true">yes</option>
            <option value="false">no</option>
          </select>
        </p></td>
      </tr>
      <tr>
        <td class="myfont">Animation Step Duration</td>
        <td><p>
          <input type="text" name="animation.step.duration" size="10" class="mynput" value="100">
        </p></td>
      </tr>
      <tr>
        <td class="myfont">Interaction Manager Class</td>
        <td><p>
          <input type="text" name="interaction.manager.class" size="10" class="mynput" value="default">
        </p></td>
      </tr>
      <tr>
        <td class="myfont">Language</td>
        <td><select name="language" size="1" class="mynput">
          <option value="en" selected>English</option>
          <option value="br">Brazilian</option>
        </select></td>
      </tr>
    </table>
	<table align="center">
	  <tr><td>&nbsp;</td></tr>
	  <tr><td align="center"><input type="submit" name="open" value="open" size="4"></td></tr>	  
	 </table>

  </form>
	</td>
  </tr>
  <tr>
    <td align="right" class="myfont">build (<%=release%>)</td>
  </tr>
</table>

</body>
</html>
