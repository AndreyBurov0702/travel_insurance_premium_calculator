package org.javaguru.travel.insurance.core.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "travel_medical_age_coefficient")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TMAgeCoefficient {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "age_from", nullable = false)
    private Integer ageFrom;

    @Column(name = "age_to", nullable = false)
    private Integer ageTo;

    @Column(name = "coefficient", precision = 10, scale = 2, nullable = false)
    private BigDecimal coefficient;

}
