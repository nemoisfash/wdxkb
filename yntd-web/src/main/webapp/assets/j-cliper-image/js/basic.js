$(function(){
    //默认图像居中显示
    var _Jw = ($("#target").width() - 120) / 2 ,
            _Jh = ($("#target").height() - 120) / 2 ,
            _Jw2 = _Jw + 120,
            _Jh2 = _Jh + 120;

    $('#target').Jcrop({
        setSelect: [_Jw, _Jh, _Jw2, _Jh2],
        onChange: showPreview,
        onSelect: showPreview,
        bgFade: true,
        bgColor: "#000",
        aspectRatio: 120/120,
        bgOpacity: .5
    });
    $("#idBig").click(function(e){
        imgToSize(20);
        return false;
    });
    $("#idSmall").click(function(e){
        imgToSize(-20);
        return false;
    });
});
 
function imgToSize(size) {
    var iw = $('.jcrop_w>img').width(),
        ih = $('.jcrop_w>img').height(),
        _data = $(".jc-demo-box").attr("data"),
        _w = Math.round(iw + size),
        _h = Math.round(((iw + size) * ih) / iw);

    if((_data == 90 || _data == 270)){
        $('.jcrop_w>img').width(_h).height(_w);
    }else{
        $('.jcrop_w>img').width(_w).height(_h);
    }

    var     fiw = $('.jcrop_w>img').width(),
            fih = $('.jcrop_w>img').height(),
            ow = (500 - fiw) / 2,
            oh = (370 - fih) / 2,
            cx = $("#small").position().left,
            cy = $("#small").position().top,
            rx = 120 / $("#small").width(),
            ry = 120 / $("#small").height(),
            rx1 = 80 / $("#small").width(),
            ry1 = 80 / $("#small").height(),
            rx2 = 40 / $("#small").width(),
            ry2 = 40 / $("#small").height();

    /* if((_data == 90 || _data == 270)){
        pre_img2($('.pre-1 img'), rx, fih, ry, fiw, cx, cy, ow, oh);
        pre_img2($('.pre-2 img'), rx1, fih, ry1, fiw, cx, cy, ow, oh);
        pre_img2($('.pre-3 img'), rx2, fih, ry2, fiw, cx, cy, ow, oh);
    }else{
        pre_img2($('.pre-1 img'), rx, fiw, ry, fih, cx, cy, ow, oh);
        pre_img2($('.pre-2 img'), rx1, fiw, ry1, fih, cx, cy, ow, oh);
        pre_img2($('.pre-3 img'), rx2,  fiw, ry2, fih, cx, cy, ow, oh);
    }
    $(".jcrop_w img").css({
        left: ow,
        top: oh
    });*/
};

        //
function pre_img2(obj, rx, iw, ry, ih, cx, cy, ow, oh){
            obj.css({
                width: Math.round(rx * iw) + 'px',
                height: Math.round(ry * ih) + 'px'
            });
            if( cy >= oh && cx >= ow){
                obj.css({
                    marginLeft: '-' + Math.round(rx * (cx - ow)) + 'px',
                    marginTop: '-' + Math.round(ry * (cy - oh)) + 'px'
                });
            }else if( cy <= oh && cx >= ow){
                obj.css({
                    marginLeft: "-" + Math.round(rx * (cx - ow)) + 'px',
                    marginTop: Math.round(ry * (oh - cy)) + 'px'
                });
            }else if( cy >= oh && cx <= ow){
                obj.css({
                    marginLeft: Math.round(rx * (ow - cx)) + 'px',
                    marginTop: '-' + Math.round(ry * (cy - oh)) + 'px'
                });
            }else if( cy <= oh && cx <= ow){
                obj.css({
                    marginLeft: Math.round(rx * (ow - cx)) + 'px',
                    marginTop: Math.round(ry * (oh - cy)) + 'px'
                });
            }

        };
//默认图像位置
function cutImage(obj) {
    var w = 500,
        h = 370,
        iw = obj.width(),
        ih = obj.height();
    if(iw > w || ih > h){
        if(iw / ih > w / h){
            obj.css({
                width: w,
                height: w * ih / iw,
                top: (h - (w * ih / iw)) / 2,
                left: 0
            });
        }else{
            obj.css({
                height: h,
                width: h * iw / ih,
                top: 0,
                left: (w - (h * iw / ih)) / 2
            });
        }
    }else{
    	obj.css({
            left:0,
            top:0
        });
    }
}

function showPreview(c){
	var iw = $('.jcrop_w>img').width(),
		ih = $('.jcrop_w>img').height(),
        ow = (500 - 500) / 2,
        oh = (370 - ih) / 2,
		rx = 120 / c.w,
		ry = 120 / c.h,
		rx1 = 80 / c.w,
		ry1 = 80 / c.h,
		rx2 = 40 / c.w,
		ry2 = 40 / c.h,
		_data = $(".jc-demo-box").attr("data");

	if((_data == 90 || _data == 270)){
		pre_img2($('.pre-1 img'), rx, ih, ry, iw, c.x, c.y, ow, oh);
		pre_img2($('.pre-2 img'), rx1, ih, ry1, iw, c.x, c.y, ow, oh);
		pre_img2($('.pre-3 img'), rx2, ih, ry2, iw, c.x, c.y, ow, oh);
	}else{
		pre_img2($('.pre-1 img'), rx, iw, ry, ih, c.x, c.y, ow, oh);
		pre_img2($('.pre-2 img'), rx1, iw, ry1, ih, c.x, c.y, ow, oh);
		pre_img2($('.pre-3 img'), rx2, iw, ry2, ih, c.x, c.y, ow, oh);
	}
	$('#x').val(c.x);
	$('#y').val(c.y);
	$('#w').val(c.w);
	$('#h').val(c.h);
}