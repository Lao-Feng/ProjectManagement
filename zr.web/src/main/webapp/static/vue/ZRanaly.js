/**
 * 统计引擎工具js
 * 
 * @parms nfzr
 * @parms wwww.zrpower.cn
 */
let $zranaly;// vue注册
let $hot, $dataList,$headlen, $zrheads, $zrwidths, $spans;// 统计报表头部
let $zrdatatype;// 展示的数据列数
let $zrYears, $zrQuarters, $zrMonths, $zrWeeks;// 定义初始化年份、季度、月份、周
let ifExp = false;//不能导出excl

/**
 * 加载时，进行初始化值加载
 */
$zrYears = ZRanaly_years();
$zrQuarters = ZRanaly_quarters();
$zrMonths = ZRanaly_months();
$zrWeeks = ZRanaly_weeks();

/**
 * 统计引擎获取数据
 * 
 * @returns
 */
function ZRanaly_list(data, callback) {
	$zrdatatype = getColumnsType($zrwidths.length);//(($zrwidths.length + 1));
	if (ZRanaly_validation()) {
		$zranaly.dataLoading = true;
		$.post('/zranaly/statiscompute', data, function(list) {
			if (list.code == 0) {
				$hot = null;
				let json = JSON.parse(list.json);
				let dataJson = $zrheads;
				dataJson.splice($headlen - 1, dataJson.length);
				dataJson.push.apply(dataJson, json);
				$dataList = dataJson;
				ZRanaly_table(dataJson, $zrwidths, $spans);
				if(json!=null&&json.length >0 ){
					ifExp = true;
				}
				callback(json);
			} else {
				$zranaly.$message.error(rback.msg);
			}
			$zranaly.dataLoading = false;
		}, 'json')
	} else {
		$zranaly.$message.error('请选择完整的条件条件进行统计!');
	}
}

/**
 * 统计引擎通用模板
 * 
 * @param da
 * @returns
 */

function ZRanaly_table(heads, widths, spans) {
	$hot = null;
	$("#zrexcl").empty();
	$zrdatatype = getColumnsType(widths.length);//((widths.length + 1));//增加一行。扩宽
	var container = document.getElementById('zrexcl');
	$hot = new Handsontable(container, {
		data : heads,// 需要修改
		colWidths : widths,// 列宽
		//stretchH : 'last',// last:延伸最后一列,all:延伸所有列,none默认不延伸。
		minCols : widths.length,//+ 1,// 列+1
		autoColumnSize : true, // 自适应列大小
		autoWrapCol : true,
		autoWrapRow : true,
		colHeaders : true,
		rowHeaders : true,
		mergeCells : true,// 合并单元格
		// columnSorting: true,//是否打开列排序 默认是false
		columns : $zrdatatype,
		mergeCells : spans,// 合并的列
		enterBeginsEditing : false,
		manualColumnResize : true,
		wordWrap : true,// 自动换行
		manualRowResize : true,
		readOnly : true,// 只读
		afterOnCellMouseDown:onclickRowOrCol,
	});
}
/**
 * 报表引擎点击事件
 * @param event
 * @param coords
 * @param TD
 * @returns
 */
function onclickRowOrCol(event, coords, TD){
	let e = $zranaly;
	if(coords.row >= 0 && coords.col == -1 && coords.row >= $headlen-1){//点击行
		if($dataList[coords.row].ZRDOWN != 0){
			let data = e.hidden;
			let unitId = e.hidden.UNITID;
			data.UNITID = $dataList[coords.row].ZRDOWN;
		    ZRanaly_list(data,function(backdata){});
		    e.hidden.UNITID = unitId;
		}
	}else if(coords.row == -1 && coords.col >= 0){//点击列
		console.info('单击列')
	}else if(coords.row >= 0 && coords.col >= 0 && coords.row >= $headlen-1){//点击单元格
		let field = 'FIELD'+(coords.col+1);//单元格名称
		let fieldval = $dataList[coords.row][field];//单元格值
		let row = $dataList[coords.row];//单元格所在行
		if(e.hidden.tdLink != null && e.hidden.tdLink != ''){
			window.location.href = e.hidden.tdLink;
		}
	}
}

