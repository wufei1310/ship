<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="adminMain"/>
    <title>金士代发</title>
  </head>
  <body>
    <div class="container body">
      <g:if test="${flash.message}">
         <script>
         alert('${flash.message}')
        </script>
      </g:if>
      
     <div id="bukuanModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
        <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 id="myModalLabel">提示</h3>
      </div>
      <div class="modal-body">     
        <g:form class="bukuanForm form-horizontal" data-validate="parsley"  action="shipDiff" target="innerFrame" name="bukuanForm">
          <input type='hidden' name='id'  value="${order.id}">
          <input type='hidden' name='actual_shipfee'  value="">
          <div class="control-group">
            
            <div  id="bukuanHtml">
                
            </div>
          </div>
          
          <div class="control-group">
            <div class="controls">
              <button type="button" class="btn btn-primary btn-large"  onclick="$('#bukuanModal').modal('hide');$('#actual_shipfee').focus()">重新输入</button>
              <button type="button" class="btn btn-danger btn-large"  onclick="if(confirm('确认提交运费补款吗，此订单补款价格是'+($('#actual_shipfee').val()*1 - ${order.shipFee})+'元')){toActionFormCom('bukuanForm')}">停止发货，需要补运费</button>
            </div>
          </div>
          </g:form>
      </div>
        
    </div>
      
      

      
      <div id="wuliuModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
      <div class="modal-header">
      <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
      <h3 id="myModalLabel">填写物流单号</h3>
    </div>
      
      <div class="modal-body">     
        <g:form class="wuliuForm form-horizontal" data-validate="parsley"  action="updateForShipped" target="innerFrame">
         <input type='hidden' name='id' id='id' value="${order.id}">
         <input type='hidden' name='actual_shipfee'  value="">
          <div class="control-group">
            <label class="control-label">物流单号</label>
            <div class="controls">
              <input  data-required-message="物流单号不能为空" data-required="true" type="text" name="wuliu_no" >
            </div>
          </div>

          <div class="control-group">
            <div class="controls">
              <button type="button" class="btn btn-primary btn-large" onclick="toActionFormCom('wuliuForm')" >发货</button>
            </div>
          </div>
          </g:form>
      </div>
        
    </div>
      
      
      <div id="updateWuliuModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
      <div class="modal-header">
      <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
      <h3 id="myModalLabel">修改物流单号</h3>
    </div>
      
      <div class="modal-body">     
        <g:form class="updateWuliuForm form-horizontal" data-validate="parsley"  action="updateForShippedAgain" target="innerFrame">
         <input type='hidden' name='id' id='id' value="${order.id}">
          <div class="control-group">
            <label class="control-label">物流单号</label>
            <div class="controls">
              <input  data-required-message="物流单号不能为空" data-required="true" type="text" name="wuliu_no" value="${order.wuliu_no}">
            </div>
          </div>

          <div class="control-group">
            <div class="controls">
              <button type="button" class="btn btn-primary btn-large" onclick="toActionFormCom('updateWuliuForm')" >确定</button>
            </div>
          </div>
          </g:form>
      </div>
        
    </div>
      
      

      <div class="page-header">
        <h3>订单详细(<small>订单号：</small>${order.orderSN})</h3>
      </div>
      <g:form class="form-signin" data-validate="parsley"  target="innerFrame">
        <input type='hidden' name='id' id='id' value="${order.id}">
        <h4>1.商品信息</h4>
        <div class="bs-docs-example">
          <table class="table table-bordered">
            <thead>
              <tr>
                <th style="width: 200px;">市场位置</th>
                <th style="width: 200px;">档口号</th>
                <th >商品货号</th>
                <th>规格（颜色/尺码）</th>
                <th>件数</th>
                <th>单件价格</th>
                <th>实际拿货价格（单件）</th>
                <th>图片</th>
                <th>状态</th>
              </tr>
            </thead>
            <tbody>
              <g:each in="${order.daiFaGoods}" status="i" var="daiFaGoods">
                <tr class="daiFaGoods">
                  <td >
                    ${daiFaGoods.market} - ${daiFaGoods.floor}
                  </td>
                  <td>${daiFaGoods.stalls}</td>
                  <td>${daiFaGoods.goods_sn}</td>
                  <td>${daiFaGoods.spec}</td>
                  <td>${daiFaGoods.num}</td>
                  <td>${daiFaGoods.price}</td>
                  <td><input onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')" data-error-message="实际拿货价格不能为空" data-required="true"   name="actual_price" class="input-small" type="text" placeholder="" value="${daiFaGoods.actual_price?daiFaGoods.actual_price:daiFaGoods.price}">
                    <input type="hidden" name="goods_id" value="${daiFaGoods.id}">
                  </td>
                  <td>
                    <g:if test="${daiFaGoods.attach_id}">
                        <a href="${daiFaGoods.attach_id}" target="blank">
                        <img src="${daiFaGoods.attach_id}?imageView/2/w/100"  style="display: inline;max-width: 100px"/></a>
                    </g:if>
                  </td>
                  <td><goods:goodsStatusDic status="${daiFaGoods.status}"/></td>
                </tr>
              </g:each>
            </tbody>
          </table>
        </div>

       <h4>2.下单人信息</h4>
        <div class="bs-docs-example">
          <table class="table table-bordered">

            <tbody>

              <tr>
                <td >
                    下&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;单&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;人：
                  ${order.sendperson}
                  <br/>
                  发&nbsp;&nbsp;&nbsp;件&nbsp;&nbsp;地&nbsp;&nbsp;&nbsp;址：
                  ${order.sendaddress}
                  <br/>
                  联系手机/电话：
                  ${order.sendcontphone}
                  <br/>
                  备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注：
                  ${order.senddesc}
                  <br/>
                  <g:if test="${order.h_senddesc}">
                    历&nbsp;&nbsp;&nbsp;史&nbsp;&nbsp;备&nbsp;&nbsp;&nbsp;注：
                    <ul>
                      <g:each in="${order.h_senddesc.split("\\|")}" var="item">
                        <li>${item}</li>
                       </g:each>
                    </ul>
                    
                </g:if>
              </tr>

            </tbody>
          </table>
        </div>
        
        <h4>3.收货地址</h4>
        <div class="bs-docs-example">
          <table class="table table-bordered">

            <tbody>

              <tr>
                <td >
                  收&nbsp;&nbsp;件&nbsp;&nbsp;人：
                  ${order.reperson}
                  &nbsp;&nbsp;
                  联系手机/电话：
                  ${order.contphone}
                  <br/>
                  收件地区：
                  <g:areaName area_id="${order.area_id}" />
                  <br/>
                  收件地址：               
                  ${order.address}
                  <br/>
                  快递：
                  ${order.wuliu} 
                  <br/>
                </td>
              </tr>

            </tbody>
          </table>
        </div>
        
        <h4>4.礼品</h4>
        <div class="bs-docs-example">
          <table class="table table-bordered">

            <tbody>

              <tr>
                <td >

                  礼品：<order:orderRegards status="${order.regards}" />
                  
                </td>
              </tr>

            </tbody>
          </table>
        </div>

        <h4>5.支付款项</h4>
        <div class="bs-docs-example">
          <table class="table table-bordered">
 
            <tbody>

              <tr>
                <td >
                  商品总价：
                  <strong id="goodsFee">${order.goodsFee}</strong>
                  
                  <br/>
                  运&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;费：&nbsp;<strong id="shipFee">${order.shipFee}</strong>

                    &nbsp;包裹重量：${order.weight}
                  
                  <br/>
                  代&nbsp; 发&nbsp; 费：&nbsp;<strong id="serviceFee">${order.serviceFee}(代发每件1元)</strong>
                  
                  <br/>
                  礼&nbsp; 品&nbsp; 费：&nbsp;<strong id="regardsFee">${order.regardsFee}(好评卡+小礼物1元/件，包装盒+小礼物2元/件，好评卡+包装盒+小礼物3元/件)</strong>

                  <br/>
                  订单总价：&nbsp;<strong id="totalFee">${order.totalFee}</strong>
                  <br/>
                  
                </td>
              </tr>

            </tbody>
          </table>
        </div>
        
        <h4>5.状态</h4>
        <div class="bs-docs-example">
          <table class="table table-bordered">
 
            <tbody>

              <tr>
                <td >
                  订单状态：
                  <strong ><orderAdmin:orderStatusDic status="${order.status}"/></strong>
                  
                  <br/>
                  <g:if test="${order.status == 'processing'}">
                    受理时间：
                    <strong >${order.processing_time.toString()[0..18]}</strong>
                   <br/>
