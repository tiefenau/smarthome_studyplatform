import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-configuration-main',
  templateUrl: './configuration-main.component.html',
  styleUrls: ['./configuration-main.component.css']
})
export class ConfigurationMainComponent implements OnInit {
  activeConfig: string = 'general';
  constructor() { }

  ngOnInit() {
  }

  navigateConfiguration(value: string) {
    this.activeConfig = value;
  }
}
