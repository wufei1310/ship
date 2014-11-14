<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="adminMain"/>
    <title>金士代发</title>
    <style>
      .control-group label{
        font-weight:bold
      }
    </style>
  </head>
  <body>
    <div class="container body">
      <g:if test="${flash.message}">
         <script>
         alert('${flash.message}')
        </script>
      </g:if>

      <div id="jiageModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
      <div class="modal-header">
      <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
      <h3 id="myModalLabel">填写实际价格</h3>
    </div>
      
      <div class="modal-body">     
        <g:form class="jiageForm form-horizontal" data-validate="parsley"  action="nahuo" target="innerFrame">
         <input type='hidden' name='id' id='id' value="${daiFaGoods.id}">
         <input type='hidden' name='nahuo_type' id='nahuo_type' >
          <div class="control-group">
            <label class="control-label">实际拿货价格(单价)</label>
            <div class="controls">
              <input onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')"  data-required-message="实际拿货价格不能为空" data-required="true" type="text" name="actual_price" id="actual_price">
            </div>
          </div>

          <div class="control-group">
            <div class="controls">
              <button type="button" class="btn btn-primary btn-large" onclick="nahuo()" >确定</button>
            </div>
          </div>
          </g:form>
      </div>
        
        
        
      
    </div>
      
       <div id="shuomingModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
          <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
          <h3 id="myModalLabel">填写缺货说明</h3>
      </div>
      <div class="modal-body">     
        <g:form class="shuomingForm form-horizontal" data-validate="parsley"  action="zsquehuo" target="innerFrame">
         <input type='hidden' name='id' id='id' value="${daiFaGoods.id}">
          <div class="control-group">
            <label class="control-label">缺货说明</label>
            <div class="controls">
              <textarea maxlength="50"  data-required-message="缺货说明不能为空" data-required="true" name="shuoming" style="width: 250px;height: 100px;" ></textarea>
            </div>
          </div>

          <div class="control-group">
            <div class="controls">
              <button type="button" class="btn btn-primary btn-large" onclick="toActionFormCom('shuomingForm')" >确定</button>
            </div>
          </div>
          </g:form>
      </div>
       </div>
      
      <div id="admin_shuomingModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
          <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
          <h3 id="myModalLabel">填写缺货说明</h3>
      </div>
      <div class="modal-body">     
        <g:form class="admin_shuomingForm form-horizontal" data-validate="parsley"  action="quehuo" target="innerFrame">
         <input type='hidden' name='id' id='id' value="${daiFaGoods.id}">
          <div class="control-group">
            <label class="control-label">缺货说明</label>
            <div class="controls">
              <textarea maxlength="50"  data-required-message="缺货说明不能为空" data-required="true" name="shuoming" style="width: 250px;height: 100px;" >${daiFaGoods.shuoming}</textarea>
            </div>
          </div>

          <div class="control-group">
            <div class="controls">
              <button type="button" class="btn btn-primary btn-large" onclick="toActionFormCom('admin_shuomingForm')" >确定</button>
            </div>
          </div>
          </g:form>
      </div>
       </div>
      
      <script>
          function nahuo(){
            var nahuo_type = $("#nahuo_type").val()
            var actual_price = $.trim($("#actual_price").val())
            if(actual_price==''){
                alert("实际拿货价格不能为空")
                return false;
            }
            if(nahuo_type == '0'){
               if(actual_price*1>${daiFaGoods.price}){
                   alert("实际拿货价格不能大于${daiFaGoods.price}")
                   return false
               }else{
                    $(".jiageForm").attr("action","<%=request.getContextPath()%>/adminDaiFaGoods/nahuo")
                    toActionFormCom('jiageForm')
                }
            }
            if(nahuo_type == '1'){
               if(actual_price*1<=${daiFaGoods.price}){
                   alert("实际拿货价格不能小于${daiFaGoods.price}")
                   return false
               }else{
                    $(".jiageForm").attr("action","<%=request.getContextPath()%>/adminDaiFaGoods/noNahuo")
                    toActionFormCom('jiageForm')
                }
            }
          }
          
       
      </script>
