$(function(){
	var url = document.URL;
	if(url.search("/page/login")==-1&&url.search("/page/index")==-1){
		if(url.search("/page")==-1){
			window.location.href = "./resource/page/index";
		}else{
			window.location.href = "./resource/page/index"+url.substr(url.lastIndexOf("/page")+5);
		}
	}
});