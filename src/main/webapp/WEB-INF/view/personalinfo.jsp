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
        <title>personalinfo</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"> 
        <meta name="description" content="" />
        <meta name="author" content="" />
        <!-- <link rel="shortcut icon" href="../favicon.ico"> -->
        <link rel="stylesheet" type="text/css" href="static/css/sweet-alert.css" />
        <link rel="stylesheet" type="text/css" href="static/css/bootstrap.min.css" />
        <link rel="stylesheet" type="text/css" href="static/css/bootstrap-theme.min.css" />
        <link rel="stylesheet" type="text/css" href="static/css/style.css" />
        <link rel="stylesheet" type="text/css" href="static/css/personalinfocss/personalinfo.css" />
        <link rel="stylesheet" type="text/css" href="static/css/jquery.datetimepicker.css" />
        
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
								aria-expanded="flase">个人资料</a></li>
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
                          		<td align="right"><label>头像:&nbsp;</label></td>
                          		<td colspan="3" align="left" class="relativetd" >
<!--                           			<span class="file_upload_btn col-lg-3">点击上传头像</span> -->
									<div id="showimage" style="display: block">
										<img alt="头像" src="" id="userimage1">
									</div>
									<div id="updateimage" style="display: none">
	                          			<img alt="头像" src="" id="userimage">
	                          			<img alt="添加" src="static/images/uploadImage.png" id="updateimg">
	                               		<input type="file" class="file_upload" name="paper_file" accept="image/*" value="上传头像">
	                               		<span class="upinfo" style="display: none;color:red;">正在上传，请稍候！</span>
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
                              <td><input id="birthdate" name="birthdate" class=" form-control setdate"  readonly="readonly"/></td>
                              <td align="right"><label>手机:&nbsp;</label></td>
                              <td><input id="tel" name="tel" class=" form-control onlynum" readonly="readonly"/></td>
                            </tr>
                            <tr role="row">
                              <td align="right"><label>QQ号码:&nbsp;</label></td>
                              <td><input id="qq" name="qq" class="form-control onlynum"  readonly="readonly" /></td>
                              <td align="right"><label>邮箱:&nbsp;</label></td>
                              <td><input id="email" name="email" class="form-control"  readonly="readonly"/></td>
                            </tr>
                          </table>
                          <div align="center">
                          	<input id="updateinfo" class="btn btn-info" value="修改" type="button">&nbsp;&nbsp;
                            <input id="cancelupdate" class="btn btn-info" style="display: none;" value="取消" type="button">
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
<script type="text/javascript" src="static/js/commonjs/commonUploadFiles.js"></script>
<script type="text/javascript" src="static/js/jquery.datetimepicker.full.js"></script>
<script type="text/javascript" src="static/js/personalinfojs/personalinfo.js"></script>
<script type="text/javascript">
	$(function(){
		loadPersonalInfo();
	});
	function loadPersonalInfo(){
		if("${user.userimage}"==""){
			$("#userimage").prop("src","static/images/userimage/defaultUserImage.jpg");
			$("#userimage1").prop("src","static/images/userimage/defaultUserImage.jpg");
		}else{
			$("#userimage").prop("src","${sessionScope.user.userimage}");
			$("#userimage1").prop("src","${sessionScope.user.userimage}");
		}
		$("#username").val("${sessionScope.user.username}");
		$("input[name='sex'][value='${sessionScope.user.sex}']").prop("checked",true);
		$("#birthdate").val("${sessionScope.user.birthdate.toLocaleString().split(" ")[0]}");
		$("#tel").val("${sessionScope.user.tel}");
		$("#qq").val("${sessionScope.user.qq}");
		$("#email").val("${sessionScope.user.email}");
	}
	
	function seccessFunction(filename){
		$(".upinfo").css("color","green");
		$(".upinfo").text("上传成功！");
		$(".upinfo").show();
		$("#userimage").prop("src","static/images/userimage/${sessionScope.user.loginname}/"+filename);
	}
</script>
</body>
</html>