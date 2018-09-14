
import { fakeAsync, ComponentFixture, TestBed } from '@angular/core/testing';

import { NluDataTableComponent } from './nlu-data-table.component';

describe('NluDataTableComponent', () => {
  let component: NluDataTableComponent;
  let fixture: ComponentFixture<NluDataTableComponent>;

  beforeEach(fakeAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ NluDataTableComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NluDataTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should compile', () => {
    expect(component).toBeTruthy();
  });
});
