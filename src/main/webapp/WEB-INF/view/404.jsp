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
		<title>404 - Not found</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		
		<link rel="stylesheet" href="static/css/errorPagecss/main.css" type="text/css" media="screen, projection" /> <!-- main stylesheet -->
		<link rel="stylesheet" type="text/css" media="all" href="static/css/tipsy.css" /> <!-- Tipsy implementation -->
		<!-- <link rel="stylesheet" type="text/css" href="static/css/errorPagecss/ie8.css" /> -->
	</head>
	
	<body>
		<!-- Universal preloader -->
		<!-- <div id="universal-preloader">
		    <div class="preloader">
		        <img src="static/images/errorPageImage/universal-preloader.gif" alt="universal-preloader" class="universal-preloader-preloader" />
		    </div>
		</div> -->
		<!-- Universal preloader -->
		
		<div id="wrapper">
		<!-- 404 graphic -->
			<div class="graphic">404</div>
		<!-- 404 graphic -->
		<!-- Not found text -->
			<div class="not-found-text">
		    	<h1 class="not-found-text">File not found, sorry!</h1>
		    </div>
		<!-- Not found text -->
		
		<!-- search form -->
		<!-- <div class="search">
			<form name="search" method="get" action="#" />
		        <input type="text" name="search" value="Search ..." />
		        <input class="with-tooltip" title="Search!" type="submit" name="submit" value="" />
		    </form>
		</div> -->
		<!-- search form -->
		
		<!-- top menu -->
		<!-- <div class="top-menu">
			<a href="javaScript:history.go(-1);" class="with-tooltip" title="Back">goBack</a>
		</div> -->
		<!-- top menu -->
		
		<div class="dog-wrapper">
		<!-- dog running -->
			<div class="dog"></div>
		<!-- dog running -->
			
		<!-- dog bubble talking -->
			<div class="dog-bubble">
		    	
		    </div>
		    
		    <!-- The dog bubble rotates these -->
		    <div class="bubble-options">
		    	<p class="dog-bubble">
		        	Are you lost, bud? No worries, I'm an excellent guide!
		        </p>
		    	<p class="dog-bubble">
			        <br />
		        	Arf! Arf!
		        </p>
		        <p class="dog-bubble">
		        	<br />
		        	Don't worry! I'm on it!
		        </p>
		        <p class="dog-bubble">
		        	I wish I had a cookie<br /><img style="margin-top:8px" src="static/images/errorPageImage/cookie.png" alt="cookie" />
		        </p>
		        <p class="dog-bubble">
		        	<br />
		        	Geez! This is pretty tiresome!
		        </p>
		        <p class="dog-bubble">
		        	<br />
		        	Am I getting close?
		        </p>
		        <p class="dog-bubble">
		        	Or am I just going in circles? Nah...
		        </p>
		        <p class="dog-bubble">
		        	<br />
		            OK, I'm officially lost now...
		        </p>
		        <p class="dog-bubble">
		        	I think I saw a <br /><img style="margin-top:8px" src="static/images/errorPageImage/cat.png" alt="cat" />
		        </p>
		        <p class="dog-bubble">
		        	What are we supposed to be looking for, anyway? @_@
		        </p>
		    </div>
		    <!-- The dog bubble rotates these -->
		<!-- dog bubble talking -->
		</div>
		
		<!-- planet at the bottom -->
			<div class="planet"></div>
		<!-- planet at the bottom -->
		</div>
		
		<script type="text/javascript" src="static/js/jquery-1.7.2.min.js"></script> <!-- uiToTop implementation -->
		<script type="text/javascript" src="static/js/errorPagejs/custom-scripts.js"></script>
		<script type="text/javascript" src="static/js/jquery.tipsy.js"></script> <!-- Tipsy -->
		<script type="text/javascript" src="static/js/security_url_js/security_url.js"></script>
		<script type="text/javascript">
			$(document).ready(function(){
				//universalPreloader();
				rotate();
			    dogRun();
				dogTalk();
				//Tipsy implementation
				$('.with-tooltip').tipsy({gravity: $.fn.tipsy.autoNS});
			});
			/* $(window).load(function(){
				//remove Universal Preloader
				//universalPreloaderRemove();
				rotate();
			    dogRun();
				dogTalk();
				//Tipsy implementation
				$('.with-tooltip').tipsy({gravity: $.fn.tipsy.autoNS});
			}); */
			</script>
	</body>
</html>