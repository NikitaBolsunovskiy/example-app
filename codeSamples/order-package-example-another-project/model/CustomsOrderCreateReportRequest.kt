package model

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.format.annotation.DateTimeFormat
import java.time.Instant
import javax.validation.constraints.NotNull

class CustomsOrderCreateReportRequest(
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    var dateFrom: @NotNull Instant? = null,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    var dateTo: @NotNull Instant? = null,
    var customerId: Long? = null,
    var customerIdCct: @NotNull String? = null,
)
