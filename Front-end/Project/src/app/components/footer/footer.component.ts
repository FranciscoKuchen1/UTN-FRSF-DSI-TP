import { Component } from '@angular/core';

@Component({
  selector: 'tp-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent {

  dateYear = new Date().getFullYear();
}