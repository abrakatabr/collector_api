let debtorCount = 1;
let addressCounts = {0: 1};
let documentCounts = {0: 1};

function addDebtor() {
    const debtorIndex = debtorCount;
    debtorCount++;
    addressCounts[debtorIndex] = 1;
    documentCounts[debtorIndex] = 1;

    const debtorForm = document.createElement('div');
    debtorForm.className = 'debtor-form';
    debtorForm.id = `debtor-${debtorIndex}`;
    debtorForm.innerHTML = `
        <div class="section-title">Заемщик ${debtorCount}</div>
        <button type="button" onclick="removeDebtor(${debtorIndex})" style="float: right; background: #dc3545;">× Удалить</button>
        <div class="form-group">
            <label>Имя:</label>
            <input type="text" name="debtors[${debtorIndex}].firstname" required>
        </div>
        <div class="form-group">
            <label>Фамилия:</label>
            <input type="text" name="debtors[${debtorIndex}].lastname" required>
        </div>
        <div class="form-group">
            <label>Отчество:</label>
            <input type="text" name="debtors[${debtorIndex}].patronymic">
        </div>
        <div class="form-group">
            <label>Дата рождения:</label>
            <input type="date" name="debtors[${debtorIndex}].birthday" required>
        </div>
        <div class="form-group">
            <label>Пол:</label>
            <select name="debtors[${debtorIndex}].gender" required>
                <option value="male">Мужской</option>
                <option value="female">Женский</option>
            </select>
        </div>
        <div class="form-group">
            <label>Роль:</label>
            <select name="debtors[${debtorIndex}].role" required>
                <option value="co-debtor">Созаемщик</option>
                <option value="single debtor">Единственный заемщик</option>
                <option value="guarantor">Поручитель</option>
                <option value="charger">Залогодатель</option>
            </select>
        </div>
        <div class="form-group">
            <label>Телефон:</label>
            <input type="tel" name="debtors[${debtorIndex}].phoneNumber">
        </div>

        <!-- Адреса заемщика -->
        <div class="address-section">
            <h4>Адреса <button type="button" onclick="addAddress(${debtorIndex})" style="background: #28a745; padding: 2px 8px;">+ Добавить адрес</button></h4>
            <div id="addresses-${debtorIndex}">
                <div class="address-form" id="address-${debtorIndex}-0">
                    <button type="button" onclick="removeAddress(${debtorIndex}, 0)" style="float: right; background: #dc3545; padding: 2px 8px;">×</button>
                    <div class="form-group">
                        <label>Тип адреса:</label>
                        <select name="debtors[${debtorIndex}].addressDTOs[0].addressStatus" required>
                            <option value="registration">Прописка</option>
                            <option value="residential">Проживание</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Страна:</label>
                        <input type="text" name="debtors[${debtorIndex}].addressDTOs[0].country" required>
                    </div>
                    <div class="form-group">
                        <label>Город:</label>
                        <input type="text" name="debtors[${debtorIndex}].addressDTOs[0].city" required>
                    </div>
                    <div class="form-group">
                        <label>Улица:</label>
                        <input type="text" name="debtors[${debtorIndex}].addressDTOs[0].street" required>
                    </div>
                    <div class="form-group">
                        <label>Дом:</label>
                        <input type="text" name="debtors[${debtorIndex}].addressDTOs[0].house" required>
                    </div>
                    <div class="form-group">
                        <label>Квартира:</label>
                        <input type="text" name="debtors[${debtorIndex}].addressDTOs[0].apartment">
                    </div>
                </div>
            </div>
        </div>

        <!-- Документы заемщика -->
        <div class="document-section">
            <h4>Документы <button type="button" onclick="addDocument(${debtorIndex})" style="background: #28a745; padding: 2px 8px;">+ Добавить документ</button></h4>
            <div id="documents-${debtorIndex}">
                <div class="document-form" id="document-${debtorIndex}-0">
                    <button type="button" onclick="removeDocument(${debtorIndex}, 0)" style="float: right; background: #dc3545; padding: 2px 8px;">×</button>
                    <div class="form-group">
                        <label>Тип документа:</label>
                        <select name="debtors[${debtorIndex}].documentDTOs[0].documentType" required>
                            <option value="NATIONAL_PASSPORT">Паспорт РФ</option>
                            <option value="INTERNATIONAL_PASSPORT">Заграничный паспорт</option>
                            <option value="DRIVER_LICENSE">Водительское удостоверение</option>
                            <option value="INN">ИНН</option>
                            <option value="SNILS">СНИЛС</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Номер документа:</label>
                        <input type="text" name="debtors[${debtorIndex}].documentDTOs[0].documentNumber" required>
                    </div>
                    <div class="form-group">
                        <label>Дата выдачи:</label>
                        <input type="date" name="debtors[${debtorIndex}].documentDTOs[0].issueDate" required>
                    </div>
                </div>
            </div>
        </div>
    `;
    document.getElementById('debtorsContainer').appendChild(debtorForm);
}

