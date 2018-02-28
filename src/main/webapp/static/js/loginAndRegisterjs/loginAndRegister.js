$(function(){
	jQueryFormat();
	LoginFormValidate();
	addValidateMethod();
	SignUpFormValidate();
//	alertErrorMsg();
});
/**
 * 为jquery增加format方法
 */
function jQueryFormat(){
	$.extend({
		format : function(source,args){
			var result = source;
			if(typeof(args) == "object"){
				if(args.length==undefined){
					for (var key in args) {
						if(args[key]!=undefined){
							var reg = new RegExp("({" + key + "})", "g");
							result = result.replace(reg, args[key]);
						}
					}
				}else{
					for (var i = 0; i < args.length; i++) {
						if (args[i] != undefined) {
							var reg = new RegExp("({[" + i + "]})", "g");
							result = result.replace(reg, args[i]);
						}
					}
				}
			}
			return result;
		}
	});
}

/**
 * 登录表单验证
 */
function LoginFormValidate(){
	$("#loginform").validate({
		rules:{
			username:{
				required:true
			},
			password:{
				required:true
//				minlength:6
			}
		},
//		debug:true,
		messages:{
			username:{
				required:"用户名不能为空"
			},
			password:{
				required:"密码不能为空"
//				minlength:"请输入至少6个字符"
			}
		},
		errorElement:"span",
		errorClass:"validateerror",
		submitHandler:function(form){
			$(form).submit();
		}
	});
}
/**
 * 增加验证方法，验证用户名是否被注册
 */
function addValidateMethod(){
	$.validator.addMethod("usernamesame",function(value,element){
		var flag = 1;
		$.ajax({
			type:"post",
			url:"resource/user/selUser",
			async:false,
			data:{
				loginname:value
			},
			dataType:"json",
			success:function(data){
				if(data!=null&&data!=""){
					if(data.hasuser==true){
						flag = 0;
					}
				}
			},
		});
		if(flag==1){
			return true;
		}else{
			return false;
		}
	},"该用户名已被注册");
}
/**
 * 注册表单验证
 */
function SignUpFormValidate(){
	$("#signupform").validate({
		rules:{
			usernamesignup:{
				required:true,
				usernamesame:true
			},
			passwordsignup:{
				required:true,
				minlength:6
			},
			passwordsignup_confirm:{
				required:true,
				minlength:6,
				equalTo:"#passwordsignup"
			}
		},
//		debug:true,
		messages:{
			usernamesignup:{
				required:"用户名不能为空",
				usernamesame:"该用户名已被注册，请换一个用户名",
			},
			passwordsignup:{
				required:"密码不能为空",
				minlength: $.format("密码不能小于{0}个字符")
			},
			passwordsignup_confirm:{
				required:"确认密码不能为空",
				minlength:$.format("确认密码不能小于{0}个字符"),
				equalTo:"两次输入密码不一致"
			}
		},
		errorElement:"span",
		errorClass:"validateerror",
		submitHandler:function(form){
			$(form).ajaxSubmit(function(data){
				if(data.state=="success"){
					swal("","注册用户成功！","success");
					window.location.hash = "#tologin";
				}
			});
		}
	});
}