import { element, by, ElementFinder } from 'protractor';

export class PersonalInfoComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-personal-info div table .btn-danger'));
    title = element.all(by.css('jhi-personal-info div h2#page-heading span')).first();

    async clickOnCreateButton() {
        await this.createButton.click();
    }

    async clickOnLastDeleteButton() {
        await this.deleteButtons.last().click();
    }

    async countDeleteButtons() {
        return this.deleteButtons.count();
    }

    async getTitle() {
        return this.title.getText();
    }
}

export class PersonalInfoUpdatePage {
    pageTitle = element(by.id('jhi-personal-info-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    employeeIdInput = element(by.id('field_employeeId'));
    fullNameInput = element(by.id('field_fullName'));
    fathersNameInput = element(by.id('field_fathersName'));
    mothersNameInput = element(by.id('field_mothersName'));
    birthDateInput = element(by.id('field_birthDate'));
    maritalStatusSelect = element(by.id('field_maritalStatus'));
    genderSelect = element(by.id('field_gender'));
    religionSelect = element(by.id('field_religion'));
    permanentAddressInput = element(by.id('field_permanentAddress'));
    presentAddressInput = element(by.id('field_presentAddress'));
    nationalIdInput = element(by.id('field_nationalId'));
    tinNumberInput = element(by.id('field_tinNumber'));
    contactNumberInput = element(by.id('field_contactNumber'));
    emailInput = element(by.id('field_email'));
    bloodGroupInput = element(by.id('field_bloodGroup'));
    emergencyContactInput = element(by.id('field_emergencyContact'));
    salaryInput = element(by.id('field_salary'));
    photoIdInput = element(by.id('field_photoId'));
    bankAccountInput = element(by.id('field_bankAccount'));
    remarksInput = element(by.id('field_remarks'));
    departmentSelect = element(by.id('field_department'));
    designationSelect = element(by.id('field_designation'));

    async getPageTitle() {
        return this.pageTitle.getText();
    }

    async setEmployeeIdInput(employeeId) {
        await this.employeeIdInput.sendKeys(employeeId);
    }

    async getEmployeeIdInput() {
        return this.employeeIdInput.getAttribute('value');
    }

    async setFullNameInput(fullName) {
        await this.fullNameInput.sendKeys(fullName);
    }

    async getFullNameInput() {
        return this.fullNameInput.getAttribute('value');
    }

    async setFathersNameInput(fathersName) {
        await this.fathersNameInput.sendKeys(fathersName);
    }

    async getFathersNameInput() {
        return this.fathersNameInput.getAttribute('value');
    }

    async setMothersNameInput(mothersName) {
        await this.mothersNameInput.sendKeys(mothersName);
    }

    async getMothersNameInput() {
        return this.mothersNameInput.getAttribute('value');
    }

    async setBirthDateInput(birthDate) {
        await this.birthDateInput.sendKeys(birthDate);
    }

    async getBirthDateInput() {
        return this.birthDateInput.getAttribute('value');
    }

    async setMaritalStatusSelect(maritalStatus) {
        await this.maritalStatusSelect.sendKeys(maritalStatus);
    }

    async getMaritalStatusSelect() {
        return this.maritalStatusSelect.element(by.css('option:checked')).getText();
    }

    async maritalStatusSelectLastOption() {
        await this.maritalStatusSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async setGenderSelect(gender) {
        await this.genderSelect.sendKeys(gender);
    }

    async getGenderSelect() {
        return this.genderSelect.element(by.css('option:checked')).getText();
    }

    async genderSelectLastOption() {
        await this.genderSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async setReligionSelect(religion) {
        await this.religionSelect.sendKeys(religion);
    }

    async getReligionSelect() {
        return this.religionSelect.element(by.css('option:checked')).getText();
    }

    async religionSelectLastOption() {
        await this.religionSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async setPermanentAddressInput(permanentAddress) {
        await this.permanentAddressInput.sendKeys(permanentAddress);
    }

    async getPermanentAddressInput() {
        return this.permanentAddressInput.getAttribute('value');
    }

    async setPresentAddressInput(presentAddress) {
        await this.presentAddressInput.sendKeys(presentAddress);
    }

    async getPresentAddressInput() {
        return this.presentAddressInput.getAttribute('value');
    }

    async setNationalIdInput(nationalId) {
        await this.nationalIdInput.sendKeys(nationalId);
    }

    async getNationalIdInput() {
        return this.nationalIdInput.getAttribute('value');
    }

    async setTinNumberInput(tinNumber) {
        await this.tinNumberInput.sendKeys(tinNumber);
    }

    async getTinNumberInput() {
        return this.tinNumberInput.getAttribute('value');
    }

    async setContactNumberInput(contactNumber) {
        await this.contactNumberInput.sendKeys(contactNumber);
    }

    async getContactNumberInput() {
        return this.contactNumberInput.getAttribute('value');
    }

    async setEmailInput(email) {
        await this.emailInput.sendKeys(email);
    }

    async getEmailInput() {
        return this.emailInput.getAttribute('value');
    }

    async setBloodGroupInput(bloodGroup) {
        await this.bloodGroupInput.sendKeys(bloodGroup);
    }

    async getBloodGroupInput() {
        return this.bloodGroupInput.getAttribute('value');
    }

    async setEmergencyContactInput(emergencyContact) {
        await this.emergencyContactInput.sendKeys(emergencyContact);
    }

    async getEmergencyContactInput() {
        return this.emergencyContactInput.getAttribute('value');
    }

    async setSalaryInput(salary) {
        await this.salaryInput.sendKeys(salary);
    }

    async getSalaryInput() {
        return this.salaryInput.getAttribute('value');
    }

    async setPhotoIdInput(photoId) {
        await this.photoIdInput.sendKeys(photoId);
    }

    async getPhotoIdInput() {
        return this.photoIdInput.getAttribute('value');
    }

    async setBankAccountInput(bankAccount) {
        await this.bankAccountInput.sendKeys(bankAccount);
    }

    async getBankAccountInput() {
        return this.bankAccountInput.getAttribute('value');
    }

    async setRemarksInput(remarks) {
        await this.remarksInput.sendKeys(remarks);
    }

    async getRemarksInput() {
        return this.remarksInput.getAttribute('value');
    }

    async departmentSelectLastOption() {
        await this.departmentSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async departmentSelectOption(option) {
        await this.departmentSelect.sendKeys(option);
    }

    getDepartmentSelect(): ElementFinder {
        return this.departmentSelect;
    }

    async getDepartmentSelectedOption() {
        return this.departmentSelect.element(by.css('option:checked')).getText();
    }

    async designationSelectLastOption() {
        await this.designationSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async designationSelectOption(option) {
        await this.designationSelect.sendKeys(option);
    }

    getDesignationSelect(): ElementFinder {
        return this.designationSelect;
    }

    async getDesignationSelectedOption() {
        return this.designationSelect.element(by.css('option:checked')).getText();
    }

    async save() {
        await this.saveButton.click();
    }

    async cancel() {
        await this.cancelButton.click();
    }

    getSaveButton(): ElementFinder {
        return this.saveButton;
    }
}

export class PersonalInfoDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-personalInfo-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-personalInfo'));

    async getDialogTitle() {
        return this.dialogTitle.getText();
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
