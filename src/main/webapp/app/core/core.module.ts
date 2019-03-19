import { NgModule, LOCALE_ID } from '@angular/core';
import { DatePipe, registerLocaleData } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { Title } from '@angular/platform-browser';
import locale from '@angular/common/locales/en';
import { MatButtonModule, MatCheckboxModule } from '@angular/material';

@NgModule({
    imports: [HttpClientModule, MatButtonModule, MatCheckboxModule],
    exports: [MatButtonModule, MatCheckboxModule],
    declarations: [],
    providers: [
        Title,
        {
            provide: LOCALE_ID,
            useValue: 'en'
        },
        DatePipe
    ]
})
export class SofplCoreModule {
    constructor() {
        registerLocaleData(locale);
    }
}
