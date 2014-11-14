<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="adminMain"/>
    <title>金士代发</title>
  </head>
  <body>
    <div class="container body" id="page">

      <g:if test="${flash.message}">
         <script>
         alert('${flash.message}')
        </script>
      </g:if>
      
    <div id="reasonModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
      <div class="modal-header">
      <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
      <h3 id="myModalLabel">填写理由</h3>
    </div>
      
      <div class="modal-body">     
        <form class="form-horizontal">
          <div class="control-group">
            <label class="control-label">审核不通过理由</label>
            <div class="controls">
              <textarea cols="15" rows="5"  data-required-message="审核不通过理由不能为空" data-required="true"  id="reason_text" ></textarea>
            </div>
          </div>
          <input type="hidden" id="remit_id">
          <div class="control-group">
            <div class="controls">
              <button type="button" class="btn btn-primary btn-large" onclick="if($( '#reason_text' ).parsley( 'validate' )) toActionCom({id:$('#remit_id').val(),status:'2',reason:$('#reason_text').val()},'<%=request.getContextPath()%>/adminRemit/doCheck')" >确定</button>
            </div>
          </div>
          </form>
      </div>
        
    </div>

      <div class="page-header">
        <h3>
          汇款充值审核列表
         
      </h3>
        
        </div>
      <div class="well well-large">
        <form class="form-inline">
             状态：<select name="status">
               <option value=""  >全部</option>
                <option value="0" <g:if test="${params.status == '0'}">selected</g:if>>未审核</option>
                <option value="1" <g:if test="${params.status == '1'}">selected</g:if>>审核通过</option>
                <option value="2" <g:if test="${params.status == '2'}">selected</g:if>>审核不通过</option>
              </select>
          <button type="submit" class="btn btn-primary">查询</button>
        </form>
      </div>


      <div class="bs-docs-example">
        <table class="table table-bordered">
          <thead>
            <tr>
                <th>会员ID</th>
                <th>email</th>
              <th>汇款人姓名</th>
              <th>汇出款银行账号</th>
              <th>汇出款银行名称</th>
              <th>汇入款单号</th>
              <th>汇入款金额</th>
              <th>汇入款时间</th>
              <th>状态</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            
          <g:each in="${list}" status="i" var="remit">
            <tr>
                <td>${remit.submit_user.id}</td>
                <td>${remit.submit_user.email}</td>
              <td>${remit.user_name}</td>
              <td>${remit.bank_account}</td>
              <td>${remit.bank_name}</td>
              <td>${remit.bank_order}</td>
              <td>${remit.bank_amount}</td>
              <td>${remit.remit_date.toString()[0..10]}</td>
              <td><g:if test="${remit.status == '0'}">
                      处理中
                  </g:if>
                  <g:if test="${remit.status == '1'}">
                      审核通过
                  </g:if>
                  <g:if test="${remit.status == '2'}">
                      审核不通过
                  </g:if>
              </td>
              <td>
              <g:if test="${remit.status == '0'}">
                  <a class="btn" href="javascript:void(0)"  onclick=" toActionCom({id:'${remit.id}',status:'1',reason:''},'<%=request.getContextPath()%>/adminRemit/doCheck','确定审核通过吗？')">审核通过</a>
                  <a class="btn" href="#reasonModal" data-toggle="modal"  onclick="$('#remit_id').val('${remit.id}')">审核不通过</a></td>
              </g:if>
            </tr>
          </g:each>
            
            
            
          </tbody>
        </table>
      </div>
      <div class="row-fluid">
        <div class="span12">
          <div  class="pagination pagination-right new-pagination">
          


            <g:if test="${total != 0}">
                <g:paginate  params="${params}" action="list" total="${total}" />
           </g:if>
            
            <span class="currentStep">共${total}条数据</span>
          </div>
        
        </div>
      </div>
      <form     method="post" id="commonActionForm" target="innerFrame">
                  <input type='hidden' name='id' id='id' class='key'>
                  <input type='hidden' name='status' id='status' class='key'>
                  <input type='hidden' name='reason' id='reason' class='key'>
        </form>
        <iframe id="innerFrame" name="innerFrame" height="0" frameborder="0"></iframe>
         <g:form  action="${actionName}" params="${params}"  method="post" class="commonListForm">
        </g:form >
<g:render template="/layouts/footer"/>
    </div> <!-- /container -->
    
  </body>
</html>
