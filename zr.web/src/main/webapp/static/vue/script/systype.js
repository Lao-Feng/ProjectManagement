
//��������

/**
 * ������ʾ���� type=2��ʾ��html5����
 */
function alertTile(tile,type){
	if(type=="2"){//����2Ϊhtml����
		layer.alert(tile, {icon: 7});
	}else{
		alert(tile);
	}
}

/**
 * ����ѯ�ʴ��� 
 * type=2��ʾ��html5����
 * tile  ����
 * quer  ȷ�Ϸ���  ���磻actionstr()
 * extstr  ȡ������  ���磻actionstr()
 */
function confirmTile(tile,quer,extstr,type){
	var returstr="false";//����false
	if(type=="2"){
		layer.confirm(tile, {
			  btn: ['ȷ��','ȡ��'] //��ť
			}, function(){
				layer.closeAll('dialog');
				eval(quer);
			}, function(){
				eval(extstr);
			});
	}else{
		if(confirm(tile)){
			eval(quer);
		}else{
			eval(extstr);
		}
	}

}

/**
 * ��ѡ�����  �û�����λѡ��
 * @param mFileUrl
 * @return
 */

function selectUserHtml(mFileUrl){
	var index =layer.open({
        type: 2,
        title: "�û�����λѡ��",
        area: ['500px', '400px'],
        shade: 0.3,
        closeBtn: 0,
        shadeClose: true,
         content: mFileUrl
    });
	setTimeout("initValue("+index+");",500);
}



function initValue(index){
	//�޸ĵ������ھ��붥����λ��
	$("#layui-layer"+index).css("top","50px");
	
}

/**
 * ��ѡ�����  �ֵ��ѡ��
 * @param mFileUrl
 * @return
 */
function selectDictHtml(mFileUrl){
	layer.open({
        type: 2,
        title: false,
        area: ['382px', '312px'],
        shade: 0.3,
        closeBtn: 0,
        shadeClose: true,
         content: mFileUrl
    });
}