package com.example.deal.entity

import com.example.deal.entity.enums.ApplicationStatus
import com.example.deal.entity.inner.LoanOffer
import com.example.deal.entity.inner.StatusHistory
import lombok.Getter
import lombok.Setter
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "application", schema = "deal")
open class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    open var applicationId: Long? = null

    @ManyToOne
    @JoinColumn(name = "client_id")
    open var client: Client? = null

    @ManyToOne
    @JoinColumn(name = "credit_id")
    open var credit: Credit? = null

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    open var status: ApplicationStatus? = null

    @Column(name = "creation_date")
    open var creationDate: Date? = null

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "applied_offer_id")
    open var appliedOffer: LoanOffer? = null

    @Column(name = "sign_date")
    open var signDate: Date? = null

    @Column(name = "ses_code")
    open var sesCode: String? = null

    @OneToMany(mappedBy = "application", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
    open val statusHistory: List<StatusHistory> = mutableListOf()
}
