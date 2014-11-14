<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="adminMain"/>
    <title>金士代发</title>
  </head>
  <body>
    <g:if test="${flash.message}">
         <script>
         alert('${flash.message}')
        </script>
      </g:if>
<script>
      function toTuihuo(id,num,actual_price){
         $('#tuihuoModal').find('#id').val(id)
         $('#tuihuoModal').find('#num').val(num)
         $('#tuihuoModal').find('#actual_price').val(actual_price)
      }
      
      function tuihuo(){
         if($(".tuihuoForm").parsley( 'validate' )){
            if($("#tuihuo_price").val()*1>$("#actual_price").val()*1){
                alert("退款金额不能大于拿货金额"+$("#actual_price").val()+"元")
                return false
            }
            if($("#tuihuo_num").val()*1>$("#num").val()*1){
                alert("退货数量不能大于拿货数量"+$("#num").val()+"件")
                return false
            }
            toActionFormCom('tuihuoForm')
         }
      }
  </script>
  <div id="tuihuoModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
      <div class="modal-header">
      <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
      <h3 id="myModalLabel">填写退货退款申请</h3>
    </div>
      
      <div class="modal-body">     
        <g:form class="tuihuoForm form-horizontal" data-validate="parsley" controller="adminReturnGoodsApp"  action="doAdd" target="innerFrame">
          <div class="control-group">
            <label class="control-label">单件退款金额</label>
            <div class="controls">
              <input onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')"  data-required-message="单件退款金额不能为空" data-required="true" type="text" id="tuihuo_price" name="tuihuo_price">
            </div>
          </div>
          
          <div class="control-group">
            <label class="control-label">退货数量</label>
            <div class="controls">
              <input onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')"  data-required-message="退货数量不能为空" data-required="true" type="text" id="tuihuo_num"  name="tuihuo_num">
            </div>
          </div>

          <div class="control-group">
            <div class="controls">
              <input type="hidden" id="id" name="id">
              <input type="hidden" id="num" name="num">
              <input type="hidden" id="actual_price" name="actual_price">
              <button type="button" class="btn btn-primary btn-large" onclick="tuihuo()" >确定</button>
            </div>
          </div>
          </g:form>
      </div>
        
    </div>
  
    <div class="container body" id="page">
      <div class="page-header">
        <h3>代发商品列表
        </h3>
      </div>
      <div class="well well-large">
        <g:form class="form-inline" action="list" >
          订单号：
          <input name="orderSN" type="text" class="" placeholder="" value="${params.orderSN}">
          市场：
          <g:select  class="input-medium" value="${params.market}" noSelection="['':'全部']"  name="market"  from="${marketList}" />
