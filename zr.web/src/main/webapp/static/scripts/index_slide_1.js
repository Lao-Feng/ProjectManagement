$(document).ready(function () {

    setModule($(".index_wrap"));

    function setModule(div){
        var screen_height=$(window).height()||$(document).height();
        div.css({'height':screen_height+'px'});//修改高度
    }


});
$(document).ready(function() {

    //获取轮播ID
    //var CarouselId;
    var currentTimer;
//    var CarouselId1;
//    var CarouselId2;
//    var CarouselId3;
    //切换时间
    var speedT = 500;
    //延迟时间
    var delayT = 4000;

    clone('#Carousel .carousel-img ul');
    clone('#Carousel1 .carousel-img ul');
    clone('#Carousel2 .carousel-img ul');
    clone('#Carousel3 .carousel-img ul');
    clone('#Carousel4 .carousel-img ul');
    clone('#Carousel5 .carousel-img ul');

    //资讯中心导航切换
    var kx_tab = $(".column .col1 ul a");
    var kx_con = $(".gz-sy-qh").children();
    kx_tab.hover(function(){
        kx_con.eq(kx_tab.index(this)).show().siblings().hide();
        $(this).addClass("top-act").siblings().removeClass("top-act");
        //每次切换导航后其对应主体的图片开始轮播
        show();
    });

    //默认导航一对应的内容图片开始轮播
    show();

    //导航切换其对应主体的图片开始轮播
    function show() {
        var index=$(".column .col1 ul .top-act").index();
        var CarouselId = $(".ya-bz-lb").eq(index).attr('id');
//        console.log(CarouselId+"CarouselId00")
        if(currentTimer!=null){
            clearInterval(currentTimer);//当切换导航时停止当前轮播
        }
        currentTimer=lb('#'+CarouselId);
    }

    //轮播
    function lb(Carousel){
        var len = 4;
        var historyImgWidth=$(Carousel+' .carousel-img ul li').eq(0).width();
//        console.log("historyImgWidth:"+historyImgWidth);
        $(Carousel+' .carousel-img ul').width(len*historyImgWidth);
//        console.log("banner_img_box:"+$(Carousel+' .banner_img_box').width())


        $(Carousel+' .opt-pre').click(function(){
//            if(!$(Carousel+' .carousel-img ul').is(':animated')) {
//                clickPlay_left(Carousel+' .carousel-img ul', historyImgWidth,Carousel+' .carousel-img ul .cur-li');
//                spChg(Carousel+' .carousel-img ul .cur-li',Carousel+' .carousel-des-sp a');
//            }
            if(!$(Carousel+' .carousel-img ul').is(':animated')) {
                clickPlay_left(Carousel+' .carousel-img ul', historyImgWidth,Carousel+' .carousel-img ul .cur-li');
                spChg(Carousel+' .carousel-chg .carousel-chg-cur',null);
            }

        });
        $(Carousel+' .opt-next').click(function(){
//            if(!$(Carousel+' .carousel-img ul').is(':animated')) {
//                clickPlay_right(Carousel+' .carousel-img ul', historyImgWidth,Carousel+' .carousel-img ul .cur-li');
//                spChg(Carousel+' .carousel-img ul .cur-li',Carousel+' .carousel-des-sp a');
//            }
            if(!$(Carousel+' .carousel-img ul').is(':animated')) {
                clickPlay_right(Carousel+' .carousel-img ul', historyImgWidth,Carousel+' .carousel-img ul .cur-li');
                spChg(Carousel+' .carousel-chg .carousel-chg-cur',null);
            }

        });

        function timePlay(div){
            $(Carousel+' .carousel-img ul').eq($(Carousel+' .carousel-chg .carousel-chg-cur').index()).find("li").eq(0).addClass("cur-li");
            if(!$(div).is(':animated')){
//                console.log("$(div).length:"+$(div).length);
                for(var i=0;i<$(div).length;i++){
                    var nowPositioin=$(div).eq(i).position().left;
                    if(-nowPositioin>=$(div).eq(i).width()){
                        var left=$(div).eq(i).siblings().position().left;
                        $(div).eq(i).stop().css({'left':$(div).eq(i).width()+left+'px'},speedT);
                    }
                    var nowPositioin2=$(div).eq(i).position().left;

                    $(div).eq(i).stop().animate({'left':nowPositioin2-historyImgWidth+'px'},speedT);

                }
                nextLi(Carousel+' .carousel-img ul .cur-li',Carousel+' .carousel-img ul li',Carousel+' .carousel-chg a');
                spChg(Carousel+' .carousel-chg .carousel-chg-cur',null);
            }

        }

        var timer =window.setInterval(function()
        {
            timePlay(Carousel+' .carousel-img ul');
        }, delayT);
        return timer;

        function nextLi(curLi,curImg,num){
            var a=$(curLi).next().index();
            //        a=(a+4)%4;
            if($(curLi).index()<3){
                $(curLi).next().addClass("cur-li").siblings().removeClass("cur-li");
                var i = $(curLi).index();
                $(num).eq(i).addClass("carousel-chg-cur").siblings().removeClass("carousel-chg-cur");
            }
            else{
                $(curImg).eq(0).addClass("cur-li").siblings().removeClass("cur-li");
                var i = $(curLi).index();
                $(num).eq(i).addClass("carousel-chg-cur").siblings().removeClass("carousel-chg-cur");
            }
        }


        function clickPlay_right(div,imgwidth,curLi){
            if(!$(div).is(':animated')){
                for(var i=0;i<$(div).length;i++){
                    if($(div).eq(i).position().left<=-$(div).eq(i).width()){
                        var pervLeft= $(div).eq(i).siblings().position().left;
                        $(div).eq(i).css({'left':pervLeft+$(div).eq(i).width()+'px'});
                    }
                    var historyContainer=$(div).eq(i).position().left;
                    var news_position=historyContainer-imgwidth;
                    $(div).eq(i).stop(true,true).animate({'left':news_position+'px'},speedT);
                }
            }
            var a=$(curLi).index();
            //var index =a==4?0:a;
            var indexNext;
            if(a == 3){
                indexNext=0;
            }else{
                indexNext=a+1;
            }
            moveT(indexNext,Carousel+' .carousel-chg a',Carousel+' .carousel-img ul li');
        }
        function clickPlay_left(div,imgwidth,curLi){
            if(!$(div).is(':animated')){
                for(var i=0;i<$(div).length;i++){
                    if($(div).eq(i).position().left>=$(div).parent().width()){
                        var pervLeft= $(div).eq(i).siblings().position().left;

                        $(div).eq(i).css({'left':-$(div).eq(i).width()+pervLeft+'px'})
                    }
                    var historyContainer=$(div).eq(i).position().left;
                    var news_position=historyContainer+imgwidth;
                    $(div).eq(i).stop(true,true).animate({'left':news_position+'px'},speedT);
                }

            }
            var a=$(curLi).index();
            //var index =a==4?0:a;
            var indexNext;
            if(a == 0){
                indexNext=3;
            }else{
                indexNext=a-1;
            }
            moveT(indexNext,Carousel+' .carousel-chg a',Carousel+' .carousel-img ul li');

        }

        //序号，当前图片切换
        function moveT(indexNext,num,curImg){
            $(num).eq(indexNext).addClass("carousel-chg-cur").siblings().removeClass("carousel-chg-cur");
            $(curImg).eq(indexNext).addClass("cur-li").siblings().removeClass("cur-li");
        }
        //文字改变
        function spChg(numCur,imgSp) {
            var chg_index = $(numCur).index();
            $(imgSp).eq(chg_index).show().siblings().hide();
        }
    }
    //end 轮播

    function clone(div){
        var divclone=$(div).clone();
        $(div).after(divclone);
        var divWidth=$(div).width();
        for(var i=0;i<$(div).parent().children().length;i++){
            $(div).parent().children().eq(i).css({'left':divWidth*i+'px'});
        }
    }

    // 新增轮播7-27
    $(".carousel-chg a").hover(function(){

    })

});

