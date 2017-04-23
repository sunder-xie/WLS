function WeChat(data){
	$.get("./ajax_getconfig.php",{url:window.location.href},function(data) {
		wx.config(data);
		wx.ready(function (){
			// 在这里调用 API
			wx.onMenuShareTimeline({  //例如分享到朋友圈的API 
				title:  data.title, // 分享标题
				link:  data.url, // 分享链接
				imgUrl:  data.img, // 分享图标
				success: function () {
				},
				cancel: function () {
				// 用户取消分享后执行的回调函数
				        }
			});
			wx.onMenuShareAppMessage({
				title: data.title, // 分享标题
				desc:  data.desc, // 分享描述
				link:  data.url, // 分享链接
				imgUrl:  data.img, // 分享图标
				type: '', // 分享类型,music、video或link，不填默认为link
				dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
				success: function () {
				},
				cancel: function () {
				// 用户取消分享后执行的回调函数
			    }
			});
	        wx.error(function (res) {
	        	alert(res.errMsg);  //打印错误消息。及把 debug:false,设置为debug:ture就可以直接在网页上看到弹出的错误提示
	        });
		});
	},"json");
}