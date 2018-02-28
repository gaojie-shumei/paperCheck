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
        <title>upPassword</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"> 
        <meta name="description" content="" />
        <meta name="author" content="" />
        <!-- <link rel="shortcut icon" href="../favicon.ico"> -->
        <link rel="stylesheet" type="text/css" href="static/css/sweet-alert.css" />
        <link rel="stylesheet" type="text/css" href="static/css/bootstrap.min.css" />
        <link rel="stylesheet" type="text/css" href="static/css/bootstrap-theme.min.css" />
        <link rel="stylesheet" type="text/css" href="static/css/style.css" />
        
    </head>
<body class="white-bg">
<div id="page-wrapper" class="col-lg-push-1 col-lg-pull-3 col-lg-12" > 
  <!--中间的主要页面-->
  <div class="wrapper wrapper-content animated fadeIn">
    <div class="row">
      <div class="col-lg-12">
        <div class="tabs-container">
          <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#tab-1"
								aria-expanded="flase">修改密码</a></li>
          </ul>
          <div class="tab-content">
            <div id="tab-1" class="tab-pane active">
              <div class="panel-body">
                <div class="row">
                  <div class="col-lg-12">
                    <div class="ibox float-e-margins">
                      <div class="ibox-content">
                        <div class="row">
                          <table id="submittab" class="table  table-striped table-bordered table-hover dataTable table-condensed" align="center">
                            <tr role="row">
                              <td align="right"><label>原密码:&nbsp;</label></td>
                              <td><input id="oldpassword" name="oldpassword" type="password" class="form-control"/></td>
                            </tr>
                            <tr role="row">
                              <td align="right"><label>新密码:&nbsp;</label></td>
                              <td><input id="newpassword" name="newpassword" class=" form-control" type="password"/></td>
                            </tr>
                          </table>
                          <div align="center">
                          	<input id="uppwd" class="btn btn-info" value="提交" type="button">&nbsp;&nbsp;
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>    
    </div>
  </div>
</div>


<script type="text/javascript" src="static/js/jquery.js" ></script>
<script type="text/javascript" src="static/js/bootstrap.min.js" ></script>
<script type="text/javascript" src="static/js/sweet-alert.min.js"></script>
<script type="text/javascript" src="static/js/personalinfojs/personalinfo.js"></script>
<script type="text/javascript">
	$(function(){
		$("#uppwd").click(function(){
			uppwd();
		});
	});
	function uppwd(){
		var oldpwd = $("#oldpassword").val();
		var newpwd = $("#newpassword").val();
		if(oldpwd==null||oldpwd.trim()==""){
			swal("","原密码不能为空！","error");
		}else if(newpwd==null||newpwd.trim()==""){
			swal("","新密码不能为空！","error");
		}else if(newpwd.trim().length<6){
			swal("","密码要求至少为6位！","error");
		}else{
			$.ajax({
				type:"post",
				url:"resource/user/uppwd",
				data:{
					oldpwd:oldpwd,
					newpwd:newpwd
				},
				dataType:"json",
				success:function(data){
					if(data.state==true){
						swal("","修改密码成功，请重新登录！","success");
						window.location.href = "./resource/user/logout";
					}else{
						swal("",data.error,"error");
					}
				},
				error:function(){
					swal("","系统异常，请稍后再试！","error");
				}
			});
		}
	}
</script>