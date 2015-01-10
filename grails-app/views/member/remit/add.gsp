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
                <h3>汇款充值</h3>
            </div>

            <g:form name="updateUserInfoForm" data-validate="parsley" url="[controller: 'memberRemit', action: 'doAdd']"
                method="post" class="form-horizontal">

                <fieldset>
                    <div class="row-fluid">
                        <h4>1.请先向如下账户汇入您想充值的金额</h4>

                        <div class="span12">
                            <label>户名:沈欣婷</label>

                        </div>

                        <div class="span12">
                            <label>帐号:6217 8620 0000 1819 920</label>

                        </div>

                        <div class="span12">
                            <label>开户行:中国银行深圳福田保税区支行</label>

                        </div>
                    </div>

                    <div class="row-fluid">
                        <h4>2.在此填写如下汇款信息</h4>

                        <div class="span4">
                            <label>汇出银行名称</label> 
                            <select name="bank_name" id="bank_name" data-required-message="汇出款银行不能为空" data-required="true">

                                <option value="">请选择...</option>
                                <option value="支付宝">支付宝</option>
                                <option value="财富通">财富通</option>
                                <option value="中国工商银行">中国工商银行</option>
                                <option value="招商银行">招商银行</option>
                                <option value="中国银行">中国银行</option>
                                <option value="中国邮政储蓄银行">中国邮政储蓄银行</option>
                                <option value="中国建设银行">中国建设银行</option>
                                <option value="中国农业银行">中国农业银行</option>
                                <option value="中国光大银行">中国光大银行</option>
                                <option value="中信银行">中信银行</option>
                                <option value="浦发银行">浦发银行</option>
                                <option value="交通银行">交通银行</option>
                                <option value="兴业银行">兴业银行</option>
                                <option value="华夏银行">华夏银行</option>
                                <option value="广发银行">广发银行</option>
                                <option value="民生银行">民生银行</option>
                                <option value="浙商银行">浙商银行</option>
                                <option value="成都银行">成都银行</option>
                            </select>
                        </div>

                        <div class="span4">
                            <label>汇出款银行账号</label>
                            <input value="" type="text" name="bank_account" id="bank_account" placeholder=""
                            data-required-message="汇出款银行账号不能为空" data-required="true">

                        </div>
                        <div class="span3">
                            <label>汇款人姓名</label>
                            <input value="" type="text" name="user_name" id="user_name" placeholder=""
                            data-required-message="汇款人姓名不能为空" data-required="true">

                        </div>

                        <div class="span4">
                            <label>汇出金额</label>
                            <input onkeyup="if (isNaN(value))execCommand('undo')"
                            onafterpaste="if(isNaN(value))execCommand('undo')" value="" type="text" name="bank_amount"
                            id="bank_amount" placeholder="" data-required-message="汇出款金额不能为空" data-required="true">

                        </div>

                        <div class="span4">
                            <label>汇款单号</label>
                            <input value="" type="text" name="bank_order" id="bank_order" placeholder=""
                            data-required-message="汇出款单号不能为空" data-required="true">

                        </div>

                        <div class="span3">
                            <label>汇出时间</label>
                            <input class="datetime" value="" type="text" name="remit_date" id="remit_date" data-required="true"
                            data-required-message="汇款时间">

                        </div>

                        <div class="span4" style="margin-top: 20px;">
                            <button type="submit" class="btn btn-large btn-primary">提交</button>
                        </div>

                    </div>

















                </fieldset>

            </g:form>


            <script>
                $(document).ready(function () {
                $(".datetime").datetimepicker({
                format: "yyyy-mm-dd",
                autoclose: true,
                todayBtn: true,
                pickerPosition: "bottom-left",
                minView: 4,
                language: 'zh-CN'
                });
                })

            </script>
            <g:render template="/layouts/footer"/>
        </div> <!-- /container -->
    </body>
</html>
