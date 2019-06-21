/**
 * 
 */
var validateForm;
$(function(){
	validateForm = $("#validateForm").validate({
rules:{
	username: { required:true },
	password: { required:true },
	captcha: { required:true },
},
messages:{
	username: { required:"用户名不能为空" },
	password: { required:"密码不能为空" },
	captcha: { required:"验证码不能为空" },
	}
});

$("a.captcha,img.captcha").on("click",function(){ $("img.captcha").prop("src","/captcha.png?"+Math.random()); });
});
	
$("#btn-logout").on("click", function(){
	layer.confirm("退出登录？", {icon: 3, title:'提示'}, function(index){
		layer.close(index);
		top.location.href = "${adminPath}/logout.html";
	});
});
	