/*******************************************************************************
 * 生成统计报表数据json对应格式
 * 
 * @param len
 * @returns
 */
function getColumnsType(len) {
	let data = [];
	for (let i = 1; i <= len; i++) {
		data.push({
			data : 'FIELD' + i
		});
	}
	return data;
}

/**
 * 生成报表统计年度选择（初始年度2000年）
 * 
 * @returns
 */
function ZRanaly_years() {
	let years = [];
	let nowYear = new Date().getFullYear();
	for (var i = 2000; i <= parseInt(nowYear); i++) {
		years.push({
			value : i,
			label : i
		});
	}
	return years;
}
/**
 * 生成报表统计季度
 * 
 * @returns
 */
function ZRanaly_quarters() {
	let values = [], dx = [ '一', '二', '三', '四' ];
	for (var i = 1; i <= 4; i++) {
		values.push({
			value : i,
			label : '第' + dx[i - 1] + '季度'
		});
	}
	return values;
}
/**
 * 生成报表统计月份
 * 
 * @returns
 */
function ZRanaly_months() {
	let values = [], dx = [ '一', '二', '三', '四', '五', '六', '七', '八', '九', '十',
			'十一', '十二' ];
	let nowYear = new Date().getFullYear();
	for (var i = 1; i <= 12; i++) {
		let n = i >= 10 ? '' + i : '0' + i;
		values.push({
			value : n,
			label : dx[i - 1] + '月'
		});
	}
	return values;
}
/**
 * 生成报表统计周下拉
 * 
 * @returns
 */
function ZRanaly_weeks() {
	let values = [];
	let nowYear = new Date().getFullYear();
	for (var i = 1; i <= 52; i++) {
		values.push({
			value : i,
			label : '第' + i + '周'
		});
	}
	return values;
}

/**
 * 报表引擎条件日期类型选择监听
 * 
 * @param data
 * @returns
 */
function ZRanaly_query_type(data) {
	if (data != null && data.length > 0) {
		let val = data[data.length - 1];
		$zranaly.query.type = [ '' + val ];
		ZRanaly_init_show(val);
	}
}

/**
 * 初始化条件显示隐藏
 * 
 * @returns
 */
