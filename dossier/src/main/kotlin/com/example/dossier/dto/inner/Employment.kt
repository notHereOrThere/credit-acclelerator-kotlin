package com.example.dossier.dto.inner

import com.example.deal.entity.enums.EmploymentStatus
import com.example.deal.entity.enums.Position
import java.io.Serializable
import java.math.BigDecimal

class Employment : Serializable {
    private var employmentId: Long? = null
    private var employmentStatus: EmploymentStatus? = null
    private var employerINN: String? = null
    private var salary: BigDecimal? = null
    private var position: Position? = null
    private var workExperienceTotal: Int? = null
    private var workExperienceCurrent: Int? = null
}
