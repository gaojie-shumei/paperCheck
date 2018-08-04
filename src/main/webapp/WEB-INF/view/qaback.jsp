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
	<div id="page-wrapper" class="col-lg-12" > 
	  <!--中间的主要页面-->
	  <div class="wrapper wrapper-content animated fadeIn">
	    <div class="row">
	      <div class="col-lg-12">
	        <div class="tabs-container">
	          <ul class="nav nav-tabs">
	            <li class="active"><a data-toggle="tab" href="#tab-1"
									aria-expanded="flase">问题反馈</a></li>
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
	                              <td align="right"  style="height:100px;vertical-align:middle;line-height:100px;"><label style="vertical-align:middle;line-height:100px;">问题描述:&nbsp;</label></td>
	                              <td><textarea id="qdescription" name="qdescription"  style="height:100px;resize:none" class="form-control" ></textarea></td>
	                            </tr>
	                          </table>
	                          <div align="center">
	                          	<input id="upqa" class="btn btn-info" value="提交" type="button">&nbsp;&nbsp;
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
	<script type="text/javascript" src="static/js/security_url_js/security_url.js"></script>
	<script type="text/javascript">
		$(function(){
			$("#upqa").click(function(){
				upqa();
			});
		});
		function upqa(){
			var qdescription = $("#qdescription").val();
			if(qdescription==null||qdescription.trim()==""){
				swal("","问题描述不能为空！","error");
			}else{
				$.ajax({
					type:"post",
					url:"resource/qa/upqa",
					data:{
						qdescription:qdescription
					},
					dataType:"json",
					success:function(data){
						if(data.state==true){
							swal("","问题反馈成功，我们会尽快解决，谢谢！","success").then(function(){
								$("#qdescription").val("");
							});
							
						}else{
							swal("","系统异常，请稍后再试！","error");
						}
					},
					error:function(){
						swal("","系统异常，请稍后再试！","error");
					}
				});
			}
		}
	</script>
</body>
</html>