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
<title>EmailList</title>
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
						<td>ADDRESS:</td>
						<td><input class="inputtext" name="address" id="address" /></td>
						<td>TYPE:</td>
						<td>
							<select class="easyui-combobox"
								data-options="height:32,width:360"
								id="type" name="type">
									<option value="">ALL</option>
									<option value="Confirmation">CONFIRMATION</option>
									<option value="Amend">AMEND</option>
									<option value="Pending">PENDING</option>
									<option value="error">ERROR</option>
									<option value="NewBooking">NEWBOOKING</option>
							</select>
						</td>
					</tr>
					<tr>
						<td>STATUS:</td>
						<td><select class="easyui-combobox"
							data-options="height:32,width:$('#status').parent().width()"
							id="status" name="status">
								<option value="">ALL</option>
								<option value="wait">WAIT</option>
								<option value="sent">SENT</option>
								<option value="error">ERROR</option>
						</select></td>
						<td>DATE:</td>
						<td><input class="easyui-datebox" name="startdate"
							id="startdate" style="height: 32px; width: 180px" /> --- <input
							class="easyui-datebox" name="stoptime" id="stopdate"
							style="height: 32px; width: 180px" /></td>
					</tr>
				</table>
				<br />
			</div>
			<div id="list" data-options="tools:'#action',collapsible:false"
				title="<span class='accstyle'><span></span>Search Results</span>">
				<p><span id='Statistics' style="color:red;font-size:15px"></span></p>
				<table id="emailList" name="emailList">
				</table>
			</div>
		</div>
	</div>

	<div id="seachbutton">
		<a href="javascript:void(0)" class="easyui-linkbutton button" onclick="searchs()"
			style="width: 85px; height: 30px; line-height: 30px; color: #000000;">Search</a>
		<a href="javascript:void(0)" class="easyui-linkbutton button" onclick="clearsearchs()"
			style="width: 125px; height: 30px; line-height: 30px; color: #000000;">Clear
			Search</a>
	</div>

	<div id="mEditdiv" name="mEditdiv" title="Edit Email" style="background: #F5F5F5;">
		<div class="easyui-layout" data-options="border:false"
			style="height: 140px">
			<form
				method="post" id="form1" name="form1">
				<div data-options="region:'center',border:false"
					style="padding-top: 20px; padding-left: 10px;">
					Address: <input id="editaddress" name="editaddress" class="inputtext" />
					 <input type="hidden" id="emailid" name="emailid" />
				</div>
				<div data-options="region:'south',border:false"
					style="text-align: center; padding: 12px 0px 0 0; height: 68px; border-top: solid 1px #E9E9E9">
					<a href="javascript:void(0)" id="sub" class="easyui-linkbutton"
						data-options="size:'large'" onclick="commit()"><span
						style="font-size: 18px">Commit</span></a> <a href="javascript:void(0)"
						id="back" class="easyui-linkbutton" data-options="size:'large'"
						onclick="returns()"><span style="font-size: 18px">Return</span></a>
				</div>
				<input type="hidden" id="action" name="action" value="edit" />
			</form>
		</div>
	</div>
	
	<div id="mm1" style="width: 230px;">
		<div id="mm1-item" onclick="resend()">Resend</div>
	</div>
	
	<div id="selectedIdList"></div>


	<div id="action">
		<a href="javascript:void(0)" class="easyui-menubutton"
			data-options="menu:'#mm1',plain:false,menuAlign:'right'"
			style="padding: 10px; width: 105px; height: 30px; line-height: 30px; color: #307EB7; background: -webkit-linear-gradient(white, silver);"><span
			class='actionstyle'>Action</span></a>
	</div>
	
	<div id="mPreviewdiv" name="mPreviewdiv" title="Preview Email" style="background: #F5F5F5;">
		<div id="preview" name="preview" style="padding: 5px;max-height:660px;" >
		</div>
	</div>
