//*****************************
//  子表操作类
//
//  描述：多表采集操作的子表操作类
//  作者：NFZR
//  日期：2007.4
//*****************************
function SubTable(){
   this.Data = "";                                       //数据
   this.Init = Init;                                     //绑定初始化函数
   this.SetTableData = SetTableData;
   this.CollData = CollData;                             //从表单提取数据 
   this.AddNew = AddNew;                                 //绑定添加新行函数 
   this.Add = Add;
   this.View = View;                                     //绑定查看函数     
   this.Edit = Edit;                                     //绑定修改函数
   this.Delete = Delete;                                 //绑定删除函数
   this.Save = Save;                                     //绑定保存函数
   this.Reflash = Reflash;                               //绑定刷新函数
   this.SetFormValue = SetFormValue;                     //设置表单值 
   this.getElementValue = getElementValue;               //获取元素值
   this.getStringValue = getStringValue;                 //获取字符串值 
   this.getLook = getLook;                               //获取父表单显示控件
   this.setLook = setLook;                               //设置父表单显示控件
   this.LocalData = LocalData;                           //绑定本地数据
   this.getUpdateType = getUpdateType;                   //获取记录更新方式
}

//初始化类
function Init(){
   if(this.DataObject.value!=""){
      this.Data = this.DataObject.value;
	  this.Reflash();
   }
}

//设置表格数据
function SetTableData(){
    var cells = this.Table.rows[0].cells;
    var row = this.Table.insertRow();
	var index = this.Table.rows.length-1;
    for (var i = 0; i < cells.length; i++) {
		 var cell = row.insertCell();
		 if(cells[i].tagid=="coll_SubOperate"){
		    cell.innerHTML = "<input name=\"view\" type=\"button\" id=\""+index+"\" value=\"查看\" onClick=\"mSubTable.View(this.id)\"/><input name=\"del\" type=\"button\" id=\""+index+"\" value=\"删除\" onClick=\"mSubTable.Delete(this.id)\"/>";
		 }else{
	        cell.innerText = this.getElementValue(cells[i].tagid);	
		 }    
	}  
}

//提取表单数据,并清空表单值
function CollData(){
     var str = ""; 
     for(var i=0;i<this.Form.elements.length;i++){	    
		 if(this.Form.elements[i].type == "select-one" && this.Form.elements[i].code!= undefined){
			if(!(this.Form.elements[i].value==""||this.Form.elements[i].value==" "||this.Form.elements[i].value=="请选择")){
			   str = str+ this.ColumnCode+this.Form.elements[i].name + this.ValueCode + this.Form.elements[i].value ;
			}
			this.Form.elements[i].options.length = 0; 
			var oOption = document.createElement("OPTION");
			this.Form.elements[i].options.add(oOption);
			this.Form.elements[i].options[0].innerText = "请选择";
			this.Form.elements[i].options[0].value = "请选择";
			this.Form.elements[i].options[0].selected = true;
		  }else if(this.Form.elements[i].type=="radio"){
		     if(this.Form.elements[i].checked==true){
			     str = str+ this.ColumnCode+this.Form.elements[i].name + this.ValueCode + this.Form.elements[i].value ;
				 this.Form.elements[i].checked=false;
			 }		  
	      }else if(this.Form.elements[i].type!="button"){
			if(!(this.Form.elements[i].value==""||this.Form.elements[i].value==" "||this.Form.elements[i].value==undefined)){
			   str = str+ this.ColumnCode+this.Form.elements[i].name + this.ValueCode + this.Form.elements[i].value ;
			}  
		    this.Form.elements[i].value = "";
	      }
     }
     return str;
}

//添加新行
function AddNew(){
   if(TableValidate()==true){  
     this.SetTableData();
	 var str = this.CollData();
	 str = str + this.ColumnCode+"coll_updatetype"+this.ValueCode+"new";
	 this.Data = this.Data +str+this.LineCode;
     this.Input.rows[this.Input.rows.length-1].cells[0].innerHTML = "";
   }	   
}

