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
	$(document).ready(function () {
		window.hand=setInterval("test()",5000);
	});
	</script>
  </head>
  
  <body>
    Direct Test <br>
    <input type="text" id="a"><br><br>
    <input type="button" onclick="action()" value="Action"><br><br>
    <input type="button" onclick="cancel()" value="cancel"/>
    <script type="text/javascript">

    	function cancel(){
    		clearInterval(hand);
    	}
    	
    	function action(){
    		hand=setInterval("test()",5000);
    	}
        function test(){  
        
          debugger
        $.ajax({  
            url:"/callDirectConsumer?isLasting=true&exchangeName=directEx&bindNames=directBinding&queueName=directQueue",  
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
    </script>
  </body>
</html>