<!--                   受理人：
                    <strong >${order.processing_user_name}</strong>
                   <br/>-->
                  </g:if>
            
                   <g:if test="${order.status == 'waitpaydiff'}">
                   
                   受理人：
                    <strong >${order.processing_user_name}</strong>
                   <br/>
                  </g:if>
            
                  <g:if test="${order.status == 'shipped'}">
                    发货时间：
                  <strong >
                    <g:if test="${order?.ship_time}">
                    ${order?.ship_time?.toString()[0..18]}
                    </g:if>
                  </strong>
                   <br/>
<!--                   受理人：
                    <strong >${order.processing_user_name}</strong>
                   <br/>-->
                    发货单号：
                    <strong >${order.wuliu_no}</strong>
                   <br/>
                   实际发货运费：&nbsp;<strong >${order.actual_shipfee}</strong>
                  </g:if>
                  <g:if test="${order.status == 'diffship'}">
                      实际发货运费：&nbsp;<strong >${order.actual_shipfee}</strong>
                      <br/>
                      运&nbsp;&nbsp;费&nbsp;&nbsp;&nbsp;差&nbsp;&nbsp;额：&nbsp;<strong >${order.actual_shipfee - order.shipFee}</strong>
                  </g:if>
                  <br/>
                </td>
              </tr>

            </tbody>
          </table>
         
        </div>
        
