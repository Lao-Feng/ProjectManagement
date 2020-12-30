var $zrcoll;// vue属性this
var $zrcoll_field;// 定义查询条件选择的字段

var sid = "";

/*******************************************************************************
 * 报表引擎，选择单位，返回值
 * 
 * @param formname
 *            需要赋值的字段
 * @returns
 */
function on_query_unit(formname) {
	$zrcoll.dialog_unit = true;
	$zrcoll_field = formname;
}
/*******************************************************************************
 * 报表引擎，选择用户，返回值
 * 
 * @param formname
 *            需要赋值的字段
 * @returns
 */

function on_query_user(formname) {
	$zrcoll.dialog_user = true;
	$zrcoll_field = formname;
}

/**
 * 获取单位tree数据
 * 
 * @returns
 */
function ZRcollenUnitList() {
	var url = "bpipunit/list";
	ZRcollenList('/' + url, null, function(data) {
		$zrcoll.unit_tree = ZRcollenUnitTree(data.list, '');
	})
}
/**
 * 选择单位tree，返回选中值
 * 
 * @returns
 */
function on_unit_onclick(data, keys) {
	if (keys.checkedKeys.length > 0) {
		$zrcoll.$refs.unit_tree.setCheckedKeys([ data.unitid ]);
		$zrcoll.form[$zrcoll_field] = data.unitid;
		$zrcoll.dialog_unit = false;
	}
}
/**
 * 获取单位、单位用户tree数据
 * 
 * @returns
 */
function ZRcollenUserList() {
	ZRcollenList('/bpipunit/list', null, function(data) {
		$zrcoll.unit_tree = ZRcollenUnitTree(data.list, '');
		ZRcollenList('/bpipuser/list', null, function(user) {
			ZRunitAndUserList(data.list, user.list, function(list) {
				$zrcoll.user_tree = ZRunitAndUserTree(list, '0');
			})
		})
	})
}
/**
 * 选择用户tree，返回值
 * 
 * @returns
 */
function on_user_onclick(data, keys) {
	if (keys.checkedKeys.length > 0) {
		$zrcoll.$refs.user_tree.setCheckedKeys([ data.userid ]);
		if (data.userid.length == 16) {
			$zrcoll.form[$zrcoll_field] = data.userid;
			$zrcoll.dialog_user = false;
		}
	}
}
/**
 * 获取数据
 * 
 * @returns
 */
function ZRcollenList(url, data, callback) {
	$.post(url, data, function(data) {
		callback(data)
	}, 'json')
}

/**
 * @param 将单位转换tree格式
 * @param {*}
 *            list
 * @param {*}
 *            parentid
 */
