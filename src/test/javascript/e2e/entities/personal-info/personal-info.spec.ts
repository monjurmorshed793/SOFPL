/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { PersonalInfoComponentsPage, PersonalInfoDeleteDialog, PersonalInfoUpdatePage } from './personal-info.page-object';

const expect = chai.expect;

describe('PersonalInfo e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let personalInfoUpdatePage: PersonalInfoUpdatePage;
    let personalInfoComponentsPage: PersonalInfoComponentsPage;
    let personalInfoDeleteDialog: PersonalInfoDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load PersonalInfos', async () => {
        await navBarPage.goToEntity('personal-info');
        personalInfoComponentsPage = new PersonalInfoComponentsPage();
        await browser.wait(ec.visibilityOf(personalInfoComponentsPage.title), 5000);
        expect(await personalInfoComponentsPage.getTitle()).to.eq('Personal Infos');
    });

    it('should load create PersonalInfo page', async () => {
        await personalInfoComponentsPage.clickOnCreateButton();
        personalInfoUpdatePage = new PersonalInfoUpdatePage();
        expect(await personalInfoUpdatePage.getPageTitle()).to.eq('Create or edit a Personal Info');
        await personalInfoUpdatePage.cancel();
    });

    it('should create and save PersonalInfos', async () => {
        const nbButtonsBeforeCreate = await personalInfoComponentsPage.countDeleteButtons();

        await personalInfoComponentsPage.clickOnCreateButton();
        await promise.all([
            personalInfoUpdatePage.setEmployeeIdInput('employeeId'),
            personalInfoUpdatePage.setFullNameInput('fullName'),
            personalInfoUpdatePage.setFathersNameInput('fathersName'),
            personalInfoUpdatePage.setMothersNameInput('mothersName'),
            personalInfoUpdatePage.setBirthDateInput('2000-12-31'),
            personalInfoUpdatePage.maritalStatusSelectLastOption(),
            personalInfoUpdatePage.genderSelectLastOption(),
            personalInfoUpdatePage.religionSelectLastOption(),
            personalInfoUpdatePage.setPermanentAddressInput('permanentAddress'),
            personalInfoUpdatePage.setPresentAddressInput('presentAddress'),
            personalInfoUpdatePage.departmentSelectLastOption(),
            personalInfoUpdatePage.designationSelectLastOption()
        ]);
        expect(await personalInfoUpdatePage.getEmployeeIdInput()).to.eq('employeeId');
        expect(await personalInfoUpdatePage.getFullNameInput()).to.eq('fullName');
        expect(await personalInfoUpdatePage.getFathersNameInput()).to.eq('fathersName');
        expect(await personalInfoUpdatePage.getMothersNameInput()).to.eq('mothersName');
        expect(await personalInfoUpdatePage.getBirthDateInput()).to.eq('2000-12-31');
        expect(await personalInfoUpdatePage.getPermanentAddressInput()).to.eq('permanentAddress');
        expect(await personalInfoUpdatePage.getPresentAddressInput()).to.eq('presentAddress');
        await personalInfoUpdatePage.save();
        expect(await personalInfoUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await personalInfoComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last PersonalInfo', async () => {
        const nbButtonsBeforeDelete = await personalInfoComponentsPage.countDeleteButtons();
        await personalInfoComponentsPage.clickOnLastDeleteButton();

        personalInfoDeleteDialog = new PersonalInfoDeleteDialog();
        expect(await personalInfoDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Personal Info?');
        await personalInfoDeleteDialog.clickOnConfirmButton();

        expect(await personalInfoComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
