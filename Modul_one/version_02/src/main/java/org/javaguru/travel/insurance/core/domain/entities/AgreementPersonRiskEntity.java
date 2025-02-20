package org.javaguru.travel.insurance.core.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "agreement_person_risks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgreementPersonRiskEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "agreement_person_id", nullable = false)
    private AgreementPersonEntity agreementPerson;

    @Column(name = "risk_ic", nullable = false)
    private String riskIc;

    @Column(name = "premium", nullable = false)
    private BigDecimal premium;

}