<!--      <div id="bukuanModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
      <div class="modal-header">
      <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
      <h3 id="myModalLabel">填写单件补款差额</h3>
    </div>
      
      <div class="modal-body">     
        <g:form class="bukuanForm form-horizontal" data-validate="parsley"  action="bukuan" target="innerFrame">
         <input type='hidden' name='id' id='id' value="${daiFaGoods.id}">
          <div class="control-group">
            <label class="control-label">单件补款差额</label>
            <div class="controls">
              <input onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')"  data-required-message="单件补款差额不能为空" data-required="true" type="text" name="diffFee" id="diffFee">
            </div>
          </div>

          <div class="control-group">
            <div class="controls">
              <button type="button" class="btn btn-primary btn-large" onclick="toActionFormCom('bukuanForm')" >确定</button>
            </div>
          </div>
          </g:form>
      </div>
      
    </div>-->
      
      <div class="page-header">
        <h3>代发商品详细</h3>
      </div>
        
        <form   class="form-inline">


  <fieldset>
    <div class="row-fluid">
      <div class="control-group span6">
          <label>订单号：</label>
          ${daiFaGoods.daiFaOrder.orderSN}
        </div>
      </div>
    <div class="row-fluid">
      <div class="control-group span6">
          <label>市场：</label>
          ${daiFaGoods.market}
        </div>
    
    
     <div class="control-group span6">
          <label>货号：</label>
          ${daiFaGoods.goods_sn}
      </div>
       </div> 
    <div class="row-fluid">
    <div class="control-group span6">
          <label>档口：</label>
           ${daiFaGoods.floor}-${daiFaGoods.stalls}
        </div>
   
    
    <div class="control-group span6">
          <label>规格：</label>
           ${daiFaGoods.spec}
        </div>
       </div>
    <div class="row-fluid">
    <div class="control-group span6">
          <label>件数：</label>
          ${daiFaGoods.num}
        </div>
   
    <div class="control-group span6">
          <label>单价：</label>
           ${daiFaGoods.price}<g:if test="${daiFaGoods.actual_price}">（<label>实际拿货价格</label>：${daiFaGoods.actual_price}）</g:if>
        </div>
    </div>
    <div class="row-fluid">
    <div class="control-group span6">
          <label>状态：</label>
           <goods:goodsStatusDic status="${daiFaGoods.status}"/>
        </div>
      <g:if test="${daiFaGoods.nahuotime}">
        <div class="control-group span6">
          <label>拿货时间：</label>
            ${daiFaGoods.nahuotime?daiFaGoods.nahuotime.toString()[0..18]:''}
        </div>
      </g:if>
    </div>
    <g:if test="${daiFaGoods.attach_id}">
    <div class="control-group">
      <label>图片：</label>
      
                        <a href="${daiFaGoods.attach_id}" target="blank">
                        <img src="${daiFaGoods.attach_id}?imageView/2/w/100"  style="display: inline;max-width: 100px"/></a>
       
    </div>
      </g:if>
    <g:if test="${daiFaGoods.status == '10'}">
        <div class="row-fluid">
          <div class="control-group span6">
                <label>单件退款金额：</label>
                ${daiFaGoods.tuihuo_price}
              </div>

          <div class="control-group span6">
                <label>退款件数：</label>
                 ${daiFaGoods.tuihuo_num}
          </div>
      </div>
    </g:if>
    <g:if test="${daiFaGoods.daifa_user && session.loginPOJO.user.role == 'admin'}">
        <div class="row-fluid">

        <div class="control-group span6">
              <label>受理人：</label>
               ${daiFaGoods.daifa_user?daiFaGoods.daifa_user.email:''}
            </div>
        <div class="control-group span6">
              <label>受理时间：</label>
               ${daiFaGoods.processtime?daiFaGoods.processtime.toString()[0..18]:''}
            </div>

        </div>
     </g:if>
    <g:if test="${daiFaGoods.check_user && session.loginPOJO.user.role == 'admin'}">
    <div class="row-fluid">
    <div class="control-group span6">
          <label>验收人：</label>
           ${daiFaGoods.check_user?daiFaGoods.check_user.email:''}
        </div>
    <div class="control-group span6">
          <label>验收时间：</label>
           ${daiFaGoods.checktime?daiFaGoods.checktime.toString()[0..18]:''}
        </div>
    </div>
    </g:if>
    
    <g:if test="${daiFaGoods.shuoming && session.loginPOJO.user.role == 'admin'}">
      <div class="row-fluid">
      <div class="control-group span6">
            <label>缺货说明：</label>
             ${daiFaGoods.shuoming}
          </div>
      </div>
    </g:if>
    
    
<g:if test="${diffGoodsList}">
  <h4>补款列表</h4>
   <div class="bs-docs-example">
        
        <table class="table table-bordered">
          <thead>
            <tr>
              <th>单件差额</th>
              <g:if test="${session.loginPOJO.user.role == 'admin'}">
                <th>补款提交人</th>
                <th>补款提交时间</th>
              </g:if>
              <th>状态</th>
              <th>支付时间</th>
            </tr>
          </thead>
          <tbody>
            
          <g:each in="${diffGoodsList}" status="i" var="diffGoods">
            <tr>
              <td>${diffGoods.diffFee}</td>
              <g:if test="${session.loginPOJO.user.role == 'admin'}">
                <td>${diffGoods.addUser.email}</td>
                <td>${diffGoods.addTime.toString()[0..18]}</td>
              </g:if>
              <td><g:if test="${diffGoods.status=='0'}">未付款</g:if>
                  <g:if test="${diffGoods.status=='1'}">已付款</g:if>
                  <g:if test="${diffGoods.status=='2'}">已取消</g:if>
              </td>
              
             <td>${diffGoods.payTime?diffGoods.payTime.toString()[0..18]:''}</td>
              
            </tr>
          </g:each>
            
            
            
          </tbody>
        </table>
      </div>
