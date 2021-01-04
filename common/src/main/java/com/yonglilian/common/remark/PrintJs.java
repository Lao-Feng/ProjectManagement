package com.yonglilian.common.remark;

public class PrintJs {

	public PrintJs() {
	}

	public static final String get(String page, String mode) {
		StringBuffer sb = new StringBuffer();
		sb.append("var myx;\n");
		sb.append("var myy;\n");
		sb.append("\n");
		sb.append("function MM_reloadPage(init) {\n");
		sb.append("  if (init==true) with (navigator) {if ((appName==\"Netscape\")&&(parseInt(appVersion)==4)) {\n");
		sb.append("   document.MM_pgW=innerWidth; document.MM_pgH=innerHeight; onresize=MM_reloadPage; }}\n");
		sb.append("   else if (innerWidth!=document.MM_pgW || innerHeight!=document.MM_pgH) location.reload();}\n");
		sb.append("MM_reloadPage(true);\n");
		sb.append("\n");
		sb.append("function MM_findObj(n, d) { \n");
		sb.append(" var p,i,x;  if(!d) d=document; if((p=n.indexOf(\"?\"))>0&&parent.frames.length) {\n");
		sb.append("  d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}\n");
		sb.append(" if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];\n");
		sb.append(" for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);\n");
		sb.append(" if(!x && d.getElementById) x=d.getElementById(n); return x;\n");
		sb.append("}\n");
		sb.append("\n");
		sb.append(
				"function MM_dragLayer(objName,x,hL,hT,hW,hH,toFront,dropBack,cU,cD,cL,cR,targL,targT,tol,dropJS,et,dragJS) { \n");
		sb.append("   var i,j,aLayer,retVal,curDrag=null,curLeft,curTop,IE=document.all,NS4=document.layers;\n");
		sb.append("   var NS6=(!IE&&document.getElementById), NS=(NS4||NS6); if (!IE && !NS) return false;\n");
		sb.append("   retVal = true; if(IE && event) event.returnValue = true;\n");
		sb.append("   if (MM_dragLayer.arguments.length > 1) {\n");
		sb.append("   curDrag = MM_findObj(objName); if (!curDrag) return false;\n");
		sb.append("   if (!document.allLayers) { document.allLayers = new Array();\n");
		sb.append("   with (document) if (NS4) { for (i=0; i<layers.length; i++) allLayers[i]=layers[i];\n");
		sb.append(
				"     for (i=0; i<allLayers.length; i++) if (allLayers[i].document && allLayers[i].document.layers)\n");
		sb.append(
				"     with (allLayers[i].document) for (j=0; j<layers.length; j++) allLayers[allLayers.length]=layers[j];\n");
		sb.append("    } else {\n");
		sb.append(
				"    if (NS6) { var spns = getElementsByTagName(\"span\"); var all = getElementsByTagName(\"div\");\n");
		sb.append(
				"       for (i=0;i<spns.length;i++) if (spns[i].style&&spns[i].style.position) allLayers[allLayers.length]=spns[i];}\n");
		sb.append(
				"       for (i=0;i<all.length;i++) if (all[i].style&&all[i].style.position) allLayers[allLayers.length]=all[i]; \n");
		sb.append(" } }\n");
		sb.append(" curDrag.MM_dragOk=true; curDrag.MM_targL=targL; curDrag.MM_targT=targT;\n");
		sb.append(" curDrag.MM_tol=Math.pow(tol,2); curDrag.MM_hLeft=hL; curDrag.MM_hTop=hT;\n");
		sb.append(" curDrag.MM_hWidth=hW; curDrag.MM_hHeight=hH; curDrag.MM_toFront=toFront;\n");
		sb.append(" curDrag.MM_dropBack=dropBack; curDrag.MM_dropJS=dropJS;\n");
		sb.append(" curDrag.MM_everyTime=et; curDrag.MM_dragJS=dragJS;\n");
		sb.append(" curDrag.MM_oldZ = (NS4)?curDrag.zIndex:curDrag.style.zIndex;\n");
		sb.append(" curLeft= (NS4)?curDrag.left:(NS6)?parseInt(curDrag.style.left):curDrag.style.pixelLeft;\n");
		sb.append(" if (String(curLeft)==\"NaN\") curLeft=0; curDrag.MM_startL = curLeft;\n");
		sb.append(" curTop = (NS4)?curDrag.top:(NS6)?parseInt(curDrag.style.top):curDrag.style.pixelTop; \n");
		sb.append(" if (String(curTop)==\"NaN\") curTop=0; curDrag.MM_startT = curTop;\n");
		sb.append(" curDrag.MM_bL=(cL<0)?null:curLeft-cL; curDrag.MM_bT=(cU<0)?null:curTop-cU;\n");
		sb.append(" curDrag.MM_bR=(cR<0)?null:curLeft+cR; curDrag.MM_bB=(cD<0)?null:curTop+cD;\n");
		sb.append(" curDrag.MM_LEFTRIGHT=0; curDrag.MM_UPDOWN=0; curDrag.MM_SNAPPED=false;\n");
		sb.append(" document.onmousedown = MM_dragLayer; document.onmouseup = MM_dragLayer;\n");
		sb.append(" if (NS) document.captureEvents(Event.MOUSEDOWN|Event.MOUSEUP);\n");
		sb.append(" } else {\n");
		sb.append(" var theEvent = ((NS)?objName.type:event.type);\n");
		sb.append(" if (theEvent == 'mousedown') {\n");
		sb.append("   var mouseX = (NS)?objName.pageX : event.clientX + document.body.scrollLeft;\n");
		sb.append("   var mouseY = (NS)?objName.pageY : event.clientY + document.body.scrollTop;\n");
		sb.append("   var maxDragZ=null; document.MM_maxZ = 0;\n");
		sb.append("   for (i=0; i<document.allLayers.length; i++) { aLayer = document.allLayers[i];\n");
		sb.append("     var aLayerZ = (NS4)?aLayer.zIndex:parseInt(aLayer.style.zIndex);\n");
		sb.append("     if (aLayerZ > document.MM_maxZ) document.MM_maxZ = aLayerZ;\n");
		sb.append("       var isVisible = (((NS4)?aLayer.visibility:aLayer.style.visibility).indexOf('hid') == -1);\n");
		sb.append("       if (aLayer.MM_dragOk != null && isVisible) with (aLayer) {\n");
		sb.append("       var parentL=0; var parentT=0;\n");
		sb.append("       if (NS6) { parentLayer = aLayer.parentNode;\n");
		sb.append("           while (parentLayer != null && parentLayer.style.position) {\n");
		sb.append(
				"           parentL += parseInt(parentLayer.offsetLeft); parentT += parseInt(parentLayer.offsetTop);\n");
		sb.append("           parentLayer = parentLayer.parentNode;\n");
		sb.append("       } } else if (IE) { parentLayer = aLayer.parentElement;\n");
		sb.append("       while (parentLayer != null && parentLayer.style.position) {\n");
		sb.append("           parentL += parentLayer.offsetLeft; parentT += parentLayer.offsetTop;\n");
		sb.append("           parentLayer = parentLayer.parentElement; } }\n");
		sb.append(
				"       var tmpX=mouseX-(((NS4)?pageX:((NS6)?parseInt(style.left):style.pixelLeft)+parentL)+MM_hLeft);\n");
		sb.append(
				"       var tmpY=mouseY-(((NS4)?pageY:((NS6)?parseInt(style.top):style.pixelTop) +parentT)+MM_hTop);\n");
		sb.append("       if (String(tmpX)==\"NaN\") tmpX=0; if (String(tmpY)==\"NaN\") tmpY=0;\n");
		sb.append("       var tmpW = MM_hWidth;  if (tmpW <= 0) tmpW += ((NS4)?clip.width :offsetWidth);\n");
		sb.append("       var tmpH = MM_hHeight; if (tmpH <= 0) tmpH += ((NS4)?clip.height:offsetHeight);\n");
		sb.append("         if ((0 <= tmpX && tmpX < tmpW && 0 <= tmpY && tmpY < tmpH) && (maxDragZ == null\n");
		sb.append("           || maxDragZ <= aLayerZ)) { curDrag = aLayer; maxDragZ = aLayerZ; } } }\n");
		sb.append("      if (curDrag) {\n");
		sb.append("     document.onmousemove = MM_dragLayer; if (NS4) document.captureEvents(Event.MOUSEMOVE);\n");
		sb.append("     curLeft = (NS4)?curDrag.left:(NS6)?parseInt(curDrag.style.left):curDrag.style.pixelLeft;\n");
		sb.append("     curTop = (NS4)?curDrag.top:(NS6)?parseInt(curDrag.style.top):curDrag.style.pixelTop;\n");
		sb.append("     if (String(curLeft)==\"NaN\") curLeft=0; if (String(curTop)==\"NaN\") curTop=0;\n");
		sb.append("     MM_oldX = mouseX - curLeft; MM_oldY = mouseY - curTop;\n");
		sb.append("     document.MM_curDrag = curDrag;  curDrag.MM_SNAPPED=false;\n");
		sb.append("     if(curDrag.MM_toFront) {\n");
		sb.append("       eval('curDrag.'+((NS4)?'':'style.')+'zIndex=document.MM_maxZ+1');\n");
		sb.append("       if (!curDrag.MM_dropBack) document.MM_maxZ++; }\n");
		sb.append("      retVal = false; if(!NS4&&!NS6) event.returnValue = false;\n");
		sb.append("  } } else if (theEvent == 'mousemove') {\n");
		sb.append("   if (document.MM_curDrag) with (document.MM_curDrag) {\n");
		sb.append("    var mouseX = (NS)?objName.pageX : event.clientX + document.body.scrollLeft;\n");
		sb.append("    var mouseY = (NS)?objName.pageY : event.clientY + document.body.scrollTop;\n");
		sb.append("    myx = (NS)?objName.pageX : event.clientX - event.offsetX + document.body.scrollLeft;\n");
		sb.append("    myy = (NS)?objName.pageY : event.clientY - event.offsetY + document.body.scrollTop;\n");
		sb.append("    window.status = \"当前印章：x=\"+myx+\";y=\"+myy;\n");
		sb.append("    newLeft = mouseX-MM_oldX; newTop  = mouseY-MM_oldY;\n");
		sb.append("    if (MM_bL!=null) newLeft = Math.max(newLeft,MM_bL);\n");
		sb.append("    if (MM_bR!=null) newLeft = Math.min(newLeft,MM_bR);\n");
		sb.append("    if (MM_bT!=null) newTop  = Math.max(newTop ,MM_bT);\n");
		sb.append("    if (MM_bB!=null) newTop  = Math.min(newTop ,MM_bB);\n");
		sb.append("    MM_LEFTRIGHT = newLeft-MM_startL; MM_UPDOWN = newTop-MM_startT;\n");
		sb.append(" if(myx>-110 && myy>-110 && myx<740 && myy<1030) \n{ ");
		sb.append("    if (NS4) {left = newLeft; top = newTop;}\n");
		sb.append("    else if (NS6){style.left = newLeft; style.top = newTop;}\n");
		sb.append("    else {style.pixelLeft = newLeft; style.pixelTop = newTop;}\n");
		sb.append("}\n");
		sb.append("    if (MM_dragJS) eval(MM_dragJS);\n");
		sb.append("    retVal = false; if(!NS) event.returnValue = false;\n");
		sb.append("   } } else if (theEvent == 'mouseup') {\n");
		sb.append("   document.onmousemove = null;\n");
		sb.append("   if (NS) document.releaseEvents(Event.MOUSEMOVE);\n");
		sb.append("   if (NS) document.captureEvents(Event.MOUSEDOWN);\n");
		sb.append("   if (document.MM_curDrag) with (document.MM_curDrag) {\n");
		sb.append("   if (typeof MM_targL =='number' && typeof MM_targT == 'number' &&");
		sb.append("        (Math.pow(MM_targL-((NS4)?left:(NS6)?parseInt(style.left):style.pixelLeft),2)+");
		sb.append("         Math.pow(MM_targT-((NS4)?top:(NS6)?parseInt(style.top):style.pixelTop),2))<=MM_tol) {\n");
		sb.append("        if (NS4) {left = MM_targL; top = MM_targT;}\n");
		sb.append("        else if (NS6) {style.left = MM_targL; style.top = MM_targT;}\n");
		sb.append("        else {style.pixelLeft = MM_targL; style.pixelTop = MM_targT;}\n");
		sb.append("        MM_SNAPPED = true; MM_LEFTRIGHT = MM_startL-MM_targL; MM_UPDOWN = MM_startT-MM_targT; }\n");
		sb.append("       if (MM_everyTime || MM_SNAPPED) eval(MM_dropJS);\n");
		sb.append("     if(MM_dropBack) {if (NS4) zIndex = MM_oldZ; else style.zIndex = MM_oldZ;}\n");
		sb.append("      retVal = false; if(!NS) event.returnValue = false; }\n");
		sb.append("     document.MM_curDrag = null;\n");
		sb.append(" }\n");
		sb.append(" if (NS) document.routeEvent(objName);\n");
		sb.append(" } return retVal;\n");
		sb.append("}\n");
		sb.append("function thisx(obj)\n");
		sb.append("{\n");
		sb.append("  var xx = obj.style.left;\n");
		sb.append("  xx = xx.substring(0,xx.length-2);\n");
		sb.append("  return xx;\n");
		sb.append("}\n");
		sb.append("function thisy(obj)\n");
		sb.append("{\n");
		sb.append("  var yy = obj.style.top;\n");
		sb.append("  yy = yy.substring(0,yy.length-2);\n");
		sb.append(" return yy;\n");
		sb.append("}\n");
		sb.append("\nvar XML = document.frmCachat.PmXML.value;\n");
		sb.append("if(XML==null||XML.length<34)\n");
		sb.append("{\n");
		sb.append("  XML=\"<?xml version=\\\"1.0\\\"?><EPM></EPM>\";\n");
		sb.append("}\n");
		sb.append(" function saveXML()\n");
		sb.append("{\n");
		sb.append("document.frmCachat.PmXML.value = XML;\n");
		sb.append("}\n");
		sb.append("document.onmouseout = saveXML;\n");
		sb.append("try{\n  ini('" + page + "','" + mode + "');\n}\ncatch(e)\n{\n}\n");
		sb.append("function nomenu(){return false;}document.oncontextmenu=nomenu;");
		return sb.toString();
	}
}