//装载行
function Add(){
      this.SetTableData();
	  this.CollData();
      this.Input.rows[this.Input.rows.length-1].cells[0].innerHTML = "";
}


function getElementValue(Cell_ID){
     for(var i=0;i<this.Form.elements.length;i++){
        if(this.Form.elements[i].name==Cell_ID){
		   if(this.Form.elements[i].type == "select-one" && this.Form.elements[i].selectedIndex > -1 ){
		       if(this.Form.elements[i].options[this.Form.elements[i].selectedIndex].value==""||this.Form.elements[i].options[this.Form.elements[i].selectedIndex].value=="请选择"){   
		         return " ";
			   }else{			     
				 return this.Form.elements[i].options[this.Form.elements[i].selectedIndex].innerText;
			   }	 
		   }else
		    if(this.Form.elements[i].value!="")
		       return this.Form.elements[i].value; 	    
		}
	 } 
	 return " ";   
}

function getStringValue(strData,Cell_ID){
    var dataRow = new String(strData);
    var dataColumns = dataRow.split(this.ColumnCode);
	for(var i=0;i<dataColumns.length;i++){
	    var dataColumn = dataColumns[i].split(this.ValueCode); 
		if(dataColumn[0]==Cell_ID){
		   if(dataColumn[1]!="")
		   return dataColumn[1];
		}
    }    
	return " ";
}

function View(id)
{
    id--;
    if(this.Data!="" && this.Data.length>0)
	{
	   this.LocalData(this.Data,id)
	   this.Input.rows[this.Input.rows.length-1].cells[0].innerHTML = "<input name=\"button\" type=\"button\" id=\""+id+"\" value=\"修改\" onClick=\"mSubTable.Edit("+id+")\" />";
	}
}

function SetFormValue(name,row){    
    for(var i=0;i<row.length;i++){
	    var col = row[i].split(this.ValueCode);
        if(name==col[0]){
          return col[1]; 	    
	    }
	}
	return "";   
}


function Delete(id){
    id--;
    if(this.Data!="" && this.Data.length>0)
	{
	  var s = new String(this.Data);
	  var datars = s.split(this.LineCode);
	  this.Data = "";
	  var str = "";
	  for(var i=0;i<datars.length-1;i++){
	      if(i!=id){
		     this.Data = this.Data+ datars[i]+this.LineCode;
		  }
	  }
	  
	  this.Reflash();
	}
}

function Reflash(){
      if(this.Table.rows.length>1){
	  	     for(var i=this.Table.rows.length-1;i>0;i--){
		       this.Table.deleteRow(i);
		     }
	  }
      if(this.Data != "" && this.Data.length>0){	     
	     var s = this.Data;
	     var dataRows = s.split(this.LineCode);		
	     for(var i=0;i<dataRows.length-1;i++)
	     { 
	          this.LocalData(s,i);
			  this.Add();
		 }	  
	} 	
	
}