function ZRcollenUnitTree(list, parentid) {
	let re = new RegExp("00", "g"); // 定义正则表达式
	let result = [], temp;
	for (let i = 0; i < list.length; i++) {
		let unitId = list[i].unitid.replace(re, "");
		if (unitId.substring(0, unitId.length - 2) == parentid) {
			let obj = list[i];
			// 递归运算
			if (list[i].unitid.length >= 2) {
				temp = ZRcollenUnitTree(list, unitId);
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
 * @param {*}
 *            unitList
 * @param {*}
 *            userList
 * @param {*}
 *            callback
 */
function ZRunitAndUserList(unitList, userList, callback) {
	let re = new RegExp("00", "g"); // 定义正则表达式
	let unit_list = [];
	if (unitList.length > 0) {
		$.each(unitList, function(i, perms) {
			let parentId = perms.unitid.replace(re, "");
			parentId = parentId.substring(0, parentId.length - 2);
			if (parentId.length >= 2) {
				parentId = parentId + "000000000000";
				parentId = parentId.substring(0, 12);
			} else {
				parentId = '0';
			}
			let json = {
				userid : perms.unitid,
				name : perms.unitname,
				unitid : parentId
			}
			unit_list.push(json);
		});
	}
	unit_list.push.apply(unit_list, userList);
	callback(unit_list);
}

/**
 * @param 将单位and用户组合转换tree格式
 * @param {*}
 *            list
 * @param {*}
 *            parentid
 */
function ZRunitAndUserTree(list, parentid) {
	let result = [], temp;
	for (let i = 0; i < list.length; i++) {
		if (list[i].unitid == parentid) {
			let obj = list[i];
			// 递归运算
			temp = ZRunitAndUserTree(list, list[i].userid);
			if (temp.length > 0) {
				obj.list = temp;
			} else {
				// 设置单位下没有绑定用户的情况禁用或者不展示判断 disabled: true
				if (list[i].userid.length < 16) {
					obj.disabled = true;
				}
			}
			result.push(obj);
		}
	}
	return result;
}

/**
 * @param 表单POST提交
 * @param {*}
 *            type
 * @param {*}
 *            url
 * @param {*}
 *            data
 * @param {*}
 *            callback
 */
function ZRsubmit(data, callback) {
	let e = $zrcoll
	$.post('/zrcollen/saveorupdate', data, function(rback) {
		if (rback.code == 0) { // 修改成功
			e.$message({
				message : '操作成功！',
				type : 'success',
				effect:'dark'
			})
			callback(rback)
		} else {
			e.$message.error(rback.msg)
		}
	}, 'json');
}

/**
 * 表单提交（前）验证
 * 
 * @param callback
 * @returns
 */
function ZRsave_collen(eForm,callback){
    $zrcoll.$refs['form'].validate((valid) => {
        if (valid) {
        	let obj = eForm
        	// 查找是否有数组字段的数据
        	if($zrcoll.checkLists!=null&&$zrcoll.checkLists.length>0){
        		for(let i =0;i<$zrcoll.checkLists.length;i++){
        			obj[$zrcoll.checkLists[i]] = obj[$zrcoll.checkLists[i]].join(',');
        		}
        	}
            ZRsubmit(obj, function (data) {
                callback(data)
            })
        } else {
            return false
        }
    })
}

/**
 * 表单删除（前）验证
 * 
 * @param callback 回调函数
 * @returns
 */
function ZRdel_collen(callback){
	let e = $zrcoll;
	e.$confirm('确定删除吗, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    }).then(_ => {
        let parms = {
        	docid:e.form.COLL_PKID,
        	formid:e.form.COLL_AID,
        }
        $.post('/zrcollen/delform', parms, function(rback) {
    		if (rback.code == 0) {
    			e.$message({
    				message : '删除成功！',
    				type : 'success',
    				effect:'dark'
    			})
    			callback(rback)
    		} else {
    			e.$message.error(rback.msg)
    		}
    	}, 'json');
    }).catch(_ => {})
}
/**
 * 调用表单引擎打印
 * @param callback
 * @returns
 */
function ZRprint_collen(callback){
	let e = $zrcoll;
	var IndexWin = null;
	var url = "/collect/opencollengine?Act=openprint&AID="+e.form.COLL_AID+"&DOCID="+e.form.COLL_PKID+"&TYPE=1";
	var w,h,s;
	w = screen.availWidth - 4;
	h = screen.availHeight - 8;
	s = "directories=no,left=0,top=0,location=no,menubar=no,resizable=yes,scrollbars=yes,status=yes,toolbar=no,width="+w+",height="+h;
	IndexWin = window.open(url,"print",s);
	IndexWin.moveTo(-4,-4);
	IndexWin.focus();
	callback();
}

/**
 * 获取表单所有数据
 * @param callback
 * @returns
 */
function ZRform_datas(callback){
	let e = $zrcoll;
	callback(e.form);
}

/**
 * 表单上传图片时，生成base64字符串
 * 
 * @param options
 * @returns
 */
function ZRcollenRequest (options) {
	var that = this;
    // 获取文件对象
    let file = options.file;
    // 判断图片类型
    if (file.type == 'image/jpeg' || file.type == 'image/png' || file.type == 'image/JPG') {
        var  isJPG =  true
    } else {
        isJPG =  false
    }
    // 判断图片大小
    const isLt2M = file.size / 1024 / 1024 < 2
    if (!isJPG) {
        this.$message.error('上传产品图片只能是 JPG/PNG/JPEG 格式!')
    }
    if (!isLt2M) {
        this.$message.error('上传产品图片大小不能超过 2MB!')
    }
    // 创建一个HTML5的FileReader对象
    var reader = new FileReader();
    // 创建一个img对象
    var  img = new Image();
    let filename = options.filename;
    if (file) {
        reader.readAsDataURL(file);
    }
    if (isJPG && isLt2M) {
        reader.onload = (e) => {
            let base64Str = reader.result.split(',')[1];
            img.src = e.target.result;
            // base64地址图片加载完毕后执行
            img.onload = function () {
                // 缩放图片需要的canvas（也可以在DOM中直接定义canvas标签，这样就能把压缩完的图片不转base64也能直接显示出来）
                var canvas = document.createElement('canvas');
                var context = canvas.getContext('2d');
                // 图片原始尺寸
                var originWidth = this.width;
                var originHeight = this.height;
                // 最大尺寸限制，可通过设置宽高来实现图片压缩程度
                var maxWidth = 200,
                    maxHeight = 200;
                // 目标尺寸
                var targetWidth = originWidth,
                    targetHeight = originHeight;
                // 图片尺寸超过最大尺寸的限制
                if(originWidth > maxWidth || originHeight > maxHeight) {
                    if(originWidth / originHeight > maxWidth / maxHeight) {
                        // 更改宽度，按照宽度限定尺寸
                        targetWidth = maxWidth;
                        targetHeight = Math.round(maxWidth * (originHeight / originWidth));
                    } else {
                        targetHeight = maxHeight;
                        targetWidth = Math.round(maxHeight * (originWidth / originHeight));
                    }
                }
                // 对图片进行缩放
                canvas.width = targetWidth;
                canvas.height = targetHeight;
                // 清除画布
                context.clearRect(0, 0, targetWidth, targetHeight);
                // 图片压缩
                context.drawImage(img, 0, 0, targetWidth, targetHeight);
                /* 第一个参数是创建的img对象；第二三个参数是左上角坐标，后面两个是画布区域宽高 */
                // 压缩后的base64文件
                var newUrl = canvas.toDataURL('image/jpeg', 0.92);
                $zrcoll.image[filename] = newUrl;
                $zrcoll.form[filename] = newUrl.substring(23,newUrl.length);
            }
        }
    }
}
