import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from "./paginas/home/home.component";
import {BuscarBedelComponent} from "./paginas/administrador/buscar-bedel/buscar-bedel.component";
import {RegistrarBedelComponent} from "./paginas/administrador/registrar-bedel/registrar-bedel.component";
import {
  RegistrarReservaPeriodicaComponent
} from "./paginas/bedel/registrar-reserva-periodica/registrar-reserva-periodica.component";
import {
  RegistrarReservaEsporadicaComponent
} from "./paginas/bedel/registrar-reserva-esporadica/registrar-reserva-esporadica.component";
import {BuscarAulaComponent} from "./paginas/bedel/buscar-aula/buscar-aula.component";
import {ListadoReservasDiaComponent} from "./paginas/bedel/listado-reservas-dia/listado-reservas-dia.component";
import {
  ListadoReservasCursoComponent
} from "./paginas/bedel/listado-reservas-curso/listado-reservas-curso.component";
import {authGuard} from "./guards/auth.guard";


const routes: Routes = [
  {path: 'buscar-bedel', component: BuscarBedelComponent, canActivate: [authGuard], data: { expectedRol: 1 }},
  {path: 'registrar-bedel', component: RegistrarBedelComponent, canActivate: [authGuard], data: { expectedRol: 1 }},
  {
    path: 'registrar-reserva-periodica',
    component: RegistrarReservaPeriodicaComponent,
    canActivate: [authGuard],
    data: { expectedRol: 2 }
  },
  {
    path: 'registrar-reserva-esporadica',
    component: RegistrarReservaEsporadicaComponent,
    canActivate: [authGuard],
    data: { expectedRol: 2 }
  },
  {path: 'buscar-aula', component: BuscarAulaComponent, canActivate: [authGuard], data: { expectedRol: 2 }},
  {
    path: 'listado-reserva-dia',
    component: ListadoReservasDiaComponent,
    canActivate: [authGuard],
    data: { expectedRol: 2 }
  },
  {
    path: 'listado-reserva-curso',
    component: ListadoReservasCursoComponent,
    canActivate: [authGuard],
    data: { expectedRol: 2 }
  },
  {path: '', component: HomeComponent},
  {path: '**', redirectTo: '', pathMatch: 'full'},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {

}
