package aspect

import de.rhenus.scs.customs.common.events.CctApplicationEventPublisher
import de.rhenus.scs.customs.common.model.OptimisticLockingAuditableEntity
import de.rhenus.scs.customs.order.event.CustomsOrderEntityCreatedEvent
import de.rhenus.scs.customs.order.event.CustomsOrderEntityUpdatedEvent
import de.rhenus.scs.customs.order.model.dependant.CustomsOrderDependant
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component

@Component
@Aspect
class CustomsOrderEntitySavedAspect(
    private val cctApplicationEventPublisher: CctApplicationEventPublisher
) {
    enum class PersistingType { CREATE, UPDATE }

    @Around("execution(* de.rhenus.scs.customs.order.repository..*.save(..))")
    fun saveCalled(pjp: ProceedingJoinPoint): Any {
        val entity = pjp.args[0]
        val persistingType = if((entity as OptimisticLockingAuditableEntity).id === null) PersistingType.CREATE else PersistingType.UPDATE
        val result = pjp.proceed()
        triggerEventForEntity(pjp.args[0] as CustomsOrderDependant, persistingType)
        return result
    }

    @Around("execution(* de.rhenus.scs.customs.order.repository..*.saveAndFlush(..))")
    fun saveAndFlushCalled(pjp: ProceedingJoinPoint): Any {
        val entity = pjp.args[0]
        val persistingType = if((entity as OptimisticLockingAuditableEntity).id === null) PersistingType.CREATE else PersistingType.UPDATE
        val result = pjp.proceed()
        triggerEventForEntity(pjp.args[0] as CustomsOrderDependant, persistingType)
        return result
    }

    @Around("execution(* de.rhenus.scs.customs.order.repository..*.saveAll(..))")
    fun saveAllCalled(pjp: ProceedingJoinPoint): Any {
        val entitiesWithPersistingType = (pjp.args[0] as List<*>)
            .map { Pair(it as CustomsOrderDependant, if((it as OptimisticLockingAuditableEntity).id === null) PersistingType.CREATE else PersistingType.UPDATE) }
        val result = pjp.proceed()
        entitiesWithPersistingType.forEach { triggerEventForEntity(it.first, it.second) }
        return result
    }

    fun triggerEventForEntity(entityToSave: CustomsOrderDependant, persistingType: PersistingType) {
        val customsOrderId = entityToSave.retrieveCustomsOrderId()
        when(persistingType) {
            PersistingType.CREATE -> cctApplicationEventPublisher.publishEvent(CustomsOrderEntityCreatedEvent(entityToSave.javaClass.simpleName, customsOrderId))
            PersistingType.UPDATE -> cctApplicationEventPublisher.publishEvent(CustomsOrderEntityUpdatedEvent(entityToSave.javaClass.simpleName, customsOrderId))
        }
    }
}
