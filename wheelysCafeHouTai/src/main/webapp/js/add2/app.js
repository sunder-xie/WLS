(function($) {
  'use strict';

  $(function() {
    var $fullText = $('.admin-fullText');
    $('#admin-fullscreen').on('click', function() {
      $.AMUI.fullscreen.toggle();
    });

    $(document).on($.AMUI.fullscreen.raw.fullscreenchange, function() {
      $fullText.text($.AMUI.fullscreen.isFullscreen ? '閫€鍑哄叏灞�' : '寮€鍚叏灞�');
    });
  });
})(jQuery);
;(function($, win, undefined){
	if(!$.fn) return;
	function noop () {}
	// 弹出事件处理程序
	function popupHandler () {
		$(this).closePopup();
	}
	var $cover = $("<div />",{
		css:{
			background:'rgba(0,0,0,.6)',
			position:'fixed',
			left:'0',
			top:'0',
			right: '0',
			bottom: '0',
			'backface-visibility': 'hidden'
		}
	});
	var isShown = false;
	// 默认配置
	var defaultOptions = {
		hasMask: true,         // 是否需要蒙板
		duration: 500,         // 动画持续时间,单位ms
		easing: 'ease',        // 动画时间函数
		animateType: 'zoomin', // 动画效果类型
		callback: noop,        // 动画结束后的回调函数
		delay: 0,               // 动画推迟时间
		zIndex : '500'
	};
	var popupInitStyl = {
		zoomin: {
			'transform': "scale(0) translate(-50%, -50%) translateZ(0)",
			'-webkit-transform': "scale(0) translate(-50%, -50%) translateZ(0)",
			'transform-origin':'left top',
			'-webkit-transform-origin':'left top'
		},
		slideinleft: {
			'left': '0',
			'top': '50%',
			'transform': 'scale(1) translate(-150%, -50%) translateZ(0)',
			'-webkit-transform': 'scale(1) translate(-150%, -50%) translateZ(0)',
			'transform-origin':'center center',
			'-webkit-transform-origin':'center center'
		},
		slideinright: {
			'right': '0',
			'left': '',
			'top': '50%',
			'transform': 'scale(1) translate(150%, -50%) translateZ(0)',
			'-webkit-transform': 'scale(1) translate(150%, -50%) translateZ(0)',
			'transform-origin': 'center center',
			'-webkit-transform-origin': 'center center'
		},
		slideindown: {
			'top': '0',
			'left': '50%',
			'transform': 'scale(1) translate(-50%, -150%) translateZ(0)',
			'-webkit-transform': 'scale(1) translate(-50%, -150%) translateZ(0)',
			'transform-origin': 'center center',
			'-webkit-transform-origin': 'center center'
		},
		slideinup: {
			'bottom': '0',
			'left': '50%',
			'top': '',
			'transform': 'scale(1) translate(-50%, 150%) translateZ(0)',
			'-webkit-transform': 'scale(1) translate(-50%, 150%) translateZ(0)',
			'transform-origin': 'center center',
			'-webkit-transform-origin': 'center center'
		}
	};
	var animateType, hasMask;
	$.fn.closePopup = function(options){//$.closePopup关闭
		if (!isShown) return;
		options = $.extend(true, {}, defaultOptions, {animateType: animateType}, options); //关闭时默认用弹出时的动画效果
		this.animate(popupInitStyl[options.animateType], options.duration, options.easing, options.callback, options.delay);
		if (hasMask) {
			$cover.off('click');
			$cover.remove();
			/*setTimeout(function(){
			},options.delay)*/
		}
		isShown = false;
	};
	$.fn.popup = function(options){//$.popup弹出
		if (isShown) return; // 已经弹出不再次弹出
		options = $.extend(true, {}, defaultOptions, options);
		hasMask = options.hasMask;
		animateType = options.animateType;
		// 基本样式
		this.css({
			'display': 'block',
			'transform-style': 'preserve-3D',
			'-webkit-transform-style': 'preserve-3D',
			'z-index':options.zIndex,
			'position':'fixed'
		});
		this.on("touchmove",function(event){event.preventDefault();});
		// 根据动画效果设置初始样式
		switch (options.animateType) {
			case 'zoomin':
				this.css(popupInitStyl.zoomin);
				break;
			case 'slideinleft':
				this.css(popupInitStyl.slideinleft);
				break;
			case 'slideinright':
				this.css(popupInitStyl.slideinright);
				break;
			case 'slideinup':
				this.css(popupInitStyl.slideinup);
				break;
			case 'slideindown':
				this.css(popupInitStyl.slideindown);
				break;
			default: // 默认zoomin
				this.css(popupInitStyl.zoomin);
		}
		this.css({
			'left': '50%',
			'top': '50%'
		})
		this.animate({
			'display':'block',
			'transform': "scale(1) translate(-50%, -50%) translateZ(0)",
			'-webkit-transform': "scale(1) translate(-50%, -50%) translateZ(0)",
			'transform-origin':'left top',
			'-webkit-transform-origin':'left top'
		}, options.duration, options.easing, options.callback, options.delay);
		if (hasMask) {
			$cover.on("touchmove",function(event){event.preventDefault();});
			$cover.on('click', popupHandler.bind(this));
			$cover.css('z-index',options.zIndex-1)
			$('body').append($cover);
		}
		isShown = true;
	}
})(jQuery, window);