import { Component } from '@angular/core';
import { JhiImageUploadService } from './image-upload.service';

class JhiImageSnippet {
    pending = false;
    status = 'init';

    constructor(public src: string, public file: File) {}
}

@Component({
    selector: 'jhi-image-upload',
    templateUrl: './image-upload.component.html',
    styleUrls: ['image-upload.component.scss']
})
export class JhiImageUploadComponent {
    selectedFile: JhiImageSnippet;

    constructor(private jhiImageUploadService: JhiImageUploadService) {}

    private onSuccess() {
        this.selectedFile.pending = false;
        this.selectedFile.status = 'ok';
    }

    private onError() {
        this.selectedFile.pending = false;
        this.selectedFile.status = 'fail';
        this.selectedFile.src = '';
    }

    processFile(imageInput: any) {
        const file: File = imageInput.files[0];
        const reader = new FileReader();

        reader.addEventListener('load', (event: any) => {
            this.selectedFile = new JhiImageSnippet(event.target.result, file);

            this.selectedFile.pending = true;
            this.jhiImageUploadService.uploadImage(this.selectedFile.file).subscribe(
                res => {
                    console.log(res);
                    this.onSuccess();
                },
                err => {
                    console.log(err);
                    this.onError();
                }
            );
        });

        reader.readAsDataURL(file);
    }
}
