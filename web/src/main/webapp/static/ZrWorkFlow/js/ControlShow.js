//显示并调整所有的线段
function ShowAllLine()
{
	var allLine = document.body.all.item('ConnectLine');
	var i;


	if (allLine!=null)
	{
		var count=allLine.length;
		if (count)
		{
			for (i=count-1; i>=0; i--)
			{
				UpdateOneLinePos(allLine[i]);
				allLine[i].className = "NormalLine";
			}
		}
		else
		{
			UpdateOneLinePos(allLine);
			allLine.className="NormalLine";
		}
	}
}

function UpdateOneLinePos(line)
{
	var beginShape;
	var endShape;

	beginShape = document.all.item(line.getAttribute("BeginShape")).parentElement;
	endShape = document.all.item(line.getAttribute("EndShape")).parentElement;
	SetJoinLine(beginShape,endShape,line);
}

function SetJoinLine(fromShape,toShape,Line)
{
	var fromShapeCenterX = GetCenterX(fromShape);
	var fromShapeCenterY = GetCenterY(fromShape);

	var toShapeCenterX = GetCenterX(toShape);
	var toShapeCenterY = GetCenterY(toShape);
	//取得开始点与结束点
	//此处附值时，必须用（括起来。
	var StartX=(fromShape.style.pixelLeft + fromShape.style.pixelWidth+150);
	var StartY=fromShapeCenterY;
	var EndXLine=(toShape.style.pixelLeft-130);
	var EndYLine=toShapeCenterY;
	//设定新的线段点集合

        if(StartY!=EndYLine)
        {Line.points.value=StartX+","+StartY+","+EndXLine+","+EndYLine;}
        else
        {
	if(EndXLine>StartX)
	{
		Line.points.value=EndXLine+","+EndYLine+","+(StartX+200)+","+EndYLine+","+(StartX+200)+","+StartY+","+StartX+","+StartY
		+","+(StartX+200)+","+StartY+","+(StartX+200)+","+EndYLine+","+EndXLine+","+EndYLine;

	}
	else
	{
		Line.points.value=EndXLine+","+EndYLine+","+EndXLine+","+(EndYLine+500)+","+(StartX+200)+","+(EndYLine+500)+","+(StartX+200)+","+StartY+","+StartX+","+StartY+","+
		(StartX+200)+","+StartY+","+
		(StartX+200)+","+(EndYLine+500)+","+
		EndXLine+","+(EndYLine+500)+","+
		EndXLine+","+EndYLine;
	}
        }
}


function GetCenterX(shape)
{
	 return shape.style.pixelLeft + shape.style.pixelWidth / 2;
}

function GetCenterY(shape)
{
	return shape.style.pixelTop + shape.style.pixelHeight / 2;
}