jQuery.fn.extend({

    slideFocus: function(){
        var This = $(this);
        var sWidth = This.width();
        var	len    =This.find('ul li').length;
        var	index  = 0;
        var	Timer;

        // btn event
        var btn = "<div class='btn'>";
        for(var i=0; i < len-1; i++) {
            btn += "<span></span>";
        };
        btn += "</div>";
        $(This).append(btn);
        $(This).find('.btn span').eq(0).addClass('on');

        $(This).find('.btn span').mouseover(function(){
            index = $(This).find('.btn span').index(this);
            Tony(index);
        });
        $(This).find('ul').css("width",sWidth * (len));     //确定ul的总长
        $(This).hover(function(){
            clearInterval(Timer);
        },function(){
            Timer=setInterval(function(){
                if(len == index){
                    index = 1;
                    $(This).find('ul').css('left' , 0);
                    return;
                }
                Tony(index);
                index++;
            }, 2000)
        }).trigger("mouseleave");
        function Tony(index){
            var new_width = -index * sWidth;
            $(This).find('ul').animate({'left' : new_width},300);
            if(index == len - 1){
                $(This).find('.btn span').eq(0).addClass('on').siblings().removeClass('on')
            }else{
                $(This).find('.btn span').eq(index).addClass('on').siblings().removeClass('on');
            }
        };
    }
});

