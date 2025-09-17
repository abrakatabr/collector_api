async function getDocuments() {
    const data = getFormData('getDocumentsForm');
    const result = await apiCall(`${API_BASE}/debtors/${data.debtorId}/document`);
    displayResult(result, 'documentResult');
}

async function uploadDocument() {
    const form = document.getElementById('uploadDocumentForm');
    const formData = new FormData(form);
    const debtorId = formData.get('debtorId');
    const type = formData.get('type');
    formData.delete('type');
    
    try {
        const response = await fetch(`${API_BASE}/debtors/${formData.get('debtorId')}/document/file?type=${type}`, {
            method: 'POST',
            body: formData
        });

        if (response.ok) {
            const result = await response.text();
            displayResult({
                success: true,
                status: response.status,
                message: 'Файл успешно загружен',
                data: { path: result }
            }, 'documentResult');
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
                message: 'Ошибка при загрузке файла',
                data: errorData
            }, 'documentResult');
        }
    } catch (error) {
        displayResult({
            success: false,
            status: 0,
            message: `Сетевая ошибка: ${error.message}`,
            data: null
        }, 'documentResult');
    }
}

async function downloadDocument() {
    const data = getFormData('downloadDocumentForm');
    
    try {
        const response = await fetch(`${API_BASE}/debtors/${data.debtorId}/document/file?type=${data.type}`);
        
        if (response.ok) {
            const blob = await response.blob();
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;

            const contentDisposition = response.headers.get('Content-Disposition');
            let filename = `document_${data.debtorId}_${data.type}`;
            if (contentDisposition) {
                const filenameMatch = contentDisposition.match(/filename="(.+)"/);
                if (filenameMatch) {
                    filename = filenameMatch[1];
                }
            }
            
            a.download = filename;
            document.body.appendChild(a);
            a.click();
            window.URL.revokeObjectURL(url);
            document.body.removeChild(a);
            
            displayResult({
                success: true,
                status: response.status,
                message: 'Файл успешно скачан',
                data: null
            }, 'documentResult');
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
                message: 'Ошибка при скачивании файла',
                data: errorData
            }, 'documentResult');
        }
    } catch (error) {
        displayResult({
            success: false,
            status: 0,
            message: `Сетевая ошибка: ${error.message}`,
            data: null
        }, 'documentResult');
    }
}
