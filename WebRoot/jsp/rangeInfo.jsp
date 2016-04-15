<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css"
	href="../css/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="../css/themes/icon.css">
<style>
#sub, #back {
	width: 120px;
	background: #307EB7;
	color: #ffffff;
	border-radius: 10px;
	margin-right: 3px;
	margin-left: 3px;
}
.bold{
	font-weight:bold;
}
#edittable{
	border-left: 1px solid #E9E9E9; 
	border-bottom: 1px solid #E9E9E9;
}
#edittable th{
	font-weight:bold;
	background-color:#F6F6F6;
}
#edittable td,#edittable th{
   border-right:1px solid #E9E9E9;
   border-top: 1px solid #E9E9E9;
}
</style>
<script type="text/javascript" src="../js/jquery.min.js?a1"></script>
<script type="text/javascript" src="../js/jquery.easyui.min.js?b2"></script>
<script type="text/javascript" src="../js/accordion-header.js?c3"></script>
<title>Range Manage</title>
</head>
<body class="easyui-layout">
	<div region="center" style="padding: 10px;">
		<div id="acc" class="easyui-accordion"
			data-options="multiple:true,border:false" style="width: 99%">
			<div id="search"
				data-options="tools:'#seachbutton',collapsible:false,selected:true"
				title="<span class='accstyle'><span></span>Search</span>">
				<table id="searchtable"  cellpadding="10">
					<tr>
						<td>Name:</td>
						<td><input class="inputtext" name="searchcode" id="searchcode" style="width:180px"/>
						</td>
						<td>Booking# Log:</td>
						<td>
							<select class="easyui-combobox"
								data-options="height:32,width:180"
								id="type" name="type">
									<option value="all">All</option>
									<option value="Active">Active</option>
							</select>
						</td>
						<td>Editable:</td>
						<td>
							<select class="easyui-combobox"
								data-options="height:32,width:180"
								id="editable" name="editable">
									<option value="all">All</option>
									<option value="Editable">Editable</option>
							</select>
						</td>
					</tr>
				</table>
				<br />
			</div>
			<div id="list" data-options="tools:'#menu',collapsible:false,selected:true"
				title="<span class='accstyle'><span></span>List</span>">
						<table id="ManageList" name="ManageList">
						</table>
			</div>
		</div>
	</div>
	<!--
	<div id="menu">
		<a href="#" class="easyui-menubutton" data-options="menu:'#mm1',plain:false,menuAlign:'right'" style="padding:10px;width:105px;height:30px;line-height:30px;color:#307EB7;background:-webkit-linear-gradient(white,silver);filter:none;"><span class='actionstyle'>Query</span></a>
	</div>
	
	<div id="mm1" style="width:230px;">
	 	 <div onclick='loadgrid(2,"","valid")'>Country</div>  
        <div onclick='loadgrid(3,"","Active","edit")'>Location</div>
        <div onclick='loadgrid(4,"","Active","edit")'>Booking Party</div>
    </div>-->
    
	<div id="seachbutton">
		<a href="#" class="easyui-linkbutton button" onclick="searchs()"
			style="width: 85px; height: 30px; line-height: 30px; color: #000000;">Search</a>
		<a href="#" class="easyui-linkbutton button" onclick="clearsearchs()"
			style="width: 125px; height: 30px; line-height: 30px; color: #000000;">Clear
			Search</a>
	</div>
	<!-- 
	<div id="showall">
		<table>
			<tr>
				<td class="bold"><input type="checkbox" id="check" name="check"
					onclick="changecheck()" />Show Log</td>
			</tr>
		</table>
	</div>
	-->