//用行数据填充表单
function LocalData(strData,id)
{
    if(strData!="" && strData.length>0)
	{
	  var s = new String(this.Data);
	  
	  var datars = s.split(this.LineCode);
	  if(datars[id] != "" && datars[id].length>0)
	  {
	         var s2 = new String(datars[id]);
			 var dars = s2.split(this.ColumnCode);
             for(var i=0;i<this.Form.elements.length;i++){    
	     	    if(this.Form.elements[i].type=="select-one"){
				   if(this.Form.elements[i].code!=undefined){
		              var code = this.SetFormValue(this.Form.elements[i].name,dars);
					  var codevalue = "";
					  if(code==""||code==" "||code=="请选择"){
					     code = "请选择";
						 codevalue = "请选择";
					  }else{	
				         codevalue = mCodeInput.getCodeName(this.Form.elements[i].code,code);				 				     
					  }
					  this.Form.elements[i].options.length = 0; 
				      var oOption = document.createElement("OPTION");
				      this.Form.elements[i].options.add(oOption);
				      this.Form.elements[i].options[0].innerText = codevalue;
				      this.Form.elements[i].options[0].value = code;
				      this.Form.elements[i].options[0].selected = true;	
				   }else{
				      this.Form.elements[i].value = this.SetFormValue(this.Form.elements[i].name,dars); 
				   }  	  			   
				 }else if(this.Form.elements[i].type=="radio"){
				       if(this.Form.elements[i].value==this.SetFormValue(this.Form.elements[i].name,dars)){
					      this.Form.elements[i].checked = true;
					   }else{
					      this.Form.elements[i].checked = false;
					   }
				 }else if(this.Form.elements[i].type=="text"||this.Form.elements[i].type=="textarea"||this.Form.elements[i].type=="hidden"){
		              this.Form.elements[i].value = this.SetFormValue(this.Form.elements[i].name,dars);
				 }				 				 
	          } //for			 
	  }
	}
}

//修改保存
function Edit(id){
 if(TableValidate()==true){
    if(this.Data!="" && this.Data.length>0)
	{
	  var s = new String(this.Data);
	  var datars = s.split(this.LineCode);
	  this.Data = "";

	  var str = this.CollData();
	  
	  for(var i=0;i<datars.length-1;i++){
	      if(i!=id){
		     this.Data = this.Data+ datars[i]+this.LineCode;
		  }else{
		     this.Data = this.Data +str+this.getUpdateType(datars[i])+this.LineCode;	 
		  }
	  }
	   this.Input.rows[this.Input.rows.length-1].cells[0].innerHTML = "";
	   this.Reflash();
	}
 }  
}

//获取记录更新方式
function getUpdateType(RecordData){
    var columns = RecordData.split(this.ColumnCode);
	for(var i=0;i<columns.length;i++){
	   var column = columns[i].split(this.ValueCode);
	   if(column[0]=="coll_updatetype"){
	      return this.ColumnCode+columns[i];
	   }
	}
	return "";
}

function getLook(){
       var str = "";
      if(this.Data != "" && this.Data.length>0){	     
	     var s = this.Data;
		 
	     var dataRows = s.split(this.LineCode);
	     for(var i=0;i<dataRows.length-1;i++)
	     { 
	          this.LocalData(s,i);
			  str += this.setLook();			  
		 }	  
	}
	this.Input.rows[this.Input.rows.length-1].cells[0].innerHTML = "";
	return str;
}

function setLook(){
    var str = "";
    var cells = this.Table.rows[0].cells;
    for (var i = 0; i < cells.length; i++) {
		 if(cells[i].tagid!="coll_SubOperate"){
	        if(this.getElementValue(cells[i].tagid)!=" "){
			   str += cells[i].innerText + "：" + this.getElementValue(cells[i].tagid) + "，"
			}
		}
		   
	}  
     for(var i=0;i<this.Form.elements.length;i++){
		 if(this.Form.elements[i].type == "select-one" && this.Form.elements[i].code!= undefined){
			this.Form.elements[i].options.length = 0; 
			var oOption = document.createElement("OPTION");
			this.Form.elements[i].options.add(oOption);
			this.Form.elements[i].options[0].innerText = "请选择";
			this.Form.elements[i].options[0].value = "请选择";
			this.Form.elements[i].options[0].selected = true;
		  }else if(this.Form.elements[i].type=="radio"){
             this.Form.elements[i].checked=false;	  
	      }else if(this.Form.elements[i].type!="button"){
		    this.Form.elements[i].value = "";
	      }
		
	 }	
	return str.substring(0,str.length-1)+"。\n";
}



function Save(){
   this.DataObject.value = this.Data;
   this.LookObject.value = this.getLook();
}