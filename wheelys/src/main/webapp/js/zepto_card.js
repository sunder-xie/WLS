/* 基於框架zepto和CSS3使用
整屏翻转切换JS库使用方法:
zeptoCard.init();
必传参数:_pageBox//模块父集(id) _page//模块元素(class) 
此两项为必传 其他参数选传;
黄绩恒2015-12-24*/
;var zeptoCard = {
	_events: {},									// 自定义事件---this._execEvent('scrollStart');
	_windowHeight : $(window).height(),					// 设备屏幕高度
	 _windowWidth : $(window).width(),		
	     _pageNow : 0,						// 页面当前的index数
	    _pageNext : null,					// 页面下一个的index数
	       _arrow : false,                  //箭头提示(ID)
	    _slideFrom: false,                 //滑动方向x(水平)默认垂直
   _touchStartVal : 0,						// 触摸开始获取的第一个值
     _touchDeltaY : 0,						// 滑动的距离
       _moveStart : true,					// 触摸移动是否开始
	_movePosition : null,					// 触摸移动的方向（上、下）
 _movePosition_c  : null,					// 触摸移动的方向的控制
	_mouseDown	  : false,				// 判断鼠标是否按下
	_moveFirst	  : true,
	_moveInit	  : false,
	_firstChange  : false,                //是否支持第一页可以向上循环滚动
	_lastChange   : false,          		//是否支持最后一页循环到第一页滚动
   _elementStyle  : document.createElement('div').style,	// css属性保存对象
   		 _imgLoad : true,
        _callBack : false, //每頁滑動成功后的回調 默認第一個參數會把當前頁面的index傳出來
 	// 判断函数是否是null空值
	_isOwnEmpty		: function (obj) { 
						for(var name in obj) { 
							if(obj.hasOwnProperty(name)) { 
								return false; 
							} 
						} 
						return true; 
					},
	// 判断浏览器内核类型
	_vendor			: function () {
						var vendors = ['t', 'webkitT', 'MozT', 'msT', 'OT'],transform,i ,l = vendors.length;
						for (i = 0; i < l; i++) {
							transform = vendors[i] + 'ransform';
							if ( transform in this._elementStyle ) return vendors[i].substr(0, vendors[i].length-1);
						}
						return false;
					},
	// 判断浏览器来适配css属性值
	_prefixStyle	: function (style) {
						if ( this._vendor() === false ) return false;
						if ( this._vendor() === '' ) return style;
						return this._vendor() + style.charAt(0).toUpperCase() + style.substr(1);
					},
	// 自定义事件操作
 	_handleEvent: function (type) {
						if ( !this._events[type] ) {
							return;
						}
						var i = 0,
							l = this._events[type].length;
						if ( !l ) {
							return;
						}
						for ( ; i < l; i++ ) {
							this._events[type][i].apply(this, [].slice.call(arguments, 1));	
						}
					},
	// 给自定义事件绑定函数
     _on		: function (type, fn) {
						if ( !this._events[type] ) {
							this._events[type] = [];
						}
						this._events[type].push(fn);
					},
	// 页面切换开始
 	page_start: function(){
 		zeptoCard._page.on('touchstart mousedown',zeptoCard.page_touch_start);
 		zeptoCard._page.on('touchmove mousemove',zeptoCard.page_touch_move);
 		zeptoCard._page.on('touchend mouseup',zeptoCard.page_touch_end);
 	},
 	// 页面切换停止
 	page_stop: function(){
		zeptoCard._page.off('touchstart mousedown');
 		zeptoCard._page.off('touchmove mousemove');
 		zeptoCard._page.off('touchend mouseup');
 	},
 	// page触摸移动start
 	page_touch_start: function(e){
 		if(!zeptoCard._moveStart) return;
 		if(e.type == "touchstart"){
 			if(zeptoCard._slideFrom == 'x') zeptoCard._touchStartVal = window.event.touches[0].pageX;
 			else zeptoCard._touchStartVal = window.event.touches[0].pageY;
        }else{
        	if(zeptoCard._slideFrom == 'x') zeptoCard._touchStartVal = e.pageX||e.x;
        	else zeptoCard._touchStartVal = e.pageY||e.y;
        	zeptoCard._mouseDown = true;
        }
        zeptoCard._moveInit = true;
        // start事件
        zeptoCard._handleEvent('start');
 	},
 	// page触摸移动move
 	page_touch_move : function(e){
 		e.preventDefault();
		if(!zeptoCard._moveStart) return;
		if(!zeptoCard._moveInit) return;
		// 设置变量值
 		var $self = zeptoCard._page.eq(zeptoCard._pageNow),
 			h = parseInt($self.height()),
 			moveP,
 			scrollTop,
 			node=null,
 			move=false;
 		// 获取移动的值
 		if(e.type == "touchmove"){
 			if(zeptoCard._slideFrom == 'x') moveP = window.event.touches[0].pageX;
 			else moveP = window.event.touches[0].pageY;
        	move = true;
        }else{
        	if(zeptoCard._mouseDown){
        		if(zeptoCard._slideFrom == 'x') moveP = e.pageX||e.x;
        		else moveP = e.pageY||e.y;
        		move = true;
        	}else return;
        }
		// 获取下次活动的page
        node = zeptoCard.page_position(e,moveP,$self);
		// page页面移动 		
 		zeptoCard.page_translate(node);
        // move事件
        zeptoCard._handleEvent('move');
	},
 	// page触摸移动判断方向
 	page_position : function(e,moveP,$self){	
 		var now,next;
 		// 设置移动的距离
 		if(moveP!='undefined') zeptoCard._touchDeltaY = moveP - zeptoCard._touchStartVal;
 		// 设置移动方向
    	zeptoCard._movePosition = moveP - zeptoCard._touchStartVal >0 ? 'down' : 'up';
    	if(zeptoCard._movePosition!=zeptoCard._movePosition_c){
    		zeptoCard._moveFirst = true;
    		zeptoCard._movePosition_c = zeptoCard._movePosition;
    	}else{
			zeptoCard._moveFirst = false;
    	}
		// 设置下一页面的显示和位置        
        if(zeptoCard._touchDeltaY<=0){
        	if($self.next(this._pageName).length == 0){
	        	if(zeptoCard._lastChange){
	        		zeptoCard._pageNext = 0;
	 			}else{
	 				return
	 			};
        	} else {
        		zeptoCard._pageNext = zeptoCard._pageNow+1;	
        	}
 		}else{
 			if($self.prev(this._pageName).length == 0 ) {
 				if (zeptoCard._firstChange) {
 					zeptoCard._pageNext = zeptoCard._pageNum - 1;
 				} else {
 					return
 				}
 			} else {
 				zeptoCard._pageNext = zeptoCard._pageNow-1;	
 			}
 		}
        next = zeptoCard._page.eq(zeptoCard._pageNext)[0];
        now = zeptoCard._page.eq(zeptoCard._pageNow)[0];
 		node = [next,now];
 		// move阶段根据方向设置页面的初始化位置--执行一次
 		if(zeptoCard._moveFirst) init_next(node);
 		function init_next(node){
 			var s,l;
 			zeptoCard._page.removeClass('action');
 			$(node[1]).addClass('action').css({'display':'block'});
 			zeptoCard._page.not('.action').css({'display':'none'});
	 		// 显示对应移动的page
			$(node[0]).css({'display':'block','z-index':'40'}); 
			// 设置下一页面的显示和位置        
	        if(zeptoCard._movePosition=='up'){
	        	if(zeptoCard._slideFrom == 'x'){
	 				s = parseInt($(window).scrollLeft());
	 				if(s>0) l = zeptoCard._windowWidth+s;
	 				else l = zeptoCard._windowWidth;
	 				node[0].style[zeptoCard._prefixStyle('transform')] = 'translate('+l+'px,0)';
	        	}else{
	        		s = parseInt($(window).scrollTop());
	 				if(s>0) l = zeptoCard._windowHeight+s;
	 				else l = zeptoCard._windowHeight;
	 				node[0].style[zeptoCard._prefixStyle('transform')] = 'translate(0,'+l+'px)';
	        	}
 				$(node[0]).attr('data-translate',l);
 				$(node[1]).attr('data-translate',0);
	 		}else{
	 			if(zeptoCard._slideFrom == 'x'){
	 				 node[0].style[zeptoCard._prefixStyle('transform')] = 'translate(-'+Math.max(zeptoCard._windowWidth,$(node[0]).width())+'px,0)';
	 				$(node[0]).attr('data-translate',-Math.max(zeptoCard._windowWidth,$(node[0]).width()));
	 			}else{
	 				node[0].style[zeptoCard._prefixStyle('transform')] = 'translate(0,-'+Math.max(zeptoCard._windowHeight,$(node[0]).height())+'px)';
	 				$(node[0]).attr('data-translate',-Math.max(zeptoCard._windowHeight,$(node[0]).height()));
	 			}
 				
 				$(node[1]).attr('data-translate',0);
	 		}
 		}
 		return node;
 	},
 	// page触摸移动设置函数
 	page_translate	: function(node){
 		// 没有传值返回
 		if(!node) return;
		var y_1,y_2,scale, y = zeptoCard._touchDeltaY;
 		// 切换的页面移动
 		if($(node[0]).attr('data-translate')) y_1 = y + parseInt($(node[0]).attr('data-translate'));
 		if(zeptoCard._slideFrom == 'x') node[0].style[zeptoCard._prefixStyle('transform')] = 'translate('+y_1+'px,0)';
 		else node[0].style[zeptoCard._prefixStyle('transform')] = 'translate(0,'+y_1+'px)';
		// 当前的页面移动
		if($(node[1]).attr('data-translate')) y_2 = y + parseInt($(node[1]).attr('data-translate'));
		if(zeptoCard._slideFrom == 'x') scale = 1 - Math.abs(y*0.2/zeptoCard._windowWidth);
		else scale = 1 - Math.abs(y*0.2/zeptoCard._windowHeight);
		y_2 = y_2/2;
		if(zeptoCard._slideFrom == 'x') node[1].style[zeptoCard._prefixStyle('transform')] = 'translate('+y_2+'px,0)'+' scale('+scale+')';
		else node[1].style[zeptoCard._prefixStyle('transform')] = 'translate(0,'+y_2+'px)'+' scale('+scale+')';
 	},
 	// page触摸移动end
 	page_touch_end	: function(e){
 		zeptoCard._moveInit = false;
 		zeptoCard._mouseDown = false;
 		if(!zeptoCard._moveStart) return;
 		if(!zeptoCard._pageNext&&zeptoCard._pageNext!=0) return;
 		zeptoCard._moveStart = false;
 		// 确保移动了
 		if(Math.abs(zeptoCard._touchDeltaY)>10){
 			zeptoCard._page.eq(zeptoCard._pageNext)[0].style[zeptoCard._prefixStyle('transition')] = 'all .3s';
 			zeptoCard._page.eq(zeptoCard._pageNow)[0].style[zeptoCard._prefixStyle('transition')] = 'all .3s';
 		}
		// 页面切换
 		if(Math.abs(zeptoCard._touchDeltaY)>=50){		// 切换成功
	 		zeptoCard.page_success();
 		}else if(Math.abs(zeptoCard._touchDeltaY)>10&&Math.abs(zeptoCard._touchDeltaY)<100){	// 切换失败
 			zeptoCard.page_fial();
 		}else{									// 没有切换
 			zeptoCard.page_fial();
 		}
 		// end事件
        zeptoCard._handleEvent('end');
        // 注销控制值
 		zeptoCard._movePosition = null;
 		zeptoCard._movePosition_c = null;
 		zeptoCard._touchStartVal = 0;
	},
	// 切换成功
 	page_success : function(){
		// 下一个页面的移动
 		zeptoCard._page.eq(zeptoCard._pageNext)[0].style[zeptoCard._prefixStyle('transform')] = 'translate(0,0)';
 		// 当前页面变小的移动
 		var y = zeptoCard._touchDeltaY > 0 ? zeptoCard._windowHeight/5 : -zeptoCard._windowHeight/5;
 		var scale = 0.2;
 		zeptoCard._page.eq(zeptoCard._pageNow)[0].style[zeptoCard._prefixStyle('transform')] = 'translate(0,'+y+'px)'+' scale('+scale+')';
 		setTimeout(function(){
			if(zeptoCard._callBack)zeptoCard._callBack(zeptoCard._pageNow);
		},800)
		//预加载下一页图片
		zeptoCard.imgLoad();
 		// 成功事件
    	zeptoCard._handleEvent('success');
 	},
	// 切换失败
 	page_fial : function(){
 		// 判断是否移动了
		if(!zeptoCard._pageNext&&zeptoCard._pageNext!=0) {
			zeptoCard._moveStart = true;
			zeptoCard._moveFirst = true;
			return;
		}
 		if(zeptoCard._movePosition=='up'){
 			if(zeptoCard._slideFrom == 'x') zeptoCard._page.eq(zeptoCard._pageNext)[0].style[zeptoCard._prefixStyle('transform')] = 'translate('+zeptoCard._windowHeight+'px,0)';
 			else zeptoCard._page.eq(zeptoCard._pageNext)[0].style[zeptoCard._prefixStyle('transform')] = 'translate(0,'+zeptoCard._windowHeight+'px)';
 		}else{
 			if(zeptoCard._slideFrom == 'x') zeptoCard._page.eq(zeptoCard._pageNext)[0].style[zeptoCard._prefixStyle('transform')] = 'translate(-'+zeptoCard._windowHeight+'px,0)';
 			else zeptoCard._page.eq(zeptoCard._pageNext)[0].style[zeptoCard._prefixStyle('transform')] = 'translate(0,-'+zeptoCard._windowHeight+'px)';
 		}
		zeptoCard._page.eq(zeptoCard._pageNow)[0].style[zeptoCard._prefixStyle('transform')] = 'translate(0,0)'+' scale(1)';
		// fial事件
    	zeptoCard._handleEvent('fial');
 	},
 	haddle_envent_fn : function(){
 		// 当前页面移动
		zeptoCard._on('move',function(){
		});
		// 切换失败事件
		zeptoCard._on('fial',function(){
			setTimeout(function(){
				zeptoCard._page.eq(zeptoCard._pageNow).attr('data-translate','');
 				zeptoCard._page.eq(zeptoCard._pageNow)[0].style[zeptoCard._prefixStyle('transform')] = '';
 				zeptoCard._page.eq(zeptoCard._pageNow)[0].style[zeptoCard._prefixStyle('transition')] = '';
 				zeptoCard._page.eq(zeptoCard._pageNext)[0].style[zeptoCard._prefixStyle('transform')] = '';
	 			zeptoCard._page.eq(zeptoCard._pageNext)[0].style[zeptoCard._prefixStyle('transition')] = '';
	 			zeptoCard._page.eq(zeptoCard._pageNext).css({'display':'none','z-index':'0'});
				zeptoCard._moveStart = true;
				zeptoCard._moveFirst = true;
				zeptoCard._pageNext = null;
				zeptoCard._touchDeltaY = 0;
				zeptoCard._page.eq(zeptoCard._pageNow).attr('style','');
	 		},300)
		});
		// 切换成功事件
		zeptoCard._on('success',function(){
			// 判断最后一页让，开启循环切换
			if (zeptoCard._pageNext == 0 && zeptoCard._pageNow == zeptoCard._pageNum -1) {
				zeptoCard._firstChange = true;
			}
			setTimeout(function(){
				// 判断是否为最后一页，显示或者隐藏箭头
				if(zeptoCard._pageNext == zeptoCard._pageNum-1){
					if(zeptoCard._arrow)zeptoCard._arrow.css({'display':'none'});
				}else{
					if(zeptoCard._arrow)zeptoCard._arrow.css({'display':'block'});
				}
				zeptoCard._page.eq(zeptoCard._pageNow).css({'display':'none'});
 				zeptoCard._page.eq(zeptoCard._pageNow)[0].style[zeptoCard._prefixStyle('transform')] = '';
 				zeptoCard._page.eq(zeptoCard._pageNow)[0].style[zeptoCard._prefixStyle('transition')] = '';
 				zeptoCard._page.eq(zeptoCard._pageNext)[0].style[zeptoCard._prefixStyle('transform')] = '';
	 			zeptoCard._page.eq(zeptoCard._pageNext)[0].style[zeptoCard._prefixStyle('transition')] = '';
	 			// 初始化切换的相关控制值
	 			zeptoCard._page.eq(zeptoCard._pageNext).css({'z-index':'0'});
				zeptoCard._pageNow = zeptoCard._pageNext;
				zeptoCard._moveStart = true;
				zeptoCard._moveFirst = true;
				zeptoCard._pageNext = null;
				zeptoCard._touchDeltaY = 0;
				zeptoCard._page.eq(zeptoCard._pageNow)
				// 切换成功后，执行当前页面的动画,在下面待续
	 		},200)
		})
 	},
 	imgLoad : function(){//下一屏的背景图预加载
 		if(!this._imgLoad) return;
 		var loadIndex = this._pageNext + 1;
 		if(loadIndex >= this._pageNum) return;
 		var imgNext = this._page.eq(loadIndex).css('background-image');
 		if(imgNext == 'none' || imgNext == 'initial') return;
 		var imgUrl = imgNext.split('("')[1].split('")')[0];
 		if(imgUrl){
 	 		var imgSrc = new Image();
 	 		imgSrc.src = imgUrl;
 		}
 	},
	// 对象初始化
	init : function(e){
		for(var k in e){
			this[k] = e[k];
		}
		this._pageName = '.'+this._page+'';
		this._page = $(this._pageName);
		this._pageNum = this._page.size(); // 模版页面的个数
		this._pageBox = $('#'+this._pageBox+'');
		if(!this._pageBox || !this._page) return;
		console.info(this._page);
		this._pageBox.css({
			'width':'100%',
			'max-width':'640px',
			'height':'100%',
			'overflow':'hidden'
		})
		if(this._arrow) this._arrow = $('#'+this._arrow+'');
		// 对象操作事件处理
		this.haddle_envent_fn();
        $(window).on('load',function(){
			var now = new Date().getTime();
			var time;
			setTimeout(function(){
				zeptoCard._windowHeight = $(window).height();/*安卓兼容*/
		 		// 开启页面切换
				zeptoCard.page_start();
		        zeptoCard._pageBox.height(zeptoCard._windowHeight);
		        zeptoCard._page.height(zeptoCard._windowHeight);
		        zeptoCard.imgLoad();
			},time)
		})
	}
};