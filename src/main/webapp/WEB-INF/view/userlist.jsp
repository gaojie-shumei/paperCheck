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
        <link rel="stylesheet" type="text/css" href="static/css/personalinfocss/personalinfo.css" />
        <style type="text/css">
        	th, td {
				text-align: center;
				margin: 0px;
				padding: 0px;
			}
        </style>
	</head>
<body class="white-bg">
	<div class="row col-lg-12">
		<div id="pageTitle" style="text-align: center;">
			<strong style="text-align: center;font-size: 20px;font-weight: 30px;margin-bottom: 20px;">用户管理</strong>
		</div>
		<br/><br/>
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
	
	<!-- 模态框 -->
	<div class="modal fade" id="userdetailmodal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	    <div class="modal-dialog">
	        <div class="modal-content">
	            <div class="modal-header">
	                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	                <h4 class="modal-title" id="myModalLabel">用户详情</h4>
	            </div>
	            <div class="modal-body">
	            	<table id="submittab" class="table  table-striped table-bordered table-hover dataTable table-condensed" align="center">
                          	<tr role="row">
                          		<td align="right"><label>头像:&nbsp;</label></td>
                          		<td colspan="3" align="left" class="relativetd" >
									<div id="showimage" style="display: block">
										<img alt="头像" src="" id="userimage1">
									</div>
                          		</td>
                          	</tr>
                            <tr role="row">
                              <td align="right"><label>姓名:&nbsp;</label></td>
                              <td><input id="username" name="username" type="text" class="form-control" readonly="readonly" /></td>
                              <td align="right"><label>性别:&nbsp;</label></td>
                              <td align="center">
                                <input name="sex" type="radio" checked="checked" value="男" disabled="disabled"/> <label>男</label>
                               
                                <input name="sex" type="radio" value="女" disabled="disabled"/> <label>女</label>
                              </td>
                            </tr>
                            <tr role="row">
                              <td align="right"><label>出生日期:&nbsp;</label></td>
                              <td><input id="birthdate" name="birthdate" class=" form-control"  readonly="readonly"/></td>
                              <td align="right"><label>手机:&nbsp;</label></td>
                              <td><input id="tel" name="tel" class=" form-control" readonly="readonly"/></td>
                            </tr>
                            <tr role="row">
                              <td align="right"><label>QQ号码:&nbsp;</label></td>
                              <td><input id="qq" name="qq" class="form-control"  readonly="readonly" /></td>
                              <td align="right"><label>邮箱:&nbsp;</label></td>
                              <td><input id="email" name="email" class="form-control"  readonly="readonly"/></td>
                            </tr>
                            <tr role="row">
                              <td align="right"><label>称呼:&nbsp;</label></td>
                              <td align="center"><input id="callname" name="callname" class="form-control"  readonly="readonly" />
                              </td>
                              <td align="right"><label>工作单位:&nbsp;</label></td>
                              <td><input id="organization" name="organization" class="form-control"  readonly="readonly"/></td>
                            </tr>
                          </table>
	            </div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
	            </div>
	        </div><!-- /.modal-content -->
	    </div><!-- /.modal -->
	</div>
	
	    <script type="text/javascript" src="static/js/jquery.js" ></script>
<!-- 	    <script type="text/javascript" src="static/js/chosen.jquery.js"></script> -->
	    <script type="text/javascript" src="static/js/datatables.min.js"></script>
	    <script type="text/javascript" src="static/js/mydataTable.js"></script>
    	<!-- <script type="text/javascript" src="static/js/jquery.form.js" ></script> -->
    	<script type="text/javascript" src="static/js/bootstrap.min.js" ></script>
    	<script type="text/javascript" src="static/js/select2.full.min.js"></script>
    	<script type="text/javascript" src="static/js/sweet-alert.min.js"></script>
    	<script type="text/javascript" src="static/js/security_url_js/security_url.js"></script>
    	<script type="text/javascript">
    		var permission = "${sessionScope.permission}";
    	</script>
    	<script type="text/javascript" src="static/js/userjs/user.js"></script>
</body>
</html>