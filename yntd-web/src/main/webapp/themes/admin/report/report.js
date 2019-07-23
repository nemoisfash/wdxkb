var app = angular.module('myApp', []);
app.controller('myCtrl', function($scope,$http,$interval) {
var uuid=$("#uuid").val();
		$http({
				method: 'GET',
				url:"/admin/report/data.json",
				cache:false,
				async:false
			}).then(function(res){
				$scope.items=res.data.result.content;
				var totalElements=res.data.result.totalElements;
				var totalPages=res.data.result.totalPages;
				var number=res.data.number;
				pagination(totalElements,totalPages,number)
		})
		
		function pagination(totalElements,totalPages,number){
			$(".tcdPageCode").createPage({
			elementCount :totalElements,
			pageCount :totalPages,
			current :number,
			backFn : function(to){
			Fw.updateFilter(uuid, 'page', to);
				}
			})
		};
		
		
}).directive('submitInput',function($timeout){
	return{
		restrict:'A',
		link:function(scope,elem,attrs){
			var childrens= elem.find("input.input-form");
			childrens.change(function(){
				var parent= $(this).parents("tr.odd");
				var id=$(parent).attr("id")
				var $form=$("<form></form>");
				$form.append($(this).clone(true));
				$form.append($("<input />").attr({
					type:"text",
					name:"id",
					id:"id",
					value:id
				}));
				$.ajax({
		                type: "POST",
		                dataType: "json",
		                url: "/admin/report/update.json",
		                data: $form.serialize(),
		                success: function (result) {
		                    if (result.success) {
		                    	layer.msg("修改成功");
		                    };
		                },
		                error:function(){
		                	layer.msg("异常");
		                }
		        });
			})
		}
	}
})

