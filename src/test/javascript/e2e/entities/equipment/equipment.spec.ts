/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { EquipmentComponentsPage, EquipmentDeleteDialog, EquipmentUpdatePage } from './equipment.page-object';

const expect = chai.expect;

describe('Equipment e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let equipmentUpdatePage: EquipmentUpdatePage;
    let equipmentComponentsPage: EquipmentComponentsPage;
    let equipmentDeleteDialog: EquipmentDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Equipment', async () => {
        await navBarPage.goToEntity('equipment');
        equipmentComponentsPage = new EquipmentComponentsPage();
        await browser.wait(ec.visibilityOf(equipmentComponentsPage.title), 5000);
        expect(await equipmentComponentsPage.getTitle()).to.eq('Equipment');
    });

    it('should load create Equipment page', async () => {
        await equipmentComponentsPage.clickOnCreateButton();
        equipmentUpdatePage = new EquipmentUpdatePage();
        expect(await equipmentUpdatePage.getPageTitle()).to.eq('Create or edit a Equipment');
        await equipmentUpdatePage.cancel();
    });

    it('should create and save Equipment', async () => {
        const nbButtonsBeforeCreate = await equipmentComponentsPage.countDeleteButtons();

        await equipmentComponentsPage.clickOnCreateButton();
        await promise.all([equipmentUpdatePage.setNameInput('name'), equipmentUpdatePage.setPriceInput('5')]);
        expect(await equipmentUpdatePage.getNameInput()).to.eq('name');
        expect(await equipmentUpdatePage.getPriceInput()).to.eq('5');
        await equipmentUpdatePage.save();
        expect(await equipmentUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await equipmentComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last Equipment', async () => {
        const nbButtonsBeforeDelete = await equipmentComponentsPage.countDeleteButtons();
        await equipmentComponentsPage.clickOnLastDeleteButton();

        equipmentDeleteDialog = new EquipmentDeleteDialog();
        expect(await equipmentDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Equipment?');
        await equipmentDeleteDialog.clickOnConfirmButton();

        expect(await equipmentComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
