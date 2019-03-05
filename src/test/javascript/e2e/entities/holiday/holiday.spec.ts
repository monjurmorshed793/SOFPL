/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { HolidayComponentsPage, HolidayDeleteDialog, HolidayUpdatePage } from './holiday.page-object';

const expect = chai.expect;

describe('Holiday e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let holidayUpdatePage: HolidayUpdatePage;
    let holidayComponentsPage: HolidayComponentsPage;
    let holidayDeleteDialog: HolidayDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Holidays', async () => {
        await navBarPage.goToEntity('holiday');
        holidayComponentsPage = new HolidayComponentsPage();
        await browser.wait(ec.visibilityOf(holidayComponentsPage.title), 5000);
        expect(await holidayComponentsPage.getTitle()).to.eq('Holidays');
    });

    it('should load create Holiday page', async () => {
        await holidayComponentsPage.clickOnCreateButton();
        holidayUpdatePage = new HolidayUpdatePage();
        expect(await holidayUpdatePage.getPageTitle()).to.eq('Create or edit a Holiday');
        await holidayUpdatePage.cancel();
    });

    it('should create and save Holidays', async () => {
        const nbButtonsBeforeCreate = await holidayComponentsPage.countDeleteButtons();

        await holidayComponentsPage.clickOnCreateButton();
        await promise.all([
            holidayUpdatePage.setYearInput('5'),
            holidayUpdatePage.typeSelectLastOption(),
            holidayUpdatePage.setStartDateInput('2000-12-31'),
            holidayUpdatePage.setEndDateInput('2000-12-31'),
            holidayUpdatePage.holidayCatSelectLastOption()
        ]);
        expect(await holidayUpdatePage.getYearInput()).to.eq('5');
        expect(await holidayUpdatePage.getStartDateInput()).to.eq('2000-12-31');
        expect(await holidayUpdatePage.getEndDateInput()).to.eq('2000-12-31');
        await holidayUpdatePage.save();
        expect(await holidayUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await holidayComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last Holiday', async () => {
        const nbButtonsBeforeDelete = await holidayComponentsPage.countDeleteButtons();
        await holidayComponentsPage.clickOnLastDeleteButton();

        holidayDeleteDialog = new HolidayDeleteDialog();
        expect(await holidayDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Holiday?');
        await holidayDeleteDialog.clickOnConfirmButton();

        expect(await holidayComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
