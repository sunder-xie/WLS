var buyCart = {};
buyCart.init = function(){//购物车入口
	var addOption = {//商品弹窗默认添加配置项
		hot:'hot',
		milk:'n',
		bean:'n'
	};
	var allOption = $.extend({
		id:'',
		num:'1',
		type:''
	},addOption);
	/*for(var k in allOption){
		console.info(k);
	}*/
	
	this.selectCofe(allOption);
	this.gotoCart();
}

buyCart.selectCofe = function(allOption){//点击选择商品
	var cofeListMod = $('#cofeList ul li');
	var that;
	cofeListMod.on('click',function(){
		var a = $(this).attr('data-add');
		that = $(this);
		thatDiv = $(this).children('div');
		var price = $(this).attr('data-price');
		var img = thatDiv.children('.goodsImg').attr('src');
		var Ename = thatDiv.children('.goodsEname').html();
		var Cname = thatDiv.children('.goodsCname').html();
		var cofeDetailMod = $('#cofeDetail').children('.cofeDetailMod');
		$('#nowPrice').html(price);
		$('.re').removeClass('on');
		$('#addThree').hide();
		cofeDetailMod.children('.cofePic').children('img').attr('src',img);
		cofeDetailMod.children('#goodsEname').html(Ename);
		cofeDetailMod.children('#goodsCname').html(Cname);
		$('#newCart').attr('data-id',that.attr('data-id'));
		var ajaxList = {
				productid:'',
				quantity:'',
				otherinfo:{}
		};
		$('#newCart').attr('value','1');
		$('.cofeOption.addOption').remove();
		if($(that).attr('data-hot')){
			var html = '<div class="cofeOption clear addOption hotCold">';
				html +=		'<div class="optionKey">';
				html +=			'<span>Cold</span>/<span>Hot</span><span class="ml10">冷</span>/<span>热</span>';
				html +=		'</div>';
				html +=		'<div class="optionValue optionTag on" data-type="hot">';
				html +=			'<span class="temperature">';
				html +=			allOption.hot.toUpperCase();
				html +=			'</span>';
				html +=			'<span class="switch"></span>';
				html +=		'</div>';
				html +=	'</div>';
			$('#cofeNum').before(html);
			ajaxList.otherinfo.hot = allOption.hot;
		}
		if($(that).attr('data-bean')){
			var html = '<div class="cofeOption clear addOption">';
				html +=		'<div class="optionKey">';
				html +=			'<span>Double</span><span class="ml10">特浓</span>';
				html +=		'</div>';
				html +=		'<div class="optionValue optionTag off" data-type="bean">';
				html +=			'<span class="temperature">';
				html +=			allOption.bean == 'n'?'NO':'YES';
				html +=			'</span>';
				html +=			'<span class="switch"></span>';
				html +=		'</div>';
				html +=	'</div>';
				$('#cofeNum').before(html);
			ajaxList.otherinfo.bean = allOption.bean;
		}
		if($(that).attr('data-milk')){
			var html = '<div class="cofeOption clear addOption">';
				html +=		'<div class="optionKey">';
				html +=			'<span>Milk</span><span class="ml10">加奶</span>';
				html +=		'</div>';
				html +=		'<div class="optionValue optionTag off" data-type="milk">';
				html +=			'<span class="temperature">';
				html +=			allOption.milk == 'n'?'NO':'YES';
				html +=			'</span>';
				html +=			'<span class="switch"></span>';
				html +=		'</div>';
				html +=	'</div>';
				$('#cofeNum').before(html);
			ajaxList.otherinfo.milk = allOption.milk;
		}
		$('#cofeDetail').popup({
			zIndex:'510',
			callback:function(){
				var self = this;
				var numberMod = $('.number .optionValue');
				$('.optionTag').on('click',function(){//左右切换
					if($(this).hasClass('on')){
						$(this).removeClass('on');
						$(this).addClass('off');
						if($(this).attr('data-type')=='hot'){
							$(this).children('.temperature').html('COLD');
							$('#newCart').attr('data-hot','cold');
							ajaxList.otherinfo.hot = 'n'
						}
						if($(this).attr('data-type')=='milk'){
							$(this).children('.temperature').html('NO');
							$('#newCart').attr('data-milk','n');
							ajaxList.otherinfo.milk = 'n'
						}
						if($(this).attr('data-type')=='bean'){
							$(this).children('.temperature').html('NO');
							$('#newCart').attr('data-bean','n');
							ajaxList.otherinfo.bean = 'n';
							$('#addThree').hide();
						}
					}else{
						$(this).removeClass('off');
						$(this).addClass('on');
						if($(this).attr('data-type')=='hot'){
							$(this).children('.temperature').html('HOT');
							$('#newCart').attr('data-hot','hot');
							ajaxList.otherinfo.hot = 'y'
						}
						if($(this).attr('data-type')=='milk'){
							$(this).children('.temperature').html('YES');
							$('#newCart').attr('data-milk','y');
							ajaxList.otherinfo.milk = 'y'
						}
						if($(this).attr('data-type')=='bean'){
							$(this).children('.temperature').html('YES');
							$('#newCart').attr('data-bean','y');
							ajaxList.otherinfo.bean = 'y';
							$('#addThree').html('+'+$('#newCart').attr('value')*3);
							$('#addThree').show();
						}
					}
				});
				buyCart.numSelect(numberMod,false,'nowPrice',price);//数量选择
				$('.toCart').off();
				$('.toCart').on('click',function(){//确认商品选择
					var newCart = $('#newCart');
					var num = newCart.attr('value');
					var id = newCart.attr('data-id');
					ajaxList = {'hot':ajaxList.otherinfo.hot,'milk':ajaxList.otherinfo.milk,'bean':ajaxList.otherinfo.bean}
					allList = {'productid':id,'quantity':num};
					allList.otherinfo = JSON.stringify(ajaxList);
					wheelys.ajax({
						type:'post',
						url:wheelys.basePath+'shop/shoppingCart.xhtml',
						data:allList,
						callback:function(data){
							$(self).closePopup();
							$('#gotoCart span').html(data.data.quantity);
						/*	buyCart.gotoCartNum(num);*/
							$('#allPrice i').html(data.data.totalfee);
						}
					})
				})
			}
		});
	})
};
buyCart.gotoCart = function(){//购物车
	var clearCart = $('.clearCart');//清空购物车star
	var cover = $("<div />",{
		css:{
			background:'rgba(0,0,0,.6)',
			position:'fixed',
			left:'0',
			top:'0',
			right: '0',
			bottom: '0',
			'z-index':'499',
			'backface-visibility': 'hidden'
		}
	});
	function down(){
		$('body').css({'overflow':'hidden'});
		$('.shoppingCartBox').addClass('down');
		$('.shoppingCartBox').removeClass('up');
		$(cover).remove();
		$('.shoppingCart').removeClass('cartUp');
		$('.shoppingCart').addClass('cartDown');
		$('.clearCart').removeClass('on');
	}
	$('#gotoCart').on('click',function(){
		if($('.shoppingCart').hasClass('cartUp')){//关闭购物车
			down();
		}else{//打开购物车
			$('#shoppingCartList').html('');
			wheelys.ajaxHtml('shoppingCartList',wheelys.basePath+'shop/getShoppingCart.xhtml',function(data){//请求购物车列表
				buyCart.numSelect($('.carNumMod'),true);//绑定数量组件
				$('.delectCart').each(function(e,a){//删除单项
					$(a).on('click',function(){
						var thatP = $(this).parent();
						var key = $(thatP).attr('id');
						var ajaxList = {
							opttype:'remove',
							key:key
						};
						wheelys.ajax({
							type:'post',
							url:wheelys.basePath+'shop/shoppingCart.xhtml',
							data:ajaxList,
							callback:function(data){
								$(thatP).closest('li').remove();
								$('#gotoCart span').html(data.data.quantity);
								$('#allPrice i').html(data.data.totalfee);
							}
						})
					})
				})
			})
			clearCart.addClass('on');
			$('.shoppingCartBox').removeClass('down');//购物车动画
			$('.shoppingCartBox').addClass('up');
			cover.off();
			cover.on('click',function(){
				down();
			});
			cover.on("touchmove",function(event){event.preventDefault();});
			$('body').append(cover);
			$('body').css({'overflow':'hidden'});
			$('.shoppingCart').removeClass('cartDown');
			$('.shoppingCart').addClass('cartUp');
		}
	})
	/*clearCart.on('click',function(){
		 if (confirm("你确定清空吗？")){ 
				var ajaxList = {
					opttype:'reset'
				}
				wheelys.ajax({
					type:'post',
					url:wheelys.basePath+'shop/shoppingCart.xhtml',
					data:ajaxList,
					callback:function(){
						$('.shoppingCartList li').remove();
						buyCart.gotoCartNum('zero');
						$('#allPrice i').html('0');
					}
				})
	     }
	})*/								//清空购物车end
}
buyCart.numSelect =function(numberMod ,toAjax ,id ,price){//数量选择组件传组件dom
	$(numberMod).each(function(e,a){
		if($(a).children('input').attr('value')>1) $(a).children('span.re').addClass('on');
		var numberSp = $(a).children('span');
		$(numberSp).off();
		$(numberSp).on('touchstart',function(){
			var num = $(a).children('.num').attr('value');
			var re = $(a).children('.re');
			if($(this).hasClass('re')){
				if(num <= 1){
					if($(this).hasClass('on'))$(this).removeClass('on');
					if(toAjax){
						$(a).closest('li').remove();
						toAjaxFn(a,'-1')
					}
					return;
				}
				num--;
				if(toAjax){
					toAjaxFn(a,'-1',re,id)
				}else{
					if(num<=1)re.removeClass('on');
					if(id)$('#'+id).html(parseInt($('#'+id).html())-parseInt(price));
				}
			}else if($(this).hasClass('add')){
				if(num >= 99){
					alert('商品数量已达上限');
					return;
				}
				$(this).addClass('on');
				num++;
				if(toAjax){
					toAjaxFn(a,'1',re,id);
				}else{
					if(!re.hasClass('on'))re.addClass('on');
					if(id)$('#'+id).html(parseInt($('#'+id).html())+parseInt(price));
				}
			}
			$(a).children('.num').attr('value',num);
			$('#addThree').html('+'+$('#newCart').attr('value')*3);
			$('#addThree').css({
				'position':'absolute'
			})
		})
	})
	function toAjaxFn(m,n ,re,id){
		var key = $(m).closest('li').attr('id');
		var ajaxList = {
			key:key,
			quantity:n
		}
		wheelys.ajax({
			type:'post',
			url:wheelys.basePath+'shop/shoppingCart.xhtml',
			data:ajaxList,
			callback:function(data){
				if(!$(re).hasClass('on'))$(re).addClass('on');
				if(id)$('#'+id).html(parseInt($('#'+id).html())+parseInt(price));
				var li = $(m).closest('li');
				$(li).children('.shoppingCartRight').children('.price').html(data.data.productfee);
				$('#gotoCart span').html(data.data.quantity);
				$('#allPrice i').html(data.data.totalfee);
			}
		})
	}
}
/*buyCart.gotoCartNum = function(num){//购物车数量计算
	if(num == 'zero'){
		$('#gotoCart span').html('0');
		return;
	}
	var gotoCartNum = $('#gotoCart span').html();
	gotoCartNum = parseInt(gotoCartNum) + parseInt(num);
	$('#gotoCart span').html(gotoCartNum);
}*/