import {Injectable} from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  getUserRole(): string {
    const storedValue = sessionStorage.getItem('user');
    const userValue = storedValue ? JSON.parse(storedValue) : null;
    return userValue.rol.id;
  }
}
