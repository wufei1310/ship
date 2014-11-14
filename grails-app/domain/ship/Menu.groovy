/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ship

/**
 *
 * @author DELL
 */
class Menu{

    static constraints = {
        menuDesc(blank:true,nullable:true)
        menuPath(blank:true,nullable:true)
    }
    static belongsTo = Role
    static hasMany = [ role:Role]
    static mapping = {
        sort sortNo: "desc" // or "asc"
    }
    
    
    String menuName
    String menuPath
    String menuDesc
    Long pId
    String status //0无效 1有效
    Long sortNo //越大越靠前
    List<Menu> childrenMenu = new ArrayList<Menu>();
    
    Date dateCreated
    Date lastUpdated
}
