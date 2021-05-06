package com.example.consumer_a.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    private String client_id;
    String account_nr;
    String account_type;
    String CCY;
    String IBAN;
    int balance;



}