// 条件控件，不能删除
let $hidden_type, $hidden_year1, $hidden_year2, $hidden_quarter, $hidden_quarter2, $hidden_month, $hidden_month2, $hidden_week;
let $hidden_date1, $hidden_date2;// 日期类型
let $hidden_code;//字典
function ZRanaly_init_show(type) {
	let e = $zranaly;
	if (e.hidden.SINPUTTYPE == '2') {
		if (type == null) {
			e.query.type = [ '1' ];
			e.query.year1 = new Date().getFullYear();
		} else {
			e.query.type = [ type ];
		}
	}
	if (e.hidden.SINPUTTYPE == 'A' || e.hidden.SINPUTTYPE == 'C') {
		e.query.type = [ '3' ];
		e.query.year1 = new Date().getFullYear();
	}
	// 类别显示隐藏
	$hidden_type = function() {
		return e.hidden.SINPUTTYPE == '2';
	}();
	// 年份显示隐藏（年份1）
	$hidden_year1 = function() {
		if (e.hidden.SINPUTTYPE == '2') {
			return e.query.type[0] != '5' && e.query.type[0] != '6';
		} else if (e.hidden.SINPUTTYPE == '9' || e.hidden.SINPUTTYPE == 'A'
				|| e.hidden.SINPUTTYPE == 'C') {
			e.query.year1 = new Date().getFullYear();
			return true;
		} else if (e.hidden.SINPUTTYPE == '6' || e.hidden.SINPUTTYPE == '7' || e.hidden.SINPUTTYPE == '8') {
			e.query.year1 = new Date().getFullYear() - 1;
			return true;
		}
		return false;
	}();
	// 年份显示隐藏（年份2）
	$hidden_year2 = function() {
		if (e.hidden.SINPUTTYPE == '6' || e.hidden.SINPUTTYPE == '7' || e.hidden.SINPUTTYPE == '8') {
			e.query.year2 = new Date().getFullYear();
			return true;
		}
		return false;
	}();
	// 季度显示隐藏（季度1）
	$hidden_quarter = function() {
		if (e.hidden.SINPUTTYPE == '2') {
			return e.query.type[0] == '2';
		} else if (e.hidden.SINPUTTYPE == '8') {
			return true;
		}
		return false;
	}();
	// 季度显示隐藏（季度2）
	$hidden_quarter2 = function() {
		if (e.hidden.SINPUTTYPE == '8') {
			return true;
		}
		return false;
	}();
	// 月份显示隐藏（月份1）
	$hidden_month = function() {
		if (e.hidden.SINPUTTYPE == '2') {
			return e.query.type[0] == '3';
		} else if (e.hidden.SINPUTTYPE == 'A' || e.hidden.SINPUTTYPE == '7') {
			let nowDate = new Date();
			e.query.Month = nowDate.getMonth() + 1 < 10 ? "0"
					+ (nowDate.getMonth() + 1) : nowDate.getMonth() + 1;
			return true;
		} else if (e.hidden.SINPUTTYPE == 'C') {
			let nowDate = new Date();
			e.query.Month = nowDate.getMonth() == 0 ? "12" : nowDate.getMonth();
			e.query.Month = e.query.Month < 10 ? "0" + e.query.Month
					: e.query.Month;
			return true;
		}
		return false;
	}();
	// 月份显示隐藏（月份2）
	$hidden_month2 = function() {
		if (e.hidden.SINPUTTYPE == '7') {
			let nowDate = new Date();
			e.query.Month2 = nowDate.getMonth() + 1 < 10 ? "0"
					+ (nowDate.getMonth() + 1) : nowDate.getMonth() + 1;
			return true;
		}
		return false;
	}();
	// 周显示隐藏
	$hidden_week = function() {
		if (e.hidden.SINPUTTYPE == 2) {
			return e.query.type[0] == '4';
		}
		return false;
	}();
	// 日期显示隐藏
	$hidden_date1 = function() {
		if (e.hidden.SINPUTTYPE == '2') {
			return e.query.type[0] == '5';
		} else if (e.hidden.SINPUTTYPE == '3') {
			e.query.date1 = ZRuntilDateDay(new Date(), 0);
			return true;
		} else if (e.hidden.SINPUTTYPE == 'B') {
			e.query.date1 = ZRuntilDateDay(new Date(), -1);
			return true;
		}
		return false;
	}();
	// 日期范围显示隐藏
	$hidden_date2 = function() {
		return e.query.type[0] == '6';
	}();
	//字典选择控件
	$hidden_code = function() {
		if(e.hidden.SINPUTTYPE == '3' || e.hidden.SINPUTTYPE == '8' || e.hidden.SINPUTTYPE == '9'
			|| e.hidden.SINPUTTYPE == 'A' || e.hidden.SINPUTTYPE == 'C'){
			return e.codelist != null && e.codelist.length >0 ;
		}
		return false;
	}();
}
/**
 * 提交前验证
 * 
 * @returns
 */
