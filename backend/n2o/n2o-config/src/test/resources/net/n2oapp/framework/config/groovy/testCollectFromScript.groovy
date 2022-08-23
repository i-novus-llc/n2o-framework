package net.n2oapp.framework.config.groovy

import net.n2oapp.framework.api.metadata.global.dao.query.N2oQuery
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject
import net.n2oapp.framework.api.metadata.global.view.page.N2oStandardPage

object1 = new N2oObject(id: 'object1')
object2 = new N2oObject(id: 'object2')

page1 = new N2oStandardPage(id: 'page1')
query = new N2oQuery(id: 'query')

collection = [new N2oStandardPage(id: 'page2'), new N2oObject(id: 'object3')]

def testMethod() {
    true
}
