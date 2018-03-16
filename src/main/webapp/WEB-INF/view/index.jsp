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
        <title>PaperCheck</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"> 
        <meta name="description" content="" />
        <meta name="author" content="" />
        <!-- <link rel="shortcut icon" href="../favicon.ico"> -->
        <link rel="stylesheet" type="text/css" href="static/css/sweet-alert.css" />
        <link rel="stylesheet" type="text/css" href="static/css/bootstrap.min.css" />
        <link rel="stylesheet" type="text/css" href="static/css/bootstrap-theme.min.css" />
        <link rel="stylesheet" type="text/css" href="static/css/style.css" />
        <link rel="stylesheet" type="text/css" href="static/css/indexcss/index.css" />
        
    </head>
    <body class="white-bg">
    	<div id="wrapper" class="front_page wrapper_s" style="padding-bottom: 0px;">
    		<!-- head start-->
    		<div id="head" class="LOG_WR" data-log="{'actblock': 'head'}">
    			<div class="headCont lightBg">
    				<div id="u">
				    	<span id="user">
				    		<a href="javascript:void(0);" class="username" id="userName">
				    			<img id="user_pic" alt="用户头像">
				    		</a>
				    		<div id="userMenu" class="menuDropList" style="display: none;">
				    			<div class="menuarrow" style="display: block;">
				    				<i class="head-icon head-icon-menu-triangle"></i>
				    			</div>
				    			<div class="usermenu">
				    				<a href="resource/page/index/personalinfo">个人资料</a>
				    				<a href="resource/page/index/uppassword">修改密码</a>
				    				<a href="javascript:void(0)" class="delloginUser">注销账户</a>
				    				<a href="resource/user/logout" class="logout">退出</a>
				    				
				    			</div>
				    		</div>
				    	</span>
					</div>
					
					<div class="headtags">
    					<a href="resource/page/index" class="home" target="_self">
    						<img id="toindex" src="static/images/defaultIndex.jpg" border="0" alt="To Index" title="To Index">
        				</a>
    				</div>
				</div>
			</div>
			<!-- head end-->
			<!-- main content start-->
			<div id="container">
				<!-- left menu start -->
				<div id="left_menu_content" class="LOG_WR" data-log="{'actblock':'menu'}">
    				<div class="left_menu_logo btn-info">
        				<!-- <a href="resource/page/index"> -->
        					<!-- <img class="sologan onlyOn" src="static/images/defaultIndex.png"> -->
        					<i class="glyphicon glyphicon-th-list logosim"></i>
        				<!-- </a> -->
    				</div>
    				<div class="main_menu_wr">
    					<div class="menu_listitem">
               				<a href="./resource/page/index/checkpaper" class="" data-log="{'type':'checkpaper'}">
                   				<span>文章查重</span>
               				</a>
           				</div>
           				
           				<shiro:hasPermission name="checkdoc:bashcheck">
	           				<div class="menu_listitem">
	               				<a href="./resource/page/index/bashcheckpaper" class="" data-log="{'type':'bashcheckpaper'}">
	                   				<span>批量查重</span>
	               				</a>
	           				</div>
           				</shiro:hasPermission>
           				
    					<div class="menu_listitem">
               				<a href="./resource/page/index/report" class="" data-log="{'type':'report'}">
                   				<span>我的报告</span>
               				</a>
           				</div>
           				
           				<shiro:hasPermission name="history:add">
	           				<div class="menu_listitem">
	               				<a href="./resource/page/index/history" class="" data-log="{'type':'history'}">
	                   				<span>库更新</span>
	               				</a>
	           				</div>
           				</shiro:hasPermission>
   
           				<shiro:hasPermission name="user:select">
	           				<div class="menu_listitem">
	               				<a href="./resource/page/index/userlist" class="" data-log="{'type':'usermanager'}">
	                   				<span>用户管理</span>
	               				</a>
	           				</div>
           				</shiro:hasPermission>
           				
	           				<shiro:hasPermission name="role:select">
		           				<div class="menu_listitem">
		               				<a href="./resource/page/index/rolelist" class="" data-log="{'type':'rolemanager'}">
		                   				<span>角色管理</span>
		               				</a>
		           				</div>
	           				</shiro:hasPermission>
    				</div>
   				</div>
   				<!-- left menu end -->
   				
   				<!-- right content start  -->
   				<div id="right_content" class="content_wr">
                	<div id="right_content_main" class="right_content_main">
                		<!-- 通过load方法加载 -->
                		
                	</div>
               	</div>
   				<!-- right content end  -->
			</div>
			<!-- main content end-->
    	</div>
    	
    	<div id="scriptdiv">
	    	<script type="text/javascript" src="static/js/jquery.js" ></script>
	    	<!-- <script type="text/javascript" src="static/js/jquery.form.js" ></script> -->
	    	<script type="text/javascript" src="static/js/jquery.validate.min.js"></script>
	    	<script type="text/javascript" src="static/js/bootstrap.min.js" ></script>
	    	<script type="text/javascript" src="static/js/sweet-alert.min.js"></script>
	    	<script type="text/javascript" src="static/js/security_url_js/security_url.js"></script>
	    	<script type="text/javascript" src="static/js/commonjs/commonUploadFiles.js""></script>
	    	<script type="text/javascript" src="static/js/indexjs/index.js"></script>
	    	<script type="text/javascript">
	    		$(function(){
	    			if("${user.userimage}"==""){
	    				$("#user_pic").prop("src","static/images/userimage/defaultUserImage.jpg");
	    			}else{
	    				$("#user_pic").prop("src","${sessionScope.user.userimage}");
	    			}
	    			$("#user_pic").prop("title","${sessionScope.user.loginname}");
	    		});
	    	</script>
    	</div>
    </body>
</html>