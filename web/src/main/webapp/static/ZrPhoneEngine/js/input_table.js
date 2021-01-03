
//监听客户端宽度，设置数据到表格
function _$set_htmlTab(){
	var f=4,v=6;//至少大于5
    //通过标题宽度和屏幕宽度判断生成几张表格
	var tab_sum=1,wid_sum=0;
	var tab_html="";
	var tab_num= new Array();//每一张表截止的列表序号
	if(title_with!= undefined&& title_with.length != 0){
	       $.each(title_with,function(n,value){
	            wid_sum+=parseInt(value);
				if(wid_sum>=(parseInt(winWidth)-150)){
				   tab_num[parseInt(tab_sum)-1]=n;
				   tab_sum++;
				   wid_sum=0;
				}else{
					if(n==(parseInt(title_with.length)-1)){
						tab_num[parseInt(tab_sum)-1]=n;
						tab_sum++;
						wid_sum=0;
					}else{
						
					}
				}
	        });
	    }
		if(tab_sum>=1){
			tab_sum=tab_sum-1;
		}
	//开始填充表格
	var re_num=0;
	for(var i=0;i<parseInt(tab_sum);i++){
		if(re_num==0||tab_num[i]<=(re_num-1)){
			   tab_html+="<li>";
			   tab_html+="<table id='tableTi"+i+"' class='am-table am-table-striped am-table-hover'>";
			   //填充标题及值
			   tab_html+="<thead>";
			   tab_html+="<TR>";
			   tab_html+="<th class='NoNewline' width='50px'>序号</th>";
			   tab_html+="<th fieldName='"+title_ymc_w[0]+"' class='NoNewline' width='"+title_with[0]+"px'>"+$_vStr(title_zmc_w[0],f)+"</th>";//第一列
		       $.each(title_zmc_w,function(n,value){
		    	  var style="";
		    	  if(parseInt(title_with[n])==0){
		    		  style="style='display: none'";
		    		  if(re_num==0){
		    			  re_num=n;
		    		  }
		    	  }
			      //从第二列开始
			      if(i==0&&n>=1){
				     //超过后，隐藏，方便后面参数传递
				     if(tab_num[i]<n){
				    	 style="style='display: none'";
				    	 tab_html+="<th fieldName='"+title_ymc_w[n]+"' class='NoNewline' "+style+" width='"+title_with[n]+"px'>"+$_vStr(title_zmc_w[n],f)+"</th>";
				     }else{
				    	 tab_html+="<th fieldName='"+title_ymc_w[n]+"' class='NoNewline' "+style+" width='"+title_with[n]+"px'>"+$_vStr(title_zmc_w[n],f)+"</th>";
				     }
				  }
				  if(i>0&&n>=1){
					  
				   //超过后，隐藏，方便后面参数传递
				     if(n>tab_num[i-1]&&tab_num[i]>=n){
				    	 tab_html+="<th fieldName='"+title_ymc_w[n]+"' class='NoNewline' "+style+" width='"+title_with[n]+"px'>"+$_vStr(title_zmc_w[n],f)+"</th>";
				     }else{
				    	 style="style='display: none'";
				    	 tab_html+="<th fieldName='"+title_ymc_w[n]+"' class='NoNewline' "+style+" width='"+title_with[n]+"px'>"+$_vStr(title_zmc_w[n],f)+"</th>";
				     }
				  }
		        });
				tab_html+="</TR>";
				tab_html+="</thead>";
				//填充数据行
				tab_html+="<tbody>";
				$.each(value_array,function(k,attay_json){
					tab_html+="<TR class='tr_row' onclick=\"ExeOnclick('"+body_tr_key[k]+"');\" ondblclick=\"ExeOndblclick('"+body_tr_key[k]+"')\" class=\"oddtrcss\" >";
		            tab_html+="<td onclick='Settrback("+(k+1)+",'tableTi"+i+"');'  class='NoNewline' width='50px' fieldValue='"+(k+1)+"'><span title='"+(k+1)+"'>"+(k+1)+"</span></td>";//序号
					tab_html+="<td fieldName='"+value_array[k][0]+"'>"+value_array[k][0]+"</td>";//第一列
		            $.each(attay_json,function(m,value){
		            	var style="";
		          	    if(parseInt(title_with[m])==0){
		          		   style="style='display: none'";
		          	    }
						//从第二列开始
						if(i==0&&m>=1){
					       if(tab_num[i]<m){
					    	   style="style='display: none'";
					    	   tab_html+="<td "+style+" onclick=\"Settrback("+(k+1)+",'tableTi"+i+"');\"  class='NoNewline' width='"+title_with[m]+"px' fieldValue='"+value_array[k][m]+"'><span title='"+value_array[k][m]+"' class='NoNewline'>"+$_vStr(value_array[k][m],v)+"</span></td>";
					       }else{
					    	   tab_html+="<td "+style+" onclick=\"Settrback("+(k+1)+",'tableTi"+i+"');\" class='NoNewline' width='"+title_with[m]+"px' fieldValue='"+value_array[k][m]+"'><span title='"+value_array[k][m]+"' class='NoNewline'>"+$_vStr(value_array[k][m],v)+"</span></td>";
					       }
				        }
				        if(i>0&&m>=1){
					       if(m>tab_num[i-1]&&tab_num[i]>=m){
					    	   tab_html+="<td "+style+" onclick=\"Settrback("+(k+1)+",'tableTi"+i+"');\"  class='NoNewline' width='"+title_with[m]+"px' fieldValue='"+value_array[k][m]+"'><span title='"+value_array[k][m]+"' class='NoNewline'>"+$_vStr(value_array[k][m],v)+"</span></td>";
					       }else{
					    	   style="style='display: none'";
					    	   tab_html+="<td "+style+" onclick=\"Settrback("+(k+1)+",'tableTi"+i+"');\"  class='NoNewline' width='"+title_with[m]+"px' fieldValue='"+value_array[k][m]+"'><span title='"+value_array[k][m]+"' class='NoNewline'>"+$_vStr(value_array[k][m],v)+"</span></td>";
					       }
				        }
						
					});
				    tab_html+="</TR>";
		        });
		        tab_html+="</tbody>";
			    tab_html+="</table>";
			    tab_html+="</li>";
		}
	}
	//console.log(tab_html);
	//给id填充表格
	$("#tab_list").html(tab_html);
}

