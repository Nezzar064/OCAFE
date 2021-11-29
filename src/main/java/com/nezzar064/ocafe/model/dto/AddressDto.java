package com.nezzar064.ocafe.model.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@RequiredArgsConstructor
public class AddressDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Please provide a valid country!")
    private String country;
    @NotBlank(message = "Please provide a valid zip code!")
    private String zipCode;
    @NotBlank(message = "Please provide a valid city!")
    private String city;
    @NotBlank(message = "Please provide a valid street name!")
    private String streetName;
    @NotBlank(message = "Please provide a valid street number (1-999)!")
    private String streetNumber;
}
