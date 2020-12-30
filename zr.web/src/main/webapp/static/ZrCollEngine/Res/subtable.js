//*****************************
//  �ӱ������
//
//  ���������ɼ��������ӱ������
//  ���ߣ�NFZR
//  ���ڣ�2007.4
//*****************************
function SubTable(){
   this.Data = "";                                       //����
   this.Init = Init;                                     //�󶨳�ʼ������
   this.SetTableData = SetTableData;
   this.CollData = CollData;                             //�ӱ���ȡ���� 
   this.AddNew = AddNew;                                 //��������к��� 
   this.Add = Add;
   this.View = View;                                     //�󶨲鿴����     
   this.Edit = Edit;                                     //���޸ĺ���
   this.Delete = Delete;                                 //��ɾ������
   this.Save = Save;                                     //�󶨱��溯��
   this.Reflash = Reflash;                               //��ˢ�º���
   this.SetFormValue = SetFormValue;                     //���ñ�ֵ 
   this.getElementValue = getElementValue;               //��ȡԪ��ֵ
   this.getStringValue = getStringValue;                 //��ȡ�ַ���ֵ 
   this.getLook = getLook;                               //��ȡ������ʾ�ؼ�
   this.setLook = setLook;                               //���ø�����ʾ�ؼ�
   this.LocalData = LocalData;                           //�󶨱�������
   this.getUpdateType = getUpdateType;                   //��ȡ��¼���·�ʽ
}

//��ʼ����
function Init(){
   if(this.DataObject.value!=""){
      this.Data = this.DataObject.value;
	  this.Reflash();
   }
}

//���ñ������
function SetTableData(){
    var cells = this.Table.rows[0].cells;
    var row = this.Table.insertRow();
	var index = this.Table.rows.length-1;
    for (var i = 0; i < cells.length; i++) {
		 var cell = row.insertCell();
		 if(cells[i].tagid=="coll_SubOperate"){
		    cell.innerHTML = "<input name=\"view\" type=\"button\" id=\""+index+"\" value=\"�鿴\" onClick=\"mSubTable.View(this.id)\"/><input name=\"del\" type=\"button\" id=\""+index+"\" value=\"ɾ��\" onClick=\"mSubTable.Delete(this.id)\"/>";
		 }else{
	        cell.innerText = this.getElementValue(cells[i].tagid);	
		 }    
	}  
}

//��ȡ������,����ձ�ֵ
function CollData(){
     var str = ""; 
     for(var i=0;i<this.Form.elements.length;i++){	    
		 if(this.Form.elements[i].type == "select-one" && this.Form.elements[i].code!= undefined){
			if(!(this.Form.elements[i].value==""||this.Form.elements[i].value==" "||this.Form.elements[i].value=="��ѡ��")){
			   str = str+ this.ColumnCode+this.Form.elements[i].name + this.ValueCode + this.Form.elements[i].value ;
			}
			this.Form.elements[i].options.length = 0; 
			var oOption = document.createElement("OPTION");
			this.Form.elements[i].options.add(oOption);
			this.Form.elements[i].options[0].innerText = "��ѡ��";
			this.Form.elements[i].options[0].value = "��ѡ��";
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

//�������
function AddNew(){
   if(TableValidate()==true){  
     this.SetTableData();
	 var str = this.CollData();
	 str = str + this.ColumnCode+"coll_updatetype"+this.ValueCode+"new";
	 this.Data = this.Data +str+this.LineCode;
     this.Input.rows[this.Input.rows.length-1].cells[0].innerHTML = "";
   }	   
}

//װ����
function Add(){
      this.SetTableData();
	  this.CollData();
      this.Input.rows[this.Input.rows.length-1].cells[0].innerHTML = "";
}


function getElementValue(Cell_ID){
     for(var i=0;i<this.Form.elements.length;i++){
        if(this.Form.elements[i].name==Cell_ID){
		   if(this.Form.elements[i].type == "select-one" && this.Form.elements[i].selectedIndex > -1 ){
		       if(this.Form.elements[i].options[this.Form.elements[i].selectedIndex].value==""||this.Form.elements[i].options[this.Form.elements[i].selectedIndex].value=="��ѡ��"){   
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
	   this.Input.rows[this.Input.rows.length-1].cells[0].innerHTML = "<input name=\"button\" type=\"button\" id=\""+id+"\" value=\"�޸�\" onClick=\"mSubTable.Edit("+id+")\" />";
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

//������������
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
					  if(code==""||code==" "||code=="��ѡ��"){
					     code = "��ѡ��";
						 codevalue = "��ѡ��";
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

//�޸ı���
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

//��ȡ��¼���·�ʽ
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
			   str += cells[i].innerText + "��" + this.getElementValue(cells[i].tagid) + "��"
			}
		}
		   
	}  
     for(var i=0;i<this.Form.elements.length;i++){
		 if(this.Form.elements[i].type == "select-one" && this.Form.elements[i].code!= undefined){
			this.Form.elements[i].options.length = 0; 
			var oOption = document.createElement("OPTION");
			this.Form.elements[i].options.add(oOption);
			this.Form.elements[i].options[0].innerText = "��ѡ��";
			this.Form.elements[i].options[0].value = "��ѡ��";
			this.Form.elements[i].options[0].selected = true;
		  }else if(this.Form.elements[i].type=="radio"){
             this.Form.elements[i].checked=false;	  
	      }else if(this.Form.elements[i].type!="button"){
		    this.Form.elements[i].value = "";
	      }
		
	 }	
	return str.substring(0,str.length-1)+"��\n";
}



function Save(){
   this.DataObject.value = this.Data;
   this.LookObject.value = this.getLook();
}