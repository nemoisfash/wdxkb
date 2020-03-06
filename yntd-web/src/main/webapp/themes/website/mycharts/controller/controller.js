var app = angular.module('myApp', []);
app.controller(
		'myCtrl',
		function($scope, $http, $timeout, $interval) {
			getTopices()
			function getTopices() {
				$.ajax({
							method : "get",
							url : "/member/getAllTopics.json",
							async : false,
							dataType : "json",
							contentType : "application/json",
							success : function(data) {
								var chatsTopices = [ "pies", "ranking", "timeLine" ];
								var machineTopices = data.topices;
								localStorage.setItem("machineTopices", machineTopices.join("&"));
								var machineNames = data.machineNames;
								localStorage.setItem("machineNames", machineNames.join("&"));
								$scope.topices = machineTopices .concat(chatsTopices);
								localStorage.setItem("topices", chatsTopices.join("&"));
								$scope.topicesLength = $scope.topices.length;
								connection();
							}

						})
			}

			var msgFactory = {
				"products" : [],
				"capacity" : $scope.topicesLength
			};
			function connection() {
				var websocket;
				var host = window.location.host;
				if ('WebSocket' in window) {
					websocket = new WebSocket("ws://" + host + "/ws.html");
				} else if ('MozWebSocket' in window) {
					websocket = new MozWebSocket("ws://" + host + "/ws.html");
				} else {
					websocket = new SockJS("http://" + host + "/ws/socketjs.html");
				}

				websocket.onError = function() {
					connection();
				};

				websocket.onClose = function() {
					console.info("链接已关闭");
				};

				websocket.onmessage = function(evn) {
					var jsonData = JSON.parse(evn.data);
					msgFactoryCreate(jsonData);
				};

				websocket.onopen = function(event) {
					console.info("通道已建立");
					websocket.send(JSON.stringify({
						"status" : 0,
						"isFinished" : false,
						"topices" : $scope.topices
					}));
				}
			}

			function msgFactoryCreate(obj) {
				if (msgFactory.products.length < msgFactory.capacity) {
					msgFactory.products.push(obj);
					clearData(obj);
				} else {
					msgFactory.products.splice(0, msgFactory.products.length);
				}
			}

			function clearData(jo) {
				if (jo["co1"] && jo["co1"] == "1") {
					jo["cnc_mode"] = "快速移动状态";
				}

				if (jo["co2"] && jo["co2"] == "1") {
					jo["cnc_mode"] = "直线切削状态";
				}

				if (jo["co3"] && jo["co3"] == "1") {
					jo["cnc_mode"] = "顺时针圆弧状态";
				}

				if (jo["co4"] && jo["co4"] == "1") {
					jo["cnc_mode"] = "逆时针圆弧状态";
				}

				if (jo["rz"] && jo["rz"] == "1") {
					jo["machineSignal"] = "RUNNING";
					jo["machineSignalZH"] = "运行";
				}

				if (jo["wz"] && jo["wz"] == "1") {
					jo["machineSignal"] = "WAITING";
					jo["machineSignalZH"] = "等待";
				}

				if (jo["az"] && jo["az"] == "1") {
					jo["machineSignal"] = "ALARM";
					jo["machineSignalZH"] = "报警";
				}
				if (jo["device_state"]) {
					if (jo["device_state"] == "0") {
						jo["machineSignal"] = "RUNNING";
						jo["machineSignalZH"] = "运行";
						if (jo["cnc_runstatus"]) {
							if (jo["cnc_runstatus"] == "0") {
								jo["cnc_runstatus"] = "RESET";
							}
							if (jo["cnc_runstatus"] == "1") {
								jo["cnc_runstatus"] = "STOP";
							}
							if (jo["cnc_runstatus"] == "2") {
								jo["cnc_runstatus"] = "HOLD";
							}
							if (jo["cnc_runstatus"] == "3") {
								jo["cnc_runstatus"] = "START";
							}
							if (jo["cnc_runstatus"] == "4") {
								jo["cnc_runstatus"] = "MSTR";
							}
							if (jo["cnc_runstatus"] == "5") {
								jo["cnc_runstatus"] = "Other";
							}
						}
					}
					if (jo["device_state"] == "1") {
						jo["machineSignal"] = "POWEROFF";
						jo["machineSignalZH"] = "关机";
					}
				}
				if (jo["cnc_alarm"]) {
					if (jo["cnc_alarm"] != "[]") {
						jo["machineSignal"] = "ALARM";
						jo["machineSignalZH"] = "报警";
						jo["cnc_alarm_message"] = jo["cnc_alarm"];
					}
				}

				if (jo["cnc_mecpos"]) {
					var jsonData = JSON.parse(jo["cnc_mecpos"]);
					$.each(jsonData, function() {
						jo["cnc_mc" + this["axis"]] = this["value"]
					})
				}

				if (jo["cnc_ablpos"]) {
					var jsonData = JSON.parse(jo["cnc_ablpos"]);
					$.each(jsonData, function() {
						jo["cnc_ab" + this["axis"]] = this["value"]
					})
				}

				if (jo["cnc_relpos"]) {
					var jsonData = JSON.parse(jo["cnc_relpos"]);
					$.each(jsonData, function() {
						jo["cnc_rc" + this["axis"]] = this["value"];
					})
				}

				if (jo["mcx"]) {
					jo["cnc_mcX"] = jo["mcx"];
				}

				if (jo["cnc_actfspeed"]) {
					var cncActfspeed = jo["cnc_actfspeed"];
					if (cncActfspeed.indexOf(".") > -1) {
						jo["cnc_actfspeed"] = cncActfspeed.split(".")[0];
					}
				}

				if (jo["cnc_actspeed"]) {
					var cncActspeed = jo["cnc_actspeed"];
					if (cncActfspeed.indexOf(".") > -1) {
						jo["cnc_actspeed"] = cncActfspeed.split(".")[0];
					}
				}

				if (jo["mcy"]) {
					jo["cnc_mcY"] = jo["mcy"];
				}
				if (jo["mcz"]) {
					jo["cnc_mcZ"] = jo["mcz"];
				}
				if (jo["mca"]) {
					jo["cnc_mcA"] = jo["mca"];
				}

				if (jo["rcx"]) {
					jo["cnc_rcX"] = jo["rcx"];
				}
				if (jo["rcy"]) {
					jo["cnc_rcY"] = jo["rcy"];
				}
				if (jo["rcz"]) {
					jo["cnc_rcZ"] = jo["rcz"];
				}
				if (jo["rca"]) {
					jo["cnc_rcA"] = jo["rca"];
				}

				if (jo["cnc_fload"] && jo["cnc_fload"].indexOf("[") > -1) {
					$.each(jsonData, function() {
						jo["cnc_l" + this["axis"]] = this["value"]
					})
				}

				if (jo["lx"]) {
					jo["cnc_lX"] = jo["lx"];
				}
				if (jo["ly"]) {
					jo["cnc_lY"] = jo["ly"];
				}
				if (jo["lz"]) {
					jo["cnc_lZ"] = jo["lz"];
				}
				localStorage.setItem(jo["code"], JSON.stringify(jo));
			}
			/*
			 * function replaceKey(jo){ for(var key in deviceParameters){
			 * if(jo[key]){ deviceParameters[key]=jo[key]; } }
			 * localStorage.setItem(deviceParameters["machineName"],
			 * JSON.stringify(deviceParameters));
			 * $scope.switchStatus(deviceParameters); }
			 */
			
			switchStatus();
			function switchStatus() {
					var machineNames= localStorage.getItem("machineNames");
					$.each(machineNames.split("&"),function(){
							var obj=localStorage.getItem(this);
							if(obj!=null){
								var _obj =JSON.parse(obj);
								var status = "";
								if (_obj.machineSignal == null || _obj.machineSignal == "") {
									status = "POWEROFF";
								} else {
									status = _obj.machineSignal;
								}	
								var machineName = _obj.code;
								$("#" + machineName + "_m").attr("class", "")
								$("#" + machineName + "_m").addClass("circle" + " " + "circle-" + status.toLowerCase() + " "+ "headerBox");
							}
					})
				$timeout(function() {
					switchStatus();
				}, 2000)
			}
			 
			createList()
			function createList() {
				 	var machineNames= localStorage.getItem("machineNames");
					$scope.devices=[];
					$scope.alarmList=[];
					$.each(machineNames.split("&"),function(){
						var obj=localStorage.getItem(this);
						if(obj!=null){
								var _obj =JSON.parse(obj);
									if(_obj.machineSignal=="ALARM"){
											$scope.alarmList.push(_obj);
											_obj.machineSignalZH="报警"
									}
									if(_obj.machineSignal=="RUNNING"){
										_obj.machineSignalZH="运行"
									}
									
									if(_obj.machineSignal=="POWEROFF"){
										_obj.machineSignalZH="停机"
									}
									
									if(_obj.machineSignal=="WAITING"){
										_obj.machineSignalZH="等待"
									}
									if(!_obj.cnc_cycletime){
										_obj.cnc_cycletime="0";
									}
									if(!_obj.cnc_alivetime){
										_obj.cnc_alivetime="0";
									}
									
									if(!_obj.cnc_srate){
										_obj.cnc_srate="0";
									}
									
									if(!_obj.cnc_rapidfeed){
										_obj.cnc_rapidfeed="0";
									}
									
									if(!_obj.cnc_sload){
										_obj.cnc_sload="0";
									}
									
									if(!_obj.cnc_products){
										_obj.cnc_products="0";
									}
									
									if(!_obj.cnc_actfspeed){
										_obj.cnc_actfspeed="0";
									}
									
									if(!_obj.cnc_actspeed){
										_obj.cnc_actspeed="0";
									}
									
									
							 $scope.devices.push(_obj);
							}
					})
				insertMontoring()
				$timeout(function() {
					createList();
					alarm()
				}, 2000)
			}
			
			
			function alarm() {
				var length = $scope.alarmList.length;
				if(length>0){
					layer.open({
						type : 1,
						shade : false,
						area : [ '600px', '350px' ],
						title :false,
						closeBtn : 0,
						content : $('#alarmList'),
						scrollbar : false
					});
				}else{
					layer.closeAll();
				}
			}

			function insertMontoring() {
				var machineNames = localStorage.getItem("machineNames").split("&");
				if (machineNames != null) {
					for (var i = 0; i < machineNames.length; i++) {
						if (localStorage.getItem(machineNames[i]) != null) {
							$.ajax({
								type : "post",
								url : "/member/insertMonitor.json",
								data : localStorage.getItem(machineNames[i]),
								contentType : "application/json;charset=utf-8",
								dataType : "json",
								timeout : 3000,
								success : function(data) {
									if (data.success) {
										console.info(data.success);
									}
								}
							})
						}
					}
				}
			}

			$interval(function() {
				publishPieData()
			}, 5000)
			function publishPieData() {
				$.get("/member/publishPieData.json");
				$timeout(function() {
					publishRanking()
				}, 2000)
			}

			function publishRanking() {
				$.get("/member/publishRanking.json")
				$timeout(function() {
					publishTimeLine()
				}, 5000)
			}

			function publishTimeLine() {
				$.get("/member/timeLine.json")
				publicMachineInfo();
			}
			
			function publicMachineInfo() {
				$.get("/member/publicMachineInfo.json")
			}
			
		}).directive(
		'myClock',
		function($interval, $http) {
			return {
				restrict : 'A',
				link : function(scope, elem, attrs) {
					$(function() {
						var monthNames = [ "1月", "2月", "3月", "4月", "5月", "6月",
								"7月", "8月", "9月", "10月", "11月", "12月" ];
						var dayNames = [ "星期一", "星期二", "星期三", "星期四", "星期五",
								"星期六", "星期天" ]
						var newDate = new Date();
						newDate.setDate(newDate.getDate());
						$('#Date').html(
								newDate.getFullYear() + " "
										+ monthNames[newDate.getMonth()] + ' '
										+ newDate.getDate() + "日" + ' '
										+ dayNames[newDate.getDay() - 1]);
						setInterval(
								function() {
									var seconds = new Date().getSeconds();
									$("#sec")
											.html(
													(seconds < 10 ? "0" : "")
															+ seconds);
								}, 1000);

						setInterval(
								function() {
									var minutes = new Date().getMinutes();
									$("#min")
											.html(
													(minutes < 10 ? "0" : "")
															+ minutes);
								}, 1000);

						setInterval(function() {
							var hours = new Date().getHours();
							$("#hours").html((hours < 10 ? "0" : "") + hours);
						}, 1000);
					});
				}
			}
		}).directive('myScoller', function($interval) {
	return {
		restrict : 'A',
		link : function(scope, elem, attrs) {
			var maxLength = 3;
			var timer = 6000 * 5;
			elem.ready(function() {
				if (elem.children().length < maxLength) {
					elem.append(elem.children().clone());
				}
				$interval(function() {
					var fchild = elem.children(":first");
					var left = fchild.width();
					fchild.animate({
						"marginLeft" : (-1 * left) + "px"
					}, 2000, function() {
						$(this).css("margin-left", "auto").appendTo(elem);
					});
				}, timer);
			})
		}
	}
}).directive('rollUp', function($interval) {
	return {
		restrict : 'A',
		link : function(scope, elem, attrs) {
			scope.$on('repeatFinished', function(event) {
				$interval(function() {
					var childFr = elem.children(".roll-up").eq(0);
					childFr.remove();
					elem.append(childFr.clone(true));
				}, 6000)
			})
		}
	}
}).directive('ngRepeatFinished', function($timeout) {
	return {
		restrict : 'A',
		link : function(scope, elem, attrs) {
			if (scope.$last == true) {
				$timeout(function() {
					scope.$emit('repeatFinished');
				});
			}
		}
	}
}).directive('watchDevice', function($timeout) {
	return {
		restrict : 'A',
		link : function(scope, elem, attrs) {
			if (scope.$last == true) {
				$timeout(function() {
					scope.$emit('repeatFinished');
				});
			}
		}
	}
})