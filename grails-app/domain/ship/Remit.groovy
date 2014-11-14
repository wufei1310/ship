package ship

class Remit {

    static constraints = {
        check_user(blank:true,nullable:true)
        reason(blank:true,nullable:true)
        check_date(blank:true,nullable:true)
      //  remit_date(blank:true,nullable:true)
        bank_amount(min:new BigDecimal(0))
    }
    
    BigDecimal bank_amount;
    User submit_user;
    String user_name;  //汇款人姓名
    String bank_account //银行账号
    String bank_name   //银行名称
    String bank_order  //银行单号
    
    String status; //0未审核 1审核通过 2审核不通过
    User check_user  //审核人
    String reason // 理由
    Date check_date //审核日期
    Date remit_date  //汇款时间
    
    Date dateCreated
    Date lastUpdated
    
}
