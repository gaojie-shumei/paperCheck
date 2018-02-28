function mydataTable(str){
	return $(str).dataTable({
	    dom: '<"html5buttons"B>lTfgitp',
	    /*
	     *  默认为true
	     *  是否自动计算列宽，计算列宽会花费一些时间，如果列宽通过aoColumns传递，可以关闭该属性作为优化
	     */
	    "bAutoWidth":false,
//	    "aoColumns":[
//	           {"sWidths":"140px"},
//	           {"sWidths":"140px"},
//	           {"sWidths":"140px"},
//	           {"sWidths":"140px"},
//	    ],
//	    scrollY: 400,
	    
	    /*
	     * 默认为true
	     * 是否允许终端用户从一个选择列表中选择分页的页数，页数为10，25，50和100，需要分页组件bPaginate的支持
	     * 
	     */
//	    "bLengthChange":false,
	    
	   /* 创建新的行追加到表格最后一行*/
	    "createdRow":function(row){
	    	$(row).insertAfter($(str+" tr:last"));  
	    },
	    /*
	     * 默认为false
	     * 是否开启状态保存，当选项开启的时候会使用一个cookie保存表格展示的信息的状态，例如分页信息，展示长度，过滤和排序等
	     * 这样当终端用户重新加载这个页面的时候可以使用以前的设置
	     */
	    'bStateSave': true,
	    "sPaginationType" : "full_numbers",
        "oLanguage" : {
            "sLengthMenu": "每页显示 _MENU_ 条记录",
            "sZeroRecords": "抱歉， 没有找到",
            "sInfo": "从 _START_ 到 _END_ /共 _TOTAL_ 条数据",
            "sInfoEmpty": "没有数据",
            "sInfoFiltered": "(从 _MAX_ 条数据中检索)",
            "sZeroRecords": "没有检索到数据",
             "sSearch": "搜索:",
            "oPaginate": {
            "sFirst": "首页",
            "sPrevious": "前一页",
            "sNext": "后一页",
            "sLast": "尾页"
            }
        },
	    buttons: [
	        { extend: 'copy',text:'copy'},
	        {extend: 'csv',text:'csv'},
	        {extend: 'excel',text:'excel'},
	        {extend: 'pdf',text:'pdf'},//title:'ExampleFile'},
	
	        {extend: 'print',text:'print',
	         customize: function (win){
	                $(win.document.body).addClass('white-bg');
	                $(win.document.body).css('font-size', '10px');
	
	                $(win.document.body).find('table')
	                        .addClass('compact')
	                        .css('font-size', 'inherit');
	        }
	        }
	    ]
	
	}).api();
}
