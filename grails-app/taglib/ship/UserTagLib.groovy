package ship

class UserTagLib {
    static namespace = "usert"
    def lastbuy = {attrs->

        def results = DaiFaOrder.findAllByAdd_user(attrs.id,[max: 1, offset: 0, sort: "payTime", order: "desc"])

        if(results){
            out << results[0].payTime
        }


    }
}
