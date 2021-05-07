package com.example.consumer_a.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    private String account_nr;
    String client_id;
    String account_type;
    String ccy;
    String iban;
    float balance;



}
