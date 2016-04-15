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

.bold {
	font-weight: bold;
}

#edittable {
	border-left: 1px solid #E9E9E9;
	border-bottom: 1px solid #E9E9E9;
}

#edittable th {
	font-weight: bold;
	background: #F6F6F6;
}

#edittable td, #edittable th {
	border-right: 1px solid #E9E9E9;
	border-top: 1px solid #E9E9E9;
}

#acc2 .panel-tool {
	position: absolute;
	top: 50%;
	margin-top: -15px;
	height: 32px;
	overflow: hidden;
}

#acc2 .panel-icon {
	left: 5px;
	width: 16px;
}

#acc2 .panel-tool {
	right: 3px;
	width: auto;
}

#acc2 .panel-tool a {
	height: 30px;
}
</style>
<script type="text/javascript" src="../js/jquery.min.js?a1"></script>
<script type="text/javascript" src="../js/jquery.easyui.min.js?b2"></script>
<script type="text/javascript" src="../js/accordion-header.js?c3"></script>
<title>RangeMaintain</title>
</head>
<body class="easyui-layout">
	<div region="center" data-options="border:false" style="padding: 10px;">
		<div id="acc" class="easyui-accordion"
			data-options="multiple:true,border:false" style="width: 98%;">
			<div id="edit" data-options="tools:'#edittool',collapsible:false,selected:false"
				title="<span class='accstyle'><span></span>Edit</span>">
				<form id="form1" name="form1" method="POST">
					<span id='range' style="color: #FF0000"></span>
					<table id="edittable" name="edittable" width="100%" border="0"
						cellspacing="0" cellpadding="5" style="text-align: center">
						<thead>
							<tr>
								<th rowspan="2" style="width: 80px">NAME</th>
								<th colspan="3">WBP</th>
								<th colspan="3">CSSS</th>
								<th rowspan="2" style="width: 20px"></th>
							</tr>
							<tr>
								<th>MIN</th>
								<th>MAX</th>
								<th>LAST</th>
								<th>MIN</th>
								<th>MAX</th>
								<th>LAST</th>
							</tr>
						</thead>
						<tbody></tbody>
					</table>
					<input type="hidden" name="action" id="action" /> <input
						type="hidden" name="level"
						value="<%=request.getParameter("level")%>"> <input
						type="hidden" name="parentid" id="parentid"
						value="<%=request.getParameter("parentid")%>" />
				</form>
			</div>
			<div id="search"
				data-options="tools:'#seachbutton',collapsible:false,selected:false"
				title="<span class='accstyle'><span></span>Search</span>">
				<table id="searchtable"  cellpadding="10">
					<tr>
						<td>Name:</td>
						<td>
							<input class="inputtext" name="searchcode" id="searchcode" style="width:180px"/>
						</td>
					</tr>
				</table>
				<br />
			</div>
			<div id="list2" data-options="tools:'#showall',collapsible:false,selected:true"
				title="<span class='accstyle'><span></span>Data</span>"
				style="padding: 0px;">
				<div class="easyui-layout" style="height: 300px">
					<div data-options="region:'center'" style="padding: 0px;">
						<table id="defaultlist" name="defaultlist">
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div data-options="region:'south',border:false"
		style="text-align: right; padding: 12px 0px 0 0; height: 68px;">
		<a href="javascript:void(0)" id="sub" class="easyui-linkbutton"
			data-options="size:'large'" onclick="sub()"><span
			style="font-size: 18px">Commit</span></a> <a href="javascript:void(0)"
			onclick="back()" id="back" class="easyui-linkbutton"
			data-options="size:'large'"><span style="font-size: 18px">Return</span></a>
	</div>

	<div id="edittool">
		<table>
			<tr id="editlocation" style="display: none">
				<td class="bold">Please Select a
					Location:&nbsp;&nbsp;&nbsp;&nbsp; <select id="location"
					name="location" style="height: 25px; width: 180px;"></select>
					<button onclick="addtr('location')">+</button>
				</td>
			</tr>
			<tr id="editbp" style="display: none">
				<td class="bold" align=right>Please Select a Booking
					Party:&nbsp;&nbsp;&nbsp;&nbsp; <select id="bp" name="bp"
					style="height: 25px; width: 180px"></select>
					<button onclick="addtr('bp')">+</button>
				</td>
			</tr>
			<tr id="editorign" style="display: none">
				<td class="bold" align=right>Please Select a
					origin:&nbsp;&nbsp;&nbsp;&nbsp; <select id="orign" name="orign"
					style="height: 25px; width: 180px"></select>
					<button onclick="addtr('orign')">+</button>
				</td>
			</tr>
		</table>
	</div>
	
	<div id="seachbutton">
		<a href="#" class="easyui-linkbutton button" onclick="searchs()"
			style="width: 85px; height: 30px; line-height: 30px; color: #000000;">Search</a>
		<a href="#" class="easyui-linkbutton button" onclick="clearsearchs()"
			style="width: 125px; height: 30px; line-height: 30px; color: #000000;">Clear
			Search</a>
	</div>

	<div id="showall">
		<table>
			<tr>
				<td class="bold"><input type="checkbox" id="check" name="check"
					onclick="changecheck()" />Show Log</td>
			</tr>
		</table>
	</div>
