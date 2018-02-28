/**
 *权限管理 
 */
///页面加载后初始化zTree数据且默认展开所有节点
var zTreeobj;//ztree对象
$(function(){
	getAllowedPermission();
	$("#submitpermission").click(function(){
		submitpermission();
	});
});
//ztree设置
var setting = {
	check : {
		enable : true,
		nocheckInherit : false,
		chkStyle:"checkbox",
	},
	data : {
		simpleData : {
			enable: true,
			idKey: "id",
			pIdKey: "pid",
			rootPId: 0
		}
	}
//	callback : {
//		onCheck : zTreeOnCheck
//	}
};

function getAllowedPermission(){
	var url = document.URL;
	var roleid = url.substring(url.lastIndexOf("=")+1);
	//alert(roleid);
	$.ajax({
		type:"post",
		url:"resource/role/selAllowedPermission",
		data:{
			roleid:roleid
		},
		dataType:"json",
		success:function(ztreeNodes){
			if(ztreeNodes!=null&&ztreeNodes!=""){
				//初始化所有的树
				zTreeobj = $.fn.zTree.init($("#treeDemo"), setting, ztreeNodes);
				zTreeobj.expandAll(true);
			}
		},
		error:function(){
			swal("","系统异常，请稍后再试！","error");
		}
	});
}

function submitpermission(){
	var url = document.URL;
	var roleid = url.substring(url.lastIndexOf("=")+1);
	var checkset = zTreeobj.getCheckedNodes(true);
	//alert(checkset.length);
	var permissionids = "";
	for(var i=0;i<checkset.length;i++){
		if(checkset[i].id!="1"&&checkset[i].id!="2"&&checkset[i].id!=1&&checkset[i].id!=2){
			permissionids = permissionids + checkset[i].id + ",";
		}
	}
	permissionids = permissionids.substring(0,permissionids.length-1);
	//alert(permissionids);
	swal({
		  title: "",
		  text: "确认修改权限？",
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
				url:"resource/role/upRole",
				data:{
					id:roleid,
					permissionids:permissionids
				},
				dataType:"json",
				success:function(data){
					if(data.state==true){
						swal("","设置权限成功","success");
					}else{
						swal("","设置权限失败","error");
					}
				},
				error:function(){
					swal("","系统异常，请稍后再试！","error");
				}
			});
		  }
	});
}