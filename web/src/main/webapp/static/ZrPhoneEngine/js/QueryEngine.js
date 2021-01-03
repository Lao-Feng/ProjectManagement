/************************************************************************************/
/****   表格前台JS类        ***********************************************************/
/****   主要功能：读取指定字段名选中行的值                       ×××××××××××/
/*************************************************************************************/
//   类名：TableGrid

var $tab_id="";//tab_id

function TableGrid(fieldName)
{
 //私有变量
 var fieldName;   //字段名
 fieldName=fieldName;

/***********************************************/
/*得到指定的字段名的值                            */
/***********************************************/
this.GetCheckFieldValue=GetCheckFieldValue;
function GetCheckFieldValue()
{
   var fieldValue;
   var getrow = document.getElementById("selectrow").value;

   fieldName = fieldName.replace(".","_");
   fieldValue = mygrid.getCellValue(fieldName,getrow);
   return fieldValue;

}

}

//   类名：TableGrid1
function TableGrid1(fieldName)
{
 //私有变量
 var objTableTou;    //表格对象
 var objTableTi;
 var fieldName;   //字段名
 //objTableTou=document.getElementById("tableTou");
 objTableTi=document.getElementById($tab_id);
 fieldName=fieldName;



/***********************************************/
/*得到指定的字段名的值                            */
/***********************************************/
this.GetCheckFieldValue=GetCheckFieldValue;
function GetCheckFieldValue()
{
  var fieldValue;

   var rowSequence=document.getElementById("selectrow").value;

   if(rowSequence=="0")
   {
     $('#my-err').modal(options);
     return null;
   }



   var colSequence=GetColSequence();


   if(rowSequence>-1 && colSequence>-1)
   {
     fieldValue=objTableTi.rows[rowSequence].cells[colSequence].getAttribute("fieldValue");
   }
   else
   {
      $('#my-tile').modal(options);
      fieldValue=null;
   }

   return fieldValue;

}


/**********************************************/
/*得到指定的字段名的列序号                       */
/**********************************************/

this.GetColSequence=GetColSequence;
function GetColSequence()
{
   var rowObj=objTableTi.rows[0];
   var cellObj;
   var colSequence=-1;
   var js=0;
   var js1=0;

   for(var i=0;i<rowObj.cells.length;i++)
   {
     cellObj=rowObj.cells[i];
     js1 = cellObj.getAttribute("colspan");
     if (js1>1)
     {
       js = js + (js1-1); 
     }
     if(cellObj.getAttribute("fieldName")==fieldName)
	 {
	   colSequence=i;
	   break;
	 }
   }
   colSequence = colSequence + js;
   return colSequence;
   

}



}


//得到点击行指定字段的值
function getClickColumnValue(fieldName)
{
  var tableGrid;
  tableGrid=new TableGrid(fieldName);
   return tableGrid.GetCheckFieldValue()
}



//解析按钮的JS中串,并执行
function parseButtonJs(ScriptStr)
{
var beginIndex=-1;
var endIndex=-1;
var ScriptStrtmp;
var tableGrid;

  while(ScriptStr.indexOf("[")>-1)
  {
     beginIndex = ScriptStr.indexOf("[");
     endIndex = ScriptStr.indexOf("]");

     fieldName=ScriptStr.substr(beginIndex+1,endIndex-beginIndex-1);
     tableGrid=new TableGrid(fieldName);


     if(tableGrid.GetCheckFieldValue() == null || tableGrid.GetCheckFieldValue() == "&nbsp")
     {
    	 $('#my-err').modal(options);
       return;
     }

     ScriptStrtmp=ScriptStr;
     ScriptStr=ScriptStr.replace("["+fieldName+"]",tableGrid.GetCheckFieldValue());

     if(ScriptStrtmp == ScriptStr)
	   break;

  }

if(ScriptStr.indexOf("(")>-1 && ScriptStr.indexOf(")")>-1)
{
   eval(ScriptStr);

}else if(ScriptStr.indexOf("[")== -1 && ScriptStr.indexOf("]")==-1)
{
  self.location=ScriptStr;
}
}


