package com.grupo12.Voy.features.parties.scheduler;

import com.grupo12.Voy.features.parties.repository.PartyRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class PartyScheduler {
    private final PartyRepository partyRepository;

    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void closeExpiredParties() {
        partyRepository
                .findByDateTimeBeforeAndLogicStateTrue(LocalDateTime.now())
                .forEach(party -> party.setLogicState(false));
    }
}
