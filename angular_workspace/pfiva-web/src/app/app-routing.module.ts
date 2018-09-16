import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { MainComponent } from "./main/main.component";
import { MessageCenterComponent } from "./message-center/message-center.component";
import { ComposeMessageComponent } from "./message-center/compose-message/compose-message.component";
import { PageNotFoundComponent } from "./page-not-found/page-not-found.component";
import { ConfigurationMainComponent } from "./configuration-main/configuration-main.component";
import { UserConfigurationComponent } from "./configuration-main/user-configuration/user-configuration.component";
import { GeneralConfigurationComponent } from "./configuration-main/general-configuration/general-configuration.component";

const appRoutes: Routes = [
    //{ path: '', redirectTo: '/main', pathMatch: 'full' },
    { path: '', component: MainComponent  },
    { path: 'message-center', component: MessageCenterComponent, children: [
        { path: 'compose-message', component: ComposeMessageComponent }
    ] },
    { path: 'configuration', component: ConfigurationMainComponent, children: [
        { path: '', pathMatch: 'full', redirectTo: '/configuration/general' },
        { path: 'general', component: GeneralConfigurationComponent },
        { path: 'user', component: UserConfigurationComponent }
    ] },
    { path: 'not-found', component: PageNotFoundComponent  },
    { path: '**', redirectTo: '/not-found'  }
  ];

@NgModule({
    imports: [RouterModule.forRoot(appRoutes)],
    exports: [RouterModule]
})
export class AppRoutingModule {

}