function ZRanaly_validation() {
	let e = $zranaly;
	if (e.hidden.SINPUTTYPE == '2') {
		return ZRstatistics2();
	} else if (e.hidden.SINPUTTYPE == '3' || e.hidden.SINPUTTYPE == 'B') {
		return ZRstatistics3();
	} else if (e.hidden.SINPUTTYPE == '9') {
		return ZRstatistics4();
	} else if (e.hidden.SINPUTTYPE == '6') {
		return ZRstatistics5();
	} else if (e.hidden.SINPUTTYPE == '7') {
		return ZRstatistics6();
	}else if (e.hidden.SINPUTTYPE == '8') {
		return ZRstatistics7();
	}else if(e.hidden.SINPUTTYPE == 'A' || e.hidden.SINPUTTYPE == 'C'){
		return ZRstatisticsA();
	}else if(e.hidden.SINPUTTYPE == '1'){
		return true;
	}
	return false;
}

/**
 * 提交统计前，进行条件值转换（年月周日固定格式条件）
 * 
 * @returns
 */
function ZRstatistics2() {
	let e = $zranaly;
	let type = e.query.type[0];
	let sdate = '';
	let edate = '';
	let year1 = typeof (e.query.year1) != "undefined" ? e.query.year1 : null;
	let Quarter = typeof (e.query.Quarter) != "undefined" ? e.query.Quarter
			: null;
	let Month = typeof (e.query.Month) != "undefined" ? e.query.Month : null;
	let Week = typeof (e.query.Week) != "undefined" ? e.query.Week : null;
	let date1 = typeof (e.query.date1) != "undefined" ? e.query.date1 : null;
	let date2 = typeof (e.query.date2) != "undefined" ? e.query.date2 : null;
	if (type == '1' && year1 != null) {
		e.hidden.P1 = year1 + '-01-01';
		e.hidden.P2 = year1 + '-12-31';
		return true;
	} else if (type == '2' && year1 != null) {
		if (Quarter == '1') {
			e.hidden.P1 = year1 + '-01-01';
			e.hidden.P2 = year1 + '-03-31';
			return true;
		} else if (Quarter == '2') {
			e.hidden.P1 = year1 + '-04-01';
			e.hidden.P2 = year1 + '-06-30';
			return true;
		} else if (Quarter == '3') {
			e.hidden.P1 = year1 + '-07-01';
			e.hidden.P2 = year1 + '-09-30';
			return true;
		} else if (Quarter == '4') {
			e.hidden.P1 = year1 + '-10-01';
			e.hidden.P2 = year1 + '-12-31';
			return true;
		} else {
			return false;
		}
	} else if (type == '3' && year1 != null) {
		if (Month == '01' || Month == '03' || Month == '05' || Month == '07'
				|| Month == '08' || Month == '10' || Month == '12') {
			e.hidden.P1 = year1 + '-' + Month + '-01';
			e.hidden.P2 = year1 + '-' + Month + '-31';
			return true;
		} else if (Month == '04' || Month == '06' || Month == '09'
				|| Month == '11') {
			e.hidden.P1 = year1 + '-' + Month + '-01';
			e.hidden.P2 = year1 + '-' + Month + '-30';
			return true;
		} else if (Month == '02' && RunNian(year1) == true) {
			e.hidden.P1 = year1 + '-' + Month + '-02';
			e.hidden.P2 = year1 + '-' + Month + '-29';
			return true;
		} else if (Month == '02' && RunNian(year1) == false) {
			e.hidden.P1 = year1 + '-' + Month + '-02';
			e.hidden.P2 = year1 + '-' + Month + '-28';
			return true;
		} else {
			return false;
		}
	} else if (type == '4' && year1 != null && Week != null) {
		var d = new Date(year1, 0, 1);
		d.setDate(parseInt('1065432'.charAt(d.getDay())) + Week * 7);
		var fe = getFirstAndEnd(d);
		e.hidden.P1 = (fe.first).toISOString().slice(0, 10);
		e.hidden.P2 = (fe.end).toISOString().slice(0, 10);
		return true;
	} else if (type == '5' && date1 != null) {
		e.hidden.P1 = date1;
		e.hidden.P2 = date1;
		return true;
	} else if (type == '6' && date2 != null) {
		e.hidden.P1 = date2[0];
		e.hidden.P2 = date2[1];
		return true;
	} else {
		return false;
	}
	return false;
}
/**
 * 提交统计前，进行条件值转换（月报固定格式条件）
 * 
 * @returns
 */
