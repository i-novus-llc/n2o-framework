package net.n2oapp.framework.config.groovy
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject

long start = System.nanoTime();
def id = "thread" + Thread.currentThread().getId();
object = new N2oObject(id: id, name: 'Igor')
println "I'm ${object.id}! Estimated ${System.nanoTime() - start} ns"