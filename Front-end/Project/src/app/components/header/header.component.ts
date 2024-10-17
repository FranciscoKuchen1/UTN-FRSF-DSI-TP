import {Component, Input} from '@angular/core';
import {User} from "../../interfaces/user";

@Component({
  selector: 'tp-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {

  @Input() user!: User;

}
