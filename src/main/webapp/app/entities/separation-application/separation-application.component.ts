import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { ISeparationApplication } from 'app/shared/model/separation-application.model';
import { Principal } from 'app/core';
import { SeparationApplicationService } from './separation-application.service';
import { saveAs } from 'file-saver/FileSaver';
import { Moment } from 'moment';
import { Angular5Csv } from 'angular5-csv/Angular5-csv';

@Component( {
    selector: 'jhi-separation-application',
    templateUrl: './separation-application.component.html'
} )
export class SeparationApplicationComponent implements OnInit, OnDestroy {
    separationApplications: ISeparationApplication[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private separationApplicationService: SeparationApplicationService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) { }

    loadAll() {
        this.separationApplicationService.query().subscribe(
            ( res: HttpResponse<ISeparationApplication[]> ) => {
                this.separationApplications = res.body;
            },
            ( res: HttpErrorResponse ) => this.onError( res.message )
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then( account => {
            this.currentAccount = account;
        } );
        this.registerChangeInSeparationApplications();
    }

    ngOnDestroy() {
        this.eventManager.destroy( this.eventSubscriber );
    }

    trackId( index: number, item: ISeparationApplication ) {
        return item.id;
    }

    registerChangeInSeparationApplications() {
        this.eventSubscriber = this.eventManager.subscribe( 'separationApplicationListModification', response => this.loadAll() );
    }

    private onError( errorMessage: string ) {
        this.jhiAlertService.error( errorMessage, null, null );
    }

    isPending() {
        return this.separationApplications.length > 0;
    }

    downloadFile() {
        const options = {
                fieldSeparator: ',',
                quoteStrings: '"',
                decimalseparator: '.',
                showLabels: true,
                showTitle: true,
                useBom: true,
                noDownload: false,
                headers: ['ID', 'Date of Leaving', 'Date of Submission', 'Reason For Leaving', 'Completed', 'Employee']
              };
        return new Angular5Csv(this.separationApplications, 'My Report', options);
//        if ( this.separationApplications[0] != null ) {
//            const downloadString = this.makeCSV();
//            const blob = new Blob( [downloadString], { type: 'text/csv' } );
//            saveAs( blob, 'myFile.csv' );
//        } else {
//            this.jhiAlertService.error( 'employee list is blank, cannot download blank set' );
//        }
    }

    makeCSV() {
        return new Angular5Csv(this.separationApplications, 'My Report');
/*        const header = Object.keys( this.separationApplications[0] );
        let stringToCSV = '';
        for ( let i = 0; i < header.length; i++ ) {
            stringToCSV += header[i];
            if ( i < header.length - 1 ) {
                stringToCSV += ',';
            }
        }
        stringToCSV += '\r\n';

        for (let i = 0; i < this.separationApplications.length; i++) {
            if (this.separationApplications[i].dateOfLeaving) {
                stringToCSV += this.separationApplications[i].dateOfLeaving.toDate() + ',';
            } else {
                stringToCSV += null + ',';
            }
        }*/
    }
}
