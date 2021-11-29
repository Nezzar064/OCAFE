package com.nezzar064.ocafe.model.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Embeddable
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "country", table = "address")
    private String country;

    @Column(name = "zip_code", table = "address")
    private String zipCode;

    @Column(name = "city", table = "address")
    private String city;

    @Column(name = "street_name", table = "address")
    private String streetName;

    @Column(name = "street_number", table = "address")
    private String streetNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(country, address.country) && Objects.equals(zipCode, address.zipCode) && Objects.equals(city, address.city) && Objects.equals(streetName, address.streetName) && Objects.equals(streetNumber, address.streetNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, zipCode, city, streetName, streetNumber);
    }
}