//监听客户端宽度，设置数据到表格
function _$set_htmlTab_statis(){
	var f=8,v=6;//至少大于5
    //通过标题宽度和屏幕宽度判断生成几张表格
	var tab_sum=1,wid_sum=0;
	var tab_html="";
	var tab_num= new Array();//每一张表截止的列表序号
	if(title_with!= undefined&& title_with.length != 0){
       $.each(title_with,function(n,value){
            wid_sum+=parseInt(value);
			if(wid_sum>=(parseInt(winWidth)-150)){
			   tab_num[parseInt(tab_sum)-1]=n;
			   tab_sum++;
			   wid_sum=0;
			}else{
				if(n==(parseInt(title_with.length)-1)){
					tab_num[parseInt(tab_sum)-1]=n;
					tab_sum++;
					wid_sum=0;
				}else{
					
				}
			}
        });
    }
	if(tab_sum>=1){
		tab_sum=tab_sum-1;
	}
	//开始填充表格
	var re_num=0;
	for(var i=0;i<parseInt(tab_sum);i++){
		if(re_num==0||tab_num[i]<=(re_num-1)){
			   tab_html+="<li>";
			   tab_html+="<table id='tableTi"+i+"' class='am-table am-table-striped am-table-hover'>";
			   //填充标题及值
			   tab_html+="<thead>";
			   tab_html+="<TR>";
			   
			   tab_html+="<th fieldName='"+title_ymc_w[0]+"' class='NoNewline' width='"+title_with[0]+"px'>"+$_vStr(title_zmc_w[0],f)+"</th>";//第一列
			   tab_html+="<th fieldName='"+title_ymc_w[1]+"' class='NoNewline' width='"+title_with[1]+"px'>"+$_vStr(title_zmc_w[1],f)+"</th>";//第二列
		       $.each(title_zmc_w,function(n,value){
		    	  var style="";
		    	  if(parseInt(title_with[n])==0){
		    		  style="style='display: none'";
		    		  if(re_num==0){
		    			  re_num=n;
		    		  }
		    	  }
			      //从第二列开始
			      if(i==0&&n>=2){
				     //超过后，隐藏，方便后面参数传递
				     if(tab_num[i]<n){
				    	 style="style='display: none'";
				    	 tab_html+="<th fieldName='"+title_ymc_w[n]+"' class='NoNewline' "+style+" width='"+title_with[n]+"px'>"+$_vStr(title_zmc_w[n],f)+"</th>";
				     }else{
				    	 tab_html+="<th fieldName='"+title_ymc_w[n]+"' class='NoNewline' "+style+" width='"+title_with[n]+"px'>"+$_vStr(title_zmc_w[n],f)+"</th>";
				     }
				  }
				  if(i>0&&n>=2){
					  
				   //超过后，隐藏，方便后面参数传递
				     if(n>tab_num[i-1]&&tab_num[i]>=n){
				    	 tab_html+="<th fieldName='"+title_ymc_w[n]+"' class='NoNewline' "+style+" width='"+title_with[n]+"px'>"+$_vStr(title_zmc_w[n],f)+"</th>";
				     }else{
				    	 style="style='display: none'";
				    	 tab_html+="<th fieldName='"+title_ymc_w[n]+"' class='NoNewline' "+style+" width='"+title_with[n]+"px'>"+$_vStr(title_zmc_w[n],f)+"</th>";
				     }
				  }
		        });
				tab_html+="</TR>";
				tab_html+="</thead>";
				//填充数据行
				tab_html+="<tbody>";
				$.each(value_array,function(k,attay_json){
					tab_html+="<TR class='tr_row' onclick=\"ExeOnclick('"+body_tr_key[k]+"');\" ondblclick=\"ExeOndblclick('"+body_tr_key[k]+"')\" class=\"oddtrcss\" >";
		            tab_html+="<td onclick='Settrback("+(k+1)+",'tableTi"+i+"');'  class='NoNewline' width='50px' fieldValue='"+(k+1)+"'><span title='"+(k+1)+"'>"+value_array[k][0]+"</span></td>";//序号
					tab_html+="<td fieldName='"+value_array[k][0]+"'>"+value_array[k][1]+"</td>";//第一列
		            $.each(attay_json,function(m,value){
		            	var style="";
		          	    if(parseInt(title_with[m])==0){
		          		   style="style='display: none'";
		          	    }
						//从第二列开始
						if(i==0&&m>=2){
					       if(tab_num[i]<m){
					    	   style="style='display: none'";
					    	   tab_html+="<td "+style+" onclick=\"Settrback("+(k+1)+",'tableTi"+i+"');\"  class='NoNewline' width='"+title_with[m]+"px' fieldValue='"+value_array[k][m]+"'><span title='"+value_array[k][m]+"' class='NoNewline'>"+$_vStr(value_array[k][m],v)+"</span></td>";
					       }else{
					    	   tab_html+="<td "+style+" onclick=\"Settrback("+(k+1)+",'tableTi"+i+"');\" class='NoNewline' width='"+title_with[m]+"px' fieldValue='"+value_array[k][m]+"'><span title='"+value_array[k][m]+"' class='NoNewline'>"+$_vStr(value_array[k][m],v)+"</span></td>";
					       }
				        }
				        if(i>0&&m>=2){
					       if(m>tab_num[i-1]&&tab_num[i]>=m){
					    	   tab_html+="<td "+style+" onclick=\"Settrback("+(k+1)+",'tableTi"+i+"');\"  class='NoNewline' width='"+title_with[m]+"px' fieldValue='"+value_array[k][m]+"'><span title='"+value_array[k][m]+"' class='NoNewline'>"+$_vStr(value_array[k][m],v)+"</span></td>";
					       }else{
					    	   style="style='display: none'";
					    	   tab_html+="<td "+style+" onclick=\"Settrback("+(k+1)+",'tableTi"+i+"');\"  class='NoNewline' width='"+title_with[m]+"px' fieldValue='"+value_array[k][m]+"'><span title='"+value_array[k][m]+"' class='NoNewline'>"+$_vStr(value_array[k][m],v)+"</span></td>";
					       }
				        }
						
					});
				    tab_html+="</TR>";
		        });
		        tab_html+="</tbody>";
			    tab_html+="</table>";
			    tab_html+="</li>";
		}
	}
	//console.log(tab_html);
	//给id填充表格
	$("#tab_list").html(tab_html);
}

