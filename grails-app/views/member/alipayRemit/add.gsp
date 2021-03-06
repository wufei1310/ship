<%@ page import="org.springframework.core.io.support.PropertiesLoaderUtils; org.springframework.core.io.ClassPathResource" %>
<%
    def properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("sysSetting.properties"))
%>
<r:require modules="bootstrapDatetimepicker"/>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>金士代发</title>
  </head>
 
  <body>
    <div class="container body">
       <g:if test="${flash.message}">
         <script>
         alert('${flash.message}')
        </script>
      </g:if>

      <div class="page-header">
        <h3>支付宝充值<font style="font-size: 0.4em">（手续费:1.2%）</font></h3>
      </div>
        
   <g:form  name="addAlipayRemitForm"  data-validate="parsley" url="[controller: 'memberAlipayRemit', action:'doAdd']"   method="post" class="form-horizontal" target="_blank">
          <input id="shouxufee" name="shouxufee" type="hidden">

  <fieldset>

    <div class="control-group">
          <label>充值金额:&nbsp;
              <input onchange="checkshouxufee(this)" onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')"   name="amount" class="input-medium" type="text" placeholder="" data-error-message="充值金额不能为空" data-required="true">
              &nbsp;
              手续费:<span id="shouxufeeshow" style="color: red">0</span>元(手续费最低为0.5元)
          </label>
        <span>友情提醒：网站暂不支持提现服务，您的账户余额可用于支付货款等相关费用。</span>

    </div>
      

    
    
    <button type="button" class="btn btn-large btn-primary" onclick="beforeCZ()">提交</button>
    <g:link  action="list"  params="${params}" class=" btn btn-large ">返回</g:link>
  </fieldset>
  
</g:form>
<div id="daiFaPayModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
          <h3 id="myModalLabel">充值</h3>
        </div>




        <div class="modal-footer">
          <g:link class="btn btn-primary"  action="list">充值完成</g:link>
        </div>
      </div>
<iframe id="innerFrame" name="innerFrame" height="0" frameborder="0"></iframe>
<script>


    function accMul(arg1,arg2)
    {
        var m=0,s1=arg1.toString(),s2=arg2.toString();
        try{m+=s1.split(".")[1].length}catch(e){}
        try{m+=s2.split(".")[1].length}catch(e){}
        return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m);
    }
    var shouxufee = '<%=properties.getProperty("AlipayConfig.shouxufee")%>'
    function checkshouxufee(o){

        var v =  accMul($(o).val(),shouxufee).toFixed(2)
        if(v<0.5){
            v=0.5
        }
        $("#shouxufee").val(v)
        $("#shouxufeeshow").html(v)
    }

    function beforeCZ(){
    if(!$("#addAlipayRemitForm").parsley( 'validate' )){
             return false
         }
      $('#daiFaPayModal').modal({keyboard: false})
      $("#addAlipayRemitForm").submit()
    }
</script>
<g:render template="/layouts/footer"/>
      </div> <!-- /container -->
  </body>
</html>
