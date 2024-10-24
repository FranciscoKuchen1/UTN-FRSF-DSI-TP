import { inject } from '@angular/core';
import {ActivatedRouteSnapshot, Router} from '@angular/router';
import {AuthService} from "../services/auth/auth.service";


export const authGuard = (next: ActivatedRouteSnapshot) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const expectedRole = next.data['expectedRol'];
  const currentUserRole = authService.getUserRole();

  if (currentUserRole === expectedRole) {
    return true;
  } else {
    router.navigate(['/']);
    return false;
  }
};
