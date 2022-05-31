package cron

import de.rhenus.scs.customs.order.service.DocumentArchiveService
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class DocumentArchiveCheck(
    private val documentArchiveService: DocumentArchiveService
) {
    // every hour at xx:30
    @Scheduled(cron = "0 30 * * * ?", zone = "Europe/Berlin")
    @SchedulerLock(name = "DocumentArchiveCheck")
    fun doCheck() {
        documentArchiveService.archivePendingDocuments()
    }
}
