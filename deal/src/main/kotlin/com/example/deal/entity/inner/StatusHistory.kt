package com.example.deal.entity.inner

import com.example.deal.entity.Application
import com.example.deal.entity.enums.ChangeType
import com.fasterxml.jackson.annotation.JsonIgnore
import lombok.Getter
import lombok.Setter
import org.springframework.data.annotation.CreatedDate
import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Getter
@Setter
@Table(name = "status_history", schema = "deal")
class StatusHistory : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_history_id")
    var statusHistoryId: Long? = null

    @ManyToOne
    @JoinColumn(name = "application_id")
    @JsonIgnore
    var application: Application? = null

    @Column(name = "status")
    var status: String? = null

    @Column(name = "created_time")
    @CreatedDate
    var time: Date? = null

    @Column(name = "change_type")
    @Enumerated(EnumType.STRING)
    var changeType: ChangeType? = null
}