<!--        <h4>6.补款列表</h4>
        <div class="bs-docs-example">
          <table class="table table-bordered">
            <thead>
              <tr>
                <th style="width: 100px;">补款金额</th>
                <th style="width: 700px;">原因</th>
                <th>时间</th>
                <th >状态</th>
              </tr>
            </thead>
            <tbody>
              <g:each in="${order.diffOrder}" status="i" var="diffOrder">
                <tr class="daiFaGoods">
                  <td >
                    ${diffOrder.diffFee}
                  </td>
                  <td>${diffOrder.reason}</td>
                  <td>${diffOrder.dateCreated.toString()[0..18]}</td>
                  <td>${diffOrder.status == "haspay" ?"已付款":"待付款"}</td>
                </tr>
              </g:each>
            </tbody>
          </table>
         
        </div>-->
        <g:if test="${order.status == 'allaccept' }">
        <div class="bs-docs-example">
          <table class="table table-bordered">
 
            <tbody>

              <tr>
                <td >
                  实际发货运费：<input onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')" data-error-message="实际发货运费不能为空" data-required="true" value="${order.actual_shipfee}" id="actual_shipfee"  name="actual_shipfee" class="input-small" type="text" placeholder="">
<!--                    <br/>
                    物&nbsp;&nbsp;流&nbsp;&nbsp;&nbsp;单&nbsp;&nbsp;号：<input  data-error-message="物流单号不能为空" data-required="true"   name="wuliu_no" value="${order.wuliu_no}"  type="text" placeholder="">-->
                </td>
              </tr>

            </tbody>
          </table>
         
        </div>
          </g:if>
      </g:form>
          <g:if test="${order.status == 'allaccept' }">
            
            <a href="javascript:void(0)" onclick="checkPrice()"  class=" btn btn-large btn-primary">发货</a>
            
             
             
          </g:if>

            <g:link controller="adminDaiFaOrder" action="list"  params="${params}" class=" btn btn-large">返回</g:link>
       <form   method="post" id="commonActionForm" target="innerFrame">
                  <input type='hidden' name='id' id='id' class='key'>
        </form>
        <iframe id="innerFrame" name="innerFrame" height="0" frameborder="0"></iframe>
         <g:form  action="show" params="${params}"  method="post" class="commonListForm">
        </g:form >

        <script>
      function checkPrice(){
        var actual_shipfee = $("#actual_shipfee").val()
        if($.trim(actual_shipfee) == ''){
            alert("实际发货运费不能为空")
            return false;
        }else{
            if(actual_shipfee*1>${order.shipFee}){
                $("#bukuanForm").find("input[name='actual_shipfee']").val(actual_shipfee) 
                $("#bukuanHtml").html("您输入运费金额"+actual_shipfee+"元，高于客户已支付运费${order.shipFee}元。请检查是否输入有误!")
                $('#bukuanModal').modal({
                   keyboard: false
                })
                
            }else{
                  $("#wuliuModal").find("input[name='actual_shipfee']").val(actual_shipfee) 
                  $('#wuliuModal').modal({
                    keyboard: false
                  })
            }
        }
      }
</script>
        
        
  <g:render template="/layouts/footer"/>
      
  </body>
</html>
