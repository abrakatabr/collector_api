package ru.pozhar.collector_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.pozhar.collector_api.model.NotificationData;
import ru.pozhar.collector_api.repository.NotificationDataRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationDataRepository notificationDataRepository;
    private final NotificationCacheService notificationCacheService;
    private final ResourceLoader resourceLoader;

    public ByteArrayResource getNotification(Long debtorId, Long agreementId) throws IOException{
        String notification;
        NotificationData notificationData = notificationDataRepository.findByDebtorIdAndAgreementId(
                debtorId, agreementId
        );
        if (notificationCacheService.hasNotification(debtorId, agreementId)) {
            notification = notificationCacheService.getNotification(debtorId, agreementId);
        } else {
            try {
                Resource resource = resourceLoader.getResource("file:notification_template.txt");
                notification = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            } catch (IOException ex) {
                throw new IOException("Не удеалось загрузить шаблон");
            }
            notification = formatNotification(notification, notificationData);
            notificationCacheService.cacheNotification(debtorId, agreementId, notification);
        }
        ByteArrayResource byteContent = new ByteArrayResource(
                notification.getBytes(StandardCharsets.UTF_8));
        return byteContent;
    }



    private String formatNotification(String notification, NotificationData notificationData) {
        notification = StringUtils.replace(notification, "{lastName}", notificationData.getDebtor().getLastname());
        notification = StringUtils.replace(notification, "{firstName}", notificationData.getDebtor().getFirstname());
        notification = StringUtils.replace(notification, "{patronymic}", notificationData.getDebtor().getPatronymic());
        notification = StringUtils.replace(notification, "{country}", notificationData.getAddress().getCountry());
        notification = StringUtils.replace(notification, "{city}", notificationData.getAddress().getCity());
        notification = StringUtils.replace(notification, "{street}", notificationData.getAddress().getStreet());
        notification = StringUtils.replace(notification, "{house}", notificationData.getAddress().getHouse());
        notification = StringUtils.replace(notification, "{apartment}", notificationData.getAddress().getApartment());
        notification = StringUtils.replace(notification, "{agreementStartDate}",
                notificationData.getAgreement().getAgreementStartDate().toString());
        notification = StringUtils.replace(notification, "{transferor}", notificationData.getAgreement().getTransferor());
        notification = StringUtils.replace(notification, "{actualDebtSum}",
                notificationData.getAgreement().getActualDebtSum().toString());
        return notification;
    }
}
