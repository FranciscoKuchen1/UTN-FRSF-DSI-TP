import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegistrarEditarBedelComponent } from './registrar-editar-bedel.component';

describe('RegistrarBedelComponent', () => {
  let component: RegistrarEditarBedelComponent;
  let fixture: ComponentFixture<RegistrarEditarBedelComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RegistrarEditarBedelComponent]
    });
    fixture = TestBed.createComponent(RegistrarEditarBedelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
