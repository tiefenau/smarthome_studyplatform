import { DataSource } from '@angular/cdk/collections';
import { MatPaginator } from '@angular/material';
import { map } from 'rxjs/operators';
import { Observable, of as observableOf, merge } from 'rxjs';
import { Survey } from '../../data-model/survey/Survey';

export class SurveyDataSource extends DataSource<Survey> {

  constructor(private paginator: MatPaginator, 
    private data: Survey[]) {
      super();
  }

  connect(): Observable<Survey[]> {
    const dataMutations = [
      observableOf(this.data),
      this.paginator.page
    ];

    this.paginator.length = this.data.length;

    return merge(...dataMutations).pipe(map(() => {
      return this.getPagedData([...this.data]);
    }));
  }

  disconnect() {}

  private getPagedData(data: Survey[]) {
    const startIndex = this.paginator.pageIndex * this.paginator.pageSize;
    return data.splice(startIndex, this.paginator.pageSize);
  }
}
