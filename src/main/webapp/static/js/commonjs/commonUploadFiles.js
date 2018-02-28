/*通用上传文件*/
function uploadFiles(object,beforesendFunction,successFunction,errorFunction){
	//object是file对象，要上传的文件
	//count表示是此次循环中第几次上传文件
	//beforesendFunction是在上传之前调用的函数
	//successFunction是上传成功后调用的函数
//	var filearr = object[0].files;
	var formData = new FormData();
//	for (var i = 0; i < filearr.length; i++) {
////		alert(filearr[i].name);
//		formData.append("files",filearr[i]);
//	}
	formData.append("files",object);
	$.ajax({ 
		url : "resource/upload/fileUpLoad", 
		type : 'POST',
		async:false,
		data : formData,
		dataType:"json",
		// 告诉jQuery不要去处理发送的数据
		processData : false, 
		// 告诉jQuery不要去设置Content-Type请求头
		contentType : false,
		beforeSend:function(){
			//console.log("正在进行，请稍候");
			beforesendFunction();
		},
		success : function(data) {
			if(data!=null&&data!=""){
				if(data.state==true){
					successFunction();
				}else{
					errorFunction();
				}
			}else{
				errorFunction();
			}
		}, 
		error : function() { 
			errorFunction();
		} 
	});
}


function uploadUserImage(object,beforesendFunction,successFunction,errorFunction){
	//object是file对象，要上传的文件
	//count表示是此次循环中第几次上传文件
	//beforesendFunction是在上传之前调用的函数
	//successFunction是上传成功后调用的函数
//	var filearr = object[0].files;
	var formData = new FormData();
//	for (var i = 0; i < filearr.length; i++) {
////		alert(filearr[i].name);
//		formData.append("files",filearr[i]);
//	}
	formData.append("files",object);
	$.ajax({ 
		url : "resource/upload/uploadUserImage", 
		type : 'POST',
		async:false,
		data : formData,
		dataType:"json",
		// 告诉jQuery不要去处理发送的数据
		processData : false, 
		// 告诉jQuery不要去设置Content-Type请求头
		contentType : false,
		beforeSend:function(){
			//console.log("正在进行，请稍候");
			beforesendFunction();
		},
		success : function(data) {
			if(data!=null&&data!=""){
				if(data.state==true){
					successFunction();
				}else{
					errorFunction();
				}
			}else{
				errorFunction();
			}
		}, 
		error : function() { 
			errorFunction();
		} 
	});
}



function removeFiles(){
	$.ajax({
		type:"post",
		url:"resource/upload/fileRemove",
		dataType:"json",
		success:function(data){
//			alert(data.state);
		},
		error:function(){
			swal("","系统异常，请稍后再试！","error");
		}
	});
}