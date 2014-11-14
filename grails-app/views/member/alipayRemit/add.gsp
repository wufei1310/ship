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
        <h3>支付宝充值</h3>
      </div>
        
   <g:form  name="addAlipayRemitForm"  data-validate="parsley" url="[controller: 'memberAlipayRemit', action:'doAdd']"   method="post" class="form-horizontal" target="_blank">
     

  <fieldset>

    <div class="control-group">
          <label>充值金额</label>
          <input onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')"   name="amount" class="input-small" type="text" placeholder="" data-error-message="充值金额不能为空" data-required="true">
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