</body>
<script>
	var curlevel = "3";//页面初始化等级为location，默认查出location数据
	var local = "<%= request.getSession().getAttribute("local") %>";//获取当前用户地区
	var userid = "<%= request.getSession().getAttribute("userid") %>";//获取当前用户id
	var groupid = "<%= request.getSession().getAttribute("groupid") %>";//获取当前用户分组id
	
	/* 页面加载完毕后进入操作 */
	$(document).ready(function(){
		window.top.updatetitle("Maintaintitles", "Booking Range");//修改蓝色部分标题显示文字
		loadgrid(curlevel, "", "Active", "Editable");//加载树状表格，默认为Location等级的树状表格，子节点默认仅显示有效
		$("#type").combobox("setValue", "Active");//设置Booking# Log的选项为Active，默认查正在使用中的数据
		$("#editable").combobox("setValue", "Editable");
		accordion_header();//初始化折叠菜单
	});
	
	/* 表格筛选操作 */
  	function searchs(){
  		var searchcode = $("#searchcode").val();//获取Name的值
  		var searchtype = $("#type").combobox("getValue");//获取Booking# Log的值
  		var editable = $("#editable").combobox("getValue");//是否只显示可编辑的
  		loadgrid(curlevel, searchcode, searchtype, editable);//根据等级、Name、Booking# Log筛选数据
  	}
  	
	/* 重置条件操作 */
  	function clearsearchs(){
  		$("#searchcode").val("");
  		$("#editable").combobox("setValue", "Editable");
  		$("#type").combobox("setValue", "Active");
  		searchs();
  	}
  	
	/* 加载树状表格 leval:查询范围等级(Location、Booking Party) code:名称或编号  type:是否删除 */
	function loadgrid(leval, code, type, editable){
		curlevel = leval;
		$("#ManageList").treegrid({
			url:'<%=request.getContextPath()%>/Range.do', //进入后台url
			queryParams : {//传到后台的参数集合
				"action" : "loadlist",//操作为加载列表
				"level" : leval,//等级
				"code" : code,//编号
				"type" : type,//是否有效
				"editable" : editable
			},
			width : '100%',
			method: 'post',
			nowrap : false,
			showFooter : true,
			striped : true, //隔行变色
			pagination : true,//是否分页
			pageSize : 10,//每页默认显示行数
			pageNumber : 1,//默认起始页数
			idField : 'ID',//主键filed
			rownumbers : false,//是否显示编号
			fitColumns : true,
			scrollbarSize : 0,
			treeField:"CODE",//作为树节点的filed
			/* 表格列属性设置 */
			columns : [ [ {
				field : 'CODE',//显示后台数据中code的值
				title : 'NAME',//前台显示列的标题
				width : 190,formatter:function(val,row){
					if(val == 'default'){//将小写default的转为大写显示
						val = '默认值';
					}
					if(row.DESC_CN!=null){//判断是否有中文显示，有则显示
						return row.DESC_CN + "(" + val + ")";
					}
					return val;
				},styler:function(value,row,index){
					if(row.ISDELETE==1){//无效的范围文字显示为红色
						return "color:#FF0000;font-size:10px";
					}
					if(row.BK_LEVEL!=curlevel){//子节点的文字显示为10px
						return "font-size:10px";
					}
				}
			}, {
				field : 'NO_WAVE1_MIN',//显示WBP的最小值和最大值
				title : 'WBP',
				width : 100,
				formatter:function(val,row){
					if(row.BK_LEVEL==curlevel){//判断是否是父节点内容
						return val+"--"+row.NO_WAVE1_MAX;//显示格式为”最小值--最大值“
					}
					return '&nbsp;&nbsp;&nbsp;&nbsp;'+val+"--"+row.NO_WAVE1_MAX;//显示格式为”最小值--最大值“,并进行缩进
				},styler:function(value,row,index){
					if(row.ISDELETE==1){//无效的范围文字显示为红色
						return "color:#FF0000;font-size:10px";
					}
					if(row.BK_LEVEL!=curlevel){//子节点的文字显示为10px
						return "font-size:10px";
					}
				}
			}, {
				field : 'NO_WAVE1_LAST_USED_ID',//显示WBP的当前使用的值
				title : 'WBP_LAST',
				width : 80,
				formatter:function(val,row){
					if(row.ISDELETE==1){//无效的不显示
						return "";
					}
					if(row.BK_LEVEL==curlevel){//判断是否是父节点内容
						return val;//正常显示
					}
					return '&nbsp;&nbsp;&nbsp;&nbsp;'+val;//对显示进行缩进
				},styler:function(value,row,index){
					if(row.ISDELETE==1){//无效的范围文字显示为红色
						return "color:#FF0000;font-size:10px";
					}
					if(row.BK_LEVEL!=curlevel){//子节点的文字显示为10px
						return "font-size:10px";
					}
				}
			},{
				field : 'WAVE1_MIN',//显示CSSS的最小值和最大值
				title : 'CSSS',
				width : 100,
				formatter:function(val,row){
					if(row.BK_LEVEL==curlevel){//判断是否是父节点内容
						return val+"--"+row.WAVE1_MAX;//显示格式为”最小值--最大值“
					}
					return '&nbsp;&nbsp;&nbsp;&nbsp;'+val+"--"+row.WAVE1_MAX;//显示格式为”最小值--最大值“,并进行缩进
				},styler:function(value,row,index){
					if(row.ISDELETE==1){//无效的范围文字显示为红色
						return "color:#FF0000;font-size:10px";
					}
					if(row.BK_LEVEL!=curlevel){//子节点的文字显示为10px
						return "font-size:10px";
					}
				}
			}, {
				field : 'WAVE1_LAST_USED_ID',//显示CSSS的上一次使用的值
				title : 'CSSS_LAST',
				width : 80,
				formatter:function(val,row){
					if(row.ISDELETE==1){//无效的不显示
						return "";
					}
					if(row.BK_LEVEL==curlevel){//判断是否是父节点内容
						return val;//正常显示
					}
					return '&nbsp;&nbsp;&nbsp;&nbsp;'+val;//对显示进行缩进
				},styler:function(value,row,index){
					if(row.ISDELETE==1){//无效的范围文字显示为红色
						return "color:#FF0000;font-size:10px";
					}
					if(row.BK_LEVEL!=curlevel){//子节点的文字显示为10px
						return "font-size:10px";
					}
				}
			}, {
				field : 'OPTION',//显示操作按钮
				title : 'OPTION',
				width : 50,
				formatter:function(val,row){
					if(row.APL_OFFICE == local || row.CODE == local){//根据当前用户的等级和地区控制是否显示编辑按钮
						if(row.BK_LEVEL==curlevel){//子节点不显示编辑按钮
							var cn= (row.DESC_CN==null)?"":row.DESC_CN;
							cn = (row.DESC_CN==null)?"":row.DESC_CN.replace(/\ /g, "&nbsp;");
							return '<a href="#" class="easyui-linkbutton" data-options="size:large" onclick=defaultlist("'+row.ID+'","'+row.CODE+'","'+ curlevel +'","'+row.WAVE1_MIN+'","'+row.WAVE1_MAX+'","'+row.NO_WAVE1_MIN+'","'+row.NO_WAVE1_MAX+'","'+cn+'")>Enter</a>';
						}
					}
				},styler:function(value,row,index){
					if(row.ISDELETE==1){//无效的范围文字显示为红色
						return "color:#FF0000;font-size:10px";
					}
					if(row.BK_LEVEL!=curlevel){//子节点的文字显示为10px
						return "font-size:10px";
					}
				}
			} ] ],
			/* 加载成功执行操作 */
			onLoadSuccess : function() {
				$("#acc").accordion("select", 1);
			}
		});
	}
	
	/* 进入编辑页面并传入参数在蓝色标题处显示 */
	function defaultlist(id,code,level,WAVE1_MIN,WAVE1_MAX,NO_WAVE1_MIN,NO_WAVE1_MAX,DESC_CN){
		var name = "";
		if(DESC_CN != '' && DESC_CN != null && DESC_CN != 'undefined' && DESC_CN != 'null'){//判断中文名是否存在，存在则显示中文
			name=DESC_CN;
		}else{
			name=code;
		}
		window.top.updatetitle("Maintaintitles", "CurrentLevel：BusinessPartner (4) | Parent: " + name +" | "+"WBP Range:&nbsp;&nbsp;"+NO_WAVE1_MIN+"--"+NO_WAVE1_MAX+"&nbsp;&nbsp;&nbsp;&nbsp;CSSS Range:&nbsp;&nbsp;"+WAVE1_MIN+"--"+WAVE1_MAX);//修改蓝色标题文字，显示出当前编辑的code和范围信息
		location.href="rangeEdit.jsp?parentid="+id+"&code="+code+"&level="+level+"&wavemin="+WAVE1_MIN+"&wavemax="+WAVE1_MAX+"&nowavemin="+NO_WAVE1_MIN+"&nowavemax="+NO_WAVE1_MAX + "&name=" + name;//跳转到编辑页面
	}
</script>
</html>