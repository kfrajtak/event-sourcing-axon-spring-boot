package com.progressivecoder.es.eventsourcingaxonspringboot.dto.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreateDTO {
    private double startingBalance;
    private String currency;
    private double overdraftLimit;
}
