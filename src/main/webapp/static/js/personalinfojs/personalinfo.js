$(function(){
	
	$("#updateinfo").click(function(){
		updateinfo(this);
	});
	$("#cancelupdate").click(function(){
		window.location.href = "./resource/page/index/personalinfo";
	});
	$(".onlynum").onlyNum();
	$(".onlynum").on("input propertychange change",function(){
		var str = "";
		var value = $(this).val();
		for (var i = 0; i < value.length; i++) {
			if(!isNaN(parseInt(value[i]))){
				str = str + value[i];
			}
		}
		$(this).val(str);
//		if($(this).val()!=""&&isNaN(parseInt($(this).val().substring($(this).val().length-1)))){
//			$(this).val($(this).val().substring(0,$(this).val().length-1));
//		}
	});
	
	$(".file_upload").change(function(){
//		alert(this.files.length);
		if(this.files.length!=0){
			for (var i = 0; i < this.files.length; i++) {
				var filename = this.files[i].name;
				//第一个function是上传之前调用的函数，第二个function是上传成功后调用的函数
				uploadUserImage(this.files[i],function(){
					$(".upinfo").css("color","red");
					$(".upinfo").text("正在上传，请稍候！");
					$(".upinfo").show();
					},function(){
						seccessFunction(filename);
				},function(){
					swal("","系统异常，请稍后再试","error");
				});
			}
		}
	});
});

function updateinfo(thisobj){
	if($(thisobj).val()=="修改"){
		$.datetimepicker.setLocale('ch');//设置中文
		$(".setdate").datetimepicker({
		      lang:"ch",           
		      formatDate:"Y-m-d",      //格式化日期
		      formatTime:"H:i",        //格式化时间
		      format:"Y-m-d",
		      timepicker:false,    //关闭时间选项
		      yearStart:new Date().getFullYear()-120,     //设置最小年份
		      yearEnd:new Date().getFullYear(),   //设置最大年份
		      todayButton:true,    //开启选择今天按钮 
		      theme:'dark',
		      step:1
		});
		$(".setdate").focus(function(){
			$(this).blur();
		});
		$("#callname").hide();
		$("#callnameselect").show();
		if($("#callname").val()!=""){
			$("#callnameselect").children("option[value="+$("#callname").val()+"]").prop("selected","selected");
			$("#callnameselect").children("option[value!="+$("#callname").val()+"]").prop("selected","");
		}
		$("#callnameselect").select2();
		$("#cancelupdate").show();
		$("input").prop("readonly",false);
		$("input").prop("disabled",false);
		$("#updateimage").show();
		$("#showimage").hide();
		$(thisobj).val("完成");
	}else{
		upuser(thisobj);
	}
}

function upuser(thisobj){
	var userimage = $("#userimage").prop("src").substring($("#userimage").prop("src").indexOf("static/"));
	var username = $("#username").val();
	var sex = $("input[name='sex']:checked").val();
	var birthdate = $("#birthdate").val();
	var tel = $("#tel").val();
	var qq = $("#qq").val();
	var email = $("#email").val();
	var callname = $("#callnameselect").children("option:selected").text();
	if(callname=="请选择"){
		callname = "";
	}
	var organization = $("#organization").val();
//	alert(userimage);
//	alert(username);
//	alert(sex);
//	alert(birthdate);
//	alert(tel);
//	alert(qq);
//	alert(email);
//	alert(callname);
//	alert(organization);
	if(validateEmailFormat(email)==true){
		$.ajax({
			type:"post",
			url:"resource/user/upPersonalInfo",
			data:{
				userimage:userimage,
				username:username,
				sex:sex,
				birthdate:birthdate,
				tel:tel,
				qq:qq,
				email:email,
				callname:callname,
				organization:organization
			},
			dataType:"json",
			success:function(data){
				if(data.state==true){
					swal("","修改个人资料成功！","success").then(function(){
						window.location.href = "./resource/page/index/personalinfo";
					});
					
				}else{
					swal("","修改个人资料失败！","error").then(function(){
						window.location.href = "./resource/page/index/personalinfo";
					});
				}
			},
			error:function(){
				swal("","系统异常，请稍后再试！","error");
			}
		});
	}
}


//----------------------------------------------------------------------
//<summary>
//限制只能输入数字,好像有些输入法还是没有被禁用
//</summary>
//----------------------------------------------------------------------
$.fn.onlyNum = function () {
 $(this).keypress(function (event) {
     var eventObj = event || e;
     var keyCode = eventObj.keyCode || eventObj.which;
     if ((keyCode >= 48 && keyCode <= 57))
         return true;
     else
         return false;
 }).focus(function () {
 //禁用输入法
     this.style.imeMode = 'disabled';
 }).bind("paste", function () {
 //获取剪切板的内容
     var clipboard = window.clipboardData.getData("Text");
     if (/^\d+$/.test(clipboard))
         return true;
     else
         return false;
 });
};

function validateEmailFormat(email){
	if(email!=""){
	  if(!email.match(/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/))
	  {
	   swal("","邮箱格式不正确！请重新输入","error");
	   return false;
	  }
	}
	return true;
}

