package ru.pozhar.collector_api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotificationData {
    Debtor debtor;
    Address address;
    Agreement agreement;
}