</body>
<script>
	var isfirst = true;//记录是否是首次进入页面
	
	$(document).ready(function() {
  		var date = formatDate(new Date());//默认的时间区间为今天
  		$("#startdate").datebox('setValue' , date);
  		$("#stopdate").datebox('setValue' , date);
		loadGrid("all", "", "all", date, date);
		accordion_header();
	});

	/* 用于修改收件人地址，现暂时关闭 */
	$("#mEditdiv").dialog({
		width: 360,
		closed: true,
		modal: true,
		border: false,
		closable:true,
		draggable:false,
		resizable:false,
		shadow:false,
		onClose:function(){
			$("#editaddress").val("");
			$("#emailid").val("");
		}
	});
	
	/* 预览邮件内容 */
	$("#mPreviewdiv").dialog({
		width: 860,
		closed: true,
		modal: true,
		border: false,
		closable:true,
		draggable:false,
		resizable:false,
		shadow:false,
		onClose:function(){
		}
	});
	
	$("#form1").form({
		url: "<%=request.getContextPath()%>/Email.do",
	    onSubmit: function(){
	    	 
	    },
	    success:function(data){
	    	$("#emailList").datagrid("unselectAll");
	    	$("#emailList").datagrid("uncheckAll");
	    	$("#emailList").datagrid("reload");
	    	$("#mEditdiv").dialog("close");
	    }
	});
	
	/* 筛选操作 */
	function searchs() {
		var type = $("#type").combobox("getValue");//邮件类型
		var address = $("#address").val();//收件地址
		var status = $("#status").combobox("getValue");//邮件发送状态
		var startdate = $("#startdate").datebox("getValue");//开始时间
		var stopdate = $("#stopdate").datebox("getValue");//结束时间
		loadGrid(type, address, status, startdate, stopdate);
	}

	/* 清空筛选操作 */
	function clearsearchs() {
		$("#type").combobox("setValue","all");
		$("#address").val("");
		$("#status").combobox("setValue","all");
		$("#startdate").datebox("setValue", "");
		$("#stopdate").datebox("setValue", "");
		loadGrid("all", "", "all", "", "");
	}

	function loadGrid(type, address, status, startdate, stopdate) {
		$("#emailList").datagrid({
			url: "<%=request.getContextPath()%>/Email.do",
			method:"GET",
			queryParams : {
				//"action" : "getlist",
				"type" : type,
				"address" : address,
				"status" : status,
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
			singleSelect: false,
			rowStyler: function(index,row){
                 if (row.STATUS == "error"){
                     return 'color:#FF0000;';
                 }
            },
			columns : [ [ {
				field : 'ID',
				title : 'ID',
				checkbox:true
			},{
				field : 'ADDRESS',
				title : 'ADDRESS',
				width : 160
			}, {
				field : 'TITLE',
				title : 'TITLE',
				width : 140
			}, {
				field : 'STATUS',
				title : 'STATUS',
				width : 60,
				formatter:function(val,row){
					if("error" == val){
						return val + " (" + row.SENDINFO + ") ";
					}
					return val;
				}
			}, {
				field : 'MAIL_TYPE',
				title : 'TYPE',
				width : 60
			}, {
				field : 'CREATEDATE',
				title : 'CREATEDATE',
				width : 100
			},{
				field : 'OPTION',
				title : 'ACTION',
				width : 60,
				formatter:function(val,row){
					if(row.STATUS == "wait"){
						return '<a href="javascript:void(0)" class="easyui-linkbutton linkbutton" data-options="iconCls:icon-search" onclick=previewdialog()>Preview</a>';
					}else{
						return '<a href="javascript:void(0)" class="easyui-linkbutton linkbutton" data-options="iconCls:icon-search" onclick=previewdialog()>View</a>';
					}
					//return '<a href="javascript:void(0)" class="easyui-linkbutton linkbutton" data-options="iconCls:icon-search" onclick=previewdialog()>Preview</a>&nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton" onclick=opendialog('+row.ID+',"'+row.ADDRESS+'")>Edit</a>';
					
				}
			}]],
			onClickRow : function(rowIndex, rowData) {
				$("#preview").html(rowData.CONTENTCLOB);
				if(rowData.MAIL_TYPE=="Amend"){
					$("#mPreviewdiv").dialog("resize",{width: 860,height:500});
				}else{
					$("#mPreviewdiv").dialog("resize",{width: 310,height:300});
				}
				$("#mPreviewdiv").dialog("center");
				$("#emailList").datagrid("unselectRow",rowIndex);
			},
			onLoadSuccess : function() {
				var Statistics = $(this).datagrid("getData").Statistics;//统计邮件发送信息
				var send=0;
				var error=0;
				var wait=0;
				for( i=0; i<Statistics.length; i++){
					if(Statistics[i].STATUS == "error"){//发送失败数
						error = Statistics[i].COUNT;
					}
					if(Statistics[i].STATUS == "sent"){//发送成功数
						send = Statistics[i].COUNT;
					}
					if(Statistics[i].STATUS == "wait"){//等待发送数
						wait = Statistics[i].COUNT;
					}
				}
				$("#Statistics").html("WaitQty&nbsp;:&nbsp;&nbsp;"+wait+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;SentQty&nbsp;:&nbsp;&nbsp;"+send+"&nbsp;&nbsp&nbsp;&nbsp;&nbsp;&nbsp;ErrQty&nbsp;:&nbsp;&nbsp;"+error);
				$("#acc").accordion("select", 1);
				if(isfirst){
					isfirst=false;
					statusRefresh();//定时刷新发送状态
				}
			}
		});
	}
	
	function opendialog(id,address){
		$("#emailid").val(id);
		$("#editaddress").val(address);
		$("#mEditdiv").dialog("open");
	}
	
	function previewdialog(){
		$("#mPreviewdiv").dialog("open");
	}
	
	function commit(){
		 $("#form1").submit();
	}
	
	function returns(){
		$("#mEditdiv").dialog("close");
	}
	
	/* 邮件批量重新发送 */
	function resend(){
		var emailidList = $("#emailList").datagrid("getSelections");
		if (emailidList != null && emailidList !="") {
			var ids = "";
			for(var i = 0; i < emailidList.length; i++){
				ids += emailidList[i].ID + ",";
			}
			if(ids != ""){
				ids = ids.substring(0 , ids.length - 1);
			}
			$.ajax({
				url : "<%=request.getContextPath()%>/Email.do",
				method : "post",
				async:false,
				data:{ "ids" : ids },
				dataType : "json",
				success : function(data) {
			    	$("#emailList").datagrid("unselectAll");
			    	$("#emailList").datagrid("uncheckAll");
					$('#emailList').datagrid("reload");
				}
			});
		}
	}
	
	/* 邮件状态定时刷新 */
	function statusRefresh() {
		var emailidList = $("#emailList").datagrid("getRows");
		var type = $("#type").combobox("getValue");
		var address = $("#address").val();
		var status = $("#status").combobox("getValue");
		var startdate = $("#startdate").datebox("getValue");
		var stopdate = $("#stopdate").datebox("getValue");
		if (emailidList != null && emailidList !="") {
			var ids = "";
			for(var i = 0; i < emailidList.length; i++){
				ids += emailidList[i].ID + ",";
				//ids += "&IDS=" + emailidList[i].ID;
			}
			if(ids != ""){
				ids = ids.substring(0 , ids.length - 1);
			}
			$.ajax({
				url : "<%=request.getContextPath()%>/EmailStatus.do",
				method : "post",
				async:false,
				//data:"Content-type=application/x-www-form-urlencoded&type="+type+"&address="+address+"&status="+status+"&startdate="+startdate+"&stopdate="+stopdate+"&action=refreshstatus"+ids,
				data:{"type" : type , "address" : address , "status" : status , "startdate" : startdate , "stopdate" : stopdate , "ids" : ids},
				dataType : "json",
				success : function(data) {
					var Statistics = data.Statistics;
					var send=0;
					var error=0;
					var wait=0;
					for( i=0; i<Statistics.length; i++){
						if(Statistics[i].STATUS == "error"){
							error = Statistics[i].COUNT;
						}
						if(Statistics[i].STATUS == "sent"){
							send = Statistics[i].COUNT;
						}
						if(Statistics[i].STATUS == "wait"){
							wait = Statistics[i].COUNT;
						}
					}
					$("#Statistics").html("WaitQty&nbsp;:&nbsp;&nbsp;"+wait+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;SendQty&nbsp;:&nbsp;&nbsp;"+send+"&nbsp;&nbsp&nbsp;&nbsp;&nbsp;&nbsp;ErrQty&nbsp;:&nbsp;&nbsp;"+error);
					for(var i=0;i<data.length;i++){
						$('#emailList').datagrid('updateRow',{
							index: i,
							row: {
								STATUS: data.rows[i].STATUS
							}
						});
					}
					setTimeout("statusRefresh()",10000);
				}
			});
		}
	}
	
	/* 格式化日期 */
	function formatDate(date){
		var day = date.getDate() > 9 ? date.getDate() : "0" + date.getDate();
		var month = (date.getMonth() + 1) > 9 ? (date.getMonth() + 1) : "0" + (date.getMonth() + 1);
		return date.getFullYear() + '-' + month + '-' + day;
	}
</script>
</html>