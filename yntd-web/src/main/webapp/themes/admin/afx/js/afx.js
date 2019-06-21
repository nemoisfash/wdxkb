/**
 * 注意：
 *     别在此文件中添加和具体项目有关的代码
 */
var Fw = {};

Fw.jump = function(url) {
	self.location.href = url;
};

Fw.checkEach = function(selector, checked) {
	if (arguments.length == 1){
		$("input:checkbox[name='" + selector + "']").each(function(index) {
			$(this).prop("checked", !this.checked);
		})
	} else if (arguments.length == 2) {
		$("input:checkbox[name='" + selector + "']").each(function(index) {
			$(this).prop("checked", checked);
		})
	}
};

Fw.post = function(url, params, on_success){
	$.ajax({
		url : url,
		type : "post",
		data : params,
		async : false,
		dataType : "json",
		success : function(data) {
			if (typeof(on_success)=="function")
				on_success(data);
		}
	});
}
/*
Fw.confirm = function(msg, options, on_ok, on_cancel) {
	var args = {
		icon : 3,
		title : '系统提示'
	};
	$.extend(args, options);
	top.layer.confirm(msg, {
		icon : args.icon,
		title : args.title
	}, function(idx) {
		on_ok();
		top.layer.close(idx);
	}, function(idx) {
	});
}
*/

Fw.updateFilter = function(uuid, key, val, on_success) {
	$.ajax({
		url : "/api/filter/update.json",
		type : "post",
		data : {"u" : uuid, "k" : key, "v" : val},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.success){
				if (typeof(on_success)=="function")
					on_success();
				else
					self.location.reload();
			}
		}
	});
};

Fw.removeFilter = function(uuid, key, on_success) {
	$.ajax({
		url : "/api/filter/remove.json",
		type : "post",
		data : {"u" : uuid, "k" : key},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.success){
				if (typeof(on_success)=="function")
					on_success();
				else
					self.location.reload();
			}
		}
	});
};

Fw.clearFilter = function(uuid, on_success) {
	$.ajax({
		url : "/api/filter/clear.json",
		type : "post",
		data : {"u" : uuid},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.success){
				if (typeof(on_success)=="function")
					on_success();
				else
					self.location.reload();
			}
		}
	});
};

$(function() {
	$(".search_adv_box").width($(".search_adv_box").parent().width()-2);
	$(".search_adv_box").parent().on("resize",function(){
		$(".search_adv_box").width($(".search_adv_box").parent().width()-2);
	});
	$(".search_adv_btn:first").hover(function() {
		btnSearchAdv = $(".search_adv_btn");
		btnSearchAdv.removeClass("btn-warning").addClass("btn-success");
		/*offsetTop = btnSearchAdv.offset().top + btnSearchAdv.outerHeight(true);
		$(".search_adv_box").offset({
			'top' : 80
		});*/
		$(".search_adv_box").fadeIn();
	}, function() {
	});
	$(".search_adv_box:first").hover(function() {}, function() {
		$(".search_adv_btn").removeClass("btn-success").addClass("btn-warning");
		$(".search_adv_box").fadeOut();
	});
});


//---------------------------------------------------------------------------
//@Deprecated

function checkEach(selector, checked) {
	$("input[name='"+selector+"']").each(function(){
		if (arguments.length == 1) {
			$(this).prop("checked", !this.checked);
	    } else if (arguments.length == 2) {
			$(this).prop("checked", checked);
	    }
	});
};

function jumpTo(url) {
	self.location.href = url;
};

var Spark = {};
Spark.jump = function(url) {
	self.location.href = url;
};

//确认对话框
function dialogConfirm(msg, href) {
	top.layer.confirm(msg, {
		icon : 3,
		title : '系统提示'
	}, function(idx) {
		// do something
		if (typeof href != undefined){
			if (typeof href == 'function') {
				href();
			} else {
				top.layer.msg("正在提交，请稍等...");
				location = href;
			}
		}
		top.layer.close(idx);
	});
	return false;
}

function showImgDelay(imgObj,imgSrc,maxErrorNum){  
    if(maxErrorNum>0){ 
        imgObj.onerror=function(){
            showImgDelay(imgObj,imgSrc,maxErrorNum-1);
        };
        setTimeout(function(){
            imgObj.src=imgSrc;
        },500);
		maxErrorNum=parseInt(maxErrorNum)-parseInt(1);
    }
}