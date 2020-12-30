// <!--Begin
// Fill the selcted item list with the items already present in parent.
function fillInitialDestList() {
	var destList = window.document.forms[2].destList;
	var srcList = self.opener.window.document.forms[1].parentList;
	for (var count = destList.options.length - 1; count >= 0; count--) {
		destList.options[count] = null;
	}
	if(srcList != null){
		for(var i = 0; i < srcList.options.length; i++) {
			if (srcList.options[i] != null)
				destList.options[i] = new Option(srcList.options[i].text,srcList.options[i].value);
   		}
   	}
}

// Add the selected items from the source to destination list
function addSrcToDestList() {
	destList = window.document.forms[2].destList;
	srcList = window.document.forms[2].srcList;
	var len = destList.length;
	for(var i = 0; i < srcList.length; i++) {
		if ((srcList.options[i] != null) && (srcList.options[i].selected)) {
			//Check if this value already exist in the destList or not
			//if not then add it otherwise do not add it.
			var found = false;
			for(var count = 0; count < len; count++) {
				if (destList.options[count] != null) {
					if (srcList.options[i].text == destList.options[count].text) {
						found = true;
						break;
			  		}
  				 }
			}
			if (found != true) {
				destList.options[len] = new Option(srcList.options[i].text,srcList.options[i].value);
				len++;
	        	}
     		}
  	 }
}

// Deletes from the destination list.
function deleteFromDestList() {
	var destList  = window.document.forms[2].destList;
	var len = destList.options.length;
	for(var i = (len-1); i >= 0; i--) {
		if ((destList.options[i] != null) && (destList.options[i].selected == true)) {
			destList.options[i] = null;
		}
	}
}

// Up selections from the destination list.
function upFromDestList() {
	var destList  = window.document.forms[2].destList;
	var len = destList.options.length;
	for(var i = 0; i <= (len-1); i++) {
		if ((destList.options[i] != null) && (destList.options[i].selected == true)) {
			if(i>0 && destList.options[i-1] != null){
				fromtext = destList.options[i-1].text;
				fromvalue = destList.options[i-1].value;
				totext = destList.options[i].text;
				tovalue = destList.options[i].value;
				destList.options[i-1] = new Option(totext,tovalue);
				destList.options[i-1].selected = true;
				destList.options[i] = new Option(fromtext,fromvalue);
			}
		}
	}
}

// Down selections from the destination list.
function downFromDestList() {
	var destList  = window.document.forms[2].destList;
	var len = destList.options.length;
	for(var i = (len-1); i >= 0; i--) {
		if ((destList.options[i] != null) && (destList.options[i].selected == true)) {
			if(i<(len-1) && destList.options[i+1] != null){
				fromtext = destList.options[i+1].text;
				fromvalue = destList.options[i+1].value;
				totext = destList.options[i].text;
				tovalue = destList.options[i].value;
				destList.options[i+1] = new Option(totext,tovalue);
				destList.options[i+1].selected = true;
				destList.options[i] = new Option(fromtext,fromvalue);
			}
		}
	}
}

function ListAll(){
	tmpstr="";
	destinationList = window.document.forms[2].destList;
	for(var count = 0; count <= destinationList.options.length - 1; count++) {
		tmpstr+=destinationList.options[count].value;
		tmpstr+=",";
	}
	window.document.forms[2].formfields.value=tmpstr;
        //alert(tmpstr);
	//window.document.forms[2].submit();
        return true;
}
// End -->



// <!--Begin
//2007-2-8 扩展list功能，在函数中指定源Select和目的Select即可操作
// * @author NFZR

