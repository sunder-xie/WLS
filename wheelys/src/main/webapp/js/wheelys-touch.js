var wheelys = {};
wheelys.basePath = $('#metaDataList')?$('#metaDataList').attr('data-basePath'):/wheelys/;
wheelys.isMobile = function(v) {
  	return (/^(?:13\d|15[0-9]|18[0-9]|17[0-9]|14[0-9])-?\d{5}(\d{3}|\*{3})$/.test(v));
};
wheelys.isEmail = function(v){
	return (/^[a-zA-Z0-9._-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)*(\.[a-zA-Z]{2,4})$/.test(v));
};
wheelys.isIdCard = function(v){
	return (/(^\d{15}$)|(^\d{17}([0-9]|[X,x])$)/.test(v));
};
wheelys.ajaxHtml = function(id,url,callback){
	var options = $.extend({
		type: "GET",
		url:url,
		addHtml:false,
        dataType: "html",
        success: function(data){
        	if(!options.addHtml)$('#'+id).html('');
        	$('#'+id).append(data);
        	if($.isFunction(callback))callback.call(this,data);
         },
         error: function(data) {
        	 if(data.status != 200){
                 alert('网络异常，请检查您的网络');
        	 }
         }
	},arguments[3]);
	$.ajax(options);
};
wheelys.ajax = function(data){
	var options = $.extend({
		type: "GET",
        dataType: "json",
        success: function(data){
			if($.isFunction(options.callback)){
				options.callback.call(this,data);
			}
		}
	},data);
	$.ajax(options);
};
wheelys.proveCode = function(){
	
};

wheelys.slidePage = function(e){
	if($('#boxCoupon').length > 0 )return;
	var options = $.extend({
		url:'',
		boxWidth:$('body').width(),
		speed: 500,
		data:{},
		closeFn:'',
		clickButton:'',
		ajaxcallback:function(){}
		
	},e);
	var BodyscrollTop = $('body').scrollTop();
	var box = $("<div />" ,{
		id:'boxCoupon'
	})
	$(box).css({
		'width':'0',
		'height':$(window).height(),
		'position':'absolute',
		'left':options.boxWidth+'px',
		'background-color':'#F7F7F7',
		'z-index':'99',
		'overflow':'hidden',
		'top':'0'
	}).appendTo($('body'));
	wheelys.ajaxHtml('boxCoupon',options.url,function(){
		if(options.ajaxcallback)options.ajaxcallback.call(this,options.closeFn);
	},{data:options.data})
	function clickSlide(){
		$(box).css({
    		'width':'100%'
    	});
		if(!$(box).css("transform") == null){
			$(box).animate({
				'transform': 'translate(-'+options.boxWidth+'px)',
				'-webkit-transform': '-webkit-translate(-'+options.boxWidth+'px)' 
		    }, options.speed, 'ease-out');
		}else{
			$(box).animate({
				'left': '0px'
		    }, options.speed, 'ease-out');
		}
		$('body').css({
			'overflow':'hidden',
			'height':'100%'
		})
		$('html').css({
			'overflow':'hidden',
			'height':'100%',
			'position':'relative'
		})
	}
	if(!options.clickButton){
		clickSlide();
	}else{
		options.clickButton.on('click',function(){
			clickSlide();
		})
	}
	options.closeFn = function(){
		if(!$(box).css("transform") == null){
			$(box).animate({
				transform: 'translate(0)' 
		    }, options.speed, 'ease-out',function(){
		    	$(box).css({
		    		'width':'0'
		    	});
		    });
		}else{
			$(box).animate({
				'left': options.boxWidth+'px' 
		    }, options.speed, 'ease-out',function(){
		    	$(box).css({
		    		'width':'0'
		    	});
		    });
		}
		$("body").removeAttr("style");
		$("html").removeAttr("style");
	}
}

