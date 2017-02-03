import {Component, Input, Inject} from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import {WindowWrapper} from "../window";

@Component({
    selector: 'ngbd-modal-content',
    template: `
    <div class="modal-header">
      <h4 class="modal-title" class="text-danger">An Unexpected Error Has Occurred</h4>
    </div>
    <div class="modal-body">
      <p [innerHTML]="message"></p>
    </div>
    <div class="modal-footer">
      Please refresh the page to try again <button type="button" class="btn btn-secondary" (click)="ok()">OK</button>     
    </div>
  `
})
export class ErrorModalComponent {
    @Input() name: string;

    constructor(public activeModal: NgbActiveModal, private window: WindowWrapper) {}

    ok() {
        window.location.reload();
    }
}