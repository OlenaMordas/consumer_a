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
@Table(name = "clients")
public class Client {

    @Id
    private String client_id;
    String firstName;
    String lastName;
    String ebanking_id;



}
