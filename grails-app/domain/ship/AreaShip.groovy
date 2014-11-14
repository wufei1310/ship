/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ship

/**
 *
 * @author DELL
 */
class AreaShip {
	static constraints = {
            f_price(min:new BigDecimal(0))
            x_price(min:new BigDecimal(0))
        }

         Express express
         String area_id
         BigDecimal f_price
         BigDecimal x_price
         
         Date dateCreated
         Date lastUpdated
}

