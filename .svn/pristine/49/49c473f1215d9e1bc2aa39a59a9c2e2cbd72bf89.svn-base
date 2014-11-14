package admin

import org.springframework.dao.DataIntegrityViolationException

class AdminApkController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [adminApkInstanceList: AdminApk.list(params), adminApkInstanceTotal: AdminApk.count()]
    }

    def create() {
        [adminApkInstance: new AdminApk(params)]
    }

    def save() {
        def adminApkInstance = new AdminApk(params)
        if (!adminApkInstance.save(flush: true)) {
            render(view: "create", model: [adminApkInstance: adminApkInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'adminApk.label', default: 'AdminApk'), adminApkInstance.id])
        redirect(action: "show", id: adminApkInstance.id)
    }

    def show(Long id) {
        def adminApkInstance = AdminApk.get(id)
        if (!adminApkInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'adminApk.label', default: 'AdminApk'), id])
            redirect(action: "list")
            return
        }

        [adminApkInstance: adminApkInstance]
    }

    def edit(Long id) {
        def adminApkInstance = AdminApk.get(id)
        if (!adminApkInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'adminApk.label', default: 'AdminApk'), id])
            redirect(action: "list")
            return
        }

        [adminApkInstance: adminApkInstance]
    }

    def update(Long id, Long version) {
        def adminApkInstance = AdminApk.get(id)
        if (!adminApkInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'adminApk.label', default: 'AdminApk'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (adminApkInstance.version > version) {
                adminApkInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'adminApk.label', default: 'AdminApk')] as Object[],
                          "Another user has updated this AdminApk while you were editing")
                render(view: "edit", model: [adminApkInstance: adminApkInstance])
                return
            }
        }

        adminApkInstance.properties = params

        if (!adminApkInstance.save(flush: true)) {
            render(view: "edit", model: [adminApkInstance: adminApkInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'adminApk.label', default: 'AdminApk'), adminApkInstance.id])
        redirect(action: "show", id: adminApkInstance.id)
    }

    def delete(Long id) {
        def adminApkInstance = AdminApk.get(id)
        if (!adminApkInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'adminApk.label', default: 'AdminApk'), id])
            redirect(action: "list")
            return
        }

        try {
            adminApkInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'adminApk.label', default: 'AdminApk'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'adminApk.label', default: 'AdminApk'), id])
            redirect(action: "show", id: id)
        }
    }
    
   
}
