<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<script type="text/javascript">
	function go() {
		document.getElementById("jform").submit();
	}
</script>
<body>
	<div>
		<form id="jform" action="/mysty/user/test">
			hello , jfinal name:
			
			<input name="username" type="text" value=""> age:<input
				name="age" type="text" value=""> 
				
				<input  type="button" value="summit"
				onclick="go();">

		</form>
	</div>
</body>
</html>