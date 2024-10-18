import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FooterComponent } from './components/footer/footer.component';
import {HeaderComponent} from "./components/header/header.component";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {MatMenuModule} from "@angular/material/menu";
import {MatToolbarModule} from "@angular/material/toolbar";
import { HomeComponent } from './paginas/home/home.component';
import { BuscarBedelComponent } from './paginas/administrador/buscar-bedel/buscar-bedel.component';
import { RegistrarBedelComponent } from './paginas/administrador/registrar-bedel/registrar-bedel.component';
import { RegistrarReservaPeriodicaComponent } from './paginas/bedel/registrar-reserva-periodica/registrar-reserva-periodica.component';
import { RegistrarReservaEsporadicaComponent } from './paginas/bedel/registrar-reserva-esporadica/registrar-reserva-esporadica.component';
import { BuscarAulaComponent } from './paginas/bedel/buscar-aula/buscar-aula.component';
import { ListadoReservasDiaComponent } from './paginas/bedel/listado-reservas-dia/listado-reservas-dia.component';
import { ListadoReservasCursoComponent } from './paginas/bedel/listado-reservas-curso/listado-reservas-curso.component';
import {MatCardModule} from "@angular/material/card";
import {MatInputModule} from "@angular/material/input";
import {ReactiveFormsModule} from "@angular/forms";
import {MatSelectModule} from "@angular/material/select";
import { AlertComponent } from './components/alert/alert.component';
import {MatDialogModule} from "@angular/material/dialog";
import {ReplaceLineBreaksPipe} from "./pipes/replaceLineBreaks.pipe";
import {MatListModule} from "@angular/material/list";


@NgModule({
  declarations: [
    AppComponent,
    FooterComponent,
    HeaderComponent,
    HomeComponent,
    BuscarBedelComponent,
    RegistrarBedelComponent,
    RegistrarReservaPeriodicaComponent,
    RegistrarReservaEsporadicaComponent,
    BuscarAulaComponent,
    ListadoReservasDiaComponent,
    ListadoReservasCursoComponent,
    AlertComponent,
    ReplaceLineBreaksPipe
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatIconModule,
    MatButtonModule,
    MatMenuModule,
    MatToolbarModule,
    MatCardModule,
    MatInputModule,
    ReactiveFormsModule,
    MatSelectModule,
    MatDialogModule,
    MatListModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