</g:if>
<g:if test="${goodsLogList && session.loginPOJO.user.role == 'admin'}">
  <h4>操作记录</h4>
   <div class="bs-docs-example">
        
        <table class="table table-bordered">
          <thead>
            <tr>
              <th>操作人</th>             
              <th>操作描述</th>
              <th>操作时间</th>
            </tr>
          </thead>
          <tbody>
            
          <g:each in="${goodsLogList}" status="i" var="goodsLog">
            <tr>
              <td>${goodsLog.addUser.email}</td>
              
              <td>${goodsLog.logdesc}
              </td>
              
             <td>${goodsLog.lastUpdated?goodsLog.lastUpdated.toString()[0..18]:''}</td>
              
            </tr>
          </g:each>
            
            
            
          </tbody>
        </table>
      </div>
</g:if>
    
    <g:if test="${daiFaGoods.status == '0'}">
         <a class="btn btn-large btn-primary" href="javascript:void(0)" onclick="toActionCom({goods_id:'${daiFaGoods.id}'},'<%=request.getContextPath()%>/adminDaiFaGoods/process','确认受理此商品吗？')">我去取货</a>
    </g:if>
    <g:if test="${daiFaGoods.status == '1' && daiFaGoods.daifa_user.id == session.loginPOJO.user.id}">
        <a href="#jiageModal" data-toggle="modal" onclick="$('#nahuo_type').val('0')" class="btn btn-large btn-primary">已拿货</a>
        <a href="#jiageModal" data-toggle="modal" onclick="$('#nahuo_type').val('1')" class="btn btn-large btn-danger">价格过高，暂不拿货</a>
        <a href="#shuomingModal" data-toggle="modal"  class="btn btn-large btn-danger">暂时缺货</a>
    </g:if>
    <g:if test="${daiFaGoods.status == '3' &&  session.loginPOJO.user.role == 'admin'}">
        <a href="javascript:void(0)" onclick="toActionCom({goods_id:'${daiFaGoods.id}'},'<%=request.getContextPath()%>/adminDaiFaGoods/bukuan','确认补款吗，此商品单件补款价格是${daiFaGoods.actual_price - daiFaGoods.price}元')"  class="btn btn-large btn-primary">补款</a>（经确认,该商品真实需要补款才能拿货,请在此处操作确认补款,并输入金额）
        <br/>
        <br/>
        <a href="javascript:void(0)" onclick="toActionCom({goods_id:'${daiFaGoods.id}'},'<%=request.getContextPath()%>/adminDaiFaGoods/noBukuan','确认取消补款吗，此商品将会等待重新受理？')"  class="btn btn-large btn-danger">取消补款</a>
        
    </g:if>
    <g:if test="${daiFaGoods.status == '8' &&  session.loginPOJO.user.role == 'admin'}">
        <a href="#admin_shuomingModal" data-toggle="modal"   class="btn btn-large btn-primary">确认缺货</a>
        <a href="javascript:void(0)" onclick="toActionCom({goods_id:'${daiFaGoods.id}'},'<%=request.getContextPath()%>/adminDaiFaGoods/noQuehuo','确认取消缺货吗，此商品将会等待重新受理？')"  class="btn btn-large btn-danger">取消缺货</a>
    </g:if>
    <g:if test="${daiFaGoods.status == '2' &&  session.loginPOJO.user.role == 'admin'}">
        <a class="btn btn-large btn-primary" href="javascript:void(0)" onclick="toActionCom({goods_id:'${daiFaGoods.id}'},'<%=request.getContextPath()%>/adminDaiFaGoods/yanshou','确认验收此商品吗？')">验收</a>
    </g:if>
    <g:link controller="adminDaiFaGoods" action="myList"  params="${params}" class=" btn btn-large">返回</g:link>
  </fieldset>

</form>
      <form     method="post" id="commonActionForm" target="innerFrame">
           <input type="hidden" name="id" id="goods_id" class="key">
        </form>
<iframe id="innerFrame" name="innerFrame" height="0" frameborder="0"></iframe>
         <g:form  action="showMy" params="${params}"  method="post" class="commonListForm">
        </g:form >
<g:render template="/layouts/footer"/>
      </div> <!-- /container -->
  </body>
</html>
