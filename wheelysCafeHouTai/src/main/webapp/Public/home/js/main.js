/**
 * Created by admain on 2016/5/5.
 */

/*  index  */


 
 function inputValue() {
    var doc = document,
        inputs = doc.getElementsByTagName('input'),
        supportPlaceholder = 'placeholder'in doc.createElement('input'),

        placeholder = function (input) {
            var text = input.getAttribute('placeholder'),
                defaultValue = input.defaultValue;
            if (defaultValue == '') {
                input.value = text
            }
            input.onfocus = function () {
                if (input.value === text) {
                    this.value = ''
                }
            };
            input.onblur = function () {
                if (input.value === '') {
                    this.value = text
                }
            }
        };

    if (!supportPlaceholder) {
        for (var i = 0, len = inputs.length; i < len; i++) {
            var input = inputs[i],
                text = input.getAttribute('placeholder');
            if (input.type === 'text' && text) {
                placeholder(input)
            }
        }
    }
}
function textareaValue(focusid) {
    var focusblurid = $(focusid);
    var defval = focusblurid.val();
    focusblurid.focus(function(){
        var thisval = $(this).val();
        if(thisval==defval){
            $(this).val("");
        }
    });
    focusblurid.blur(function(){
        var thisval = $(this).val();
        if(thisval==""){
            $(this).val(defval);
        }
    });
};

function NavShow(){
    var off = true;
    $('.icon-ul').on('click',function(){
        if(off){
            $('.nav_warp').slideDown('slow');
            $(this).addClass('colse');
            off = false;
        }else{
            $('.nav_warp').slideUp('slow');
            $(this).removeClass('colse');
            off = true;
        }
    });
}


/*   end  index  */