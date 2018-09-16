import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { MainComponent } from "./main/main.component";
import { MessageCenterComponent } from "./message-center/message-center.component";
import { ComposeMessageComponent } from "./message-center/compose-message/compose-message.component";
import { PageNotFoundComponent } from "./page-not-found/page-not-found.component";
import { ConfigurationMainComponent } from "./configuration-main/configuration-main.component";

const appRoutes: Routes = [
    //{ path: '', redirectTo: '/main', pathMatch: 'full' },
    { path: '', component: MainComponent  },
    { path: 'message-center', component: MessageCenterComponent, children: [
        { path: 'compose-message', component: ComposeMessageComponent }
    ] },
    { path: 'configuration', component: ConfigurationMainComponent },
    { path: 'not-found', component: PageNotFoundComponent  },
    { path: '**', redirectTo: '/not-found'  }
  ];

@NgModule({
    imports: [RouterModule.forRoot(appRoutes)],
    exports: [RouterModule]
})
export class AppRoutingModule {

}