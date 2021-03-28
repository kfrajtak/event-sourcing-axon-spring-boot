package com.progressivecoder.es.eventsourcingaxonspringboot.services.commands;

import com.progressivecoder.es.eventsourcingaxonspringboot.dto.commands.AccountCreateDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AccountCommandListernerService {
    @Autowired
    private AccountCommandService accountCommandService;

    @RabbitListener(queues = "CommandQueue")
    public void processCreate(AccountCreateDTO accountCreateDTO) {
        log.info("{}", accountCreateDTO);
        accountCommandService.createAccount(accountCreateDTO);
    }
}
