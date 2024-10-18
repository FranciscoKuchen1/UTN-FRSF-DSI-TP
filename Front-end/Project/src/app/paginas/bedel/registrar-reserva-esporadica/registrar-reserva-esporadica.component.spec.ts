import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegistrarReservaEsporadicaComponent } from './registrar-reserva-esporadica.component';

describe('RegistrarReservaEsporadicaComponent', () => {
  let component: RegistrarReservaEsporadicaComponent;
  let fixture: ComponentFixture<RegistrarReservaEsporadicaComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RegistrarReservaEsporadicaComponent]
    });
    fixture = TestBed.createComponent(RegistrarReservaEsporadicaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