/***
 * 截取字节显示
 * @param text
 * @param num
 * @return
 */
function $_vStr(text,num){
	num=parseInt(num);
	if(text.length>num){
		return text.slice(0,num)+"";
	}else{
		return text;
	}
}

/***
 * 统计报表 多级展示时候
 * @param unitid
 * @param showname
 * @param tier
 * @param type
 * @return
 */
function opencompute_bak(unitid,showname,tier,type){
   var strvalue = unitid;
   if (type=="1")
   {
        document.all.show1.style.display='';
        document.all.show2.style.display='';
        flashs();
        str();
        parent.document.computer.location.href="../../statisticscompute.do?ID=<%=strID%>&P1=<%=P1%>&P2=<%=P2%>&P3=<%=P3%>&P4=<%=P4%>&P5=<%=P5%>&UNITID="+strvalue;
   }else{
     if (strvalue.length>1)
     {
         //打开当前表格的详细信息
         var showlink = "<%=strSHOWLINK%>";
         var strTiers = "<%=strTiers%>";
         if (showlink.length>0)
         {
            if (strTiers.match(tier)>0)//可显示详细信息
            {
                var ID = "<%=strID%>";
                var UNITID = unitid;
                var P1 = "<%=P1%>";
                var P2 = "<%=P2%>";
                var P3 = "<%=P3%>";
                var P4 = "<%=P4%>";
                var P5 = "<%=P5%>";

                var tier1= parseInt(tier)+2;
                var strvalue = showname;
                if (parseInt(strvalue)>0)
                {
                var w,h,s;
                var MenuWindow = null;
                w = 800;
                h = 600;
                s = "directories=no,left=0,top=0,location=no,menubar=no,resizable=yes,scrollbars=no,status=no,toolbar=no,width=" + w + ",height=" + h;
                MenuWindow = window.open("openlink.jsp?ID="+ID+"&UNITID="+UNITID+"&TIER="+tier+"&LINK="+showlink+"&P1="+P1+"&P2="+P2+"&P3="+P3+"&P4="+P4+"&P5="+P5,"_blank",s);
                MenuWindow.moveTo(100,100);
                MenuWindow.focus();
                }
            }
         }
     }
   }
}
