async function createNotification() {
    const data = getFormData('createNotificationForm');
    
    try {
        const response = await fetch(`${API_BASE}/debtors/${data.debtorId}/notification/${data.agreementId}`);
        
        if (response.ok) {
            const blob = await response.blob();
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = `notification_${data.debtorId}_${data.agreementId}.txt`;
            document.body.appendChild(a);
            a.click();
            window.URL.revokeObjectURL(url);
            document.body.removeChild(a);
            
            displayResult({
                success: true,
                status: response.status,
                message: 'Уведомление успешно создано и загружено',
                data: null
            }, 'notificationResult');
        } else {
            let errorData = null;
            try {
                errorData = await response.json();
            } catch (e) {
                errorData = await response.text();
            }
            displayResult({
                success: false,
                status: response.status,
                message: 'Ошибка при создании уведомления',
                data: errorData
            }, 'notificationResult');
        }
    } catch (error) {
        displayResult({
            success: false,
            status: 0,
            message: `Сетевая ошибка: ${error.message}`,
            data: null
        }, 'notificationResult');
    }
}
