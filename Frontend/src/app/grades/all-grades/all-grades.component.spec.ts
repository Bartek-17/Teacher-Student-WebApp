import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AllGradesComponent } from './all-grades.component';

describe('AllGradesComponent', () => {
  let component: AllGradesComponent;
  let fixture: ComponentFixture<AllGradesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AllGradesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AllGradesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
