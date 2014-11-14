var messageError = "error";
String.prototype.endWith=function(str){     
  var reg=new RegExp(str+"$");     
  return reg.test(this);        
}

if (typeof jQuery !== 'undefined') {
	(function($) {
		$('#spinner').ajaxStart(function() {
			$(this).fadeIn();
		}).ajaxStop(function() {
			$(this).fadeOut();
		});
	})(jQuery);
}

function toActionFormCom(formId,tips){
       var flat = true;
       if(tips){
         flat = window.confirm(tips)
       }
       if(flat){
         var form = $('.'+formId)
         
         try{
            if(!$(form).parsley( 'validate' )){
             return false
         }
        }catch(e){
            
        }
         layer_load();
         
         form.submit()
         setTimeout("checkSave('commonListForm')",500);
       }
    }
 
 function toActionFormUrlCom(formId,url,tips){
       var flat = true;
       if(tips){
         flat = window.confirm(tips)
       }
       if(flat){
         var form = $('.'+formId)
         
         try{
            if(!$(form).parsley( 'validate' )){
             return false
         }
        }catch(e){
            
        }
         layer_load();
         
         form.attr("action",url)
         form.submit()
         setTimeout("checkSave('commonListForm')",500);
       }
    }

function toActionCom(arr,url,tips){
       var flat = true;
       if(tips){
         flat = window.confirm(tips)
       }
       if(flat){
        var form = $('#commonActionForm')
        
        try{
            if(!$(form).parsley( 'validate' )){
             return false
         }
        }catch(e){
            
        }
         layer_load();
        
         $.each(arr, function(key, val) {
		form.find("#"+key).val(val);
	 });   
         form.attr("action",url)
         form.submit()
         setTimeout("checkSave('commonListForm')",500);
       }
    }
function checkSave(param){
	var innerFrame = document.getElementById("innerFrame");
	if(innerFrame.contentWindow.isOk){
		if(innerFrame.contentWindow.isOk()){
			var messageValue=innerFrame.contentWindow.getMessage();
			var messageClassValue=innerFrame.contentWindow.getMessageClass();
			//如果验证有错 则页面不做任何跳转
			if(messageClassValue==messageError){
				innerFrame.contentWindow.setPass();
				showMessage(messageValue,messageClassValue);
			}else{
				innerFrame.contentWindow.setPass();
				var form = $("."+param);
                               // form.find("#message").val(messageValue);
				//form.find("#messageClass").val(messageClass);
				form.submit();
			}
		}else{
			setTimeout("checkSave('"+param+"')",500);
		}
	}else{
		setTimeout("checkSave('"+param+"')",500);
	}
	
}

function showMessage(messageValue,messageClassValue){
    try{
		layer_close();
	}catch(e){
		
	}
    alert(messageValue)
    if(pageProcess){//如果页面有操作需要操作 用这个函数
        setTimeout("pageProcess()",1);
    }
}
