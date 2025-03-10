package com.progressivecoder.es.eventsourcingaxonspringboot.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class AccountQueryEntity {
    @Id
    private String id;

    private double accountBalance;

    private String currency;

    private String status;
}
