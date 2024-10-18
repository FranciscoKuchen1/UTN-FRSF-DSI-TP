import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListadoReservasDiaComponent } from './listado-reservas-dia.component';

describe('ListadoReservasDiaComponent', () => {
  let component: ListadoReservasDiaComponent;
  let fixture: ComponentFixture<ListadoReservasDiaComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ListadoReservasDiaComponent]
    });
    fixture = TestBed.createComponent(ListadoReservasDiaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
