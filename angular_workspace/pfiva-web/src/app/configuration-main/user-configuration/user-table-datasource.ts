import { DataSource } from "@angular/cdk/table";
import { User } from "../../data-model/User";
import { MatPaginator } from "@angular/material";
import { Observable, of as observableOf, merge } from 'rxjs';
import { map } from 'rxjs/operators';

export class UserTableDataSource extends DataSource<User> {

    constructor(private paginator: MatPaginator, 
        private data: User[]) {
            super();
    }
    
    connect(): Observable<User[]> {
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

    private getPagedData(data: User[]) {
        const startIndex = this.paginator.pageIndex * this.paginator.pageSize;
        return data.splice(startIndex, this.paginator.pageSize);
    }
}