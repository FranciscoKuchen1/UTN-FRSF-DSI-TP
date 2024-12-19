import {Component, EventEmitter, Output} from '@angular/core';
import {User} from "../../interfaces/user";
import {NameId} from "../../interfaces/name-id";

@Component({
  selector: 'tp-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent{

  @Output() loginResult = new EventEmitter<User>();

  user: User;
  types: NameId[] = [{id: 1, name: 'administrador'}, {id: 2, name: 'bedel'}];

  username: string;
  pass: string;
  rol: NameId;

  submit(): void{
    if(this.rol.id === 1){
      this.user = {user: this.username, pass: this.pass, rol: this.rol , nombre: 'Alberto', apellido: 'Garcia'};
    }else{
      this.user = {user: this.username, pass: this.pass, rol: this.rol , nombre: 'Clara', apellido: 'Fernandez'};
    }

    this.loginResult.emit(this.user);
  }
}