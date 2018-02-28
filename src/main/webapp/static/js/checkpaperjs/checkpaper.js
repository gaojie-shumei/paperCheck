$(function(){

//	$("#submitUpPaperForm").prop("type","button");
//	registerEvent();
	addValidateMethod();
	upPaperFormValidate();
	
	$("#upPaperForm #getpaperway a").click(function(){
		var text = $(this).html();
		$(this).addClass("selected");
		if(text=="粘贴文本"){
			$("#upPaperForm #getpaperway #selectedA").val(0);
			$("#upPaperForm #getpaperway a[name='upload']").removeClass("selected");
			$("#copyPapertext").css("display","block");
			$("#uploadPaper").css("display","none");
		}else if(text=="上传文档"){
			$("#upPaperForm #getpaperway #selectedA").val(1);
			$("#upPaperForm #getpaperway a[name='copy']").removeClass("selected");
			$("#copyPapertext").css("display","none");
			$("#uploadPaper").css("display","block");
		}
	});
	
	$("#upPaperForm .file_upload").change(function(){
//		alert(this.files.length);
		if(this.files.length!=0){
			var filenames = new Array();
			for (var i = 0; i < this.files.length; i++) {
				filenames[i] = this.files[i].name;
			}
			for (var i = 0; i < this.files.length; i++) {
				//第一个function是上传之前调用的函数，第二个function是上传成功后调用的函数
				uploadFiles(this.files[i],function(){
					beforesendFunction();
					},function(){
						seccessFunction(filenames);
				},function(){
					swal("","系统异常，请稍后再试","error");
				});
			}
		}
	});
});

/**
 * 增加验证方法
 */
function addValidateMethod(){
	$.validator.addMethod("atleastrequireOne",function(value,element){
		var flag = 1;
		var content1 = $("#upPaperForm textarea[name='content1']").val();
		var content2 = $("#upPaperForm textarea[name='content2']").val();
		if(content1==""&&content2==""){
			flag=0;
		}
		if(flag==1){
			return true;
		}else{
			return false;
		}
	},"必须选择一种方式上传文本内容");
	
	$.validator.addMethod("atleastrequireOneTitle",function(value,element){
		var flag = 1;
		var title1 = $("#upPaperForm #title").val();
		var title2 = $("#upPaperForm #title2").val();
		if(title1==""&&title2==""){
			flag=0;
		}
		if(flag==1){
			return true;
		}else{
			return false;
		}
	},"在非上传文件时不能为空");
}
/**
 * papercheck表单验证
 */
function upPaperFormValidate(){
	$("#upPaperForm").validate({
		rules:{
			title:{
				atleastrequireOneTitle:true
			},
			content1:{
				atleastrequireOne:true
			},
			content2:{
				atleastrequireOne:true
			}
		},
//		debug:true,//添加这个就可以使form表单不跳转
		messages:{
			title:{
				atleastrequireOneTitle:"在非上传文件时题目不能为空"
			},
			content1:{
				atleastrequireOne:"必须选择一种方式上传文本内容"
			},
			content2:{
				atleastrequireOne:"必须选择一种方式上传文本内容"
			}
		},
		errorElement:"span",
		errorClass:"validateerror",
		submitHandler:function(form){
			$(form).submit(function(){
				$(this).ajaxSubmit(function(data){
//					alert("not turn"+data);
//					$("#right_content_main").load("./"+data);//此控件在index.jsp中
					window.location.href = data;
				});
				return false;
			});
		}
	});
}

function beforesendFunction(){
	$("#uploadPaper .uploadtips").html("正在上传，请稍候！");
	$("#uploadPaper .uploadtips").css("color","red");
	$("#uploadPaper .uploadtips").addClass("col-lg-7");
	$("#uploadPaper .info").removeClass("col-lg-7");
	$("#uploadPaper .info").css("display","none");
	$("#uploadPaper .filename").css("display","none");
	$("#uploadPaper .uploadtips").css("display","inline");
	$("#uploadPaper .uploadtips").css("height","50px");
	$("#uploadPaper .uploadtips").css("line-height","50px");
}

//filenames是一个文件名数组,在普通用户页面只有一个元素
function seccessFunction(filenames){
	//执行一个ajax，提取上传文件的内容并显示在对应的textarea里面
	$.ajax({
		type:"post",
		url:"resource/extract/extractTxt",
		data:{
			filename:filenames[0]
		},
		dataType:"json",
		success:function(data){
			$("#uploadPaper textarea").val(data.content);
			$("#uploadPaper .uploadtips").html("");
			$("#uploadPaper .uploadtips").removeClass("col-lg-7");
			$("#uploadPaper .info").removeClass("col-lg-7");
			$("#uploadPaper .info").css("display","none");
			$("#uploadPaper .uploadtips").css("display","none");
			$("#uploadPaper .filename").css("height","50px");
			$("#uploadPaper .filename").css("line-height","50px");
			$("#uploadPaper .filename").addClass("col-lg-7");
			$("#uploadPaper .filename").html(filenames[0]);
			$("#uploadPaper .filename").css("display","inline");
			$("#uploadPaper #title2").val(filenames[0]);
//			$("#submitUpPaperForm").prop("type","submit");
		},
		error:function(){
			swal("","系统异常，请稍后再试！","error");
		}
	});
}