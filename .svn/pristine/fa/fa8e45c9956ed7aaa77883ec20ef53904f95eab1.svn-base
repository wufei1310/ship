/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
class TransactionFilters {

	def sessionFactory

	def filters = {
		startTransaction(controller:'*', action:'*') {
			before = {

			   // sessionFactory.getCurrentSession().beginTransaction()
			}
		}

        // no need for an 'after' filter, Spring take care of committing or rolling back
        // the transaction

	}

}

