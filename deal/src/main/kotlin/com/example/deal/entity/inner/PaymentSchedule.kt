package com.example.deal.entity.inner

import com.example.deal.entity.Credit
import com.fasterxml.jackson.annotation.JsonIgnore
import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "payment_schedule", schema = "deal")
class PaymentSchedule : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_schedule_id")
    var paymentScheduleId: Long? = null

    @ManyToOne
    @JoinColumn(name = "credit_id")
    @JsonIgnore
    var credit: Credit? = null

    @Column(name = "payment_number")
    var number: Int? = null

    @Column(name = "payment_date")
    var date: LocalDate? = null

    @Column(name = "total_payment")
    var totalPayment: BigDecimal? = null

    @Column(name = "interest_payment")
    var interestPayment: BigDecimal? = null

    @Column(name = "debt_payment")
    var debtPayment: BigDecimal? = null

    @Column(name = "remaining_debt")
    var remainingDebt: BigDecimal? = null
}