function parseButtonJs_value(ScriptStr)
{

var beginIndex=-1;
var endIndex=-1;
var ScriptStrtmp;
var tableGrid;


  while(ScriptStr.indexOf("[")>-1)
  {


     beginIndex = ScriptStr.indexOf("[");

     endIndex = ScriptStr.indexOf("]");

     fieldName=ScriptStr.substr(beginIndex+1,endIndex-beginIndex-1);


     tableGrid=new TableGrid1(fieldName);

     if(tableGrid.GetCheckFieldValue() == null)
	 return;

     ScriptStrtmp=ScriptStr;

     ScriptStr=ScriptStr.replace("["+fieldName+"]",tableGrid.GetCheckFieldValue());

     if(ScriptStrtmp == ScriptStr)
	   break;

  }


if(ScriptStr.indexOf("(")>-1 && ScriptStr.indexOf(")")>-1)
{
   eval(ScriptStr);

}else if(ScriptStr.indexOf("[")== -1 && ScriptStr.indexOf("]")==-1)
{
  self.location=ScriptStr;
}

}



//解析按钮的JS中串,并执行
function parseButtonJs_B(ScriptStr,id)
{
var beginIndex=-1;
var endIndex=-1;
var ScriptStrtmp;
var tableGrid;

  while(ScriptStr.indexOf("[")>-1)
  {


     beginIndex = ScriptStr.indexOf("[");

     endIndex = ScriptStr.indexOf("]");

     fieldName=ScriptStr.substr(beginIndex+1,endIndex-beginIndex-1);

     ScriptStrtmp=ScriptStr;

     ScriptStr=ScriptStr.replace("["+fieldName+"]",id);

     if(ScriptStrtmp == ScriptStr)
	   break;

  }


if(ScriptStr.indexOf("(")>-1 && ScriptStr.indexOf(")")>-1)
{
   eval(ScriptStr);

}else if(ScriptStr.indexOf("[")== -1 && ScriptStr.indexOf("]")==-1)
{
  self.location=ScriptStr;
}

}


function Settrback(TdObj,tab_id){
	$tab_id=tab_id;//tab_id
	   var objTi;
	   objTi=document.getElementById(tab_id);
	   for (var i=1;i<objTi.rows.length;i++){
	    if (i==TdObj)
	    {
	      document.getElementById("selectrow").value = i;
	      for(var j=0;j<objTi.rows[i].cells.length;j++){
	        objTi.rows[i].cells[j].className="Window_TbSE";
	      }
	    }
	    else
	    {
	      if (objTi.rows[i].cells[0].className=="Window_TbSE")
	      {
	        for(var j=0;j<objTi.rows[i].cells.length;j++){
	          if (i%2==0)
	          {
	             objTi.rows[i].cells[j].className="Window_TbS";
	          }else
	          {
	             objTi.rows[i].cells[j].className="Window_TbB";
	          }
	        }
	      }
	    }
	}

	}

//弹出打印窗口
function Openprint(url){
  var w,h,s;
  var IndexWin = null;

  w = screen.availWidth - 4;
  h = screen.availHeight - 10;
  s = "directories=no,left=0,top=0,location=no,menubar=no,resizable=yes,scrollbars=yes,status=yes,toolbar=no,width=" + w + ",height=" + h;
  IndexWin = window.open(url,"",s);
  IndexWin.moveTo(-4,-4);
  IndexWin.focus();
}

//全选/取消
function SelAllChk(name)
{
  var mObject = document.all[name];
  var bool = true;
  if(mObject.length>0)
  {
    bool = !mObject[mObject.length-1].checked
  }

  for (i = 0; i < mObject.length; i++)
  {
     mObject[i].checked = bool;
  }
}