var roletable = mydataTable("#roleshow");
$(function(){
	getAllowedRole();//在对应页面中
	$("#addrole").click(function(){
		addrole(this);
	});
});


function getAllowedRole(){
	roletable.destroy();
	$("#roleshow").children("tbody").html("");
	roletable = mydataTable("#roleshow");
	$.ajax({
		type:"post",
		url:"resource/role/selAllowedRole",
		dataType:"json",
		success:function(data){
			var str = "";
			if(permission.search("permission:set")!=-1){
			      str = str +"<shiro:hasPermission name='permission:set'><a href='javascript:void(0)' class='btn btn-info setpermission' style='color:white;'>设置权限</a></shiro:hasPermission>&nbsp;&nbsp;";
			}
			
			if(permission.search("role:update")!=-1){
			      str = str +"<shiro:hasPermission name='role:update'><a href='javascript:void(0)' class='btn btn-info uprole' style='color:white;'>修改</a></shiro:hasPermission>&nbsp;&nbsp;";
			}
			
			if(permission.search("role:delete")!=-1){
			      str = str +"<shiro:hasPermission name='role:delete'><a href='javascript:void(0)' class='btn btn-warning delrole' style='color:white;'>删除</a></shiro:hasPermission>";
			}
			if(data!=null&&data!=""){
				$.each(data,function(i,n){
					roletable.row.add([
					      "<div id="+n.id+" style='width:240px;text-align:center;margin:0 auto;'>"+n.rolename+"</div>",
					      str
					]);
					$("#roleshow tbody").children("tr:eq("+(i+1)+")").prop("name","existrole");
				});
			}
			$(".uprole").off("click");
			$(".uprole").click(function(){
				uprole(this);
			});
			$(".delrole").off("click");
			$(".delrole").click(function(){
				delrole(this);
			});
			$(".setpermission").off("click");
			$(".setpermission").click(function(){
				//跳转到设置权限页面
				window.location.href = "./resource/page/index/setpermission?roleid="+$(this).parents("tr").children("td:eq(0)").find("div").prop("id");
			});
			roletable.draw(false);
		},
		error:function(){
			swal("","系统异常，请稍后再试！","error");
		}
	});
}

function addrole(object){
	if($(object).val()=="添加角色"){
		$(object).val("取消添加");
		
		var str = "";
		if(permission.search("permission:set")!=-1){
		      str = str +"<shiro:hasPermission name='permission:set'><a href='javascript:void(0)' class='btn btn-info setpermission' style='color:white;display:none;'>设置权限</a></shiro:hasPermission>&nbsp;&nbsp;";
		}
		
		if(permission.search("role:update")!=-1){
		      str = str +"<shiro:hasPermission name='role:update'><a href='javascript:void(0)' class='btn btn-info uprole' style='color:white;'>完成</a></shiro:hasPermission>&nbsp;&nbsp;";
		}
		
		if(permission.search("role:delete")!=-1){
		      str = str +"<shiro:hasPermission name='role:delete'><a href='javascript:void(0)' class='btn btn-warning delrole' style='color:white;display:none;'>删除</a></shiro:hasPermission>";
		}
		
		roletable.row.add([
		      "<input id='-1'>",
		      str
		]);
		
		$(".uprole").off("click");
		$(".uprole").click(function(){
			uprole(this);
		});
		$(".delrole").off("click");
		$(".delrole").click(function(){
			delrole(this);
		});
		$(".setpermission").off("click");
		$(".setpermission").click(function(){
			//跳转到设置权限页面
			window.location.href = "./resource/page/index/setpermission?roleid="+$(this).parents("tr").children("td:eq(0)").find("div").prop("id");
		});
		roletable.draw(false);
	}else{
		$(object).val("添加角色");
		roletable.destroy();
		var trobj = $("#roleshow tbody").children("tr[name!='existrole']");
		roletable = mydataTable("#roleshow");
		roletable.row(trobj).remove().draw(false);
	}
	
}