function ZRstatisticsA() {
	let e = $zranaly;
	let sdate = '';
	let edate = '';
	let year1 = typeof (e.query.year1) != "undefined" ? e.query.year1 : null;
	let Month = typeof (e.query.Month) != "undefined" ? e.query.Month : null;
	if (year1 != null && Month != null) {
		e.hidden.P1 = year1;
		e.hidden.P2 = Month;
		if($hidden_code){
			let code = typeof (e.query.code) != "undefined" ? e.query.code : null;
			if(code != null){
				e.hidden.P3 = code.split('/')[0];
				e.hidden.P4 = code.split('/')[1];
			}
		}
		return true;
	}
	return false;
}

/**
 * 提交统计前，进行条件值转换（日报固定格式条件、日报固定格式条件(默认上一天日期)）
 * 
 * @returns
 */
function ZRstatistics3() {
	let e = $zranaly;
	let sdate = '';
	let edate = '';
	let date1 = typeof (e.query.date1) != "undefined" ? e.query.date1 : null;
	if (date1 != null) {
		e.hidden.P1 = date1;
		if($hidden_code){
			let code = typeof (e.query.code) != "undefined" ? e.query.code : null;
			if(code != null){
				e.hidden.P2 = code.split('/')[0];
				e.hidden.P3 = code.split('/')[1];
			}
		}
		return true;
	}
	return false;
}
/**
 * 提交统计前，进行条件值转换（年报固定格式条件）
 * 
 * @returns
 */
function ZRstatistics4() {
	let e = $zranaly;
	let sdate = '';
	let edate = '';
	let year1 = typeof (e.query.year1) != "undefined" ? e.query.year1 : null;
	if (year1 != null) {
		e.hidden.P1 = year1;
		if($hidden_code){
			let code = typeof (e.query.code) != "undefined" ? e.query.code : null;
			if(code != null){
				e.hidden.P2 = code.split('/')[0];
				e.hidden.P3 = code.split('/')[1];
			}
		}
		return true;
	}
	return false;
}
/**
 * 提交统计前，进行条件值转换（年份比较条件）
 * 
 * @returns
 */
function ZRstatistics5() {
	let e = $zranaly;
	let sdate = '';
	let edate = '';
	let year1 = typeof (e.query.year1) != "undefined" ? e.query.year1 : null;
	let year2 = typeof (e.query.year2) != "undefined" ? e.query.year2 : null;
	if (year1 != null && year2 != null) {
		e.hidden.P1 = year1;
		e.hidden.P2 = year2;
		return true;
	}
	return false;
}
/**
 * 提交统计前，进行条件值转换（年份月份比较条件）
 * 
 * @returns
 */
