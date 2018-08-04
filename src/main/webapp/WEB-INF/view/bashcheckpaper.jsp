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
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Insert title here</title>

		<link rel="stylesheet" type="text/css" href="static/css/sweet-alert.css" />
		<link rel="stylesheet" type="text/css" href="static/css/bootstrap.min.css" />
		<link rel="stylesheet" type="text/css" href="static/css/bootstrap-theme.min.css" />
		<link rel="stylesheet" type="text/css" href="static/css/tipsy.css" />
		<!-- <link rel="stylesheet" type="text/css" href="static/css/checkpapercss/checkpaper.css" /> -->
		<link rel="stylesheet" type="text/css" href="static/css/style.css" />
        <link rel="stylesheet" type="text/css" href="static/css/datatables.min.css" />
		<style type="text/css">
		   	/*验证出错样式*/
			span.validateerror{
				color:red;
				border-color: red; 
			}
			th, td {
				text-align: center;
				margin: 0px;
				padding: 0px;
			}
			#upPaperForm .file_upload_btn {
			    border: 1px solid #d8d8d8;
			    padding: 0 29px;
			    float: left;
			    font-size: 16px;
			    color: #505961;
			    border-radius: 2px;
			    -moz-border-radius: 2px;
			    -webkit-border-radius: 2px;
			    -ms-border-radius: 2px;
			  /*   margin-right: 10px; */
			    height: 50px;
			    text-align: center;
			    line-height: 50px;
			}
			
			#upPaperForm .file_upload{
			     height: 50px;
			    text-align: center;
			    line-height: 50px;
			    opacity: 0;
			    cursor: pointer;
			  /*   margin: 0;
			    padding: 0;
			    top: 0;
			    left: 0; */
			    position: absolute;
			    filter: alpha(opacity=0);
			}
		</style>
</head>
<body  class="white-bg">
	<!-- 普通用户查重界面代码 -->
               		<div id="checkpaper_page" class="col-lg-12">
               			<div id="papeTitle" style="text-align: center;">
               				<strong style="text-align: center;font-size: 20px;font-weight: 30px;margin-bottom: 20px;">批量提交文章</strong>
               			</div>
               			<form id="upPaperForm" class="form-horizontal" role="form" method="post" action="resource/checkdoc/bashcheckpaper">
                            <!-- 方式start -->
                            <div class="form-group"> 
                               <div id="uploadPaper" class="col-lg-offset-2 col-lg-10" style="margin-top: 15px;" data-log="upload">
                               		<span class="file_upload_btn col-lg-3">上传文档</span>
                               		<input type="file" class="file_upload col-lg-3" name="paper_file" multiple="multiple" accept="text/plain,application/pdf,application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document" value="上传文档">
                               		<span class="col-lg-7 info">可上传多个文件，支持txt、doc、docx、pdf格式，上限15M.
                               			<br />
                               			<em>上传内容建议去除封面、目录、附录，仅上传正文。</em>
                               		</span>
                               		<div class="col-lg-10" style="height: 15px;"></div>
                               		<div class="col-lg-12">
                               		<!-- 显示上传的文章 -->
										<table  id="uploadfilesshow" class="table  table-striped table-bordered table-hover dataTable table-condensed">
											<thead>
												<tr role="row">
													<th>文章名</th>
													<th>文章大小</th>
													<th>上传状态</th>
												</tr>
											</thead>
											<tbody>
												<!-- 通过ajax获取后台数据并填充 -->
											</tbody>
										</table>
									</div>
									
                               		<input id="filenames" name="filenames"  class="form-control" type="hidden" />
                               </div>
                            </div>
                            <!-- 方式end -->
                            <!-- 选择查重算法start -->
                            		<!-- 目前只有一种算法，不用选择 -->
                            <!-- 选择查重算法end -->
                            <div class="form-group">
								<div class="col-lg-offset-6 col-lg-6">
									<button id="submitUpPaperForm" type="submit" class="btn btn-info">开始查重</button>
								</div>
							</div>
                           </form>
               		</div>
               		<div id="notknowElement"></div>
               		
	<script type="text/javascript" src="static/js/jquery.js" ></script>
	<script type="text/javascript" src="static/js/jquery.form.js" ></script>
	<script type="text/javascript" src="static/js/datatables.min.js"></script>
	<script type="text/javascript" src="static/js/mydataTable.js"></script>
  	<script type="text/javascript" src="static/js/bootstrap.min.js" ></script>
  	<script type="text/javascript" src="static/js/sweet-alert.min.js"></script>
  	<script type="text/javascript" src="static/js/security_url_js/security_url.js"></script>
  	<script type="text/javascript" src="static/js/commonjs/commonUploadFiles.js""></script>
  	<script type="text/javascript" src="static/js/checkpaperjs/bashcheckpaper.js"></script>
</body>
</html>