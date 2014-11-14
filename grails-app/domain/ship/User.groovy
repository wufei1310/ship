package ship

class User {

    static constraints = {
        name(blank:true,nullable:true)
        phone(blank:true,nullable:true)
        store_url(blank:true,nullable:true)
        qq(blank:true,nullable:true)
        safepass(blank:true,nullable:true)
        adduser(blank:true,nullable:true)
        role_id(blank:true,nullable:true)
        isCanPrint(blank:true,nullable:true)
    }

    static hasMany = [market:Market]
    Role role_id
    String email
    String password
    String user_type //用户类型(member,admin,daifa)
    String name //用户姓名或店铺名称
    String phone //
    String store_url
    String qq
    String safepass //支付密码
    Account account
    User adduser //开户人
    String role //admin member daifa wuliu
    String status //0 有效 1失效
    String isCanPrint //０不能１可以
    
    Date dateCreated
    Date lastUpdated
}