</body>
<script>
	<% request.setCharacterEncoding("UTF-8");%>
	var parentid="<%=request.getParameter("parentid")%>";
	var level= parseInt(<%=request.getParameter("level")%>) + 1;
	var code="<%=request.getParameter("code")%>"; 
	var wavemax="<%=request.getParameter("wavemax")%>";
	var wavemin="<%=request.getParameter("wavemin")%>";
	var nowavemax="<%=request.getParameter("nowavemax")%>";
	var nowavemin="<%=request.getParameter("nowavemin")%>";
	var name = "<%=new String(request.getParameter("name").getBytes("iso-8859-1"), "utf-8")%>";
	
	/* 页面加载完毕后进入操作 */
	$(document).ready(function(){
		accordion_header();
		if(level == 3){	//编辑Location页面，已关闭
	  		$("#editlocation").css("display", "");
  			$("#editbp").css("display", "none");
			$("#editorign").css("display", "none");
			$("#action").val("editlocation");
		} else if(level == 4) { //编辑Business Partner
			$('#bp').combobox({//加载Business Partner的下拉列表,用于添加编辑操作
				url:'<%=request.getContextPath()%>/BpCombo.do?code=' + code,
				method:'get',
				valueField:'COMID',
				textField:'TEXT',
				editable:true,
				onLoadSuccess:function(){
					$('#bp').combobox("setValue","");
				},
				onSelect:function(data){
					//loadGrid(data.ID);
				},
				filter: function(q, row){//筛选时大小写不敏感
					var opts = $(this).combobox('options');
					return row[opts.textField].indexOf(q.toUpperCase()) == 0;
				}
			});
	  		$("#editlocation").css("display", "none");
  			$("#editbp").css("display", "");
			$("#editorign").css("display", "none");
			$("#action").val("editbp");
		} else if(level == 5) { //编辑Origin
	  		$("#editlocation").css("display", "none");
  			$("#editbp").css("display", "none");
			$("#editorign").css("display", "");
			$("#action").val("editorign");
		}
		queryList(parentid, "", "Active");//加载当前编辑数据下的子节点数据，根据parentid筛选
	});

	/* 表单提交操作 */
	$("#form1").form({
		url: "<%=request.getContextPath()%>/RangeDetail.do",
	    onSubmit: function(){
	    	 return $(this).form('enableValidation').form('validate');
	    },
	    success:function(data){
	    	//$("#defaultlist").datagrid("reload");
	    	queryList(parentid, "", "Active");
	    	$("#edittable tbody").html("");
	    	window.top.isexitfn(false);
	    }
	});
	
	/* 加载orign的下拉列表 */
	$('#orign').combobox({
		url:'<%=request.getContextPath()%>/OriginCombo.do',
		method:'GET',
		valueField:'COMID',
		textField:'TEXT',
		editable:true,
		onLoadSuccess:function(){
		},
		onSelect:function(data){
			//loadGrid(data.ID);
		},
		filter: function(q, row){//筛选时大小写不敏感
			var opts = $(this).combobox('options');
			return row[opts.textField].indexOf(q.toUpperCase()) == 0;
		}
	});
  	
	/* 判断是否显示无效数据 */
	function changecheck(){
		if($("#check").is(":checked")){
			queryList(parentid, "", "all");
		} else {
			queryList(parentid, "", "Active");
		}
	}
	
	/* 表格筛选操作 */
  	function searchs(){
  		var searchcode = $("#searchcode").val();//获取Name的值
  		/*
  		var defaultName = searchcode + "--默认值";
  		var index = defaultName.indexOf(searchcode);
  		if(index != -1){
  			searchcode = searchcode.concat(",default");
  		}*/
  		queryList(parentid, searchcode, "Active");//根据等级、Name、Booking# Log筛选数据
  	}
  	
	/* 重置条件操作 */
  	function clearsearchs(){
  		$("#searchcode").val("");
  		queryList(parentid, "", "Active");
  	}
	
	/* 加载当前编辑数据下的子节点数据表格 */
	function queryList(id, code, type){
		$("#parentid").val(id);
		$("#defaultlist").empty();
		$("#defaultlist").datagrid({
			url:'<%=request.getContextPath()%>/RangeDetail.do?cache=' + new Date().getTime(),
			method:'GET',
			queryParams : {
				"parentId" : parentid,//通过传入父id获取它下面的所有子节点信息
				"code" : code,
				"type":type
			},
			toolbar:[{//加载编辑按钮
				 text:'Edit',
		         iconCls:'icon-edit',
		         handler:function(){
		        	 dotoolbar('edit');
		        	 //alert('Edit');
		         }
			},{//加载删除按钮
	            text:'Remove',
	            iconCls:'icon-remove',
	            handler:function(){
	            	dotoolbar('remove');
	            	//alert('cut');
	            }
	        }],
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
			singleSelect: false,
			fit:true,
			scrollbarSize : 0,
			rowStyler: function(index,row){
				if(row.ISDELETE==1){
                    return 'color:#FF0000;';
                }
            },styler: function(value,row,index){  
                if (row.showFlag){//在datagrid中记录行分割线的显示，true：表示显示，false：表示不显示  
                    return ' border-bottom-color:black; border-bottom-width: 11 11 10px 11; border-right-color:black;';                   
                }else{  
                    return 'border-bottom-color:transparent;border-right-color:black;';  
                }    
        	},
			columns : [ [ {
				field : 'ID',
				title : 'ID',
				width : 100,
				checkbox:true
			},{
				field : 'CODE',
				title : 'NAME',
				width : 100,formatter:function(val,row){
					if(val == 'default'){
						val = name + '--默认值';
					}
					if(row.DESC_CN!=null){
						return row.DESC_CN + "(" + row.CODE + ")";
					}
					return val;
				}
			},{
				field : 'NO_WAVE1_MIN',
				title : 'WBP',
				width : 100,
				formatter:function(val,row){
					return val+"--"+row.NO_WAVE1_MAX;
				}
			}, {
				field : 'NO_WAVE1_LAST_USED_ID',
				title : 'WBP_LAST',
				width : 100
			}, {
				field : 'WAVE1_MIN',
				title : 'CSSS',
				width : 100,
				formatter:function(val,row){
					return val+"--"+row.WAVE1_MAX;
				}
			}, {
				field : 'WAVE1_LAST_USED_ID',
				title : 'CSSS_LAST',
				width : 100
			},{
				field : 'ISDELETE',
				title : 'OPTION',
				width : 100,
				formatter:function(val,row){
					if(val==1){
						return "Locked";
					}
					if(row.CODE.toUpperCase() == 'DEFAULT' || level == 5){
						return "";
					}
					var cn= (row.DESC_CN==null)?"":row.DESC_CN;
					cn = (row.DESC_CN==null)?"":row.DESC_CN.replace(/\ /g, "&nbsp;");
					return '<a href="#" class="easyui-linkbutton" data-options="size:large" onclick=defaultlist("'+row.ID+'","'+row.CODE+'","'+row.BK_LEVEL+'","'+row.WAVE1_MIN+'","'+row.WAVE1_MAX+'","'+row.NO_WAVE1_MIN+'","'+row.NO_WAVE1_MAX+'","'+cn+'")>Enter</a>';
				}
			} ] ],
			onLoadSuccess : function() {
				$("#mDefaultdiv").dialog("open");
				$("#acc").accordion("select",1);
			},
			onSelect: function(index,row){
				
			},
			onClickRow : function(rowIndex, rowData) {
				$("#defaultlist").datagrid("unselectRow",rowIndex);
			}
		});
	}
	
	/* 点击按钮后执行的操作 */
	function dotoolbar(type){
		var lists = $("#defaultlist").datagrid("getSelections");//获取选中的列表
		if(lists == 0){
			$.messager.alert("Message",'Please Check One Data');
			return;
		}
		if(type=="edit"){//编辑操作
			$("#acc").accordion("select", "<span class='accstyle'><span></span>Edit</span>");
			//displayEditTab();
			for(var j=0;j<lists.length;j++){
				if(lists[j].ISDELETE==1){
					continue;
				}
				var code = lists[j].CODE;
				if($('#'+code+'editid').length>0){
					continue;
				}
			    addnumberbox(code);//添加一行编辑
			    loadnumberbox(code,lists[j]);//填充数值
				i++;
			}
			$("#defaultlist").datagrid("uncheckAll");
		} else {//删除操作
			$.messager.confirm('Confirm', 'Are you sure Remove?', function(r){//删除前进行提示
				if (r){
					var ids= "";
					for(var j=0 ; j<lists.length ; j++){
						ids += lists[j].ID + ",";
						var code = lists[j].CODE;
						if(code == 'default'){//不能删除default
							$.messager.alert("Deleting default value is unallowed !");
							return;
						}
						if(lists[j].ISDELETE == '1'){//不能删除已删除的
							$.messager.alert("Deleted value can't re-delete !");
							return;
						}
						$('#'+code+'editid').parent().parent().remove();
					}
					if(ids != ""){
						ids = ids.substring(0 , ids.length - 1);
					}
					$.ajax({//提交后台删除
			           url: "<%=request.getContextPath() %>/RangeDetail.do?ids=" + ids,
			           method: "DELETE",
			           success: function(){
			        		changecheck();
					   }
					});
					if($("#edittable tbody").html()==""){
						$("#edittable tbody").html("");
						window.top.isexitfn(false);
					}
				}
			});
		}
	}
	
	/* 加载编辑区域的input*/
	function addnumberbox(code){
		$("#edittable").append("<tr class='tr"+i+"'><td><input type='hidden' id='"+code+"editid' name='"+code+"editid'/><span id='"+code+"show' name='"+code+"show'></span><input type='hidden' name='codes' value='"+code+"'  style='width:60px' readonly /></td><td><input id='"+code+"nowavemin' name='"+code+"nowavemin' code='"+code+"' class='easyui-numberbox numbox' data-options='min:"+nowavemin+",max:"+nowavemax+",precision:0,required:true'></td><td><input id='"+code+"nowavemax' name='"+code+"nowavemax' class='easyui-numberbox numbox' code='"+code+"' data-options='min:"+nowavemin+",max:"+nowavemax+",precision:0,required:true'/></td><td><input id='"+code+"nowavelast' name='"+code+"nowavelast' class='easyui-numberbox numbox' code='"+code+"' data-options='min:"+nowavemin+",max:"+nowavemax+",precision:0,required:true'/></td><td><input id='"+code+"wavemin' name='"+code+"wavemin' code='"+code+"' class='easyui-numberbox numbox' data-options='min:"+wavemin+",max:"+wavemax+",precision:0,required:true'></td><td><input id='"+code+"wavemax' name='"+code+"wavemax' code='"+code+"' class='easyui-numberbox numbox' data-options='min:"+wavemin+",max:"+wavemax+",precision:0,required:true'></td><td><input id='"+code+"wavelast' name='"+code+"wavelast' code='"+code+"' class='easyui-numberbox numbox' data-options='min:"+wavemin+",max:"+wavemax+",precision:0,required:true'></td><td><button class='linkbuttons' onclick=removetr('tr"+i+"')>-</button></td></tr>");
		$(".numbox").numberbox({//将input变为只能输入数字的input
			width:'130px',
			height:'28px',
			onChange:function(newValue,oldValue){
				//check(newValue);
			}
		});
		$("#"+code+"wavemin").numberbox({
			onChange:function(newValue,oldValue){
				if($("#"+code+"wavelast").numberbox("getValue")==""){
					$("#"+code+"wavelast").numberbox("setValue",newValue)
				}
			}
		});
		$("#"+code+"nowavemin").numberbox({
			onChange:function(newValue,oldValue){
				if($("#"+code+"nowavelast").numberbox("getValue")==""){
					$("#"+code+"nowavelast").numberbox("setValue",newValue)
				}
			}
		});
		$(".easyui-numberbox").numberbox("disableValidation");
	}
	
	/* 将已有的值则将值加入input中 */
	function loadnumberbox(code,data){
			var show = data.DESC_CN==null?data.CODE:data.DESC_CN;
			$("#"+code+"show").html(show);
			$("#"+code+"wavemax").numberbox("setValue",data.WAVE1_MAX);//wbp最大范围
			$("#"+code+"wavemin").numberbox("setValue",data.WAVE1_MIN);//wbp最小范围
			$("#"+code+"nowavemin").numberbox("setValue",data.NO_WAVE1_MIN);//CSSS最大范围
			$("#"+code+"nowavemax").numberbox("setValue",data.NO_WAVE1_MAX);//CSSS最小范围
			$("#"+code+"wavelast").numberbox("setValue",data.WAVE1_LAST_USED_ID);//wbp当前值
			$("#"+code+"nowavelast").numberbox("setValue",data.NO_WAVE1_LAST_USED_ID);//csss当前值
			$("#"+code+"editid").val(data.ID);//编辑数据的code
	}
	
	//添加一行编辑记录(type 编辑的类型)
	var i=0;
	function addtr(type){
		var code = "";
		if(type=="location"){
			var code = $('#location').combobox("getValue");
			$('#location').combobox("setValue","");
		} else if(type=="bp") {
			code = $('#bp').combobox("getValue");
		} else if(type=="orign") {
			code = $('#orign').combobox("getValue");
		}
		if(code == "" || code == " " || !code){
			$.messager.alert("Error","Plese Select a data");
			return;
		}
		if($('#'+code+'editid').length>0){
			$.messager.alert("Error","The Data has already edited");
			return;
		}
		$("#acc").accordion("select", "<span class='accstyle'><span></span>Edit</span>");//显示出编辑区域
		addnumberbox(code);//在编辑区加载出input框
		loadData(code,type);//为input框填充已有数据
		window.top.isexitfn(true);
		i++;
	}
	
	//第一次进行编辑时必须对默认default进行编辑
	function iseditdefault(){
		var data = $("#defaultlist").datagrid("getData").rows;
		if(data.length == 1 && data[0].CODE == "default"){
			if($('#defaulteditid').length==0){
				return false;
			}
		}
		return true;
	}
	
	/* 读取后台数据用于加载 code:用于读取后台信息的key type:读取的类型(location、booking party、orign) 该方法未使用 */

	function loadData(code,type){
		if(type == "location"){
			$.ajax({            
				url: "<%=request.getContextPath()%>/ManagerServlet?action=loadlocation&code="+code,
				method: "GET",            
				dataType:'json',
				async:false,
				success: function(data) {
			  		for(var i=0;i<data.rows.length;i++){
			  			if(data.rows[i].CODE!="default"){
			  				loadnumberbox(code,data.rows[i]);
			  			}
			  		}
				}
			});
		} else if(type == "bp") {
			$.ajax({            
				url: "<%=request.getContextPath()%>/BusinessPartner.do",
				method:'GET',
		        dataType:"json",
				data: { "code" : code },  
				async:false,
				success: function(data) {
					if(data.rows.length == 0){
						var show = $('#bp').combobox("getText");
						$("#"+code+"show").html(show);
					}
			  		for(var i=0;i<data.rows.length;i++){
			  			if(data.rows[i].CODE!="default"){
			  				loadnumberbox(code,data.rows[i]);
			  			}
			  		}
			  		$('#bp').combobox("setValue","");
				}
			});
		} else if(type == "orign") {
			$.ajax({
				url: "<%=request.getContextPath() %>/Origin.do",
				method: 'GET',
				data : { "code": code , "parentId" : parentid },
				dataType: "json",
				async: false,
				success: function(data) {
					if(data.rows.length==0){
						var show = $('#orign').combobox("getText");
						$("#"+code+"show").html(show);
					}
			  		for(var i=0;i<data.rows.length;i++){
			  			loadnumberbox(code,data.rows[i]);
			  		}
			  		$('#orign').combobox("setValue","");
				}
			});
		}
	}
	 
	/* 提交数据 */
	function sub(){
		if($("#edittable tbody").html()!=""){//判断是否有待保存数据
			if(iseditdefault()){//判断在首次编辑时是有对default进行编辑动作
				$.messager.confirm('Confirm', 'Are you sure Submit?', function(r){
					if (r){
						$("#form1").submit();
					}
				});
			}else{//没有对default进行编辑动作提示不做提交
				$.messager.alert("Error","first adding new record must change default value,please click default row and edit it!");
			}
		}else{
			$.messager.alert("Error","No Edited data");
		}
	}
	
	/* 移除编辑的行 id:行的id编号 */
	function removetr(id){
		$("."+id).remove();
		if($("#edittable tbody").html()==""){
			$("#edittable tbody").html("");
			window.top.isexitfn(false);
		}
	}
	
	/* 不进行编辑，返回操作 */
	function back(){
		if($("#edittable tbody").html()!=""){
			$.messager.confirm('Confirm', 'Are you sure to exit the editor?', function(r){
				if (r){
					judgeBackPage();
				}
			});
		} else {
			judgeBackPage();
		}
	}
	
	function judgeBackPage(){
		if(level == 5){
			location.href = "javascript:history.back(-1)";
		} else {
			location.href = "rangeInfo.jsp";
		}
	}
	
	function displayEditTab(){
		if(level == 3){
			$("#editlocation").css("display", "");
  			$("#editbp").css("display", "none");
			$("#editorign").css("display", "none");
			$("#action").val("editLocation");
		} else if(level == 4){
			$("#editlocation").css("display", "none");
  			$("#editbp").css("display", "");
			$("#editorign").css("display", "none");
			$("#action").val("editBp");
		} else if(level == 5){
			$("#editlocation").css("display", "none");
  			$("#editbp").css("display", "none");
			$("#editorign").css("display", "");
			$("#action").val("editOrigin");
		}
	}
	
	/* 进入编辑页面并传入参数在蓝色标题处显示 */
	function defaultlist(id,code,level,WAVE1_MIN,WAVE1_MAX,NO_WAVE1_MIN,NO_WAVE1_MAX,DESC_CN){
		var name = "";
		if(DESC_CN != '' && DESC_CN != null && DESC_CN != 'undefined' && DESC_CN != 'null'){//判断中文名是否存在，存在则显示中文
			name = DESC_CN;
		}else{
			name = code;
		}
		window.top.updatetitle("Maintaintitles", "CurrentLevel：Origin (5) | Parent: " + name +" | "+"WBP Range:&nbsp;&nbsp;"+NO_WAVE1_MIN+"--"+NO_WAVE1_MAX+"&nbsp;&nbsp;&nbsp;&nbsp;CSSS Range:&nbsp;&nbsp;"+WAVE1_MIN+"--"+WAVE1_MAX);//修改蓝色标题文字，显示出当前编辑的code和范围信息
		location.href="rangeEdit.jsp?parentid="+id+"&code="+code+"&level="+level+"&wavemin="+WAVE1_MIN+"&wavemax="+WAVE1_MAX+"&nowavemin="+NO_WAVE1_MIN+"&nowavemax="+NO_WAVE1_MAX + "&name=" + name;//跳转到编辑页面
	}
</script>
</html>