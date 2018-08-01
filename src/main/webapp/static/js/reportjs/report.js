var reporttable = mydataTable("#reportshow");
var timeout = 0;
$(function(){
	getReports();
});

function getReports(){
	reporttable.destroy();
	$("#reportshow").children("tbody").html("");
	reporttable = mydataTable("#reportshow");
	$.ajax({
		type:"post",
		url:"resource/checkdoc/showReports",
		dataType:"json",
		success:function(data){
//			alert(data);
//			alert(data.length);
			if(data!=null&&data!=""){
				
				$.each(data,function(i,n){
					var str = "";
					if(n.state=="检测完成"&&n.usefullife=="15天"){
						str = str + "<a href='./"+n.downloadPath+"' download='"+n.title+".html' class='btn  btn-info'>下载</a>";
						str = str + "<a href='./"+n.downloadPath+"' class='btn btn-info' target='_blank'>在线查看</a>";
						str = str + "<a href='javascript:void(0);' id="+n.guid+" name="+n.downloadPath+" class='btn btn-warning deleterecord'>删除</a>";
					}else if(n.state=="检测失败"||(n.state=="检测完成"&&n.usefullife!="15天")){
						str = str + "<a href='javaScript:void(0);' class='btn btn-preventDefault  disabled'>下载</a>";
						str = str + "<a href='javaScript:void(0);' class='btn btn-preventDefault  disabled'>在线查看</a>";
						str = str + "<a href='javascript:void(0);' id="+n.guid+" name="+n.downloadPath+" class='btn btn-warning deleterecord'>删除</a>";
					}else{
						str = str + "<a href='javaScript:void(0);' class='btn btn-preventDefault flushThis  disabled'>下载</a>";
						str = str + "<a href='javaScript:void(0);' class='btn btn-preventDefault  disabled'>在线查看</a>";
						str = str + "<a href='javascript:void(0);' id="+n.guid+" name="+n.downloadPath+" class='btn btn-preventDefault disabled'>删除</a>";
					}
					reporttable.row.add([
					      "<div id="+n.guid+" style='width:240px;text-align:center;margin:0 auto;'>"+n.title+"</div>",
					      "<div style='width:120px;text-align:center;margin:0 auto;'>"+n.algorithm+"</div>",
					      n.state,
					      n.checktime,
					      n.xsl,
					      n.usefullife,
					      str
					]);
				});
				
			}
			reporttable.draw(false);
			reporttable.destroy();
			$(".flushThis").on("flushThis",function(){
				var trobj = $(this).parents("tr");
				var guid = trobj.children("td:first").children("div").prop("id");
				var state = trobj.children("td:eq(2)").text();
//				console.log("flushThis执行啦");
				if(state=="检测中"){
					getReportByGuid(guid,trobj);
				}
			});
			$(".flushThis").trigger("flushThis");
			//刷新功能
//			var trarr = $("#reportshow tbody").children("tr");
//			console.log("trarr.length="+trarr.length);
//			for (var i = 0; i < trarr.length; i++) {
//				var trobj = $(trarr[i]);
//				var guid = trobj.children("td:first").children("div").prop("id");
//				var state = trobj.children("td:eq(2)").text();
//				if(state=="检测中"){
//					getReportByGuid(guid,trobj);
//				}
//			}
//			console.log("flushThis执行啦");
			
			$(".deleterecord").off("click");
			$(".deleterecord").click(function(){
				//alert("deleterecord");
				//删除数据库中对应的记录，并删掉对应的报告
				deleterecord($(this));
			});
			reporttable = mydataTable("#reportshow");
		},
		error:function(f,e,w){
//			alert(e);
			swal("","系统异常，请稍后再试！","error");
		}
	});
}