function ZRstatistics6() {
	let e = $zranaly;
	let dateS1 = '';
	let dateE1 = '';
	let dateS2 = '';
	let dateE2 = '';
	let year1 = typeof (e.query.year1) != "undefined" ? e.query.year1 : null;
	let year2 = typeof (e.query.year2) != "undefined" ? e.query.year2 : null;
	let month1 = typeof (e.query.Month) != "undefined" ? e.query.Month : null;
	let month2 = typeof (e.query.Month2) != "undefined" ? e.query.Month2 : null;
	if (year1 != null && year2 != null && month1 != null && month2 != null) {
		if (month1 == '01' || month1 == '03' || month1 == '05'
				|| month1 == '07' || month1 == '08' || month1 == '10'
				|| month1 == '12') {
			dateS1 = year1 + '-' + month1 + '-01';
			dateE1 = year1 + '-' + month1 + '-31';
		}
		if (month1 == '04' || month1 == '06' || month1 == '09'
				|| month1 == '11') {
			dateS1 = year1 + '-' + month1 + '-01';
			dateE1 = year1 + '-' + month1 + '-30';
		}
		if (month1 == '02' && RunNian(year1) == true) {
			dateS1 = year1 + '-' + month1 + '-02';
			dateE1 = year1 + '-' + month1 + '-29';
		}
		if (month1 == '02' && RunNian(year1) == false) {
			dateS1 = year1 + '-' + month1 + '-02';
			dateE1 = year1 + '-' + month1 + '-28';
		}
		if (month2 == '01' || month2 == '03' || month2 == '05'
				|| month2 == '07' || month2 == '08' || month2 == '10'
				|| month2 == '12') {
			dateS2 = year2 + '-' + month2 + '-01';
			dateE2 = year2 + '-' + month2 + '-31';
		}
		if (month2 == '04' || month2 == '06' || month2 == '09'
				|| month2 == '11') {
			dateS2 = year2 + '-' + month2 + '-01';
			dateE2 = year2 + '-' + month2 + '-30';
		}
		if (month2 == '02' && RunNian(year2) == true) {
			dateS2 = year2 + '-' + month2 + '-02';
			dateE2 = year2 + '-' + month2 + '-29';
		}
		if (month2 == '02' && RunNian(year2) == false) {
			dateS2 = year2 + '-' + month2 + '-02';
			dateE2 = year2 + '-' + month2 + '-28';
		}
		e.hidden.P1 = dateS1;
		e.hidden.P2 = dateE1;
		e.hidden.P3 = dateS2;
		e.hidden.P4 = dateE2;
		return true;
	}
	return false;
}
/**
 * 提交统计前，进行条件值转换（年份季度比较条件）
 * 
 * @returns
 */
function ZRstatistics7() {
	let e = $zranaly;
	let dateS1 = '';
	let dateE1 = '';
	let dateS2 = '';
	let dateE2 = '';
	let year1 = typeof (e.query.year1) != "undefined" ? e.query.year1 : null;
	let year2 = typeof (e.query.year2) != "undefined" ? e.query.year2 : null;
	let Quarter1 = typeof (e.query.Quarter) != "undefined" ? e.query.Quarter
			: null;
	let Quarter2 = typeof (e.query.Quarter2) != "undefined" ? e.query.Quarter2
			: null;
	if (year1 != null && year2 != null && Quarter1 != null && Quarter2 != null) {
		if (Quarter1 == '1') {
			dateS1 = year1 + '-01-01';
			dateE1 = year1 + '-03-31';
		}
		if (Quarter1 == '2') {
			dateS1 = year1 + '-04-01';
			dateE1 = year1 + '-06-30';
		}
		if (Quarter1 == '3') {
			dateS1 = year1 + '-07-01';
			dateE1 = year1 + '-09-30';
		}
		if (Quarter1 == '4') {
			dateS1 = year1 + '-10-01';
			dateE1 = year1 + '-12-31';
		}
		if (Quarter2 == '1') {
			dateS2 = year2 + '-01-01';
			dateE2 = year2 + '-03-31';
		}
		if (Quarter2 == '2') {
			dateS2 = year2 + '-04-01';
			dateE2 = year2 + '-06-30';
		}
		if (Quarter2 == '3') {
			dateS2 = year2 + '-07-01';
			dateE2 = year2 + '-09-30';
		}
		if (Quarter2 == '4') {
			dateS2 = year2 + '-10-01';
			dateE2 = year2 + '-12-31';
		}
		e.hidden.P1 = dateS1;
		e.hidden.P2 = dateE1;
		e.hidden.P3 = dateS2;
		e.hidden.P4 = dateE2;
		return true;
	}
	return false;
}

/**
 * 导出统计引擎报表数据到excl
 * @param url
 * @returns
 */
function ZRexp_analy_excel(){
	let e = $zranaly;
	let url = '/zranaly/statisexcl?ID='+e.hidden.ID;
	if(ifExp&&e.hidden.ID!=null&&e.hidden.ID!=''){
		location.href = url;
	}else{
		e.$message.error('报表无数据，不支持导出到表格!');
	}
}

