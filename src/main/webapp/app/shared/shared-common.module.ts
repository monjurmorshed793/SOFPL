import { NgModule } from '@angular/core';

import { SofplSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [SofplSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [SofplSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class SofplSharedCommonModule {}
