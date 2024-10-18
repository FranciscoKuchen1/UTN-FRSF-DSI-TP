import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BuscarAulaComponent } from './buscar-aula.component';

describe('BuscarAulaComponent', () => {
  let component: BuscarAulaComponent;
  let fixture: ComponentFixture<BuscarAulaComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BuscarAulaComponent]
    });
    fixture = TestBed.createComponent(BuscarAulaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
