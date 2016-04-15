<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css"
	href="../css/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="../css/themes/icon.css">

<script type="text/javascript" src="../js/jquery.min.js?aa=bb"></script>
<script type="text/javascript" src="../js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="../js/accordion-header.js"></script>
<style>
#sub, #back {
	width: 120px;
	background: #307EB7;
	color: #ffffff;
	border-radius: 10px;
	margin-right: 3px;
	margin-left: 3px;
}
</style>
<title>ShowErrorinfo</title>
</head>
<body class="easyui-layout">
	<div region="center" style="padding: 10px">
		<div id="acc" class="easyui-accordion"
			data-options="multiple:true,border:false" style="width: 99%">
			<div id="search"
				data-options="tools:'#seachbutton',collapsible:false"
				title="<span class='accstyle'><span></span>Search</span>">
				<table id="searchtable" style="width: 100%" cellpadding="10">
					<tr>
						<td>FILENAME:</td>
						<td><input class="inputtext" name="filename" id="filename" />
						</td>
						<td>DATE:</td>
						<td><input class="easyui-datebox" name="startdate"
							id="startdate" style="height: 32px; width: 180px" /> --- <input
							class="easyui-datebox" name="stoptime" id="stopdate"
							style="height: 32px; width: 180px" /></td>
					</tr>
				</table>
				<br />
			</div>
			<div id="list" data-options="collapsible:false"
				title="<span class='accstyle'><span></span>Search Results</span>">
				<p><span id='Statistics' style="color:red;font-size:15px"></span></p>
				
				<table id="messageList" name="messageList">
				</table>
			</div>
		</div>
	</div>

	<div id="seachbutton">
		<a href="#" class="easyui-linkbutton button" onclick="searchs()"
			style="width: 85px; height: 30px; line-height: 30px; color: #000000;">Search</a>
		<a href="#" class="easyui-linkbutton button" onclick="clearsearchs()"
			style="width: 125px; height: 30px; line-height: 30px; color: #000000;">Clear
			Search</a>
	</div>

</body>
<script>
		var modules = "<%=request.getParameter("modules")%>";//记录显示log的类型traffic或Convert
		
		$(document).ready(function(){
	  		accordion_header();
	  		var date = formatDate(new Date());//默认的时间区间为今天
	  		$("#startdate").datebox('setValue' , date);
	  		$("#stopdate").datebox('setValue' , date);
	  		loadGrid("",date,date);
	  		
	  	});
	  	
		/* 筛选操作 */
	  	function searchs(){
	  		var filename = $("#filename").val();
	  		var startdate=$("#startdate").datebox("getValue");
	  		var stopdate=$("#stopdate").datebox("getValue");
	  		loadGrid(filename,startdate,stopdate);
	  	}
	  	
		/* 清空筛选操作 */
	  	function clearsearchs(){
	  		$("#filename").val("");
	  		$("#startdate").datebox("setValue","");
	  		$("#stopdate").datebox("setValue","");
	  		loadGrid("","","");
	  	}
	  	
		/* 加载日志数据 */
		function loadGrid(filename,startdate,stopdate){
			$("#messageList").datagrid({
				url: '<%=request.getContextPath() %>/Error.do',
				method:'GET',
				queryParams : {
					"filename" : filename,
					"modules":modules,
					"startdate" : startdate,
					"stopdate" : stopdate
				},
				width : '100%',
				nowrap : false,
				showFooter : true,
				striped : true, //隔行变色
				pagination : true,
				pageSize : 10,
				pageNumber : 1,
				idField : 'ID',
				rownumbers : false,
				fitColumns : true,
				scrollbarSize : 0,
				columns : [ [ {
					field : 'ERROR_FILE_NAME',
					title : '&nbsp;&nbsp;ERROR_FILE_NAME',
					width : 100,
					formatter: function(val,row){
						return "&nbsp;&nbsp;" + val;
					}
				}, {
					field : 'ERROR_DESC',
					title : 'ERROR_DESC',
					width : 160
				}, {
					field : 'CREATE_DATE',
					title : 'CREATEDATE',
					width : 40
				} ] ],
				onClickRow : function(rowIndex, rowData) {
					$("#messageList").datagrid("unselectRow",rowIndex);
				},
				onLoadSuccess : function() {
					/* 加载完毕后统计数据 */
					var Statistics = $(this).datagrid("getData").Statistics;
					if(modules=="CONVERT"){
						if(Statistics.FILEQTY != undefined && Statistics.FILEQTY != null){
							$("#Statistics").html("FileQty&nbsp;:&nbsp;&nbsp;"+Statistics.FILEQTY+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;SuccQty&nbsp;:&nbsp;&nbsp;"+(Statistics.FILEQTY-Statistics.ERRQTY)+"&nbsp;&nbsp&nbsp;&nbsp;&nbsp;&nbsp;ErrQty&nbsp;:&nbsp;&nbsp;"+Statistics.ERRQTY);
						}else{
							$("#Statistics").html("FileQty&nbsp;:&nbsp;&nbsp;0&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;SuccQty&nbsp;:&nbsp;&nbsp;0&nbsp;&nbsp&nbsp;&nbsp;&nbsp;&nbsp;ErrQty&nbsp;:&nbsp;&nbsp;0");
						}
					}else{
						if(Statistics.FILEQTY != undefined && Statistics.FILEQTY != null){
							$("#Statistics").html("FileQty&nbsp;:&nbsp;&nbsp;"+Statistics.FILEQTY+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;WbpQty&nbsp;:&nbsp;&nbsp;"+Statistics.WBPQTY+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Q2cQty&nbsp;:&nbsp;&nbsp;"+Statistics.Q2CQTY+"&nbsp;&nbsp&nbsp;&nbsp;&nbsp;&nbsp;ErrQty&nbsp;:&nbsp;&nbsp;"+Statistics.ERRQTY);
						}else{
							$("#Statistics").html("FileQty&nbsp;:&nbsp;&nbsp;0&nbsp&nbsp;&nbsp;;&nbsp;&nbsp;&nbsp;WbpQty&nbsp;:&nbsp;&nbsp;0&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Q2cQty&nbsp;:&nbsp;&nbsp;0&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ErrQty&nbsp;:&nbsp;&nbsp;&nbsp;&nbsp;0");
						}
					}
					$("#acc").accordion("select", 1);
				}
		});
	}
	
	/* 格式化日期 */
	function formatDate(date){
		var day = date.getDate() > 9 ? date.getDate() : "0" + date.getDate();
		var month = (date.getMonth() + 1) > 9 ? (date.getMonth() + 1) : "0" + (date.getMonth() + 1);
		return date.getFullYear() + '-' + month + '-' + day;
	}
</script>
</html>