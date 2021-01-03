function iframeAutoFit()
    {
        try
        {
            if(window != parent)
            {

                var docIframe = parent.document.getElementById("doc");
                if(docIframe.contentWindow == window)
                {
                   var h1 = 0, h2 = 0;

                   if(document.documentElement && document.documentElement.scrollHeight)
                   {
                     h1 = document.documentElement.scrollHeight;
                   }

                   if(document.body)
                   {
                     h2 = document.body.scrollHeight;
                   }
                   var h =Math.max(h1,h2);

                   if(document.all) { h += 4;}
                   if(window.opera) { h += 1;}

                   if( parseInt(h) > 1076 )
                   {
                      docIframe.style.height =  h + "px";
                   }
                }
            }
        }
        catch (ex){}
    }

    if(window.attachEvent)
    {
        window.attachEvent("onload",  iframeAutoFit);
    }
    else if(window.addEventListener)
    {
        window.addEventListener('load',  iframeAutoFit,  false);
    }

