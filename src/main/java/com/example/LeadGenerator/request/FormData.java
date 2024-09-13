package com.example.LeadGenerator.request;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Primary;


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
    private Double log;
    private String pinCode;

}

