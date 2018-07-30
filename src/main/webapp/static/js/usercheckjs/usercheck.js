var userchecktable = mydataTable("#usercheckshow");
$(function(){
	getuserchecklist();
});


function getuserchecklist(){
	userchecktable.destroy();
	$("#usercheckshow").children("tbody").html("");
	userchecktable = mydataTable("#usercheckshow");
	
	$.ajax({
		type:"post",
		url:"resource/user/selUserCheck",
		dataType:"json",
		success:function(data){
			if(data!=null&&data!=""){
				$.each(data,function(i,n){
					var str = "";
					str = str + "<a href='javascript:void(0)' name="+n.loginname+" class='btn  btn-info userdetail'>用户详情</a>";//查询详细信息，并弹出模态框
					str = str + "<a href='Javascript:void(0)' name="+n.id+" class='btn btn-info checkuser'>通过</a>";
					str = str + "<a href='javascript:void(0);' name="+n.id+" class='btn btn-warning checkuser'>拒绝</a>";
					userchecktable.row.add([
					      n.loginname,
					      n.username,
					      n.organization,
					      n.tel,
					      str
					]);
				});
				
			}
			userchecktable.draw(false);
			userchecktable.destroy();
			$(".userdetail").off("click");
			$(".userdetail").click(function(){
				userdetail(this);
			});
			$(".checkuser").off("click");
			$(".checkuser").click(function(){
				checkuser(this,$(this).parents("tr"));
			});
			
			userchecktable = mydataTable("#usercheckshow");
		},
		error:function(f,e,w){
//			alert(e);
			swal("","系统异常，请稍后再试！","error");
		}
		
	});
}

function userdetail(jsObj){
	var loginname = $(jsObj).prop("name");
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



function checkuser(jsObj,rowobj){
	var status = 1;
	if($(jsObj).text()=="拒绝"){
		status = 2;
	}
	var userid = $(jsObj).prop("name");
	swal({
		  title: "",
		  text: "确认"+$(jsObj).text()+"？",
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
					url:"resource/user/upUserStatus",
					data:{
						id:userid,
						status:status
					},
					dataType:"json",
					success:function(data){
						if(data!=null&&data!=""){
							if(data.state==true){
								swal("","已"+$(jsObj).text(),"success").then(function(){
									userchecktable.row(rowobj).remove().draw(false);
								});
							}else{
								swal("","系统异常，请稍后再试！","error");
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