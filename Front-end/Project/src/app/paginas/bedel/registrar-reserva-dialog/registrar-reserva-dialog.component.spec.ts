import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegistrarReservaDialogComponent } from './registrar-reserva-dialog.component';

describe('RegistrarReservaDialogComponent', () => {
  let component: RegistrarReservaDialogComponent;
  let fixture: ComponentFixture<RegistrarReservaDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RegistrarReservaDialogComponent]
    });
    fixture = TestBed.createComponent(RegistrarReservaDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
