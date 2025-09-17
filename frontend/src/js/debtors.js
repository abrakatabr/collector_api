async function getDebtor() {
    const data = getFormData('getDebtorForm');
    const result = await apiCall(`${API_BASE}/debtors/${data.debtorId}`);
    displayResult(result, 'debtorResult');
}

async function updateDebtor() {
    const data = getFormData('updateDebtorForm');
    const debtorId = data.debtorId;
    delete data.debtorId;

    const updateData = {};
    for (const [key, value] of Object.entries(data)) {
        if (value && value.trim() !== '') {
            updateData[key] = value;
        }
    }

    const result = await apiCall(`${API_BASE}/debtors/${debtorId}`, {
        method: 'PATCH',
        body: JSON.stringify(updateData)
    });

    displayResult(result, 'debtorResult');
}


async function removeDebtorFromAgreement() {
    const data = getFormData('removeDebtorFromAgreementForm');
    
    try {
        const response = await fetch(`${API_BASE}/debtors/${data.debtorId}/${data.agreementId}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            displayResult({
                success: true,
                status: response.status,
                message: 'Должник успешно удален из договора',
                data: null
            }, 'debtorResult');
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
                message: 'Ошибка при удалении должника из договора',
                data: errorData
            }, 'debtorResult');
        }
    } catch (error) {
        displayResult({
            success: false,
            status: 0,
            message: `Сетевая ошибка: ${error.message}`,
            data: null
        }, 'debtorResult');
    }
}
