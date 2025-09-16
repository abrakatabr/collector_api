async function getAddresses() {
    const data = getFormData('getAddressesForm');
    const result = await apiCall(`${API_BASE}/debtors/${data.debtorId}/address`);
    displayResult(result, 'addressResult');
}

async function updateAddress() {
    const data = getFormData('updateAddressForm');
    const debtorId = data.debtorId;
    delete data.debtorId;

    const result = await apiCall(`${API_BASE}/debtors/${debtorId}/address`, {
        method: 'PUT',
        body: JSON.stringify(data)
    });

    displayResult(result, 'addressResult');
}

async function deleteAddress() {
    const data = getFormData('deleteAddressForm');
    
    try {
        const response = await fetch(`${API_BASE}/debtors/${data.debtorId}/address?status=${data.status}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            displayResult({
                success: true,
                status: response.status,
                message: 'Адрес успешно удален',
                data: null
            }, 'addressResult');
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
                message: 'Ошибка при удалении адреса',
                data: errorData
            }, 'addressResult');
        }
    } catch (error) {
        displayResult({
            success: false,
            status: 0,
            message: `Сетевая ошибка: ${error.message}`,
            data: null
        }, 'addressResult');
    }
}
