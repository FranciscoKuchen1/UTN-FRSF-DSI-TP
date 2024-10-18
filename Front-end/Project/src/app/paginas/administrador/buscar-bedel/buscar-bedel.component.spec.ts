import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BuscarBedelComponent } from './buscar-bedel.component';

describe('BuscarBedelComponent', () => {
  let component: BuscarBedelComponent;
  let fixture: ComponentFixture<BuscarBedelComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BuscarBedelComponent]
    });
    fixture = TestBed.createComponent(BuscarBedelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
