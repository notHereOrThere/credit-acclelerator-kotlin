package com.example.deal.entity

import com.example.deal.entity.enums.Status
import com.example.deal.entity.inner.PaymentSchedule
import com.fasterxml.jackson.annotation.JsonIgnore
import lombok.Getter
import lombok.Setter
import java.math.BigDecimal
import javax.persistence.*

@Entity
@Table(name = "credit", schema = "deal")
@Getter
@Setter
open class Credit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "credit_id")
    open var creditId: Long? = null

    @Column(name = "amount")
    open var amount: BigDecimal? = null

    @OneToMany(mappedBy = "credit", cascade = [CascadeType.ALL], orphanRemoval = true, targetEntity = Application::class)
    @JsonIgnore
    open var applications: List<Application> = mutableListOf()

    @Column(name = "term")
    open var term: Int? = null

    @Column(name = "monthly_payment")
    open var monthlyPayment: BigDecimal? = null

    @Column(name = "rate")
    open var rate: BigDecimal? = null

    @Column(name = "psk")
    open var psk: BigDecimal? = null

    @Column(name = "payment_schedule")
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, mappedBy = "credit")
    open var paymentSchedule: List<PaymentSchedule> =  mutableListOf()

    @Column(name = "insurance_enable")
    open var isInsuranceEnabled: Boolean? = null

    @Column(name = "salary_client")
    open var isSalaryClient: Boolean? = null

    @Column(name = "credit_status")
    @Enumerated(value = EnumType.STRING)
    open var creditStatus: Status? = null
}