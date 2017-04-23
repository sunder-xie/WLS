function isee(that){//开启隐藏密码
	var input = $(that).siblings('input');
	if($(input).attr('type')!='password'){
		$(that).addClass('off');
		$(input).attr('type','password')
	}else{
		$(that).removeClass('off');
		$(input).attr('type','text')
	}
}

function getPhoneCodeFn(id,num){//获取手机号点击后
	var id = $('#'+id);
	var self = $(id);
	var ohtml = id.html();
	if(self.hasClass('wait'))return
	self.off();
	self.addClass('wait');
	var t = num;
	self.html('已发送('+t+'S)');
	var st = setInterval(function(){
		t--;
		self.html('已发送('+t+'S)');
		sessionStorage.setItem('getPhoneCode',t);
		if(t<0){
			sessionStorage.clear('getPhoneCode')
			self.removeClass('wait') ;
			self.html(ohtml);
			clearInterval(st);
		}
	},1000)
}
function inputListCheck(inputList,callback){
	var valueList = {};
	$(inputList).each(function(e,a){
		valueList[a] = $('input[name='+a+']').attr('value');
	})
	return valueList;
}