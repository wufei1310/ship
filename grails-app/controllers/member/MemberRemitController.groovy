package member

import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.support.PropertiesLoaderUtils
import ship.User
import ship.Remit
import util.StringUtil
import admin.BaseController

import java.text.SimpleDateFormat

class MemberRemitController extends BaseController {

    def mailService
    def properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("sysSetting.properties"))
    def isPro = properties.getProperty("isPro");

    def index() { 
    
        
    }
    
    def list() { 
        if (!params.max) params.max = 10  
        if (!params.offset) params.offset = 0  
        if (!params.sort) params.sort = "lastUpdated"  
        if (!params.order) params.order = "desc" 
       
       def searchClosure =  {
            
           
            submit_user{
                 eq('id',session.loginPOJO.user.id)
            }
               
            
           
        }
        
        def o = Remit.createCriteria();
        def results = o.list(params,searchClosure)
        
        def map = [list: results, total: results.totalCount]
    
        render(view: "/member/remit/list",model:map)
    }
    
    def toAdd(){
        render(view: "/member/remit/add")
    }
  
    def doAdd(){
        params.remit_date =  Date.parse("yyyy-MM-dd",params.remit_date)
        def remit = new Remit(params)
        remit.submit_user = User.get(session.loginPOJO.user.id)      
        remit.status = "0"
        remit.save()
        flash.message = "系统将启动对账流程，如果资料填写正确，您的代发帐户将会增加相应的金额"


        Thread.start {
            mailService.sendMail {
                async true
                from "service@findyi.com"
                if(isPro=="true"){
                    to "mail3298@gmail.com","wufei1310@126.com"
                }else{
                    to "wufei1310@126.com"
                }

                subject "金士代发有人充值了"
                html remit.submit_user.email + "刚刚提交了线下充值" + remit.bank_amount

            }
        }

        redirect(action:"list")
    }
}