$(function() {
	$('.pic_silde').slideFocus();
	$(".swiper-ul").append($(".swiper-ul li").clone()).css("width",$(".swiper-ul li").outerWidth(true)*$(".swiper-ul li").length);
	$('.scroll_left').click(function(){
		var now_index = -Math.floor(parseFloat($(".swiper-ul").css("marginLeft"))/$(".swiper-ul li").outerWidth(true));
		if (now_index == $(".swiper-ul li").length/2) {
			now_index = 0;
			$(".swiper-ul").stop().css("margin-left","0");
		};
		now_index++;
		$(".swiper-ul").stop().animate({
			marginLeft: -$(".swiper-ul li").outerWidth(true)*now_index + "px"
		});
	})
	$('.scroll_right').click(function(){
		var now_index = -Math.ceil(parseFloat($(".swiper-ul").css("marginLeft"))/$(".swiper-ul li").outerWidth(true));
		if (now_index == 0) {
			now_index = $(".swiper-ul li").length/2;
			$(".swiper-ul").stop().css("margin-left",-$(".swiper-ul li").outerWidth(true)*now_index+"px");
		};
		now_index--;
		$(".swiper-ul").stop().animate({
			marginLeft: -$(".swiper-ul li").outerWidth(true)*now_index + "px"
		});
//		console.log(now_index);
	})

})
// 7-27新增js
$(document).ready(function(){
    $(".column .col1"). mouseover(function(){
        // $(".xz-col1box").height($(".column .col1").height()+$(".xz-gz-sy-qh").height());
        if(!$(".xz-gz-sy-qh").is(':animated') && !$(".column .col1").is(':animated')){
            $(".xz-gz-sy-qh").animate({'height':'172px'},400);
            $(".column .col1").animate({'top':'0px','bottom':'172px'},400);
            $(".column_btn").animate({'top':'-45px'},400);
            $(".xz-con2").css('background','url(gzgg/xz-topbanner3bg.png)');
        }
        
    });
    $(".xz-slebox").mouseleave(function(){
        // if(!$(".xz-gz-sy-qh").is(':animated') && !$(".column .col1").is(':animated')){
            $(".column .col1").animate({'top':'172px','bottom':'0px'},400);
            $(".xz-gz-sy-qh").animate({'height':'0px'},400);
            $(".column_btn").animate({'top':'126px'},400);
            setTimeout(function(){
                $(".xz-con2").css('background','url(gzgg/xz-topbanner3bg2.png)');
            },400);
        // }
    });
    //点击伸缩效果
    $(".column_btn").click(function(){
        if($(".xz-gz-sy-qh").height() == 0){
            if(!$(".xz-gz-sy-qh").is(':animated') && !$(".column .col1").is(':animated')){
                $(".xz-gz-sy-qh").animate({'height':'172px'},400);
                $(".column .col1").animate({'top':'0px','bottom':'172px'},400);
                $(".column_btn").animate({'top':'-45px'},400);
                $(".xz-con2").css('background','url(gzgg/xz-topbanner3bg.png)');
            }
        }else{
            $(".column .col1").animate({'top':'172px','bottom':'0px'},400);
            $(".xz-gz-sy-qh").animate({'height':'0px'},400);
            $(".column_btn").animate({'top':'126px'},400);
            setTimeout(function(){
                $(".xz-con2").css('background','url(gzgg/xz-topbanner3bg2.png)');
            },400);
        }
    });
    // 搜索框获取焦点
    $('#j_search').focus(function(){
        if($(this).val()=='请输入关键检索字'){
            $(this).val('');
        }
    });
    $('#j_search').blur(function(){
        if($(this).val()==''){
            $(this).val('请输入关键检索字');
        }
    });
})
