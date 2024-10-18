import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegistrarReservaPeriodicaComponent } from './registrar-reserva-periodica.component';

describe('RegistrarReservaPeriodicaComponent', () => {
  let component: RegistrarReservaPeriodicaComponent;
  let fixture: ComponentFixture<RegistrarReservaPeriodicaComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RegistrarReservaPeriodicaComponent]
    });
    fixture = TestBed.createComponent(RegistrarReservaPeriodicaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
