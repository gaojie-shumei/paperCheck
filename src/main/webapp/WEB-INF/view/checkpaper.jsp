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
		<link rel="stylesheet" type="text/css" href="static/css/checkpapercss/checkpaper.css" />

		<style type="text/css">
		   	/*验证出错样式*/
			span.validateerror{
				color:red;
				border-color: red; 
			}
		</style>
</head>
<body  class="white-bg">
	<!-- 普通用户查重界面代码 -->
               		<div id="checkpaper_page" class="col-lg-12">
               			<div id="papeTitle" style="text-align: center;">
               				<strong style="text-align: center;font-size: 20px;font-weight: 30px;margin-bottom: 20px;">提交文章</strong>
               			</div>
               			<br/><br/>
               			<form id="upPaperForm" class="form-horizontal" role="form" method="post" action="resource/checkdoc/checkpaper">
                			<div class="form-group"> 
                               <label for="title" class="col-lg-2 control-label">题目</label>
                               <div class="col-lg-10">
                               		<input id="title" name="title" class="form-control" type="text" placeholder="最多输入30个字" maxlength="30"/>
                               </div>
                            </div>
                            <div class="form-group"> 
                               <label for="author" class="col-lg-2 control-label">作者</label>
                               <div class="col-lg-10">
                               		<input id="author" name="author"  class="form-control" type="text" placeholder="最多输入15个字，多个作者用半角逗号隔开" maxlength="15"/>
                               </div>
                            </div>
                            <!-- 方式start -->
                            <div class="form-group"> 
                               <label class="col-lg-2 control-label"><i style="color:red;">*</i>方式</label>
                               <div id="getpaperway" class="col-lg-10">
                               		<a href="javaScript:void(0);" name="copy" class="selected">粘贴文本</a> 
                               		<a href="javaScript:void(0);" name="upload">上传文档</a>
                               		<input id="selectedA" name="selectedA"  class="form-control" type="hidden" value="0" />
                               </div>
                               <div id="copyPapertext" class="col-lg-offset-2 col-lg-10" style="display: block;margin-top: 15px;">
                               		<textarea name="content1" class="form-control" style="margin-bottom: 10px;" 
                               				placeholder="内容字数1000字~10万字。提交内容建议去除封面、目录、附录，仅提交正文。" 
                               		maxlength="300000"></textarea>
                               </div>
                               <div id="uploadPaper" class="col-lg-offset-2 col-lg-10" style="display:none;margin-top: 15px;" data-log="upload">
                               		<span class="file_upload_btn col-lg-3">上传文档</span>
                               		<input type="file" class="file_upload col-lg-3" name="paper_file" accept="text/plain,application/pdf,application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document" value="上传文档">
                               		<span class="filename" style="display: none"></span>
                               		<span class="uploadtips" style="display: none"></span>
                               		<span class="col-lg-7 info">支持txt、doc、docx、pdf格式，上限15M.
                               			<br />
                               			<em>上传内容建议去除封面、目录、附录，仅上传正文。</em>
                               		</span>
                               		<div class="col-lg-10" style="height: 15px;"></div>
                               		<textarea name="content2" class="form-control" style="margin-bottom: 10px;" maxlength="300000" readonly="readonly"></textarea>
                               		<input id="title2" name="title2"  class="form-control" type="hidden" />
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
    <div name="scriptdiv">
		<script type="text/javascript" src="static/js/jquery.js" ></script>
	  	<script type="text/javascript" src="static/js/jquery.form.js" ></script>
	  	<script type="text/javascript" src="static/js/jquery.validate.min.js"></script>
	  	<script type="text/javascript" src="static/js/bootstrap.min.js" ></script>
	  	<script type="text/javascript" src="static/js/sweet-alert.min.js"></script>
	  	<script type="text/javascript" src="static/js/security_url_js/security_url.js"></script>
	  	<script type="text/javascript" src="static/js/commonjs/commonUploadFiles.js""></script>
	  	<script type="text/javascript" src="static/js/checkpaperjs/checkpaper.js"></script>
  	</div>
</body>
</html>