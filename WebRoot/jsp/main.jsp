<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>APL</title>
		<link rel="stylesheet" type="text/css" href="css/themes/default/easyui.css?version=1">
		<link rel="stylesheet" type="text/css" href="css/themes/icon.css">
		<script type="text/javascript" src="js/jquery.min.js"></script>
		<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
		<style type="text/css">
			body
			{
				margin: 0;
				border: 0;
				height: 100%;
				max-height: 100%;
				background:#E9E9E9;
				
			}
			
			.tabs li a.tabs-inner:before {
			  position:absolute;
			  content:"";
			  display: inline-block;
			  top:0px;
			  left:1px;
			  z-index:4;
			  content:url(css/themes/default/images/splitline.png);
			  background-image: -moz-linear-gradient(top, #A2B3C4, #778899);
			}
			
			.tabs li a.tabs-inner:after {
			  position:absolute;
			  content:"";
			  display: inline-block;
			  top:0px;
			  right:-3px;
			  z-index:4;
			  content:url(css/themes/default/images/splitline.png);
			}
			
			/*切tabs圆角css,注意z-index先后*/
			.tabs li.tabs-selected:after {/*切出右下角圆角*/
			  position:absolute;
			  bottom:0px;
			  right:-3px;
			  background-color: #E9E9E9;
			  content:"";
			  width:10px;
			  height:47px;
			  z-index:5;
			  display: inline-block;
			  border-radius: 0 0 0 7px;
			}
			.tabs li.tabs-selected:before {/*切出左下角圆角*/
			  position:absolute;
			  bottom:0px;
			  left:-3px;
			  background-color: #E9E9E9;
			  content:"";
			  width:10px;
			  height:47px;
			  z-index:5;
			  display: inline-block;
			  border-radius: 0 0 7px 0;
			}
			/*由于切出圆角后tabs形状将怪异，所以顶部要覆盖重新定义*/
			.tabs li.tabs-selected a.tabs-inner:after {/*覆盖顶部*/
			  position:absolute;
			  top:0px;
			  right:7px;
			  background-color: #E9E9E9;
			  content:"";
			  width:106px;
			  height:10px;
			  z-index:6;
			  display: inline-block;
			  border-radius: 0px 0px 0 0;
			}
			
			.tabs li.tabs-selected a.tabs-inner:before {/*重定义顶部形状*/
			  position:absolute;
			  top:0px;
			  left:7px;
			  background-color: #307EB7;
			  content:"";
			  width:106px;
			  height:10px;
			  z-index:7;
			  display: inline-block;
			  border-radius: 7px 7px 0 0;
			}
			
			.panel {
			  border-radius: 7px 7px 0 0;
			}
		</style>
	</head>
	
	<body style="padding:0 25px 0 25px;">
		<div style="top:5px;left:40px;position:absolute;z-index:10">
			<img src='img/logo.png' style="width:110px;height:38px"/>
		</div>
		<div id="maintabs" class="easyui-tabs" style="top:0px;left:25px;position:absolute;height:650px">
			<div id='tab1' title="<span style='font-size:14px;font-weight:bold;font-family: Arial,sans-serif;'>Bkg# Range</span>" height="100%">
				<div class="easyui-layout" data-options="minWidth:890"  style="height:100%">
					<div data-options="region:'north'" style="height:47px;padding:0px;border:0px;margin:0px;overflow:hidden">
						<div style="width:100%;height:45px;background:#307EB7;color:#ffffff;line-height:45px;">
							<span id="Maintaintitles" style="font-size:14px;font-weight:bold;padding-left:15px">
								
							</span>
						</div>
					</div>
					<div id="tab1fram" data-options="region:'center'" style="border:0px;overflow:hidden">
						
					</div>
				</div>
			</div>
			<div id='tab2' title="<span style='font-size:14px;font-weight:bold;z-index:9;font-family: Arial,sans-serif;'>Traffic Log</span>" style="overflow-y:visible;">
				<div class="easyui-layout" data-options="minWidth:890,border:false" style="height:100%;">
					<div data-options="region:'north'" style="height:47px;padding:0px;border:0px;margin:0px;">
						<div id="signtitle" style="width:100%;height:45px;background:#307EB7;color:#ffffff;line-height:45px;border-radius: 7px 7px 0 0;">
							<span id="Traffictitles" style="font-size:14px;font-weight:bold;padding-left:15px">
								Traffic Log
							</span>
						</div>
					</div>
					<div id="tab2fram" data-options="region:'center'" style="border:0px;overflow:hidden">
						
					</div>
				</div>
			</div>
			<div id='tab3' title="<span style='font-size:14px;font-weight:bold;z-index:9;font-family: Arial,sans-serif;'>Convert Log</span>" style="overflow-y:visible;">
				<div class="easyui-layout" data-options="minWidth:890,border:false" style="height:100%;">
					<div data-options="region:'north'" style="height:47px;padding:0px;border:0px;margin:0px;">
						<div id="signtitle" style="width:100%;height:45px;background:#307EB7;color:#ffffff;line-height:45px;border-radius: 7px 7px 0 0;">
							<span id="Converttitles" style="font-size:14px;font-weight:bold;padding-left:15px">
								Convert Log
							</span>
						</div>
					</div>
					<div id="tab3fram" data-options="region:'center'" style="border:0px;overflow:hidden">
						
					</div>
				</div>
			</div>
			<div id='tab4' title="<span style='font-size:14px;font-weight:bold;z-index:9;font-family: Arial,sans-serif;'>Email</span>" style="overflow-y:visible;">
				<div class="easyui-layout" data-options="minWidth:890,border:false" style="height:100%;">
					<div data-options="region:'north'" style="height:47px;padding:0px;border:0px;margin:0px;">
						<div id="signtitle" style="width:100%;height:45px;background:#307EB7;color:#ffffff;line-height:45px;border-radius: 7px 7px 0 0;">
							<span id="Converttitles" style="font-size:14px;font-weight:bold;padding-left:15px">
								Email
							</span>
						</div>
					</div>
					<div id="tab4fram" data-options="region:'center'" style="border:0px;overflow:hidden">
						
					</div>
				</div>
			</div>
		</div>
	</body>
	<script type="text/javascript">
		var lastindex = 0;
		var isfirstload = true;
		var isexit = false;
		
		/* 初始化tab */
		$('#maintabs').tabs({
			tabWidth:120,
			tabHeight:38,
			fit:true,
			onBeforeSelect:function(title,index){
				if(isexit){
					$.messager.confirm('Confirm', 'Are you sure to exit the editor?', function(r){
						if (r){
							isexit = false;
							$('#maintabs').tabs('select',index);
						}
					});
					return false;
				}
			},
			onSelect:function(title,index){
				if(index==0){//范围配置页面
					//$("#playlisttitles").html("<bean:message key="content.SearchAll"/>");
					$("#tab2fram").html("");
					$("#tab3fram").html("");
					$("#tab4fram").html("");
					$("#tab1fram").html("<iframe name='content' src='jsp/rangeInfo.jsp' style='border:none;margin:0px' height='100%' width='100%'></iframe>");
				}
				if(index==1){//TRAFFIC日志页面
					//$("#terminaltitles").html("<bean:message key="terminal.SearchAllTerminal"/>");
					$("#tab1fram").html("");
					$("#tab3fram").html("");
					$("#tab4fram").html("");
					$("#tab2fram").html("<iframe name='content' src='jsp/errorInfo.jsp?modules=TRAFFIC' style='border:none;margin:0px' height='100%' width='100%'></iframe>");
				}
				if(index==2){//CONVERT日志页面
					//$("#playlisttitles").html("<bean:message key="content.SearchAll"/>");
					$("#tab1fram").html("");
					$("#tab2fram").html("");
					$("#tab4fram").html("");
					$("#tab3fram").html("<iframe name='content' src='jsp/errorInfo.jsp?modules=CONVERT' style='border:none;margin:0px' height='100%' width='100%'></iframe>");
				}
				if(index==3){//邮件页面
					//$("#playlisttitles").html("<bean:message key="content.SearchAll"/>");
					$("#tab1fram").html("");
					$("#tab2fram").html("");
					$("#tab3fram").html("");
					$("#tab4fram").html("<iframe name='content' src='jsp/emailInfo.jsp' style='border:none;margin:0px' height='100%' width='100%'></iframe>");
				}
			}
		});
		
		$(document).ready(function(){
			//alert(document.body.clientWidth);
		});

		/* 修改蓝色标题方法 */
		function updatetitle(id,title){
			$("#"+id).html(title);
		}
		
		function isexitfn(isexit){
			this.isexit = isexit;
		}
	</script>
</html>