<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="./js/jquery.min.js?"></script>
<title>Range Manage</title>
</head>
<body>Loading...

</body>
<script>
	var userId = <%=request.getParameter("userId")%>;
	//userId = 13;
	window.location.href="login/" + userId;
	
	function sub(){
		$.ajax({            
			url: "<%=request.getContextPath()%>/login.do",
			method: "POST",
			data: { "userId" : userId },
			dataType:'json',
			async:false,
			success: function(data) {
				window.location.href="jsp/main.jsp";
			}
		});	
	}
</script>
</html>