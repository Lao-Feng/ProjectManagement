var $query;//定义接收vue的this
var $query_rows = [];//定义点击行数据
var $query_field;//定义查询条件选择的字段

/**
 * 获取查询引擎数据
 * 
 * @param data
 * @param callback
 *            回调函数
 * @returns
 */
function getRowsData(data, callback) {
	var url = '/zrquery/rows';
	var temp = {// 分页参数
		cur_page : 1,// 当前页码
		limit : 10, // 每页多少条
		totalCount : 0, // 总数据
	}, rows = [];
	initFormPost(data,function(rbck){
		data = rbck;
		$.post(url, data, function(rback) {
			if (rback != null && rback.rows.length > 0) {
				temp = {// 分页参数
					cur_page : rback.rows[0].cur_page,// 当前页码
					limit : rback.rows[0].limit, // 每页多少条
					totalCount : rback.rows[0].totalCount, // 总数据
				}
				// 删除第一行分页参数
				rback.rows.splice(0, 1);
				rows = rback.rows;
			}
			callback(temp, rows)
		}, 'json');
    })
}
/**
 * 提交条件，先进行预处理
 * @param callback 回调函数
 * @returns
 */
function initFormPost(post,callback){
	//获取条件所有字段 post_items
	var postData = $query.post_items;
	var list = $query.formList;
	if(list.length>0){
		$.each(list, function (i, perms){
			var field = perms.field.split('.');
			var fieldvalue = $query.form[field[0]][field[1]];
			if(perms.type == 'LONG' || perms.type == 'FLOAT'){
				postData[perms.field+'_Begin'] = typeof($query.form[field[0]][field[1]+'_Begin']) != "undefined" ? $query.form[field[0]][field[1]+'_Begin'] : '';
				postData[perms.field+'_End'] = typeof($query.form[field[0]][field[1]+'_End']) != "undefined" ? $query.form[field[0]][field[1]+'_End'] : '';
			}else if(perms.type == 'DATE' || perms.type == 'DATETIME'){
				if(typeof(fieldvalue) != "undefined" && fieldvalue != null){
					postData[perms.field+'_Begin'] = fieldvalue[0];
					postData[perms.field+'_End'] = fieldvalue[1];
				}else{
					postData[perms.field+'_Begin'] = '';
					postData[perms.field+'_End'] = '';
				}
			}else{//文本类型
				postData[perms.field] = typeof(fieldvalue) != "undefined" ? fieldvalue : '';
			}
		});
	}
	callback(postData);
}

/**
 * 解析按钮的JS中串,并执行
 * @param ScriptStr
 * @returns
 */
function parseButtonJs(ScriptStr) {
	let re = new RegExp(".", "g"); 
	var beginIndex = -1;
	var endIndex = -1;
	var ScriptStrtmp;
	var tableGrid;
	//判断是否有数据行值，判断是否为选择编辑或者查看项
	if(ScriptStr.indexOf("[") > -1 && $query_rows.length == 0){
		$query.$message.error('请选选择要操作的数据行!')
		return false;
	}
	while (ScriptStr.indexOf("[") > -1) {
		beginIndex = ScriptStr.indexOf("[");
		endIndex = ScriptStr.indexOf("]");
		fieldName = ScriptStr.substr(beginIndex + 1, endIndex - beginIndex - 1);
		var rowValue =fieldName.replace(".", "_");
		rowValue = $query_rows[0][rowValue]
		ScriptStrtmp = ScriptStr;
		ScriptStr = ScriptStr.replace("[" + fieldName + "]", rowValue);
		if (ScriptStrtmp == ScriptStr)
			break;
	}
	if (ScriptStr.indexOf("(") > -1 && ScriptStr.indexOf(")") > -1) {
		eval(ScriptStr+";$query.dialog_query = true;");

	} else if (ScriptStr.indexOf("[") == -1 && ScriptStr.indexOf("]") == -1) {
		self.location = ScriptStr;
	}
}
/**
 * 解决查询字典选择赋值
 * @param valname  需要填充的字段
 * @param selectname 选择值的字段
 * @returns
 */
function on_query_input(valname,selectname){
	$query.valname = $query.selectname
}

/***
 * 查询引擎，选择单位，返回值
 * @param formname 需要赋值的字段
 * @returns
 */
