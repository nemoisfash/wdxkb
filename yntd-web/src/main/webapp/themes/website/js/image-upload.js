/**
 * athor:李靖
 * createTime:2019-3-1
 * @param $
 * @param layer
 * @param window
 * @param document
 * @returns
 *兼容 ie 10+
 */
;(function($,layer,window,document){
    var support={};
    if(window.FileReader){
        support.reader=new FileReader()
    }else{
        reader=null;
        return;
    }
    if(window.FormData){
        support.fd=new FormData;
    }else{
        fd=null;
        return;
    }
    let option={
        uploadServer:"",
        fileUriContainer:"container",
        deleteServer:"",
        image:"image",
        previewArea:"previewArea",
        previewBtn:"previewBtn",
        filedInput:"upload",
        uploadSuccess:""
    }
    
    var Uploader=function(opt){
        var self= this;
        if(!opt) {
            throw new Error("请传入配置参数");
        }
        self.option=this.extend(option,opt);
        this.init();
    }
    
    Uploader.prototype={
        init:function(){
            this.event();
        },
        extend:function(obj,obj2){
            for(var k in obj2){
                obj[k] = obj2[k];
            }
            return obj;
        },
        event:function(){
            var _this = this;
            var _opt=this.option;
            var filedInput=document.getElementById(_opt.filedInput);
            
            filedInput.addEventListener("change",function(){
                var file =this.files[0];
                support.fd.append("upload",file);
                var _reader=support.reader;
                _reader.onloadend = function () {
                    $("#"+_opt.image).attr("src",_reader.result);
                    $("#"+_opt.previewBtn).show();
                    _this.preview();
                }
                if (file){
                _reader.readAsDataURL(file);
                }
                
            },false);
            
            $("#"+_opt.previewBtn).on("click",function(){
            		_this.preview();
            });
             
        },
        upload:function(opt){
            $.ajax({
                    type: "post",
                    url : opt.uploadServer,
                    cache: false,
                    data: support.fd,
                    timeout: 5000,
                    processData: false,
                    contentType: false,
                    xhrFields: {
                        withCredentials: true
                    },
                    success: function(data) {
                        if(data.success){
                            layer.msg("上传成功");
                            $("#"+opt.fileUriContainer).val(data.imageUri);
                            setTimeout(function(){
                                layer.closeAll();
                            }, 1000);
                        }
                    }
            })
        },
        preview:function(){
			var _this = this;
			var opt=this.option;
            layer.open({
                type: 1,
                title:"图片上传",
                offset :'t',
                btn:['上传'],
                border : [ 10, 0.3, '#000', true ],
                area : [ '70%', '90%' ],
                content: $("#"+opt.previewArea),
                yes:function(){
                    if($("#"+opt.fileUriContainer).val()!=""){
                        _this.deleteOldFile(opt.deleteServer,$("#"+opt.fileUriContainer).val());
                    }
                 _this.upload(opt);
                }
            })
        },
        
        deleteOldFile:function(deleteUrl,url){
            $.get(deleteUrl,{"imageUri":url});
        }
    }
    window.Uploader = Uploader;
})(jQuery,layer,window,document)