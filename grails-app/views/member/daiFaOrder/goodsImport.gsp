<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>金士代发</title>
  </head>
  <body>
    <div class="container body" id="page">

      <g:if test="${flash.message}">
         <script>
         alert('${flash.message}')
        </script>
      </g:if>
   
      


     <div class="page-header">
        <h3>批量上传商品
          <a href="<%=request.getContextPath()%>/resource/goodsmuban/goods.xls" class="btn btn-primary btn-large pull-right">点击下载模板</a>
          
      </h3>
        </div>


     <g:form  name="addForm"  data-validate="parsley" controller="memberImportGoods" action="goodsImport"  enctype="multipart/form-data" method="post" class="form-horizontal form-inline" >
      <fieldset>
         <div class="control-group">
               <label>选择已经填好商品数据的表格文件</label>
                 <input type="file" name="goods_path" id="goods_path">

         </div>

       <button type="button" onclick="importGoods()" class="btn btn-large btn-primary">提交</button>
     </fieldset>
   </g:form>
      
<script>
         function importGoods() {
            var goods_path = $("#goods_path").val();
            if(""== goods_path ) {
              alert("请选择要导入的excel文件");
            } else {
               var goods_path = goods_path.substring(goods_path.lastIndexOf('.')).toLowerCase();    
               if(goods_path == '.xls' || goods_path =='.xlsx'){   
                   $("#addForm").submit()
                }else{   
                    alert("请导入.xls,.xlsx格式");   
                    return;   
                } 
            }
          }
</script>

 
<g:render template="/layouts/footer"/>


    </div> <!-- /container -->
    
  </body>
</html>
