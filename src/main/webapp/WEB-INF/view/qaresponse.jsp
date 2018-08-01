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
        <link rel="stylesheet" type="text/css" href="static/css/datatables.min.css" />
    </head>
<body class="white-bg">
	<div class="row col-lg-push-3 col-lg-pull-3 col-lg-12">
		<table  id="qaresponseshow" class="table  table-striped table-bordered table-hover dataTable table-condensed">
			<thead>
				<tr role="row">
					<th>反馈者</th>
					<th>反馈描述</th>
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
	<script type="text/javascript">
		var qaresponsetable = mydataTable("#qaresponseshow");
		$(function(){
			getAllQaBack();
		});
		function getAllQaBack(){
			qaresponsetable.destroy();
			$("#qaresponseshow").children("tbody").html("");
			qaresponsetable = mydataTable("#qaresponseshow");
			$.ajax({
				type:"post",
				url:"resource/qa/selQaBack",
				dataType:"json",
				success:function(data){
					if(data!=null&&data!=""&&!$.isEmptyObject(data)){
						$.each(data,function(i,n){
							qaresponsetable.row.add([
								n.qloginname,
								"<textarea style='height:100px;resize:none;width:100%;' class='form-control'>"+n.qdescription+"</textarea>",
								"<input id="+n.id+" class='btn btn-info deal' value='已处理' type='button'>&nbsp;&nbsp;"
							]);
						});
					}
					qaresponsetable.draw(false);
					qaresponsetable.destroy();
					$("#qaresponseshow").find("td").css("vertical-align","middle");
					$("#qaresponseshow").find("td").css("text-align","center");
// 					$("#qaresponseshow").find("td").css("line-height","100px");
					$(".deal").click(function(){
						deal(this);
					});
					qaresponsetable = mydataTable("#qaresponseshow");
				},
				error:function(){
					swal("","系统异常，请稍后再试！","error");
				}
			});
		}
		function deal(jsObj){
			var qaid = $(jsObj).prop("id");
			var rowobj = $(jsObj).parents("tr");
			swal({
				  title: "",
				  text: "确认已处理？",
				  type: "warning",
				  showCancelButton: true,
				  confirmButtonColor: "#DD6B55",
				  confirmButtonText: "确定",
				  cancelButtonText: "取消",
//				  closeOnConfirm: false,
//				  closeOnCancel: false
			}).then(function(isConfirm) {
				  if (isConfirm) {
					  $.ajax({
							type:"post",
							url:"resource/qa/delQaBack",
							data:{
								qaid:qaid
							},
							dataType:"json",
							success:function(data){
								if(data!=null&&data!=""){
									if(data.state==true){
										swal("","处理成功","success").then(function(){
											qaresponsetable.row(rowobj).remove().draw(false);
										});
									}else{
										swal("","处理失败","success");
									}
								}
							},
							error:function(){
								swal("","系统异常，请稍后再试！","error");
							}
						});
				  }
			});
		}
	</script>
</body>
</html>