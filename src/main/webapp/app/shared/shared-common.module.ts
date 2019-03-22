import { NgModule } from '@angular/core';

import { SofplSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';
import { JhiImageUploadComponent } from './image-upload/image-upload.component';

@NgModule({
    imports: [SofplSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent, JhiImageUploadComponent],
    exports: [SofplSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent, JhiImageUploadComponent]
})
export class SofplSharedCommonModule {}
