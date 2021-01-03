
//将表单封装为json对象
(function($){  
       $.fn.serializeJson=function(){  
            var serializeObj={};  
            var array=this.serializeArray();  
            var str=this.serialize();  
            $(array).each(function(){  
                if(serializeObj[this.name]){  
                    if($.isArray(serializeObj[$.trim(this.name)])){  
                       serializeObj[$.trim(this.name)].push($.trim(this.value));  
                    }else{  
                        serializeObj[$.trim(this.name)]=[serializeObj[$.trim(this.name)],$.trim($.trim(this.value))];  
                    }  
                }else{  
                    serializeObj[$.trim(this.name)]=$.trim(this.value);   
                }  
            });  
           return serializeObj;  
        };  
    })(jQuery);  
