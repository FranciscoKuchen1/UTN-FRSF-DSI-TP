import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegistrarReservaEsporadicaDialogComponent } from './registrar-reserva-esporadica-dialog.component';

describe('RegistrarReservaDialogComponent', () => {
  let component: RegistrarReservaEsporadicaDialogComponent;
  let fixture: ComponentFixture<RegistrarReservaEsporadicaDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RegistrarReservaEsporadicaDialogComponent]
    });
    fixture = TestBed.createComponent(RegistrarReservaEsporadicaDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
