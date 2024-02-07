package com.example.deal.mapper

import com.example.credit.application.model.CreditDTO
import com.example.credit.application.model.EmploymentDTO
import com.example.credit.application.model.LoanApplicationRequestDTO
import com.example.credit.application.model.LoanOfferDTO
import com.example.deal.entity.Client
import com.example.deal.entity.Credit
import com.example.deal.entity.enums.EmploymentStatus
import com.example.deal.entity.enums.Position
import com.example.deal.entity.inner.Employment
import com.example.deal.entity.inner.LoanOffer
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named

@Mapper(componentModel = "spring")
interface DealMapper {

    @Mapping(target = "passport.passportSer", source = "passportSeries")
    @Mapping(target = "passport.passportNum", source = "passportNumber")
    fun loanApplicationRequestDtoToClientEntity(loanApplicationRequestDTO: LoanApplicationRequestDTO?): Client

    fun loanOfferDtoToLoanOfferEntity(loanOfferDTO: LoanOfferDTO): LoanOffer

    fun creditDtoToCreditEntity(creditDTO: CreditDTO?): Credit

    @Mapping(target = "position", source = "position", qualifiedByName = ["mapPosition"])
    @Mapping(target = "employmentStatus", source = "employmentStatus", qualifiedByName = ["mapEmploymentStatus"])
    fun employmentDtoToEmploymentEntity(employmentDTO: EmploymentDTO): Employment

    @Named("mapPosition")
    fun mapPosition(positionEnum: EmploymentDTO.PositionEnum): Position = when (positionEnum) {
        EmploymentDTO.PositionEnum.OWNER -> Position.OWNER
        EmploymentDTO.PositionEnum.MIDDLE_MANAGER -> Position.MIDDLE_MANAGER
        EmploymentDTO.PositionEnum.TOP_MANAGER -> Position.TOP_MANAGER
        else -> Position.WORKER
    }

    @Named("mapEmploymentStatus")
    fun mapEmploymentStatus(employmentStatusEnum: EmploymentDTO.EmploymentStatusEnum): EmploymentStatus = when (employmentStatusEnum) {
        EmploymentDTO.EmploymentStatusEnum.UNEMPLOYED -> EmploymentStatus.UNEMPLOYED
        EmploymentDTO.EmploymentStatusEnum.SELF_EMPLOYED -> EmploymentStatus.SELF_EMPLOYED
        EmploymentDTO.EmploymentStatusEnum.BUSINESS_OWNER -> EmploymentStatus.BUSINESS_OWNER
        else -> EmploymentStatus.EMPLOYED
    }
}
