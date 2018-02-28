var timeout = 0;
$(function(){
	registerEvent();
//	addValidateMethod();
//	upPaperFormValidate();
//	$("#right_content_main").load("./resource/page/checkpaper");
	removeFiles();
	var url = document.URL;
	if(url.search("/page/index")!=-1&&url.search("/page/index/")==-1){
		$("#right_content_main").load("./resource/page/checkpaper");
	}else{
		$("#right_content_main").load("./resource/page/"+url.substr(url.indexOf("/page/index/")+12));
	}
});




function registerEvent(){
	$("#user_pic").hover(function(){
			clearTimeout(timeout);
			$("#userMenu.menuDropList").css("display","block");
	//		$("#user_pic").tipsy();
			//$("#userMenu.menuDropList").css("left",document.width);
		},function(){
			timeout = setTimeout(function(){$("#userMenu.menuDropList").css("display","none");},500);
	});
	$(".usermenu a").hover(function(){
			clearTimeout(timeout);
			$("#userMenu.menuDropList").css("display","block");
			$(this).css("background","#ccc");
		},function(){
			$(this).css("background","white");
			timeout = setTimeout(function(){$("#userMenu.menuDropList").css("display","none");},500);
	});
	
	$("#left_menu_content .left_menu_logo").hover(function(){
			$("#wrapper").addClass("leftNavOn");
		},function(){
	});
	
	$("#left_menu_content").hover(function(){},function(){
		$("#wrapper").removeClass("leftNavOn");
	});
	
	$(".delloginUser").click(function(e){
		e.preventDefault();
		swal({
			  title: "",
			  text: "确认注销账户？",
			  type: "warning",
			  showCancelButton: true,
			  confirmButtonColor: "#DD6B55",
			  confirmButtonText: "确定",
			  cancelButtonText: "取消",
//			  closeOnConfirm: false,
//			  closeOnCancel: false
		}).then(function(isConfirm) {
			  if (isConfirm) {
				  $.ajax({
						type:"post",
						url:"resource/user/delloginUser",
						dataType:"json",
						success:function(data){
							if(data!=null&&data!=""){
								if(data.state==true){
									swal("","注销用户成功!","success");
//									alert(data.href);
									window.location.href="./"+data.href;
								}else{
									swal("","系统异常,注销用户失败!","error");
								}
							}
						},
						error:function(){
							swal("","系统异常，请稍后再试！","error");
						}
					});
			  }
		});
	});
	
//	/**
//	 * 为菜单注册点击事件
//	 */
//	$(".main_menu_wr .menu_listitem a").click(function(e){
//		e.preventDefault();
//		var url = this.href;
//		$("#right_content_main").load(url);
//	});
}

