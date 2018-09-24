import { DataSource } from '@angular/cdk/collections';
import { MatPaginator, MatSort } from '@angular/material';
import { map } from 'rxjs/operators';
import { Observable, of as observableOf, merge } from 'rxjs';
import { Message } from '../../data-model/message/Message';

export class MessageDataTableDataSource extends DataSource<Message> {

  constructor(private paginator: MatPaginator, 
    private data: Message[]) {
      super();
  }

  connect(): Observable<Message[]> {
    const dataMutations = [
      observableOf(this.data),
      this.paginator.page,
    ];

    this.paginator.length = this.data.length;

    return merge(...dataMutations).pipe(map(() => {
      return this.getPagedData([...this.data]);
    }));
  }

  disconnect() {}

  private getPagedData(data: Message[]) {
    const startIndex = this.paginator.pageIndex * this.paginator.pageSize;
    return data.splice(startIndex, this.paginator.pageSize);
  }
}
