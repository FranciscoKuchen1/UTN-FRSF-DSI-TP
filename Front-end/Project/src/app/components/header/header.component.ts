import {Component, Input} from '@angular/core';
import {User} from "../../interfaces/user";
import {Router} from "@angular/router";

@Component({
  selector: 'tp-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {

  @Input() user!: User;

  constructor(private router: Router) {}


  redirect(url: string): void{
    this.router.navigate([url]);
  }

}
