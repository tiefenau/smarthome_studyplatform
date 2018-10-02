import { Component, OnInit, OnDestroy, AfterViewInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { PlatformLocation } from '@angular/common';

@Component({
  selector: 'app-message-center',
  templateUrl: './message-center.component.html',
  styleUrls: ['./message-center.component.css']
})
export class MessageCenterComponent implements OnInit {
  isComposeMessage = false;
  queryPath: string;
  queryParam: string;

  constructor(private router: Router, 
    private route: ActivatedRoute, 
    private location: PlatformLocation) {
      this.location.onPopState(() => { this.isComposeMessage = false;});
    }

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.queryParam = params['topic'];
      this.queryPath = '/message-center?topic=' + encodeURI(params['topic']);
    });
    this.isComposeMessage = false;
  }

  navigateToComposeMessage() {
    this.isComposeMessage = true;
    this.router.navigate(['compose-message'], {relativeTo:this.route});
  }
}
