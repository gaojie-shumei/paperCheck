<%@ page language="java" import="java.util.*" pageEncoding="utf-8" isELIgnored="false"%>
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
		<title>rePorts Show</title>
        <link rel="stylesheet" type="text/css" href="static/css/sweet-alert.css" />
        <link rel="stylesheet" type="text/css" href="static/css/bootstrap.min.css" />
        <link rel="stylesheet" type="text/css" href="static/css/bootstrap-theme.min.css" />
        <link rel="stylesheet" type="text/css" href="static/css/style.css" />
        <link rel="stylesheet" type="text/css" href="static/css/datatables.min.css" />
        <style type="text/css">
        	th, td {
				text-align: center;
				margin: 0px;
				padding: 0px;
			}
			.btn-preventDefault{
				border-color: #999;
				color: #999;
			}
        </style>
	</head>
<body class="white-bg">
	<div class="row col-lg-12">
		<div id="pageTitle" style="text-align: center;">
			<strong style="text-align: center;font-size: 20px;font-weight: 30px;margin-bottom: 20px;">我的报告</strong>
		</div>
		<br/><br/>
		<table  id="reportshow" class="table  table-striped table-bordered table-hover dataTable table-condensed">
			<thead>
				<tr role="row">
					<th>文章名</th>
					<th>查重算法</th>
					<th>检测状态</th>
					<th>检测时间</th>
					<th>相似率</th>
					<th>报告有效期</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<!-- 通过ajax获取后台数据并填充 -->
			</tbody>
		</table>
	</div>
	
	    <script type="text/javascript" src="static/js/jquery.js" ></script>
	    <script type="text/javascript" src="static/js/datatables.min.js"></script>
	    <script type="text/javascript" src="static/js/mydataTable.js"></script>
    	<!-- <script type="text/javascript" src="static/js/jquery.form.js" ></script> -->
    	<script type="text/javascript" src="static/js/bootstrap.min.js" ></script>
    	<script type="text/javascript" src="static/js/sweet-alert.min.js"></script>
    	<script type="text/javascript" src="static/js/security_url_js/security_url.js"></script>
    	<script type="text/javascript" src="static/js/reportjs/report.js"></script>
    	
</body>
</html>