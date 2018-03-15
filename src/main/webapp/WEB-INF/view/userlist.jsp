<%@ page language="java" import="java.util.*" pageEncoding="utf-8" isELIgnored="false"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
+ path + "/";
%>
<!DOCTYPE html>
<html lang="en" class="no-js">
<!-- BEGIN HEAD -->
	<head>
    	<base href="<%=basePath%>">
        <meta charset="UTF-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title>User Show</title>
        <link rel="stylesheet" type="text/css" href="static/css/sweet-alert.css" />
        <link rel="stylesheet" type="text/css" href="static/css/bootstrap.min.css" />
        <link rel="stylesheet" type="text/css" href="static/css/bootstrap-theme.min.css" />
        <link rel="stylesheet" type="text/css" href="static/css/style.css" />
        <link rel="stylesheet" type="text/css" href="static/css/datatables.min.css" />
        <link rel="stylesheet" type="text/css" href="static/css/select2.min.css" />
        <style type="text/css">
        	th, td {
				text-align: center;
				margin: 0px;
				padding: 0px;
			}
        </style>
	</head>
<body class="white-bg">
	<div class="row col-lg-push-3 col-lg-pull-3 col-lg-12">
		<div class="col-lg-12">
<!-- 			<input id="adduser" type="button" value="添加用户" class="btn btn-info btn-outline" /> -->
			<table  id="usershow" class="table  table-striped table-bordered table-hover dataTable table-condensed">
				<thead>
					<tr role="row">
						<th>用户名</th>
						<th>用户角色</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<!-- 通过ajax获取后台数据并填充 -->
				</tbody>
			</table>
		</div>
	</div>
	
	
	    <script type="text/javascript" src="static/js/jquery.js" ></script>
<!-- 	    <script type="text/javascript" src="static/js/chosen.jquery.js"></script> -->
	    <script type="text/javascript" src="static/js/datatables.min.js"></script>
	    <script type="text/javascript" src="static/js/mydataTable.js"></script>
    	<!-- <script type="text/javascript" src="static/js/jquery.form.js" ></script> -->
    	<script type="text/javascript" src="static/js/bootstrap.min.js" ></script>
    	<script type="text/javascript" src="static/js/select2.full.min.js"></script>
    	<script type="text/javascript" src="static/js/sweet-alert.min.js"></script>
    	<script type="text/javascript">
    		var permission = "${sessionScope.permission}";
    	</script>
    	<script type="text/javascript" src="static/js/userjs/user.js"></script>
</body>
</html>