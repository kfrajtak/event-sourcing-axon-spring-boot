package com.progressivecoder.es.eventsourcingaxonspringboot.dto.commands;

import lombok.Value;

@Value
public class AccountCreateDTO {
    private double startingBalance;
    private String currency;
    private double overdraftLimit;
}
