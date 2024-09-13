package com.example.LeadGenerator.request;


import lombok.Data;


@Data
public class FormData {

    private String refereeID;
    private String refereeName;
    private String refereePhone;
    private String merchantName;
    private String merchantContact;
    private String shopName;
    private String email;
    private Double lat;
    private Double lng;
    private String pinCode;

}

