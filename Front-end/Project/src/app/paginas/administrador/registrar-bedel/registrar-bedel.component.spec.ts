import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegistrarBedelComponent } from './registrar-bedel.component';

describe('RegistrarBedelComponent', () => {
  let component: RegistrarBedelComponent;
  let fixture: ComponentFixture<RegistrarBedelComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RegistrarBedelComponent]
    });
    fixture = TestBed.createComponent(RegistrarBedelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
