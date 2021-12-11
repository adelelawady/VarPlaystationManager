import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISession } from '../session.model';

@Component({
  selector: 'jhi-session-detail',
  templateUrl: './session-detail.component.html',
})
export class SessionDetailComponent implements OnInit {
  session: ISession | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ session }) => {
      this.session = session;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
