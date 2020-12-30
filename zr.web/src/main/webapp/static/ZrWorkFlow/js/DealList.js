<!--Begin
// Fill the selcted item list with the items already present in parent.
function fillInitialDestList() {
	var destList = window.document.forms[0].destList;
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
function addSrcToDestList(type) {
        //type为1时为单选
	destList = window.document.forms[0].destList;
	srcList = window.document.forms[0].srcList;
        var len = destList.length;
        if (type=="1")
        {deleteAllDestList();
        destList = window.document.forms[0].destList;
	srcList = window.document.forms[0].srcList;
        len = destList.length;
	for(var i = 0; i < srcList.length; i++) {
		if ((srcList.options[i] != null) && (srcList.options[i].selected)) {
                   destList.options[len] = new Option(srcList.options[i].text,srcList.options[i].value);
     		}
  	 }
         }
         else
         {

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
}

// Deletes from the destination list.
function deleteFromDestList() {
	var destList  = window.document.forms[0].destList;
	var len = destList.options.length;
	for(var i = (len-1); i >= 0; i--) {
		if ((destList.options[i] != null) && (destList.options[i].selected == true)) {
			destList.options[i] = null;
		}
	}
}
// Deletes from the all list.
function deleteAllDestList(){
	var destList  = window.document.forms[0].destList;
	var len = destList.options.length;
	for(var i = (len-1); i >= 0; i--) {
		if ((destList.options[i] != null)) {
			destList.options[i] = null;
		}
	}
}
// Up selections from the destination list.
function upFromDestList() {
	var destList  = window.document.forms[0].destList;
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
	var destList  = window.document.forms[0].destList;
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
	destinationList = window.document.forms[0].destList;
	for(var count = 0; count <= destinationList.options.length - 1; count++) {
		tmpstr+=destinationList.options[count].value;
		tmpstr+=",";
	}
	window.document.forms[0].formfields.value=tmpstr;
        //alert(tmpstr);
	//window.document.forms[0].submit();
        return true;
}

function ListAllVue(){
	tmpstr="";
	destinationList = window.document.forms[0].destList;
	for(var count = 0; count <= destinationList.options.length - 1; count++) {
		tmpstr+=destinationList.options[count].value;
		tmpstr+=",";
	}
	window.document.forms[0].formfields.value=tmpstr;
     alert(tmpstr);
	//window.document.forms[0].submit();
        return true;
}
// End -->

//=============================================================================
<!--Begin
//2007-2-5
// * @author NFZR加
// Fill the selcted item list with the items already present in parent.
function fillInitialDestList1() {
	var destList1 = window.document.forms[0].destList1;
	var srcList1 = self.opener.window.document.forms[1].parentList;
	for (var count = destList1.options.length - 1; count >= 0; count--) {
		destList1.options[count] = null;
	}
	if(srcList1 != null){
		for(var i = 0; i < srcList1.options.length; i++) {
			if (srcList1.options[i] != null)
				destList1.options[i] = new Option(srcList1.options[i].text,srcList1.options[i].value);
   		}
   	}
}

// Add the selected items from the source to destination list
function addSrcToDestListPu(srcList1,destList1) {
	var len = destList1.length;
	for(var i = 0; i < srcList1.length; i++) {
		if ((srcList1.options[i] != null) && (srcList1.options[i].selected)) {
			//Check if this value already exist in the destList or not
			//if not then add it otherwise do not add it.
			var found = false;
			for(var count = 0; count < len; count++) {
				if (destList1.options[count] != null) {
					if (srcList1.options[i].text == destList1.options[count].text) {
						found = true;
						break;
			  		}
  				 }
			}
			if (found != true) {
				destList1.options[len] = new Option(srcList1.options[i].text,srcList1.options[i].value);
				len++;
	        	}
     		}
  	 }
}
//处理有本部门属性的情况
function addSrcToDestListPu1(srcList,destList,isdept) {
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
                              if (isdept=="1")
				{destList.options[len] = new Option(srcList.options[i].text+"(本部门)",srcList.options[i].value+";1");}
                              else
                                {destList.options[len] = new Option(srcList.options[i].text,srcList.options[i].value+";0");}

				len++;
	        	}
     		}
  	 }
}


// Deletes from the destination list.
function deleteFromDestListPu(destList1) {
	var len = destList1.options.length;
	for(var i = (len-1); i >= 0; i--) {
		if ((destList1.options[i] != null) && (destList1.options[i].selected == true)) {
			destList1.options[i] = null;
		}
	}
}

// Up selections from the destination list.
function upFromDestList1() {
	var destList1  = window.document.forms[0].destList1;
	var len = destList1.options.length;
	for(var i = 0; i <= (len-1); i++) {
		if ((destList1.options[i] != null) && (destList1.options[i].selected == true)) {
			if(i>0 && destList1.options[i-1] != null){
				fromtext = destList1.options[i-1].text;
				fromvalue = destList1.options[i-1].value;
				totext = destList1.options[i].text;
				tovalue = destList1.options[i].value;
				destList1.options[i-1] = new Option(totext,tovalue);
				destList1.options[i-1].selected = true;
				destList1.options[i] = new Option(fromtext,fromvalue);
			}
		}
	}
}

// Down selections from the destination list.
function downFromDestList1() {
	var destList1  = window.document.forms[0].destList1;
	var len = destList1.options.length;
	for(var i = (len-1); i >= 0; i--) {
		if ((destList1.options[i] != null) && (destList1.options[i].selected == true)) {
			if(i<(len-1) && destList1.options[i+1] != null){
				fromtext = destList1.options[i+1].text;
				fromvalue = destList1.options[i+1].value;
				totext = destList1.options[i].text;
				tovalue = destList1.options[i].value;
				destList1.options[i+1] = new Option(totext,tovalue);
				destList1.options[i+1].selected = true;
				destList1.options[i] = new Option(fromtext,fromvalue);
			}
		}
	}
}

function ListAll1Vue(){
	tmpstr="";
	destinationList = window.document.forms[0].destList1;
	for(var count = 0; count <= destinationList.options.length - 1; count++) {
		tmpstr+=destinationList.options[count].value;
		tmpstr+=",";
	}
	window.document.forms[0].formfields1.value=tmpstr;
        //alert(tmpstr);
	//window.document.forms[0].submit();
        return true;
}
// End -->


<!--Begin
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

// End -->

