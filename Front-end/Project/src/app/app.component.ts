import { Component } from '@angular/core';
import {User} from "./interfaces/user";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  logged: boolean = false;
  user: User;

  isLogin(data: User): void {
    if(data){
      this.logged = true;
      this.user = data;
    }else{
      this.logged = false;
    }
  }
}
