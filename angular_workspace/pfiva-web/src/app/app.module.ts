import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { MainComponent } from './main/main.component';
import { MessageCenterComponent } from './message-center/message-center.component';
import { AppRoutingModule } from './app-routing.module';
import { ComposeMessageComponent } from './message-center/compose-message/compose-message.component';
import { MessageListComponent } from './message-center/message-list/message-list.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { DropdownDirective } from './shared/dropdown.directive';
import { FormsModule } from '@angular/forms';
import { NluDataTableComponent } from './nlu-data-table/nlu-data-table.component';
import { MatTableModule, MatPaginatorModule, MatSortModule } from '@angular/material';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpModule } from '@angular/http';
import { PfivaDataService } from './services/pfiva-data.service';
import { MessageService } from './message-center/message.service';
import { ConfigurationMainComponent } from './configuration-main/configuration-main.component';
import { GeneralConfigurationComponent } from './configuration-main/general-configuration/general-configuration.component';
import { UserConfigurationComponent } from './configuration-main/user-configuration/user-configuration.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    MainComponent,
    MessageCenterComponent,
    ComposeMessageComponent,
    MessageListComponent,
    PageNotFoundComponent,
    DropdownDirective,
    NluDataTableComponent,
    ConfigurationMainComponent,
    GeneralConfigurationComponent,
    UserConfigurationComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    HttpModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    BrowserAnimationsModule
  ],
  providers: [PfivaDataService, MessageService],
  bootstrap: [AppComponent]
})
export class AppModule { }
