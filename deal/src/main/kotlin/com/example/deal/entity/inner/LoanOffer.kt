package com.example.deal.entity.inner

import com.example.deal.entity.Application
import com.fasterxml.jackson.annotation.JsonIgnore
import lombok.Getter
import lombok.Setter
import java.io.Serializable
import java.math.BigDecimal
import javax.persistence.*

@Getter
@Setter
@Entity
@Table(name = "applied_offer", schema = "deal")
class LoanOffer : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "applied_offer_id")
    var appliedOfferId: Long? = null

    @OneToOne(mappedBy = "appliedOffer")
    @JsonIgnore
    var application: Application? = null

    @Column(name = "requested_amount")
    var requestedAmount: BigDecimal? = null

    @Column(name = "total_amount")
    var totalAmount: BigDecimal? = null

    @Column(name = "term")
    var term: Int? = null

    @Column(name = "monthly_payment")
    var monthlyPayment: BigDecimal? = null

    @Column(name = "rate")
    var rate: BigDecimal? = null

    @Column(name = "is_insurance_enabled")
    var isInsuranceEnabled: Boolean? = null

    @Column(name = "is_salary_client")
    var isSalaryClient: Boolean? = null
}