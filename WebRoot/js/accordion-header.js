/*!
 * Copyright (C) 2010 Kazo Vision. (http://www.kazovision.com)
 * All rights reserved.
 *
 * 标题折叠效果。
*/
function accordion_header(){
	 $(".accordion").find(".accordion-header:first").css("border-width","0");
	 $(".accordion .panel-title span").children().addClass("accordion-selected");
	 $(".accordion .panel-title span").bind({"click":accordion_header_click});
	 $(".accordion .panel-title span[name='close']").children().removeClass("accordion-selected").addClass("accordion-unselected");
	 $(".accordion .panel-title span[name='close']").parent().parent().parent().find(".panel-tool").css("display","none");
	 
}

function accordion_header_click(){
	if($(this).parents(".accordion-header-selected").attr("class")){
		
		var accid=$(this).parent().parent().parent().parent().attr("id");
		$(this).parent().parent().parent().prev().find(".accordion-body").css("border-width","0");
		var p=$(this).parent().parent().parent().find(".panel-body");
		var button = $(this).parent().parent().parent().find(".panel-tool");
		$(button).css("display","none");
		var index = $('#'+accid).accordion('getPanelIndex', p);
		$('#'+accid).accordion('unselect',index);
		$(this).children().removeClass("accordion-selected").addClass('accordion-unselected');
	}else{
		var accid=$(this).parent().parent().parent().parent().attr("id");
		
		var p=$(this).parent().parent().parent().find(".panel-body");
		var index = $('#'+accid).accordion('getPanelIndex', p);
		var button = $(this).parent().parent().parent().find(".panel-tool");
		$(button).css("display","");
		$('#'+accid).accordion('select',index);
		$(this).children().removeClass("accordion-unselected").addClass('accordion-selected');
	}
}