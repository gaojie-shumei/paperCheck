var uploadfilestable = mydataTable("#uploadfilesshow");
var filecount = 0;
var uploadfilestablerowcount = 0;
$(function(){
//	$("#submitUpPaperForm").prop("type","button");
	$("#submitUpArticleForm").addClass("disabled");
//	registerEvent();
//	addValidateMethod();
//	upPaperFormValidate();	
	$("#upArticleForm .file_upload").change(function(){
//		alert(this.files.length);
		if(this.files.length!=0){
//			alert(this.files.length);
			filecount = this.files.length;
			uploadfilestablerowcount = uploadfilestable.rows().count();
			for (var i = 0; i < this.files.length; i++) {
				var file = this.files[i];
				var rowcount = i;
				var uploadflag = 1;
				for(var j=0;j<uploadfilestable.rows().count();j++){
					if(uploadfilestable.cell(j,0).data()==file.name){
						uploadflag = 0;
					}
				}
				if(uploadflag==1){
					$("#submitUpArticleForm").addClass("disabled");
					rowcount = rowcount-(this.files.length-filecount);
//					alert(uploadfilestable.columns(0).search(file.name).cell(0,0).data());
					//第一个function是上传之前调用的函数，第二个function是上传成功后调用的函数,第三个function是上传失败后调用的函数
					uploadFiles(this.files[i],function(){
							beforesendFunction(file);
						},function(){
							seccessFunction(rowcount,file.name);
					},function(){
						errorFunction(rowcount,file.name);
					});
				}else{
					filecount = filecount-1;
				}
			}
		}
	});
	
	$("#upArticleForm").submit(function(e){
		e.preventDefault();
		$(this).ajaxSubmit(function(data){
//			alert("not turn "+data);
//			$("#right_content_main").load("./"+data);//此控件在index.jsp中
//			window.location.href=data;
//			alert(data);
			if(data=="success"){
				swal("","更新库文档成功！","success");
				uploadfilestable.destroy();
				$("#uploadfilesshow").children("tbody").html("");
				uploadfilestable = mydataTable("#uploadfilesshow");
			}else{
				swal("","更新库文档失败！","error");
			}
		});
		return false;
	});
});


function beforesendFunction(file){
//	alert(file.name);
	var str = "";
	if(file.size/1024/1024>=1){
		var num = file.size/1024/1024;
		str = num.toFixed(2)+"MB";
	}else if(file.size/1024>=1){
		var num = file.size/1024;
		str = num.toFixed(2)+"KB";
	}else{
		var num = file.size;
		str = num.toFixed(2)+"B";
	}
	uploadfilestable.row.add([
	     file.name,
	     str,
	     "<div style='text-align:center;margin:0 auto;color:pink'>正在上传，请稍候！</div>"
	]);
	uploadfilestable.columns(0).search("").draw(false);
}

//filename是当前用户上传文件的文件名
function seccessFunction(rowcount,filename){
	uploadfilestable.cell(uploadfilestablerowcount+rowcount,2).data("<div style='text-align:center;margin:0 auto;color:green'>上传成功！</div>").draw(false);
	if($("#filenames").val()==""){
		$("#filenames").val(filename);
	}else{
		$("#filenames").val($("#filenames").val()+":"+filename);
	}
//	alert($("#filenames").val());
	if(rowcount==filecount-1){
		$("#submitUpArticleForm").removeClass("disabled");
	}
}

//filename是当前用户上传文件的文件名
function errorFunction(rowcount,filename){
	uploadfilestable.cell(uploadfilestablerowcount+rowcount,2).data("<div style='text-align:center;margin:0 auto;color:red'>上传失败！</div>").draw(false);
	if(rowcount==filecount-1){
		$("#submitUpArticleForm").removeClass("disabled");
	}
}