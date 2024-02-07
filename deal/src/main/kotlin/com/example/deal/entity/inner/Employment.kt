package com.example.deal.entity.inner

import com.example.deal.entity.enums.EmploymentStatus
import com.example.deal.entity.enums.Position
import lombok.Getter
import lombok.Setter
import java.io.Serializable
import java.math.BigDecimal
import javax.persistence.*

@Getter
@Setter
@Entity
@Table(name = "employment", schema = "deal")
open class Employment : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employment_id")
    open var employmentId: Long? = null

    @Column(name = "employment_status")
    @Enumerated(EnumType.STRING)
    open var employmentStatus: EmploymentStatus? = null

    @Column(name = "employer_inn")
    open var employerINN: String? = null

    @Column(name = "salary")
    open var salary: BigDecimal? = null

    @Column(name = "work_position")
    @Enumerated(EnumType.STRING)
    open var position: Position? = null

    @Column(name = "work_experience_total")
    open var workExperienceTotal: Int? = null

    @Column(name = "work_experience_current")
    open var workExperienceCurrent: Int? = null
}