function removeDebtor(debtorIndex) {
    const debtorElement = document.getElementById(`debtor-${debtorIndex}`);
    if (debtorElement) {
        debtorElement.remove();
        delete addressCounts[debtorIndex];
        delete documentCounts[debtorIndex];
    }
}

function addAddress(debtorIndex) {
    if (!addressCounts[debtorIndex]) addressCounts[debtorIndex] = 0;
    const addressIndex = addressCounts[debtorIndex]++;

    const addressForm = document.createElement('div');
    addressForm.className = 'address-form';
    addressForm.id = `address-${debtorIndex}-${addressIndex}`;
    addressForm.innerHTML = `
        <button type="button" onclick="removeAddress(${debtorIndex}, ${addressIndex})" style="float: right; background: #dc3545; padding: 2px 8px;">×</button>
        <div class="form-group">
            <label>Тип адреса:</label>
            <select name="debtors[${debtorIndex}].addressDTOs[${addressIndex}].addressStatus" required>
                <option value="registration">Прописка</option>
                <option value="residential">Проживание</option>
            </select>
        </div>
        <div class="form-group">
            <label>Страна:</label>
            <input type="text" name="debtors[${debtorIndex}].addressDTOs[${addressIndex}].country" required>
        </div>
        <div class="form-group">
            <label>Город:</label>
            <input type="text" name="debtors[${debtorIndex}].addressDTOs[${addressIndex}].city" required>
        </div>
        <div class="form-group">
            <label>Улица:</label>
            <input type="text" name="debtors[${debtorIndex}].addressDTOs[${addressIndex}].street" required>
        </div>
        <div class="form-group">
            <label>Дом:</label>
            <input type="text" name="debtors[${debtorIndex}].addressDTOs[${addressIndex}].house" required>
        </div>
        <div class="form-group">
            <label>Квартира:</label>
            <input type="text" name="debtors[${debtorIndex}].addressDTOs[${addressIndex}].apartment">
        </div>
    `;
    document.getElementById(`addresses-${debtorIndex}`).appendChild(addressForm);
}

function removeAddress(debtorIndex, addressIndex) {
    const addressElement = document.getElementById(`address-${debtorIndex}-${addressIndex}`);
    if (addressElement) {
        addressElement.remove();
    }
}

function addDocument(debtorIndex) {
    if (!documentCounts[debtorIndex]) documentCounts[debtorIndex] = 0;
    const documentIndex = documentCounts[debtorIndex]++;

    const documentForm = document.createElement('div');
    documentForm.className = 'document-form';
    documentForm.id = `document-${debtorIndex}-${documentIndex}`;
    documentForm.innerHTML = `
        <button type="button" onclick="removeDocument(${debtorIndex}, ${documentIndex})" style="float: right; background: #dc3545; padding: 2px 8px;">×</button>
        <div class="form-group">
            <label>Тип документа:</label>
            <select name="debtors[${debtorIndex}].documentDTOs[${documentIndex}].documentType" required>
                <option value="NATIONAL_PASSPORT">Паспорт РФ</option>
                <option value="INTERNATIONAL_PASSPORT">Заграничный паспорт</option>
                <option value="DRIVER_LICENSE">Водительское удостоверение</option>
                <option value="INN">ИНН</option>
                <option value="SNILS">СНИЛС</option>
            </select>
        </div>
        <div class="form-group">
            <label>Номер документа:</label>
            <input type="text" name="debtors[${debtorIndex}].documentDTOs[${documentIndex}].documentNumber" required>
        </div>
        <div class="form-group">
            <label>Дата выдачи:</label>
            <input type="date" name="debtors[${debtorIndex}].documentDTOs[${documentIndex}].issueDate" required>
        </div>
    `;
    document.getElementById(`documents-${debtorIndex}`).appendChild(documentForm);
}

