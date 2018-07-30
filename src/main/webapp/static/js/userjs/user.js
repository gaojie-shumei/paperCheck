var usertable = mydataTable("#usershow");
$(function(){
	getAllowedUser();//在对应的页面中
//	$("#adduser").click(function(){
//		//跳转到添加用户界面
//		$("#right_content_main").load("./resource/page/adduser");
//	});
});

function getAllowedUser(){
	usertable.destroy();
	$("#usershow").children("tbody").html("");
	usertable = mydataTable("#usershow");
	$.ajax({
		type:"post",
		url:"resource/user/selAllowedUser",
		dataType:"json",
		success:function(data){
			var str = "";
			if(data!=null&&data!=""){
				str = str + "<a href='javascript:void(0)' class='btn  btn-info userdetail'>用户详情</a>";
				if(permission.search("user:update")!=-1){
				      str = str +"<shiro:hasPermission name='user:update'><a href='javascript:void(0)' class='btn btn-info upuser' style='color:white;'>修改</a></shiro:hasPermission>&nbsp;&nbsp;";
				}
				
				if(permission.search("user:delete")!=-1){
				      str = str +"<shiro:hasPermission name='user:delete'><a href='javascript:void(0)' class='btn btn-warning deluser' style='color:white;'>删除</a></shiro:hasPermission>";
				}
				
				$.each(data,function(i,n){
					usertable.row.add([
					      "<div id="+n.id+" style='width:240px;text-align:center;margin:0 auto;'>"+n.loginname+"</div>",
					      "<div style='width:120px;text-align:center;margin:0 auto;'>"+n.rolename+"</div>",
					      str
					]);
				});
				
			}
			usertable.draw(false);
			usertable.destroy();
			$(".upuser").off("click");
			$(".upuser").click(function(){
				upuser(this);
			});
			$(".deluser").off("click");
			$(".deluser").click(function(){
				deluser(this);
			});
			$(".userdetail").off("click");
			$(".userdetail").click(function(){
				userdetail($(this).parents("tr"))
			})
			usertable = mydataTable("#usershow");
			
			
		},
		error:function(){
			swal("","系统异常，请稍后再试！","error");
		}
	});
}

function userdetail(rowobj){
	var loginname = rowobj.children("td:first").children("div").text();
	$.ajax({
		type:"post",
		url:"resource/user/selUser",
		data:{
			loginname:loginname
		},
		dataType:"json",
		success:function(data){
			if(data!=null&&data!=""){
//				alert(data.userimage);
//				alert(data.userimage==null);
//				alert(data.userimage=="null");
//				alert(data.userimage=="");
				if(data.userimage==null||data.userimage=="null"||data.userimage==""){
					$("#userimage1").prop("src","static/images/userimage/defaultUserImage.jpg");
				}else{
					$("#userimage1").prop("src",data.userimage);
				}
				$("#username").val(data.username);
				$("input[name='sex'][value="+data.sex+"]").prop("checked",true);
				$("#birthdate").val(data.birthdate);
				$("#tel").val(data.tel);
				$("#qq").val(data.qq);
				$("#email").val(data.email);
				$("#callname").val(data.callname);
				$("#organization").val(data.organization);
				$('#userdetailmodal').modal({
					backdrop:false,
					keyboard:false
				});
			}
		},
		error:function(){
			swal("","系统异常，请稍后再试！","error");
		}
	});
}

function upuser(object){
	var trobj = $(object).parents("tr");
	var cellobj = trobj.children("td:eq(1)");
	var rolenamewithdiv = usertable.cell(cellobj).data();
	if($(object).text()=="修改"){
		 getAllowedRole(cellobj);
		 $(object).text("完成");
		 $(object).next("a").off("click");
		 $(object).next("a").text("取消");
		 $(object).next("a").click(function(){
			 cancelUpuser(object,rolenamewithdiv);
		 });
	}else if($(object).text()=="完成"){
		var rolename = cellobj.children("select").children("option:selected").text();
		var roleid = cellobj.children("select").children("option:selected").val();
//		alert(cellobj.children("select").children("option:selected")[0]);
		$.ajax({
			type:"post",
			url:"resource/user/upUser",
			data:{
				id:trobj.children("td:first").children("div").prop("id"),
				roleid:roleid
			},
			dataType:"json",
			success:function(data){
				if(data.state==true){
					swal("","修改用户成功","success");
					usertable.cell(cellobj).data("<div style='width:120px;text-align:center;margin:0 auto;'>"+rolename+"</div>");
				}else{
					swal("","修改用户失败","error");
					getAllowedUser();
				}
				$(object).text("修改");
				$(object).next("a").text("删除");
				$(object).next("a").off("click");
				$(object).next("a").click(function(){
					deluser(object);
				});
			},
			error:function(){
				swal("","系统异常，请稍后再试！","error");
			}
		});
	}
}

function cancelUpuser(object,rolenamewithdiv){
	var trobj = $(object).parents("tr");
	var cellobj = trobj.children("td:eq(1)");
	$(object).text("修改");
	$(object).next("a").off("click");
	$(object).next("a").text("删除");
	$(object).next("a").click(function(){
		deluser(object);
	});
	usertable.cell(cellobj).data(rolenamewithdiv);
}


function getAllowedRole(cellobj){
	var rolename = cellobj.children("div").text();
	//alert(rolename);
	$.ajax({
		type:"post",
		url:"resource/role/selAllowedRole",
		dataType:"json",
		success:function(data){
			if(data!=null&&data!=""){
				var str = "";
				str = str + "<select class='userselect' style='width:130px;'>";
				if(rolename=="普通用户"){
					str = str +"<option value='0' selected='selected'>普通用户</option>";
				}else{
					str = str +"<option value='0'>普通用户</option>";
				}
				$.each(data,function(i,n){
					if(n.rolename==rolename){
						str = str +"<option value="+n.id+" selected='selected'>"+n.rolename+"</option>";
					}else{
						str = str +"<option value="+n.id+">"+n.rolename+"</option>";
					}
				});
				str = str +"</select>";
				usertable.cell(cellobj).data(str);
				$(".userselect").select2();
			}
		},
		error:function(){
			swal("","系统异常，请稍后再试！","error");
		}
	});
}

function deluser(object){
	var trobj = $(object).parents("tr");
	var id = trobj.children("td:first").children("div").prop("id");
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
					url:"resource/user/delUser",
					data:{
						userid:id
					},
					dataType:"json",
					success:function(data){
						if(data!=null&&data!=""){
							if(data.state==true){
								swal("","删除用户成功","success");
								usertable.row(trobj).remove().draw(false);
							}else{
								swal("","删除用户失败","error");
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