function on_query_unit(formname){
	$query.dialog_unit = true;
	$query_field = formname;
}
/***
 * 查询引擎，选择用户，返回值
 * @param formname 需要赋值的字段
 * @returns
 */

function on_query_user(formname){
	$query.dialog_user = true;
	$query_field = formname;
}
/**
 * 获取单位tree数据
 * @returns
 */
function ZRqueryUnitList(){
	var url = "bpipunit/list";
	ZRqueryList('/'+url, null, function (data) {
		$query.unit_tree = ZRqueryUnitTree(data.list, '');
    })
}
/**
 * 选择单位tree，返回选中值
 * @returns
 */
function on_unit_onclick(data, keys){
	var field = $query_field.split('.');
	if (keys.checkedKeys.length > 0) {
		$query.$refs.unit_tree.setCheckedKeys([data.unitid]);
		$query.form[field[0]][field[1]] = data.unitid;
		$query.dialog_unit = false;
	}
}
/**
 * 获取单位、单位用户tree数据
 * @returns
 */
function ZRqueryUserList(){
	ZRqueryList('/bpipunit/list', null, function (data) {
		$query.unit_tree = ZRqueryUnitTree(data.list, '');
		ZRqueryList('/bpipuser/list', null, function (user) {
            ZRunitAndUserList(data.list, user.list, function (list) {
            	$query.user_tree = ZRunitAndUserTree(list, '0');
            })
        })
    })
}
/**
 * 选择用户tree，返回值
 * @returns
 */
function on_user_onclick(data, keys){
	var field = $query_field.split('.');
	if (keys.checkedKeys.length > 0) {
		$query.$refs.user_tree.setCheckedKeys([data.userid]);
		if(data.userid.length == 16){
			$query.form[field[0]][field[1]] = data.userid;
			$query.dialog_user = false;
		}
	}
}
/**
 * 获取数据
 * @returns
 */
function ZRqueryList(url, data, callback){
	$.post(url, data, function (data) {
        callback(data)
    }, 'json')
}

/**
 * @param 将单位转换tree格式
 * @param {*} list 
 * @param {*} parentid
 */
function ZRqueryUnitTree(list, parentid) {
    let re = new RegExp("00", "g"); //定义正则表达式
    let result = [],
        temp;
    for (let i = 0; i < list.length; i++) {
        let unitId = list[i].unitid.replace(re, "");
        if (unitId.substring(0, unitId.length - 2) == parentid) {
            let obj = list[i];
            // 递归运算
            if (list[i].unitid.length >= 2) {
                temp = ZRqueryUnitTree(list, unitId);
                if (temp.length > 0) {
                    obj.list = temp;
                }
            }
            result.push(obj);
        }
    }
    return result;
}

/**
 * @param 将单位和用户合转换tree格式
 * @param {*} unitList 
 * @param {*} userList
 * @param {*} callback
 */
function ZRunitAndUserList(unitList, userList, callback) {
    let re = new RegExp("00", "g"); //定义正则表达式
    let unit_list = [];
    if (unitList.length > 0) {
        $.each(unitList, function (i, perms) {
            let parentId = perms.unitid.replace(re, "");
            parentId = parentId.substring(0, parentId.length - 2);
            if (parentId.length >= 2) {
                parentId = parentId + "000000000000";
                parentId = parentId.substring(0, 12);
            } else {
                parentId = '0';
            }
            let json = {
                userid: perms.unitid,
                name: perms.unitname,
                unitid: parentId
            }
            unit_list.push(json);
        });
    }
    unit_list.push.apply(unit_list, userList);
    callback(unit_list);
}

/**
 * @param 将单位and用户组合转换tree格式
 * @param {*} list 
 * @param {*} parentid
 */
function ZRunitAndUserTree(list, parentid) {
    let result = [],
        temp;
    for (let i = 0; i < list.length; i++) {
        if (list[i].unitid == parentid) {
            let obj = list[i];
            // 递归运算
            temp = ZRunitAndUserTree(list, list[i].userid);
            if (temp.length > 0) {
                obj.list = temp;
            } else {
                //设置单位下没有绑定用户的情况禁用或者不展示判断 disabled: true
                if (list[i].userid.length < 16) {
                    obj.disabled = true;
                }
            }
            result.push(obj);
        }
    }
    return result;
}