<!--          <select name="market" class="input-medium">
                    <option value="">全部</option>
                    <option value="大时代" <g:if test="${params.market== '大时代'}">selected</g:if>>大时代</option>
                    <option value="宝华" <g:if test="${params.market== '宝华'}">selected</g:if>>宝华</option>
                    <option value="女人街" <g:if test="${params.market== '女人街'}">selected</g:if>>女人街</option>
                    <option value="大西豪" <g:if test="${params.market== '大西豪'}">selected</g:if>>大西豪</option>
                    <option value="西街" <g:if test="${params.market== '西街'}">selected</g:if>>西街</option>
                    <option value="富丽" <g:if test="${params.market== '富丽'}">selected</g:if>>富丽</option>
                    <option value="金马" <g:if test="${params.market== '金马'}">selected</g:if>>金马</option>
                    <option value="机筑巷" <g:if test="${params.market== '机筑巷'}">selected</g:if>>机筑巷</option>
                    <option value="国大" <g:if test="${params.market== '国大'}">selected</g:if>>国大</option>
                    <option value="广州站" <g:if test="${params.market== '广州站'}">selected</g:if>>广州站</option>
                    <option value="新潮都" <g:if test="${params.market== '新潮都'}">selected</g:if>>新潮都</option>
          
                    <option value="柏美" <g:if test="${params.market== '柏美'}">selected</g:if>>柏美</option>
                    <option value="非凡" <g:if test="${params.market== '非凡'}">selected</g:if>>非凡</option>
                    <option value="佰润" <g:if test="${params.market== '佰润'}">selected</g:if>>佰润</option>
                    <option value="益民" <g:if test="${params.market== '益民'}">selected</g:if>>益民</option>
                  </select>-->
          货号：
          <input name="goods_sn" type="text" class="" placeholder="" value="${params.goods_sn}">
          <g:if test="${session.loginPOJO.user.role == 'admin'}">
          <br/><br/>
          
            受理人：
            <input name="email" type="text" class="" placeholder="" value="${params.email}">
          </g:if>
          状态：
           <g:select class="input-medium" value="${params.status}" name="status" optionKey="status" optionValue="queryShow" from="${[[status:"",queryShow:"全部"],[status:"0",queryShow:"未受理"],[status:"1",queryShow:"已受理"],[status:"2",queryShow:"已拿货"],[status:"3",queryShow:"价格过高，暂不拿货"],[status:"4",queryShow:"等待补款"],[status:"5",queryShow:"缺货"],[status:"6",queryShow:"已取消"],[status:"7",queryShow:"已验收"],[status:"8",queryShow:"暂时缺货"],[status:"9",queryShow:"已发货"],[status:"10",queryShow:"已退货退款"],[status:"killing",queryShow:"紧急中止中"],[status:"kill_wait",queryShow:"紧急中止,商品已经受理，待确认是否拿货"],[status:"kill_return",queryShow:"紧急中止,商品已经拿货"],[status:"killed",queryShow:"已中止发货"]]}" />

          <button type="submit" class="btn btn-primary">查询</button>
        </g:form>
      </div>
      
      <div class="bs-docs-example">
        
        <table class="table table-bordered">
          <thead>
            <tr>
              <th>订单号</th>
              <th>市场</th>
              <th>货号</th>
              <th style="width: 100px">档口</th>
              <th style="width: 100px">规格</th>
              <th>件数</th>
              <th>单价</th>
             <g:if test="${session.loginPOJO.user.role == 'admin'}">
               <th>受理人</th>
             </g:if>
              <th>状态</th>
              <th>支付时间</th>
             
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            
          <g:each in="${list}" status="i" var="daiFaGoods">
            <tr>
              <td>${daiFaGoods.daiFaOrder.orderSN}</td>
              <td>${daiFaGoods.market}</td>
              <td>${daiFaGoods.goods_sn}</td>
              <td>${daiFaGoods.floor}-${daiFaGoods.stalls}</td>
              <td>${daiFaGoods.spec}</td>
              <td>${daiFaGoods.num}</td>
              <td>${daiFaGoods.price}</td>
              <g:if test="${session.loginPOJO.user.role == 'admin'}">
                <td title="受理时间：${daiFaGoods.processtime?daiFaGoods.processtime.toString()[0..18]:''}"><a href="javascript:void(0)">${daiFaGoods.daifa_user?daiFaGoods.daifa_user.email:''}</a></td>
              </g:if>
              <td><goods:goodsStatusDic status="${daiFaGoods.status}"/>
              </td>
              <td>${daiFaGoods.daiFaOrder.payTime?daiFaGoods.daiFaOrder.payTime.toString()[0..18]:''}</td>
              <td>
                <g:link controller="adminDaiFaGoods" action="toOrderGoods"  params="${params}" id="${daiFaGoods.id}" class="btn">查看</g:link>
               <g:if test="${daiFaGoods.status == '0'}">
                 <a class="btn btn-primary" href="javascript:void(0)" onclick="toActionCom({id:'${daiFaGoods.id}'},'<%=request.getContextPath()%>/adminDaiFaGoods/process','确认受理此商品吗？')">我去取货</a>
               </g:if>
              <g:if test="${daiFaGoods.status == '2' && session.loginPOJO.user.role == 'admin'}">
                 <a class="btn btn-primary" href="javascript:void(0)" onclick="toActionCom({id:'${daiFaGoods.id}'},'<%=request.getContextPath()%>/adminDaiFaGoods/yanshou','确认验收此商品吗？')">验收</a>
               </g:if>
               
              </td>
            </tr>
          </g:each>
            
            
            
          </tbody>
        </table>
      </div>
<div class="row-fluid">
      <div class="span12">
      <div  class="pagination pagination-right new-pagination" >
          


            <g:if test="${total != 0}">
                <g:paginate total="${total}"  params="${params}" action="list"  update="page" />
           </g:if>
            
            <span class="currentStep">共${total}条数据</span>
          </div>
      </div>
    </div>
         <form     method="post" id="commonActionForm" target="innerFrame">
           <input type="hidden" name="id" id="id" class="key">
        </form>
        <iframe id="innerFrame" name="innerFrame" height="0" frameborder="0"></iframe>
         <g:form  action="${actionName}" params="${params}"  method="post" class="commonListForm">
        </g:form >       
      <g:render template="/layouts/footer"/>
    </div> <!-- /container -->
  </body>
</html>