function uprole(object){
	var trobj = $(object).parents("tr");
	var cellobj = trobj.children("td:eq(0)");
	var rolenamewithdiv = roletable.cell(cellobj).data();
	if($(object).text()=="修改"){
		 //getAllowedRole(cellobj);
		 roletable.cell(cellobj).data("<input id="+cellobj.children("div").prop("id")+" value="+cellobj.children("div").text()+">");
		 $(object).text("完成");
		 //$(object).prev("a").hide();
		 trobj.find("a[class*='setpermission']").hide();
		 trobj.find("a[class*='delrole']").off("click");
		 trobj.find("a[class*='delrole']").text("取消");
		 trobj.find("a[class*='delrole']").click(function(){
			 cancelUprole(object,rolenamewithdiv);
		 });
	}else if($(object).text()=="完成"){
//		alert(cellobj.children("select").children("option:selected")[0]);
		validateAndupRolename(object);
	}
}

function cancelUprole(object,rolenamewithdiv){
	var trobj = $(object).parents("tr");
	var cellobj = trobj.children("td:eq(0)");
	$(object).text("修改");
	trobj.find("a[class*='setpermission']").show();
	trobj.find("a[class*='delrole']").off("click");
	trobj.find("a[class*='delrole']").text("删除");
	trobj.find("a[class*='delrole']").click(function(){
		delrole(object);
	});
	roletable.cell(cellobj).data(rolenamewithdiv);
}

//验证该rolename是否有效，是否可以修改为该rolename,可以则进行修改
function validateAndupRolename(object){
	//var state = "success";
	var trobj = $(object).parents("tr");
	var cellobj = trobj.children("td:eq(0)");
	var rolename = cellobj.children("input").val();
	var roleid = cellobj.children("input").prop("id");
	if(rolename.trim()==""){
		swal("","角色名称不能为空","error");
		cellobj.children("input").val("");
	}else{
		$.ajax({
			type:"post",
			url:"resource/role/selRoleByRoleName",
			data:{
				id:roleid,
				rolename:rolename
			},
			dataType:"json",
			success:function(data){
				if(data.hasrole==false){
					ajaxUpRole(object);
				}else{
					swal("","该角色已存在！","error");
				}
			},
			error:function(){
				swal("","系统异常，请稍后再试！","error");
			}
		});
	}
}

function ajaxUpRole(object){
	var trobj = $(object).parents("tr");
	var cellobj = trobj.children("td:eq(0)");
	var rolename = cellobj.children("input").val();
	var roleid = cellobj.children("input").prop("id");
	$.ajax({
		type:"post",
		url:"resource/role/upRole",
		data:{
			id:roleid,
			rolename:rolename
		},
		dataType:"json",
		success:function(data){
			if(data.state==true){
				if(roleid==-1){
					swal("","添加角色成功","success");
				}else{
					swal("","修改角色成功","success");
				}
				roletable.cell(cellobj).data("<div id="+roleid+" style='width:120px;text-align:center;margin:0 auto;'>"+rolename+"</div>");
				trobj.prop("name","existrole");
			}else{
				if(roleid==-1){
					swal("","添加角色失败","error");
				}else{
					swal("","修改角色失败","error");
				}
				getAllowedRole();
			}
			$(object).text("修改");
			trobj.find("a[class*='delrole']").text("删除");
			trobj.find("a[class*='setpermission']").show();
			trobj.find("a[class*='delrole']").show();
			trobj.find("a[class*='delrole']").off("click");
			$("#addrole").val("添加角色");
			trobj.find("a[class*='delrole']").click(function(){
				delrole(object);
			});
		},
		error:function(){
			swal("","系统异常，请稍后再试！","error");
		}
	});
}

function delrole(object){
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
					url:"resource/role/delRole",
					data:{
						roleid:id
					},
					dataType:"json",
					success:function(data){
						if(data!=null&&data!=""){
							if(data.state==true){
								swal("","删除角色成功","success");
								roletable.row(trobj).remove().draw(false);
							}else{
								swal("","删除角色失败","error");
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