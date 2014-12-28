<r:require modules="bootstrapDatetimepicker"/>
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

    <div class="container body" id="page">
      <div class="page-header">
        <h3>包裹管理
        </h3>
      </div>
      <div class="well well-large">

          <g:form class="form-inline" action="list" >

              状态：
              <g:select class="input-medium" value="${params.status}" name="status" optionKey="status" optionValue="queryShow" from="${[[status:"",queryShow:"全部"],[status:"new",queryShow:"新登记包裹"],[status:"noowner",queryShow:"无主包裹"],[status:"giveup",queryShow:"放弃不要了"]]}" />
              %{--是否关联：--}%
              %{--<g:select class="input-medium" value="${params.needTui}" name="needTui" optionKey="status" optionValue="queryShow" from="${[[status:"",queryShow:"全部"],[status:"0",queryShow:"未关联退货申请"],[status:"1",queryShow:"已关联退货申请，退货未结束"],[status:"2",queryShow:"退货结束，未关联退货申请"],[status:"3",queryShow:"退货结束，已关联退货申请，等待审核退款"],[status:"4",queryShow:"已退款给会员"]]}" />--}%


              扫描时间：
              <input class="datetime" value="${params.date}"  type="text" name="date">
              物流单号：
              <input class="input-medium" value="${params.wuliu_sn}"  type="text" name="wuliu_sn">
              <button type="submit" class="btn btn-primary">查询</button>
          </g:form>
      </div>
      
      <div class="bs-docs-example">

          <table class="table table-bordered">

              <thead>
              <tr>
                  <th>物流单号</th>
                  <th>包裹状态</th>
                  %{--<th>关联申请情况</th>--}%
                  %{--<th>退款金额</th>--}%
                  %{--<th>订单号</th>--}%
                  <th>生成时间</th>
                  <th>录入人</th>
              </tr>

                <g:each in="${list}" status="i" var="shipSN">

                    <tr>
                        <td>${shipSN.wuliu_sn}</td>
                        <td>     ${shipSN.status}
                            <shipSN:shipSNStatus status="${shipSN.status}" />
</td>
                        %{--<td>--}%
                        %{--<shipSN:needTui status="${shipSN.needTui}" />--}%
                    %{--</td>--}%
                        %{--<td>${shipSN.actual_return_fee}</td>--}%
                        %{--<td>${shipSN.orderSN}</td>--}%
                        <td>${shipSN.dateCreated.toString()[0..18]}</td>
                        <td>
                            ${shipSN.addUser}
                        </td>
                    </tr>

                </g:each>
              </thead>

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
        <script>
            $(document).ready(function(){
                $(".datetime").datetimepicker({
                    format: "yyyy-mm-dd",
                    autoclose: true,
                    todayBtn: true,
                    pickerPosition: "bottom-left",
                    minView:4,
                    language:'zh-CN'
                });
            })

        </script>
    </div> <!-- /container -->
  </body>
</html>