function removeDocument(debtorIndex, documentIndex) {
    const documentElement = document.getElementById(`document-${debtorIndex}-${documentIndex}`);
    if (documentElement) {
        documentElement.remove();
    }
}

async function createAgreement() {
    const data = getFormData('createAgreementForm');
    const key = data.key;
    delete data.key;

    const requestData = {
        originalDebtSum: parseFloat(data.originalDebtSum),
        actualDebtSum: parseFloat(data.actualDebtSum),
        agreementStartDate: data.agreementStartDate,
        transferor: data.transferor,
        debtors: []
    };

    for (let debtorIndex = 0; debtorIndex < debtorCount; debtorIndex++) {
        const debtorPrefix = `debtors[${debtorIndex}]`;
        if (data[`${debtorPrefix}.firstname`]) {
            const debtor = {
                firstname: data[`${debtorPrefix}.firstname`],
                lastname: data[`${debtorPrefix}.lastname`],
                patronymic: data[`${debtorPrefix}.patronymic`],
                birthday: data[`${debtorPrefix}.birthday`],
                gender: data[`${debtorPrefix}.gender`],
                role: data[`${debtorPrefix}.role`],
                phoneNumber: data[`${debtorPrefix}.phoneNumber`],
                addressDTOs: [],
                documentDTOs: []
            };

            for (let addressIndex = 0; addressIndex < (addressCounts[debtorIndex] || 1); addressIndex++) {
                const addressPrefix = `${debtorPrefix}.addressDTOs[${addressIndex}]`;
                if (data[`${addressPrefix}.country`]) {
                    debtor.addressDTOs.push({
                        addressStatus: data[`${addressPrefix}.addressStatus`],
                        country: data[`${addressPrefix}.country`],
                        city: data[`${addressPrefix}.city`],
                        street: data[`${addressPrefix}.street`],
                        house: data[`${addressPrefix}.house`],
                        apartment: data[`${addressPrefix}.apartment`]
                    });
                }
            }

            for (let documentIndex = 0; documentIndex < (documentCounts[debtorIndex] || 1); documentIndex++) {
                const documentPrefix = `${debtorPrefix}.documentDTOs[${documentIndex}]`;
                if (data[`${documentPrefix}.documentNumber`]) {
                    debtor.documentDTOs.push({
                        documentType: data[`${documentPrefix}.documentType`],
                        documentNumber: data[`${documentPrefix}.documentNumber`],
                        issueDate: data[`${documentPrefix}.issueDate`]
                    });
                }
            }

            requestData.debtors.push(debtor);
        }
    }

    const result = await apiCall(`${API_BASE}/agreements?key=${encodeURIComponent(key)}`, {
        method: 'POST',
        body: JSON.stringify(requestData)
    });

    displayResult(result, 'agreementResult');
}

async function getAgreement() {
    const data = getFormData('getAgreementForm');
    const result = await apiCall(`${API_BASE}/agreements/${data.agreementId}`);
    displayResult(result, 'agreementResult');
}

async function updateAgreementStatus() {
    const data = getFormData('updateStatusForm');
    const result = await apiCall(`${API_BASE}/agreements/${data.agreementId}/status?status=${data.status}`, {
        method: 'PATCH'
    });

    displayResult(result, 'agreementResult');
}

async function deleteAgreement() {
    const data = getFormData('deleteAgreementForm');
    const result = await apiCall(`${API_BASE}/agreements/${data.agreementId}`, {
        method: 'DELETE'
    });

    displayResult(result, 'agreementResult');
}