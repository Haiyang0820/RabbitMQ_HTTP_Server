<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">

	<script  src="http://code.jquery.com/jquery-latest.js"></script>
	<script>
	/* $(document).ready(function () {
		window.hand=setInterval("test()",5000);
	}); */
	</script>
  </head>
  
  <body>
    RPC Test <br><br>
    send<input type="text" id="in"><br><br>
    receive<input type="text" id="a"><br><br>
    
    <input type="button" onclick="server()" value="ActiveServer"><br><br>
    <input type="button" onclick="action()" value="Send"><br><br>
    <script type="text/javascript">
    function action(){  
          debugger
        $.ajax({  
            url:"/callRPCConsumer?queueName=RPCQueue&message="+$('#in').val(),  
            type:'get',  
            dataType:'json',  
            success:function(data){
	           $('#a').val(data);
            },
            error:function(data){  
	           $('#a').val(0);
            }    
        });  
    }  
    function server(){  
          debugger
        $.ajax({  
            url:"/callRPCProducer?queueName=RPCQueue",  
            type:'get',  
            dataType:'json',  
            success:function(data){
	           alert('ok')
            },
            error:function(data){  
	          alert('failed')
            }    
        });  
    }  
    </script>
  </body>
</html>
