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
		<title>Role Show</title>
        <link rel="stylesheet" type="text/css" href="static/css/sweet-alert.css" />
        <link rel="stylesheet" type="text/css" href="static/css/bootstrap.min.css" />
        <link rel="stylesheet" type="text/css" href="static/css/bootstrap-theme.min.css" />
        <link rel="stylesheet" type="text/css" href="static/css/style.css" />
        <link rel="stylesheet" type="text/css" href="static/css/zTreeStyle/zTreeStyle.css" />
        <link rel="stylesheet" type="text/css" href="static/css/zTreeStyle/demo.css" />
        <link rel="stylesheet" type="text/css" href="static/css/animate.css" />
        
<!--         <link rel="stylesheet" type="text/css" href="static/css/chosen.jquery.css" /> -->
        <style type="text/css">
        	th, td {
				text-align: center;
				margin: 0px;
				padding: 0px;
			}
			
        </style>
	</head>
<body class="white-bg">
	<div class="wrapper wrapper-content animated fadeInrow col-lg-push-3 col-lg-pull-3 col-lg-12">
				<div class="row">
					<div class="col-lg-12">
						<div class="panel panel-primary">
							<div class="panel-heading">
								<b>权限控制</b>
							</div>
							<div class="panel-body">
								<div class=" col-md-4 col-md-push-5 col-md-pull-5">
									<h3>操作提示：</h3>
									<ul>
										<li>在你要赋予的权限的方框内勾选</li>
										<li>勾选完毕之后请点击“确认”按钮</li>
									</ul>
									<div class="content_wrap">
										<div class="zTreeDemoBackground">
											<!-- 显示树 级菜单-->
											<ul id="treeDemo" class="ztree">
											</ul>
										</div>
									</div>
									<input type="button" value="确定" class="btn btn-info" style="margin-left: 75px;" id="submitpermission"/>
								</div>
								
<!-- 								<div class="col-md-6"> -->
									
<!-- 									<div class="hr-line-dashed"></div> -->
<!-- 									<h3>你赋予的权限如下：</h3> -->
<!-- 									<div class="panel panel-primary" > -->
<!-- 										<textarea rows="11"  id="rightStr" style="width:100%"></textarea> -->
<!-- 									</div> -->
<!-- 									<input type="button" value="确定" class="btn btn-primary myBtnStyle" id="btn_sumbit"/> -->
<!-- 								</div> -->
								
							</div>
						</div>

					</div>
				</div>
			</div>
	
	
	    <script type="text/javascript" src="static/js/jquery.js" ></script>
    	<script type="text/javascript" src="static/js/bootstrap.min.js" ></script>
    	<script type="text/javascript" src="static/js/sweet-alert.min.js"></script>
    	<script type="text/javascript" src="static/js/jquery-1.8.0.js" ></script>
    	<script type="text/javascript" src="static/js/zTree/jquery.ztree.all-3.5.min.js"></script>
    	<script type="text/javascript" src="static/js/security_url_js/security_url.js"></script>
    	<script type="text/javascript" src="static/js/setpermissionjs/setpermission.js"></script>
</body>
</html>