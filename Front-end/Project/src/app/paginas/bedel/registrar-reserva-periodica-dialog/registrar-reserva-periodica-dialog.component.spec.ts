import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegistrarReservaPeriodicaDialogComponent } from './registrar-reserva-periodica-dialog.component';

describe('RegistrarReservaPeriodicaDialogComponent', () => {
  let component: RegistrarReservaPeriodicaDialogComponent;
  let fixture: ComponentFixture<RegistrarReservaPeriodicaDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RegistrarReservaPeriodicaDialogComponent]
    });
    fixture = TestBed.createComponent(RegistrarReservaPeriodicaDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