function getReportByGuid(guid,trobj){
	$.ajax({
		type:"post",
		url:"resource/checkdoc/getReportByGuid",
		dataType:"json",
		data:{
			guid:guid
		},
		success:function(n){
			reporttable.destroy();
			if(n!=null&&n!=""){
				if(n.state=="检测中"){
					timeout = setTimeout(function(){
						getReportByGuid(guid,trobj);
					},10000);
				}else{
					clearTimeout(timeout);
					var str = "";
					if(n.state=="检测完成"&&n.usefullife=="15天"){
						str = str + "<a href='./"+n.downloadPath+"' download='"+n.title+".html' class='btn  btn-info'>下载</a>";
						str = str + "<a href='./"+n.downloadPath+"' class='btn btn-info' target='_blank'>在线查看</a>";
						str = str + "<a href='javascript:void(0);' id="+n.guid+" name="+n.downloadPath+" class='btn btn-warning deleterecord'>删除</a>";
					}else if(n.state=="检测失败"||(n.state=="检测完成"&&n.usefullife!="15天")){
						str = str + "<a href='javaScript:void(0);' class='btn btn-preventDefault  disabled'>下载</a>";
						str = str + "<a href='javaScript:void(0);' class='btn btn-preventDefault  disabled'>在线查看</a>";
						str = str + "<a href='javascript:void(0);' id="+n.guid+" name="+n.downloadPath+" class='btn btn-warning deleterecord'>删除</a>";
					}else{
						str = str + "<a href='javaScript:void(0);' class='btn btn-preventDefault flushThis  disabled'>下载</a>";
						str = str + "<a href='javaScript:void(0);' class='btn btn-preventDefault  disabled'>在线查看</a>";
						str = str + "<a href='javascript:void(0);' id="+n.guid+" name="+n.downloadPath+" class='btn btn-preventDefault disabled'>删除</a>";
					}
					trobj.children("td:eq(0)").html("<div id="+n.guid+" style='width:240px;text-align:center;margin:0 auto;'>"+n.title+"</div>");
					trobj.children("td:eq(1)").html("<div style='width:120px;text-align:center;margin:0 auto;'>"+n.algorithm+"</div>");
					trobj.children("td:eq(2)").html(n.state);
					trobj.children("td:eq(3)").html(n.checktime);
					trobj.children("td:eq(4)").html(n.xsl);
					trobj.children("td:eq(5)").html(n.usefullife);
					trobj.children("td:eq(6)").html(str);
					
					$(".deleterecord").off("click");
					$(".deleterecord").click(function(){
						//alert("deleterecord");
						//删除数据库中对应的记录，并删掉对应的报告
						deleterecord($(this));
					});
				}
			}
			reporttable = mydataTable("#reportshow");
		},
		error:function(){
			swal("","系统异常，请稍后再试！","error");
		}
	});
}

function deleterecord(jqueryObject){
	var jqueryParentRow = jqueryObject.parents("tr");
	var guid = jqueryObject.prop("id");
	var downloadPath = jqueryObject.prop("name");
	swal({
		  title: "",
		  text: "确认删除？",
		  type: "warning",
		  showCancelButton: true,
		  confirmButtonColor: "#DD6B55",
		  confirmButtonText: "确定",
		  cancelButtonText: "取消",
//		  closeOnConfirm: false,
//		  closeOnCancel: false
	}).then(function(isConfirm) {
		  if (isConfirm) {
			  $.ajax({
					type:"post",
					url:"resource/checkdoc/delRecord",
					data:{
						guid:guid,
						downloadPath:downloadPath
					},
					dataType:"json",
					success:function(data){
						if(data!=null&&data!=""){
							if(data.state=="success"){
								swal("","删除成功","success").then(function(){
									reporttable.row(jqueryParentRow).remove().draw(false);
								});
							}else{
								swal("","删除失败","success");
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

//function downloadFile(path){
//	$.ajax({
//		type:"post",
//		url:"resource/downloadFile/download",
//		data:{
//			realpath:path
//		},
//		da
//	});
//}