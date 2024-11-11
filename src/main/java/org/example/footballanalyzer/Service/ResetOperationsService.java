package org.example.footballanalyzer.Service;

import lombok.RequiredArgsConstructor;
import org.example.footballanalyzer.Data.Entity.ResetOperations;
import org.example.footballanalyzer.Data.Entity.UserEntity;
import org.example.footballanalyzer.Repository.ResetOperationsRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class ResetOperationsService {

    private final ResetOperationsRepository resetOperationsRepository;

    @Transactional
    public ResetOperations initResetOperation(UserEntity user) {
        ResetOperations resetOperations = new ResetOperations();
        resetOperations.setUser(user);
        resetOperations.setUuid(java.util.UUID.randomUUID().toString());
        resetOperations.setCreateDate(new Timestamp(System.currentTimeMillis()).toString());
        resetOperationsRepository.deleteAllByUser(user);
        return resetOperationsRepository.saveAndFlush(resetOperations);
    }

    public void endOperation(String uuid) {
        resetOperationsRepository.findByUuid(uuid).ifPresent(resetOperationsRepository::delete);
    }

    //    @Scheduled(cron = "0 0/15 * * * *")
    protected void deleteExpiredOperations() {
        List<ResetOperations> resetOperations = resetOperationsRepository.findExpiredOperations();
        if (resetOperations != null && !resetOperations.isEmpty()) {
            resetOperationsRepository.deleteAll(resetOperations);
        }
    }
}
