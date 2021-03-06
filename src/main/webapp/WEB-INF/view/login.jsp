<%@ page language="java" import="java.util.*" pageEncoding="utf-8" isELIgnored="false"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
+ path + "/";
%>
<!DOCTYPE html>
<html lang="en" class="no-js">
<!-- BEGIN HEAD -->
	<head>
    	<base href="<%=basePath%>">
        <meta charset="UTF-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <title>PaperCheck登录</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"> 
        <meta name="description" content="" />
        <meta name="author" content="" />
        <!-- <link rel="shortcut icon" href="../favicon.ico"> -->
        <link rel="stylesheet" type="text/css" href="static/css/sweet-alert.css" />
        <link rel="stylesheet" type="text/css" href="static/css/loginAndRegistercss/demo.css" />
        <link rel="stylesheet" type="text/css" href="static/css/loginAndRegistercss/style3.css" />
		<link rel="stylesheet" type="text/css" href="static/css/loginAndRegistercss/animate-custom.css" />
    </head>
    <body>
        <div class="container">
            <section>				
                <div id="container_demo" >
                    <!-- hidden anchor to stop jump http://www.css3create.com/Astuce-Empecher-le-scroll-avec-l-utilisation-de-target#wrap4  -->
                    <a class="hiddenanchor" id="toregister"></a>
                    <a class="hiddenanchor" id="tologin"></a>
                    <div id="wrapper">
                        <div id="login" class="animate form">
                            <form id="loginform"  action="resource/user/login" method="post" autocomplete="on"> 
                                <h1>登录</h1> 
                                <p> 
                                    <label for="username" class="uname" data-icon="u" > 用户名 </label>
                                    <input id="username" name="username"  type="text" placeholder="myusername"/>
                                </p>
                                <p> 
                                    <label for="password" class="youpasswd" data-icon="p"> 密码 </label>
                                    <input id="password" name="password" type="password" placeholder="eg. X8df!90EO" /> 
                                </p>
                                <!-- 记住我 -->
                                <!-- <p class="keeplogin"> 
									<input type="checkbox" name="loginkeeping" id="loginkeeping" value="loginkeeping" /> 
									<label for="loginkeeping">Remember me</label>
								</p> -->
                                <p class="login button"> 
                                    <input type="submit" value="登录" /> 
								</p>
                                <p class="change_link">
									没有用户？
									<a href="#toregister" class="to_register">注册</a>
								</p>
                            </form>
                        </div>

                        <div id="register" class="animate form">
                            <form id="signupform"  action="resource/user/signup" method="post" autocomplete="on"> 
                                <h1> 注册 </h1> 
                                <p> 
                                    <label for="usernamesignup" class="uname" data-icon="u">用户名</label>
                                    <input id="usernamesignup" name="usernamesignup" type="text" placeholder="mysuperusername690" />
                                </p>
                               <!-- 姓名 -->
                                <p> 
                                    <label for="namesignup" class="uname" data-icon="u">真实姓名</label>
                                    <input id="namesignup" name="namesignup" type="text" placeholder="张三" />
                                </p>
                                <!-- 工作单位 -->
                                <p> 
                                    <label for="organizationsignup" class="uname" data-icon="u">工作单位</label>
                                    <input id="organizationsignup" name="organizationsignup" type="text" placeholder="xxx公司/xxx学校" />
                                </p>
                                <!-- 手机 -->
                                <p> 
                                    <label for="telsignup" class="uname" data-icon="u">手机号码</label>
                                    <input id="telsignup" name="telsignup" type="text" placeholder="13374440123" />
                                </p>
                                <p> 
                                    <label for="emailsignup" class="youmail" data-icon="e" > 邮箱</label>
                                    <input id="emailsignup" name="emailsignup" type="email" placeholder="mysupermail@mail.com"/> 
                                </p>
                                <p> 
                                    <label for="passwordsignup" class="youpasswd" data-icon="p">密码 </label>
                                    <input id="passwordsignup" name="passwordsignup" type="password" placeholder="eg. X8df!90EO"/>
                                </p>
                                <p> 
                                    <label for="passwordsignup_confirm" class="youpasswd" data-icon="p">确认密码 </label>
                                    <input id="passwordsignup_confirm" name="passwordsignup_confirm" type="password" placeholder="eg. X8df!90EO"/>
                                </p>
                                <p class="signin button"> 
									<input type="submit" value="注册"/> 
								</p>
                                <p class="change_link">  
									已有用户
									<a href="#tologin" class="to_register"> 登录 </a>
								</p>
                            </form>
                        </div>	
                    </div>
                </div>  
            </section>
        </div>
        <script type="text/javascript" src="static/js/jquery.js" ></script>
        <script type="text/javascript" src="static/js/jquery.validate.min.js"></script>
        <script type="text/javascript" src="static/js/jquery.metadata.js"></script>
        <script type="text/javascript" src="static/js/messages_zh.min.js"></script>
        <script type="text/javascript" src="static/js/jquery.form.js"></script>
        <script type="text/javascript" src="static/js/sweet-alert.min.js"></script>
        <script type="text/javascript" src="static/js/security_url_js/security_url.js"></script>
        <script type="text/javascript" src="static/js/loginAndRegisterjs/loginAndRegister.js"></script>
        <script type="text/javascript">
	        function alertErrorMsg(){
	        	var error = "${error}";
	        	if(error!=null&&error!=""){
	        		$("#username").val("${requestScope.user.loginname}");
	        		//swal("",error,"error");
	        	}else{
	        		$("#username").val("${requestScope.user.loginname}");
	        		$("#password").val("${requestScope.user.pwd}");
	        	}
	        }
	        $(function(){
	        	alertErrorMsg();
	        });
        </script>
    </body>
</html>