package com.example.deal.entity.inner

import java.io.Serializable
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "passport", schema = "deal")
class Passport : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "passport_id")
    var passportId: Long? = null

    @Column(name = "numbers")
    var passportNum: String? = null

    @Column(name = "series")
    var passportSer: String? = null

    @Column(name = "issue_date")
    var passportIssueDate: LocalDate? = null

    @Column(name = "issue_branch")
    var passportIssueBranch: String? = null
}
