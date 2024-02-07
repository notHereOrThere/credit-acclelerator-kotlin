package com.example.deal.entity

import com.example.deal.entity.enums.Gender
import com.example.deal.entity.enums.MaritalStatus
import com.example.deal.entity.inner.Employment
import com.example.deal.entity.inner.Passport
import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "client", schema = "deal")
open class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    open var clientId: Long? = null

    @Column(name = "last_name")
    open var lastName: String? = null

    @Column(name = "first_name")
    open var firstName: String? = null

    @Column(name = "middle_name")
    open var middleName: String? = null

    @Column(name = "birth_date")
    open var birthdate: LocalDate? = null

    @Column(name = "email")
    open var email: String? = null

    @Column(name = "gender")
    @Enumerated(value = EnumType.STRING)
    open var gender: Gender? = null

    @Column(name = "martial_status")
    @Enumerated(value = EnumType.STRING)
    open var martialStatus: MaritalStatus? = null

    @Column(name = "dependent_amount")
    open var dependentAmount: Int? = null

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "passport_id")
    open var passport: Passport? = null

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "employment_id")
    open var employment: Employment? = null

    @OneToMany(mappedBy = "client", cascade = [CascadeType.ALL], orphanRemoval = true, targetEntity = Application::class)
    @JsonIgnore
    open var applications: List<Application> = mutableListOf()
}