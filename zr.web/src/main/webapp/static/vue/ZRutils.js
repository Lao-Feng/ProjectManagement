/***
 * 用户自定义js,引擎调用
 * @param 用 My 开头，区别系统js
 * @param $query: 等同于vue this
 * @param $query_rows: 获得查询引擎选择的数据行 [{}]
 */
let $zrthis;//vue初始化控件，不能删掉
/**
 * 弹出内容窗口
 * @param url 
 * @param width 宽度，100,100px，50%
 * @param heigth 高度 ，100,100px
 * @returns
 */
function OpenDataWindow(url,width,heigth){
	if(width!=null&&width>0){
		$('.querywin').css('width',width+'px')
	}
	if(heigth!=null&&heigth>0){
		$("#winmain").css("height", heigth+"px");
	}
	$query.dialog_url = '/'+url;
}
/**
 * 导出查询数据到excl
 * @param url
 * @returns
 */
function ZRexpexcel(url){
	var oldW = $query.dialog_width;
	location.href = '/'+url+'&crow='+$query.totalCount;
//	$query.dialog_title = '导出excl表';
//	$query.dialog_width = 0;
//	$query.dialog_url = '/'+url+'&crow='+$query.totalCount;
//	$query.dialog_query = true;

}

/**
 * 通用保存。save
 * @param {e.form} eForm 
 * @param {formId} fromId
 * @param {RequestMapping} url 
 * @param {recallback} callback
 */
function ZRsave(eForm, fromId, url, callback) {
    let e = $zrthis;
    e.$refs[fromId].validate((valid) => {
        if (valid) {
            let obj = eForm
            $.post(url,ZRjsonToGet(JSON.stringify(obj)),function(data){
            	callback(data)
            })
        } else {
            return false
        }
    })
}

/**
 *@param  POST提交
 * @param {*} type 
 * @param {*} url 
 * @param {*} data 
 * @param {*} callback 
 */
function ZRsubmit(url, data, callback) {
    let e = $zrthis
    $.ajax({
        type: 'post',
        url: url,
        data: data,
        contentType: 'application/json',
        dataType: 'json',
        timeout: 3000,
        success: function (result) {
        	callback(result);
        },
        error: function (response, ajaxOptions, thrownError) {
            e.$message.error('请求异常，请重新登录或联系系统管理员!')
        }
    })
}

/**
 * json 转换 get 参数
 * @param sstr
 * @returns
 */
function ZRjsonToGet(json){
	var sstr = json;
	sstr = sstr.replace(/\t/g,"");
	sstr = sstr.replace(/\"/g,"").replace("{","").replace("}","").
    sstr = sstr.replace(",","&").replace(":","=");
	sstr = sstr.replace(/\"/g,"").replace(/{/g,"").replace(/}/g,"")
    sstr = sstr.replace(/,/g,"&").replace(/:/g,"=");
	return sstr;
}
/**
 * <option>转换json
 * @param option
 * @returns
 */
function ZRoptionToJson(option){//
	var re = new RegExp(" selected","g"); //定义正则表达式
	option = option.replace(re, "");
	re = new RegExp("<option ","g");
	option = option.replace(re, "");
	re = new RegExp("value=","g");
	option = option.replace(re, "{\"value\":");
	re = new RegExp("\">","g");
	option = option.replace(re, "\",\"label\":\"");
	re = new RegExp("</option>","g");
	option = option.replace(re, "\"");
	re = new RegExp(/[\r\n]/g,"g");
	option = option.replace(re, "},");
	if(option.length>0){
		option = option.substring(0,option.length-1);
		option = '['+option+']';
	}
	return JSON.parse(option);
}

