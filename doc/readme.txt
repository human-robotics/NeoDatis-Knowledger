1) use of Knowledger as a desktop application

execute the bat/explorer.bat command line.


2) Use of Knowledger via WEB

Copy the knowledger.war file in any java/jsp web server (tomcat for example)

Call the http://host:port/knowledger/ url. You will have to enter the name of knowledger base you want to work on. If it does not exist, it will be created.

You can specify the name of the base (default is knowledger), the window width, the window height and if the toolbar must be displayed


3) How to integrate knowledger to another web application

simply copy the applet directory of the knowledger.war to your web archive
AND neodatis-odb-jdk1.4.jar and knowledger.jar to your WEB-INF/lib

the applet/knowledger.jsp file has the applet tag.

Use the following url to access the knowledger applet:

http://host:port/knowledger/applet/knowledger.jsp?base.name=<name of the base>&applet.width=<the size of the knowledger applet> & applet.height=<the height of the knowledger applet> & with.toolbar=true or false & language=br

For more details, check www.knowledger.org