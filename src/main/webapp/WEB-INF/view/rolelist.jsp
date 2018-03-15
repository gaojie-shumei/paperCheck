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
        <link rel="stylesheet" type="text/css" href="static/css/datatables.min.css" />
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
	<div class="row col-lg-push-3 col-lg-pull-3 col-lg-12">
		<div class="col-lg-12">
			<shiro:hasPermission name="role:add">
				<input id="addrole" type="button" value="添加角色" class="btn btn-info" />
			</shiro:hasPermission>
			<br/>
			<br/>
			<br/>
			<table  id="roleshow" class="table  table-striped table-bordered table-hover dataTable table-condensed">
				<thead>
					<tr role="row">
						<th>角色名</th>
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
    	<script type="text/javascript" src="static/js/sweet-alert.min.js"></script>
    	<script type="text/javascript">
	    	function getAllowedRole(){
	    		roletable.destroy();
	    		$("#roleshow").children("tbody").html("");
	    		roletable = mydataTable("#roleshow");
	    		$.ajax({
	    			type:"post",
	    			url:"resource/role/selAllowedRole",
	    			dataType:"json",
	    			success:function(data){
	    				if(data!=null&&data!=""){
	    					$.each(data,function(i,n){
	    						roletable.row.add([
	    						      "<div id="+n.id+" style='width:240px;text-align:center;margin:0 auto;'>"+n.rolename+"</div>",
	    						      "<shiro:hasPermission name='permission:set'><a href='javascript:void(0)' class='btn btn-info setpermission' style='color:white;'>设置权限</a></shiro:hasPermission>&nbsp;&nbsp;"+
	    						      "<shiro:hasPermission name='role:update'><a href='javascript:void(0)' class='btn btn-info uprole' style='color:white;'>修改</a></shiro:hasPermission>&nbsp;&nbsp;"+
	    							  "<shiro:hasPermission name='role:delete'><a href='javascript:void(0)' class='btn btn-warning delrole' style='color:white;'>删除</a></shiro:hasPermission>"
	    						]);
	    						$("#roleshow tbody").children("tr:eq("+(i+1)+")").prop("name","existrole");
	    					});
	    				}
	    				$(".uprole").off("click");
	    				$(".uprole").click(function(){
	    					uprole(this);
	    				});
	    				$(".delrole").off("click");
	    				$(".delrole").click(function(){
	    					delrole(this);
	    				});
	    				$(".setpermission").off("click");
	    				$(".setpermission").click(function(){
	    					//跳转到设置权限页面
	    					window.location.href = "./resource/page/index/setpermission?roleid="+$(this).parents("tr").children("td:eq(0)").find("div").prop("id");
	    				});
	    				roletable.draw(false);
	    			},
	    			error:function(){
	    				swal("","系统异常，请稍后再试！","error");
	    			}
	    		});
	    	}
	
	    	function addrole(object){
	    		if($(object).val()=="添加角色"){
	    			$(object).val("取消添加");
	    			roletable.row.add([
	    			      "<input id='-1'>",
	    			      "<shiro:hasPermission name='permission:set'><a href='javascript:void(0)' class='btn btn-info setpermission' style='color:white;display:none;'>设置权限</a></shiro:hasPermission>&nbsp;&nbsp;"+
	    			      "<shiro:hasPermission name='role:update'><a href='javascript:void(0)' class='btn btn-info uprole' style='color:white;'>完成</a></shiro:hasPermission>&nbsp;&nbsp;"+
	    				  "<shiro:hasPermission name='role:delete'><a href='javascript:void(0)' class='btn btn-warning delrole' style='color:white;display:none;'>删除</a></shiro:hasPermission>"
	    			]);
	    			
	    			$(".uprole").off("click");
	    			$(".uprole").click(function(){
	    				uprole(this);
	    			});
	    			$(".delrole").off("click");
	    			$(".delrole").click(function(){
	    				delrole(this);
	    			});
	    			$(".setpermission").off("click");
	    			$(".setpermission").click(function(){
	    				//跳转到设置权限页面
	    				window.location.href = "./resource/page/index/setpermission?roleid="+$(this).parents("tr").children("td:eq(0)").find("div").prop("id");
	    			});
	    			roletable.draw(false);
	    		}else{
	    			$(object).val("添加角色");
	    			roletable.destroy();
	    			var trobj = $("#roleshow tbody").children("tr[name!='existrole']");
	    			roletable = mydataTable("#roleshow");
	    			roletable.row(trobj).remove().draw(false);
	    		}
	    		
	    	}
    	</script>
    	<script type="text/javascript" src="static/js/rolejs/role.js"></script>
</body>
</html>