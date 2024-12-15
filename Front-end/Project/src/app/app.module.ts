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
import { RegistrarEditarBedelComponent } from './paginas/administrador/registrar-editar-bedel/registrar-editar-bedel.component';
import { RegistrarReservaPeriodicaComponent } from './paginas/bedel/registrar-reserva-periodica/registrar-reserva-periodica.component';
import { RegistrarReservaEsporadicaComponent } from './paginas/bedel/registrar-reserva-esporadica/registrar-reserva-esporadica.component';
import { BuscarAulaComponent } from './paginas/bedel/buscar-aula/buscar-aula.component';
import { ListadoReservasDiaComponent } from './paginas/bedel/listado-reservas-dia/listado-reservas-dia.component';
import { ListadoReservasCursoComponent } from './paginas/bedel/listado-reservas-curso/listado-reservas-curso.component';
import {MatCardModule} from "@angular/material/card";
import {MatInputModule} from "@angular/material/input";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatSelectModule} from "@angular/material/select";
import { AlertComponent } from './components/alert/alert.component';
import {MatDialogModule} from "@angular/material/dialog";
import {ReplaceLineBreaksPipe} from "./pipes/replaceLineBreaks.pipe";
import {MatListModule} from "@angular/material/list";
import { LoginComponent } from './components/login/login.component';
import {MatRadioModule} from "@angular/material/radio";
import { HttpClientModule } from '@angular/common/http';
import {MatSnackBarModule} from "@angular/material/snack-bar";
import {MatTableModule} from "@angular/material/table";
import {MatPaginatorModule} from "@angular/material/paginator";
import {MatSortModule} from "@angular/material/sort";
import {NgxMaskDirective, NgxMaskPipe, provideNgxMask} from "ngx-mask";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatNativeDateModule} from "@angular/material/core";
import {MatStepperModule} from "@angular/material/stepper";
import {MatCheckboxModule} from "@angular/material/checkbox";
import { RegistrarReservaDialogComponent } from './paginas/bedel/registrar-reserva-dialog/registrar-reserva-dialog.component';
import {MatExpansionModule} from "@angular/material/expansion";
import {MatTreeModule} from "@angular/material/tree";


@NgModule({
  declarations: [
    AppComponent,
    FooterComponent,
    HeaderComponent,
    HomeComponent,
    BuscarBedelComponent,
    RegistrarEditarBedelComponent,
    RegistrarReservaPeriodicaComponent,
    RegistrarReservaEsporadicaComponent,
    BuscarAulaComponent,
    ListadoReservasDiaComponent,
    ListadoReservasCursoComponent,
    AlertComponent,
    ReplaceLineBreaksPipe,
    LoginComponent,
    RegistrarReservaDialogComponent
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
    MatListModule,
    MatRadioModule,
    FormsModule,
    HttpClientModule,
    MatSnackBarModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    NgxMaskDirective,
    NgxMaskPipe,
    MatDatepickerModule,
    MatNativeDateModule,
    MatStepperModule,
    MatCheckboxModule,
    MatExpansionModule,
    MatTreeModule,
  ],
  providers: [provideNgxMask()],
  bootstrap: [AppComponent]
})
export class AppModule { }
