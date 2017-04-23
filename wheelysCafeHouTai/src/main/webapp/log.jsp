<%@page import="com.wheelys.Config"%>
<div>Server:<%=Config.SYSTEMID%>,<%=Config.getHostname()%></div>
<div>CONFIG: <%=System.getProperty(Config.SYSTEMID+"-GEWACONFIG")%></div>
<div>HOST: <%=java.net.InetAddress.getLocalHost().getHostName() %></div>
<div>
sversion:test
</div>
<div>PORT: <%=request.getServerPort()%></div>