wheelys.textOver = function(parent){//input默认文字
	var input = parent ? $('#'+parent+' input[data-type=textover]'): $('input[data-type=textover]');
	input.each(function(index,item){
		var parent = $(item).parent();
		if($(parent).css('position')=='static'){
		   $(parent).css({'position':'relative'});
		};
		var lable = $('<lable></lable>')
		$(lable).html($(item).attr('alt'));
		$(lable).css({
			'display':'block',
			'height':$(item).height(),
			'line-height':$(item).height()+'px',
			'width':$(item).width(),
			'position':'absolute',
			'left':$(item).position().left,
			'top':$(item).position().top,
			'font-size':$(item).css('font-size'),
			'color':'#d6d6d6',
			'overflow':'hidden'
		});
		$(item).css({
			'position':'relative'
		})
		$(item).on('input',function(e){
			if($(item).attr('value')!=''){
				$(lable).hide();
			}else{
				$(lable).show();
			};
		});
		$(item).before($(lable));
		if($(item).val()!='')$(lable).hide();
		$(item).on('blur',function(e){
			if($(this).val() == ""){
				$(lable).show();
			}
		})
	})
};
wheelys.addNum = function(id,num,time){
	time = time?time:1000;
	var obj = $('#'+id);
	obj.css({
		'position':'relative'
	})
	var i = $('<i style="position:absolute; right:-20px;top:0px; font-size:8px; color:#e63a46">'+num+'</i>');
	obj.append(i);
	$(i).animate({
		opacity: 0,
		top:'-20px',
		color: '#abcdef',
		translate3d: '0,10px,0'
	}, time, 'ease-out')
	setTimeout(function(){
		$(i).remove()
	},time);
};
//滑动事件根据方向回调
wheelys.touchDirection = function(callback){
	var op = {
		situ:callback.situ?callback.up:function(){},
		up:callback.up?callback.up:function(){},
		down:callback.down?callback.down:function(){},
		left:callback.left?callback.left:function(){},
		right:callback.right?callback.right:function(){}
	}
	var startx, starty;
	//获得角度
	function getAngle(angx, angy) {
	    return Math.atan2(angy, angx) * 180 / Math.PI;
	};
	//根据起点终点返回方向 1向上 2向下 3向左 4向右 0未滑动
	function getDirection(startx, starty, endx, endy) {
	    var angx = endx - startx;
	    var angy = endy - starty;
	    var result = 0;
	    //如果滑动距离太短
	    if (Math.abs(angx) < 2 && Math.abs(angy) < 2) {
	        return result;
	    }
	    var angle = getAngle(angx, angy);
	    if (angle >= -135 && angle <= -45) {
	        result = 1;
	    } else if (angle > 45 && angle < 135) {
	        result = 2;
	    } else if ((angle >= 135 && angle <= 180) || (angle >= -180 && angle < -135)) {
	        result = 3;
	    } else if (angle >= -45 && angle <= 45) {
	        result = 4;
	    }
	    return result;
	}
	//手指接触屏幕
	document.addEventListener("touchstart", function(e) {
	    startx = e.touches[0].pageX;
	    starty = e.touches[0].pageY;
	}, false);
	//手指离开屏幕
	document.addEventListener("touchend", function(e) {
	    var endx, endy;
	    endx = e.changedTouches[0].pageX;
	    endy = e.changedTouches[0].pageY;
	    var direction = getDirection(startx, starty, endx, endy);
	    switch (direction) {
	        case 0:
	        	op.situ.call(this,'')
	            break;
	        case 1:
	        	op.up.call(this,'')
	            break;
	        case 2:
	        	op.down.call(this,'')
	            break;
	        case 3:
	        	op.left.call(this,'')
	            break;
	        case 4:
	        	op.right.call(this,'')
	            break;
	        default:
	    }
	}, false);
}
wheelys.isWeiXin = function(){
	var ua = window.navigator.userAgent.toLowerCase(); 
	if(ua.match(/MicroMessenger/i) == 'micromessenger'){ 
		return true; 
	}else{ 
		return false; 
	} 
} 
//校验input列表 name对应  绑定在dom上 参数为报错ID回执模块 可不传
$.fn.inputValCheck  = function(errBox){
	$(this).parent('li').removeClass('err');
	if(errBox)var errBox = $('#'+errBox);
	var isPass = true;
	var html = '';
	$(this).each(function(e,a){
		var name = $(a).attr('name');
		var val = $(a).val();
		if(name == 'phoneCode' && val.length != 11 && !wheelys.isMobile(val)){
			if(val.length == 0){
				if(isPass)html = '您的手机号不能为空，请输入';
			}else{
				if(isPass)html = '您输入的手机号有误，请重新输入';
			}
	    	$(a).parent('li').addClass('err');
			isPass = false;
		}else if(name == 'picCode' && val.length != 4){
			if(val.length == 0){
				if(isPass)html = '您的图片验证码不能为空，请输入';
			}else{
				if(isPass)html = '您输入的图片验证码有误，请重新输入';
			}
	    	$(a).parent('li').addClass('err');
			isPass = false;
		}else if(name == 'message' && val.length != 4){
			if(val.length == 0){
				if(isPass)html = '您的短信验证码不能为空，请输入';
			}else{
				if(isPass)html = '您输入的短信验证码有误，请重新输入';
			}
	    	$(a).parent('li').addClass('err');
			isPass = false;
		}
    	if(errBox)errBox.html('<p>'+html+'</p>');
	})
	if(isPass){
		return true;
	}else{
        return false;
	}
}
function setCookie(name,value,day){ 
    var Days = day?day:30; 
    var exp = new Date(); 
    exp.setTime(exp.getTime() + Days*24*60*60*1000); 
    document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString(); 
} 
function getCookie(name){ 
    var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
    if(arr=document.cookie.match(reg))
        return unescape(arr[2]);
    else 
        return null; 
} 
/*(function ($) {
	  $.fn.slideDown = function (duration) {    
	    // get old position to restore it then
	    var position = this.css('position');
	    
	    // show element if it is hidden (it is needed if display is none)
	    this.show();
	    
	    // place it so it displays as usually but hidden
	    this.css({
	      position: 'absolute',
	      visibility: 'hidden'
	    });
	 
	    // get naturally height
	    var height = this.height();
	    
	    // set initial css for animation
	    this.css({
	      position: position,
	      visibility: 'visible',
	      overflow: 'hidden',
	      height: 0
	    });
	 
	    // animate to gotten height
	    this.animate({
	      height: height
	    }, duration);
	  };
	})(Zepto);*/