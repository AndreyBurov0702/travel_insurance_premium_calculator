package org.javaguru.travel.insurance.core.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "agreement_persons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgreementPersonEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "agreement_id", nullable = false)
    private AgreementEntity agreement;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private PersonEntity person;

    @Column(name = "medical_risk_limit_level", nullable = false)
    private String medicalRiskLimitLevel;

    @Column(name = "travel_cost")
    private BigDecimal travelCost;

}

