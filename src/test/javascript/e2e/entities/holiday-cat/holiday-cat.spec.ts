/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { HolidayCatComponentsPage, HolidayCatDeleteDialog, HolidayCatUpdatePage } from './holiday-cat.page-object';

const expect = chai.expect;

describe('HolidayCat e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let holidayCatUpdatePage: HolidayCatUpdatePage;
    let holidayCatComponentsPage: HolidayCatComponentsPage;
    let holidayCatDeleteDialog: HolidayCatDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load HolidayCats', async () => {
        await navBarPage.goToEntity('holiday-cat');
        holidayCatComponentsPage = new HolidayCatComponentsPage();
        await browser.wait(ec.visibilityOf(holidayCatComponentsPage.title), 5000);
        expect(await holidayCatComponentsPage.getTitle()).to.eq('Holiday Cats');
    });

    it('should load create HolidayCat page', async () => {
        await holidayCatComponentsPage.clickOnCreateButton();
        holidayCatUpdatePage = new HolidayCatUpdatePage();
        expect(await holidayCatUpdatePage.getPageTitle()).to.eq('Create or edit a Holiday Cat');
        await holidayCatUpdatePage.cancel();
    });

    it('should create and save HolidayCats', async () => {
        const nbButtonsBeforeCreate = await holidayCatComponentsPage.countDeleteButtons();

        await holidayCatComponentsPage.clickOnCreateButton();
        await promise.all([holidayCatUpdatePage.setNameInput('name'), holidayCatUpdatePage.setDescriptionInput('description')]);
        expect(await holidayCatUpdatePage.getNameInput()).to.eq('name');
        expect(await holidayCatUpdatePage.getDescriptionInput()).to.eq('description');
        await holidayCatUpdatePage.save();
        expect(await holidayCatUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await holidayCatComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last HolidayCat', async () => {
        const nbButtonsBeforeDelete = await holidayCatComponentsPage.countDeleteButtons();
        await holidayCatComponentsPage.clickOnLastDeleteButton();

        holidayCatDeleteDialog = new HolidayCatDeleteDialog();
        expect(await holidayCatDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Holiday Cat?');
        await holidayCatDeleteDialog.clickOnConfirmButton();

        expect(await holidayCatComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
