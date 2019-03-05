import { element, by, ElementFinder } from 'protractor';

export class HolidayComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-holiday div table .btn-danger'));
    title = element.all(by.css('jhi-holiday div h2#page-heading span')).first();

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

export class HolidayUpdatePage {
    pageTitle = element(by.id('jhi-holiday-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    yearInput = element(by.id('field_year'));
    typeSelect = element(by.id('field_type'));
    startDateInput = element(by.id('field_startDate'));
    endDateInput = element(by.id('field_endDate'));
    holidayCatSelect = element(by.id('field_holidayCat'));

    async getPageTitle() {
        return this.pageTitle.getText();
    }

    async setYearInput(year) {
        await this.yearInput.sendKeys(year);
    }

    async getYearInput() {
        return this.yearInput.getAttribute('value');
    }

    async setTypeSelect(type) {
        await this.typeSelect.sendKeys(type);
    }

    async getTypeSelect() {
        return this.typeSelect.element(by.css('option:checked')).getText();
    }

    async typeSelectLastOption() {
        await this.typeSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async setStartDateInput(startDate) {
        await this.startDateInput.sendKeys(startDate);
    }

    async getStartDateInput() {
        return this.startDateInput.getAttribute('value');
    }

    async setEndDateInput(endDate) {
        await this.endDateInput.sendKeys(endDate);
    }

    async getEndDateInput() {
        return this.endDateInput.getAttribute('value');
    }

    async holidayCatSelectLastOption() {
        await this.holidayCatSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async holidayCatSelectOption(option) {
        await this.holidayCatSelect.sendKeys(option);
    }

    getHolidayCatSelect(): ElementFinder {
        return this.holidayCatSelect;
    }

    async getHolidayCatSelectedOption() {
        return this.holidayCatSelect.element(by.css('option:checked')).getText();
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

export class HolidayDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-holiday-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-holiday'));

    async getDialogTitle() {
        return this.dialogTitle.getText();
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