// Add the selected items from the source(srcList) to destination(destList) list
function addSrcToDestListPu(srcList,destList) {
	var len = destList.length;
	for(var i = 0; i < srcList.length; i++) {
		if ((srcList.options[i] != null) && (srcList.options[i].selected)) {
			//Check if this value already exist in the destList or not
			//if not then add it otherwise do not add it.
			var found = false;
			for(var count = 0; count < len; count++) {
				if (destList.options[count] != null) {
					if (srcList.options[i].text == destList.options[count].text) {
						found = true;
						break;
			  		}
  				 }
			}
			if (found != true) {
				destList.options[len] = new Option(srcList.options[i].text,srcList.options[i].value);
				len++;
	        	}
     		}
  	 }
}

// Deletes from the destination list.
function deleteFromDestListPu(destList) {
	var len = destList.options.length;
	for(var i = (len-1); i >= 0; i--) {
		if ((destList.options[i] != null) && (destList.options[i].selected == true)) {
			destList.options[i] = null;
		}
	}
}

//将选择好项的数值填充到指定的暂存域中
function fillSelectValueToField(destList,formfield)
{
      tmpstr="";
	destinationList =destList;
	for(var count = 0; count <= destinationList.options.length - 1; count++) {
		tmpstr+=destinationList.options[count].value;
		tmpstr+=",";
	}
	formfield.value=tmpstr;

}

// Up selections from the destination list.
function upItemSelection(list) {
	var destList  = list;
	var len = destList.options.length;
	for(var i = 0; i <= (len-1); i++) {
		if ((destList.options[i] != null) && (destList.options[i].selected == true)) {
			if(i>0 && destList.options[i-1] != null){
				fromtext = destList.options[i-1].text;
				fromvalue = destList.options[i-1].value;
				totext = destList.options[i].text;
				tovalue = destList.options[i].value;
				destList.options[i-1] = new Option(totext,tovalue);
				destList.options[i-1].selected = true;
				destList.options[i] = new Option(fromtext,fromvalue);
			}
		}
	}
}

// Down selections from the destination list.
function downItemSelection(list) {
	var destList  = list;
	var len = destList.options.length;
	for(var i = (len-1); i >= 0; i--) {
		if ((destList.options[i] != null) && (destList.options[i].selected == true)) {
			if(i<(len-1) && destList.options[i+1] != null){
				fromtext = destList.options[i+1].text;
				fromvalue = destList.options[i+1].value;
				totext = destList.options[i].text;
				tovalue = destList.options[i].value;
				destList.options[i+1] = new Option(totext,tovalue);
				destList.options[i+1].selected = true;
				destList.options[i] = new Option(fromtext,fromvalue);
			}
		}
	}
}

//2007-4-17
//判断srcList中选定的项是否在destList中存在
function isExistSelection(srcList,destList)
{

 var len = destList.length;
 for(var i = 0; i < srcList.length; i++)
 {
   if ((srcList.options[i] != null) && (srcList.options[i].selected))
   {
     for(var count = 0; count < len; count++)
     {
	if (destList.options[count] != null)
        {
	   if (srcList.options[i].text == destList.options[count].text)
           {
		return true;

           }
  	}
     }
    }
 }

 return false;
}

//2007-4-20
//判断多选列表multiList中选定的项的数目
function GetMultiListSelectedNum(multiList)
{

 var len = multiList.length;
 var selectedNum=0;
 for(var i = 0; i < multiList.length; i++)
 {

   if(multiList.options[i].selected)
   {
     selectedNum++;
   }
 }
 return selectedNum;
}



//===============================全选控制===================================
//选择所有
function SelAll(SelectName)
{
var mObject= document.all[SelectName];
for (i = 0; i < mObject.options.length; i++)
{
  mObject.options[i].selected = true;
}
}

//清除所有
function NullAll(SelectName)
{
var mObject= document.all[SelectName];
for (i = 0; i < mObject.options.length; i++)
{
  mObject.options[i].selected = false;
}
}

//
function CheckBoxSel(blChecked,SelectName)
{
if (blChecked){
  SelAll(SelectName);
}else{
  NullAll(SelectName);
}
}
// End -->
