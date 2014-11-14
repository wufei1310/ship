
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="adminMain"/>
    <title>金士代发</title>
  </head>
  <body>
    <script>
        function fee(){
            var goodsFee = 0.00;
            var num = 0
            $(".daiFaGoods").find("input[name='return_fee']").each(function(){
                 goodsFee = goodsFee + $(this).val()*1*$(this).attr("return_num")
            })
            $("#goodsFee").html(goodsFee)
        }
        
        function beforeTuikuan(){
            if(window.confirm("确认退货吗？")){
                toActionFormCom('checkSaleReturnForm')  
            }
        }
    </script>

    
    
    <div class="container body">
      <g:if test="${flash.message}">
         <script>
         alert('${flash.message}')
        </script>
      </g:if>

      <div class="page-header">
        <h3>退货审核(<small>订单号：</small>${returnOrder.orderSN})</h3>
      </div>
      <g:form name="checkSaleReturnForm"  data-validate="parsley" class="form-signin checkSaleReturnForm" action="checkSaleReturn" method="post" target="innerFrame">
        <input type="hidden" name="id" value="${returnOrder.id}">
        <h4>商品信息</h4>
        <div class="bs-docs-example">
          <table class="table table-bordered">
            <thead>
              <tr>
                <th style="width: 200px;">市场位置</th>
                <th style="width: 200px;">档口号</th>
                <th >商品货号</th>
                <th>规格（颜色/尺码）</th>
                <th>拿货件数</th>
                <th>单件退货价格</th>
                <th>单件实际退货价格</th>
                <th>退货件数</th>
                  <th>状态</th>
              </tr>
            </thead>
            <tbody>
              <g:each in="${returnOrder.returnGoods}" status="i" var="returnGoods">
                
                  <tr class="daiFaGoods">
                    
                    <td >
                      ${returnGoods.market} - ${returnGoods.floor}
                    </td>
                    <td>${returnGoods.stalls}</td>
                    <td>${returnGoods.goods_sn}</td>
                    <td>${returnGoods.spec}</td>
                    <td>${returnGoods.num}</td>
                    <td>${returnGoods.actual_price}</td>
                    <td><input  data-error-message="单件退货价格不能为空" data-required="true" return_num="${returnGoods.return_num}"   name="return_fee" class="input-small" type="text" placeholder="" value="${returnGoods.return_fee}"  onblur="if(isNaN(value) || value<=0)execCommand('undo');fee();"></td>
                        <input type="hidden" name="returnGoods_id" value="${returnGoods.id}"/>
                    <td>
                        ${returnGoods.return_num}
                    </td>
                      <td>

                          <g:returnGoods status="${returnGoods.status}" />
                          -
                          ${returnGoods.reason}
                      </td>
                  </tr>

              </g:each>
            </tbody>
          </table>
          
         
        </div>
        
        <h4>下单人信息</h4>
        <div class="bs-docs-example">
          <table class="table table-bordered">

            <tbody>

              <tr>
                <td >
                  下&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;单&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;人：
                  ${returnOrder.sendperson}
                  <br/>
                  发&nbsp;&nbsp;&nbsp;件&nbsp;&nbsp;地&nbsp;&nbsp;&nbsp;址：
                  ${returnOrder.sendaddress}
                  <br/>
                  联系手机/电话：
                  ${returnOrder.sendcontphone}
                  <br/>
              </tr>

            </tbody>
          </table>
        </div>
        
        <h4>退回商品物流信息</h4>
        <div class="bs-docs-example">
          <table class="table table-bordered">

            <tbody>

              <tr>
                <td >

                  物流单号：${returnOrder.wuliu_sn}
                  <br>
                  物流公司：${returnOrder.wuliu}
                    <br>
                    快递单图片：
                    <g:if test="${returnOrder.wuliupic}">
                        <a href="${returnOrder.wuliupic}" target="blank">
                            <img src="${returnOrder.wuliupic}"  style="display: inline;max-width: 100px"/>
                        </a>
                    </g:if>
                </td>
              </tr>

            </tbody>
          </table>
        </div>
        
        <h4>状态</h4>
        <div class="bs-docs-example">
          <table class="table table-bordered">
 
            <tbody>

              <tr>
                <td >
                  申请状态：
                  <strong ><g:returnOrder status="${returnOrder.status}" /></strong>
                  
                  <br/>
                  <g:if test="${returnOrder.tui_time}">
                    操作人：<strong >${returnOrder.tui_user.email}</strong>
                    <br/>
                    处理时间：
                    <strong >${returnOrder.tui_time.toString()[0..18]}</strong>
                   <br/>                  
                  </g:if>
            
                  <g:if test="${returnOrder.check_user}">
                    审核人：<strong >${returnOrder.check_user.email}</strong>
                    <br/>
                    审核时间：
                    <strong >${returnOrder.check_time.toString()[0..18]}</strong>
                   <br/>                  
                  </g:if>
                </td>
              </tr>

            </tbody>
          </table>
            
        </div>
        
        <h4>款项</h4>
        <div class="bs-docs-example">
          <table class="table table-bordered">

            <tbody>

              <tr>
                <td >
                  退货退款：<strong id="goodsFee">${returnOrder.goodsFee}</strong><br/>
                  手续费：<strong id="serviceFee">${returnOrder.totalFee}</strong>(手续费每件2元)

                </td>
              </tr>

            </tbody>
          </table>
        </div>
        
      


      
      
 <g:if test="${returnOrder.status == '4'}"><a  href="javascript:void(0)" onclick="beforeTuikuan()"  class=" btn btn-large btn-primary">档口已退货退款，确认退货到会员账户</a></g:if>
 <g:link controller="adminDaiFaOrder" action="saleReturnList"  params="${params}" class=" btn btn-large ">返回</g:link>
      
    </g:form>
      
      <form     method="post" id="commonActionForm" target="innerFrame">
                  <input type='hidden' name='id' id='id' class='key'>
                  <input type='hidden' name='safepass' id='safepass' class='key'>
        </form>
        <iframe id="innerFrame" name="innerFrame" height="0" frameborder="0"></iframe>
         <g:form  action="saleReturnShow" params="${params}"   method="post" class="commonListForm">
        </g:form >
      <g:render template="/layouts/footer"/>
  </body>
</html>
