package cron

import de.rhenus.scs.customs.order.service.CustomsOrderComplianceService
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class AutomaticComplianceCheck(private val customsOrderComplianceService: CustomsOrderComplianceService) {
    // every hour at xx:15
    @Scheduled(cron = "0 15 * * * ?", zone = "Europe/Berlin")
    @SchedulerLock(name = "AutomaticComplianceCheck")
    fun doCheck() {
        customsOrderComplianceService.checkAllNeverCheckedAddresses()
    }
}
