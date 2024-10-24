import {Component, OnInit} from '@angular/core';
import {User} from "./interfaces/user";
import {Router} from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit{

  logged: boolean | null;
  user: User | null;

  constructor(private router: Router) {}

  ngOnInit() : void{
    this.logged = this.getLogged();
    this.user = this.getUser();
  }

  login(data: User): void {
    if(data){
      this.setLogged(true);
      this.setUser(data);

      this.logged = true;
      this.user = data;
    }else{
      this.logout()
    }
  }

  logout(): void{
    this.redirect('/');
    this.setLogged(false);
    this.setUser({});

    this.user = null;
    this.logged = false;
  }

  setLogged(value: boolean) {
    sessionStorage.setItem('logged',  JSON.stringify(value));
  }

  getLogged(): boolean {
    const storedValue = sessionStorage.getItem('logged');
    return storedValue === 'true';
  }

  setUser(value: User) {
    sessionStorage.setItem('user', JSON.stringify(value));
  }

  getUser(): User | null {
    const storedValue = sessionStorage.getItem('user');
    return storedValue ? JSON.parse(storedValue) : null;
  }

  redirect(url: string): void{
    this.router.navigate([url]);
  }
}
