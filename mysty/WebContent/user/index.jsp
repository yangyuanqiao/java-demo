<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Expires" content="0">
<title>Insert title here</title>
<link href="../static/css/style.css" rel="stylesheet" type="text/css" />
<script src="../static/js/jquery-1.10.2.min.js" type="text/javascript"></script>
</head>
<style>
.s-top {
    border: 1px solid #CFCFCF;
    background-color: brown;
    background-color: slategrey;
    height: 70px;
}

.slide{
	background-color: slategrey;
	border: 1px solid #CFCFCF;
}
</style>
<script type="text/javascript">

$(function(){
	$("#list").click(function (){
		//$(this).next()
		$("#maindiv").load("/mysty/user/getAllUser?page=0");
	})
	
})
</script>
<body>
<div class="contair">
	<div class="s-top row"></div>
	<div class="main row">
		<div class="slide col2">
			<ul>
				<li id="list"><a href="#">用户列表</a></li>
				<li><a href="/mysty/user/addUser">添加用户</a></li>
			</ul>
		</div>
		<div id="maindiv" class="content col6">
		
		
		</div>
	</div>
	<div class="footer row">
	</div>
</div>
</body>
</html>