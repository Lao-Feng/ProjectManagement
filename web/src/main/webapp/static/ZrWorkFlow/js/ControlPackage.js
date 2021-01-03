var canmove=false;
var scaleFactor=10.26;

var tempOffsetX=0;
var tempOffsetY=0;

var mapOffsetX=0;
var mapOffsetY=0;
var Starline;
var activeConcept;
var thisobj=null   //为了完成各种基本编辑功能，如“置前”“复制”“删除”等
var StartPointX;
var StartPointY;
var MaxID=1;

function MoveStart(){
	if(event.srcElement.className!="WorkFlowItem") { return; }
	if (event.srcElement.className=="WorkFlowItem"){
		event.srcElement.parentElement.style.position="absolute";
		activeConcept=event.srcElement.parentElement;
		canmove=true;
	}
	tempOffsetX = event.offsetX;
	tempOffsetY = event.offsetY;

	mapOffsetX = GetMapOffsetY();
	mapOffsetY = GetMapOffsetX();
	Chart.attachEvent('onmousemove',Moving);
}

function Moving(){
	if (event.button !=1) return;
	if(canmove){
		//document.selection.empty();
		activeConcept.style.pixelLeft = (event.x  - tempOffsetX -mapOffsetX + document.body.scrollLeft ) * scaleFactor;
		activeConcept.style.pixelTop = (event.y - tempOffsetY - mapOffsetY + document.body.scrollTop ) * scaleFactor;
    }
}

function UpdateOneLinePos(line){
	var beginShape;
	var endShape;
	beginShape = document.all.item(line.getAttribute("BeginShape")).parentElement;
	endShape = document.all.item(line.getAttribute("EndShape")).parentElement;
	SetJoinLine(beginShape,endShape,line);
}

function GetCenterX(shape){
	 return shape.style.pixelLeft + shape.style.pixelWidth / 2;
}

function GetCenterY(shape){
	return shape.style.pixelTop + shape.style.pixelHeight / 2;
}

function EndMove(){
	canmove=false;
	//document.selection.empty();
}

function GetMapOffsetY(){
	var tempMap = Chart;
	var tempY = 0;
	while (tempMap.tagName!="BODY")
	{
		tempY = tempY + tempMap.offsetTop;
		tempMap = tempMap.offsetParent;
	}
	return 	tempY + document.body.topMargin ;
}

function GetMapOffsetX(){
	var tempMap = Chart;
	var tempX=0;
	while (tempMap.tagName!="BODY")
	{
		tempX = tempX + tempMap.offsetLeft;
		tempMap = tempMap.offsetParent;
	}
	return 	tempX + document.body.leftMargin ;
}

function MouseUP(){
   var tempEnd=event.srcElement;
   WorkFlowItemMenu.style.display='none';
   if(tempEnd.className=="WorkFlowItem" && event.button==2)
   {
    WorkFlowItemMenu.style.left=event.x;
    WorkFlowItemMenu.style.top=event.y;
    WorkFlowItemMenu.style.display='';
    thisobj=event.srcElement;
   }
}

//设置包属性
function SetupProperty(WorkFlowItemobj){
      OpenSetup("/flow/openflowcfg?Act=PackageEdit&ID="+WorkFlowItemobj);
}

//删除流程包
function DeletePackage(WorkFlowItemobj){
      layer.confirm("分类下有流程时不执行删除，确定要删除吗？",{icon:3, title:'提示'},function(index){
           parent.location.href="/flow/flowpackage?Act=del&ID="+WorkFlowItemobj;
      });
}

//删除流程
function DeleteFlow(WorkFlowItemobj){
      layer.confirm("将删除流程及流程下的相关配置，确定要删除吗？",{icon:3, title:'提示'},function(index){
           parent.location.href="/flow/flowprocess?Act=del&ID="+WorkFlowItemobj;
      });
}
//设置流程属性
function SetupProperty1(WorkFlowItemobj){
      OpenSetup1("/flow/openflowcfg?Act=EditFlowFrm&ID="+WorkFlowItemobj);
}

//打开流程
function OpenFlow(WorkFlowItemobj){
      parent.location.href="/flow/openflowcfg?Act=FlowShowDesign&PackageID="+WorkFlowItemobj;
}

//打开子包
function OpenSubPackage(WorkFlowItemobj){
      parent.location.href="/flow/openflowcfg?Act=PackageDesign&FID="+WorkFlowItemobj;
}

//打开复制流程包
function OpenFlowPackageCopy(WorkFlowItemobj){
      layer.confirm("将复制包及包下的所有流程，确定要复制吗？",{icon:3, title:'提示'},function(index){
           parent.location.href="/flow/openflowcfg?Act=FlowPackageCopy&PackageID="+WorkFlowItemobj;
      });
}

//打开复制流程
function OpenFlowCopy(WorkFlowItemobj,PackageID){
      layer.confirm("将复制流程及流程下的所有配置，确定要复制吗？",{icon:3, title:'提示'},function(index){
           parent.location.href="/flow/openflowcfg?Act=FlowCopy&PackageID="+PackageID+"&FlowID="+WorkFlowItemobj;
      });
}

//打开流程配置
function OpenFlowConfig(WorkFlowItemobj){
      parent.location.href="/flow/openflowcfg?Act=FlowDesign&ID="+WorkFlowItemobj;
}

function AddPackage(FID){
    var url="/flow/openflowcfg?Act=PackageAdd&FID="+FID;
    OpenSetup(url);
}

//弹出新窗体(属性设置)
function OpenSetup(url){
    OpenSetupHtml(url);
}

function OpenSetupHtml(mFileUrl){
	var index =layer.open({
        type: 2,
        title: "流程分类设置",
        area: ['500px', '360px'],
        fix: false, //不固定
        maxmin: false,
        content: mFileUrl
    });
}

//弹出新窗体1(属性设置)
function OpenSetup1(url){
    OpenSetupHtmlA(url);
}

function OpenSetupHtmlA(mFileUrl){
	var index =parent.layer.open({
        type: 2,
        title: "流程属性设置",
        area: ['600px', '480px'],
        fix: false, //不固定
        maxmin: false,
        content: mFileUrl
    });
}

//弹出新窗体(删除提示)
function OpenDelete(url){
    OpenSetupHtml(url);
}

document.onclick = EndMove;
//Chart.onmousedown = MoveStart;
//Chart.onmouseup = MouseUP;