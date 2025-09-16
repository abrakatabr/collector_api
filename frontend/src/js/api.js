const API_BASE = '/api';

function getFormData(formId) {
    const form = document.getElementById(formId);
    const formData = new FormData(form);
    const data = {};

    for (let [key, value] of formData.entries()) {
        data[key] = value;
    }

    return data;
}

function displayResult(result, elementId) {
    const resultElement = document.getElementById(elementId);

    if (result.success) {
        resultElement.className = 'result success';
    } else {
        resultElement.className = 'result error';
    }

    let dataHtml = '';
    if (result.data !== null && result.data !== undefined) {
        if (typeof result.data === 'string') {
            dataHtml = `<strong>Данные:</strong><div class="json-response">${result.data}</div>`;
        } else {
            dataHtml = `<strong>Данные:</strong><div class="json-response">${JSON.stringify(result.data, null, 2)}</div>`;
        }
    }

    resultElement.innerHTML = `
        <strong>Статус:</strong> ${result.status}<br>
        <strong>Сообщение:</strong> ${result.message}<br>
        ${dataHtml}
    `;
}

async function apiCall(url, options = {}) {
    try {
        const defaultOptions = {
            headers: {
                'Content-Type': 'application/json',
            },
        };

        const mergedOptions = {
            ...defaultOptions,
            ...options,
            headers: {
                ...defaultOptions.headers,
                ...options.headers,
            },
        };

        const response = await fetch(url, mergedOptions);

        let data = null;
        const contentType = response.headers.get('content-type');
        
        if (response.status !== 204 && contentType && contentType.includes('application/json')) {
            try {
                data = await response.json();
            } catch (jsonError) {
                data = await response.text();
            }
        } else if (response.status !== 204) {
            try {
                data = await response.text();
            } catch (textError) {
                data = null;
            }
        }

        return {
            success: response.ok,
            status: response.status,
            message: response.ok ? 'Успешно' : 'Ошибка',
            data: data
        };
    } catch (error) {
        return {
            success: false,
            status: 0,
            message: `Сетевая ошибка: ${error.message}`,
            data: null
        };
    }
}