package ru.pozhar.camunda.delegate;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import ru.pozhar.camunda.client.CollectorApiClient;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateNotificationDelegate implements JavaDelegate {
    private final CollectorApiClient collectorApiClient;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String agreementId = (String) execution.getVariable("agreementId");
        List<String> debtorsId = (List<String>) execution.getVariable("debtorsId");
        boolean isCreated = false;
        try {
            for (String debtorId : debtorsId) {
                isCreated = collectorApiClient.generateNotification(debtorId, agreementId);
            }
            execution.setVariable("isCreatedNotifications", isCreated);
        } catch (Exception e) {
            log.error("Camunda: Ошибка при генерации уведомления: {}", e.getMessage(), e);
            throw new BpmnError("notificationCreationError");
        }
    }
}
