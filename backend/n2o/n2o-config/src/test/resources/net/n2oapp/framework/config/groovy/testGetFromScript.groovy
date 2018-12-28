package net.n2oapp.framework.config.groovy
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject

object = new N2oObject(id:'test', name: 'Igor')
println "Hello ${object.name}!"