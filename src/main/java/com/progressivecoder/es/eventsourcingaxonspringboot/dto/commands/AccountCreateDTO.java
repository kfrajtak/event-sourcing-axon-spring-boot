package com.progressivecoder.es.eventsourcingaxonspringboot.dto.commands;

import lombok.Data;

@Data
public class AccountCreateDTO {
    private double startingBalance;
    private String currency;
    private double overdraftLimit;
}
