package com.gstore.gstoreapi.services;

import com.gstore.gstoreapi.models.entities.Buyer;
import com.gstore.gstoreapi.models.entities.CustomSession;
import com.gstore.gstoreapi.repositories.CustomSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomSessionService {

    private final CustomSessionRepository customSessionRepository;

    public void saveCustomSession(CustomSession customSession) {
        customSessionRepository.save(customSession);
    }

    public void deleteCustomSession(CustomSession customSession) {
        customSessionRepository.delete(customSession);
    }

    public Optional<CustomSession> getCustomSessionById(Long sessionId) {
        return customSessionRepository.findById(sessionId);
    }

    public Optional<CustomSession> getCustomSessionByBuyer(Buyer buyer) {
        return customSessionRepository.findByBuyer(buyer);
    }


}