/**
 * 周转换为日期
 * 
 * @param d
 * @returns
 */
function getFirstAndEnd(d) {
	var w = d.getDay(), n = 24 * 60 * 60 * 1000;
	var first = new Date(d.getTime() - parseInt('6012345'.charAt(w)) * n);
	var end = new Date(d.getTime() + parseInt('0654321'.charAt(w)) * n);
	return {
		first : first,
		end : end
	};
}
/**
 * 获取当前时间函数【yyyy-MM-DD hh:mm】
 * 
 * @param timeStamp
 * @param type
 *            cn\zh
 * @returns
 */
function dateTimeFormate(timeStamp, type) {
	let year = new Date(timeStamp).getFullYear();
	let month = new Date(timeStamp).getMonth() + 1 < 10 ? "0"
			+ (new Date(timeStamp).getMonth() + 1) : new Date(timeStamp)
			.getMonth() + 1;
	let date = new Date(timeStamp).getDate() < 10 ? "0"
			+ new Date(timeStamp).getDate() : new Date(timeStamp).getDate();
	let hh = new Date(timeStamp).getHours() < 10 ? "0"
			+ new Date(timeStamp).getHours() : new Date(timeStamp).getHours();
	let mm = new Date(timeStamp).getMinutes() < 10 ? "0"
			+ new Date(timeStamp).getMinutes() : new Date(timeStamp)
			.getMinutes();
	let time = year + "-" + month + "-" + date + " " + hh + ":" + mm;
	if (type == "zh") {
		time = year + "年" + month + "月" + date + "日" + " " + hh + ":" + mm;
	}
	return time;
}

/**
 * 获取当前时间函数【yyyy-MM-DD】
 * 
 * @param timeStamp
 * @param type
 *            cn\zh
 * @returns
 */
function dateFormate(timeStamp, type) {
	let year = new Date(timeStamp).getFullYear();
	let month = new Date(timeStamp).getMonth() + 1 < 10 ? "0"
			+ (new Date(timeStamp).getMonth() + 1) : new Date(timeStamp)
			.getMonth() + 1;
	let date = new Date(timeStamp).getDate() < 10 ? "0"
			+ new Date(timeStamp).getDate() : new Date(timeStamp).getDate();
	let time = year + "-" + month + "-" + date;
	if (type == "zh") {
		time = year + "年" + month + "月" + date + "日";
	}
	return time;
}

/**
 * 获取当前时间函数【yyyy-MM】
 * 
 * @param timeStamp
 * @param type
 *            cn\zh
 * @returns
 */
function dateYmFormate(timeStamp, type) {
	let year = new Date(timeStamp).getFullYear();
	let month = new Date(timeStamp).getMonth() + 1 < 10 ? "0"
			+ (new Date(timeStamp).getMonth() + 1) : new Date(timeStamp)
			.getMonth() + 1;
	let date = new Date(timeStamp).getDate() < 10 ? "0"
			+ new Date(timeStamp).getDate() : new Date(timeStamp).getDate();
	let time = year + "-" + month;
	if (type == "zh") {
		time = year + "年" + month + "月";
	}
	return time;
}

/**
 * 获取系统日期
 * 
 * @param date
 *            日期字符串/new Date()
 * @param days
 *            相差天数 1，下一天，-1，前几天，0，当前日期 *
 * @param type
 * @returns yyyy-MM-dd
 */
function ZRuntilDateDay(date, days) {
	date = new Date(date);
	if (days != 0) {
		date = +date + ((1000 * 60 * 60 * 24) * days);
	}
	date = new Date(date);
	var y = date.getFullYear();
	var m = date.getMonth() + 1;
	var d = date.getDate();
	if (m < 10) {
		m = "0" + m;
	}
	if (d < 10) {
		d = "0" + d;
	}
	return y + "-" + m + "-" + d;
}