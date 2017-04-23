<%@page import="com.wheelys.Config"%>
<div id="<%=Config.getServerIp()%>">Server:<%=Config.SYSTEMID%>,<%=Config.getHostname()%></div>
<div>CONFIG: <%=System.getProperty(Config.SYSTEMID+"-WHEELYSCONFIG")%></div>
<div>HOST: <%=java.net.InetAddress.getLocalHost().getHostName() %></div>
<div>
sversion:
</div>
<div>PORT: <%=request.getServerPort()%></div>
