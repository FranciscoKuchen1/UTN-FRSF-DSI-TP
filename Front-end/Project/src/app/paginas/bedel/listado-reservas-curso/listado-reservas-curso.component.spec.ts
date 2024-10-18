import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListadoReservasCursoComponent } from './listado-reservas-curso.component';

describe('ListadoReservasCursoComponent', () => {
  let component: ListadoReservasCursoComponent;
  let fixture: ComponentFixture<ListadoReservasCursoComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ListadoReservasCursoComponent]
    });
    fixture = TestBed.createComponent(ListadoReservasCursoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
