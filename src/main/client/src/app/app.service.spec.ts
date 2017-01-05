/* tslint:disable:no-unused-variable */

import {TestBed, async, inject} from '@angular/core/testing';
import {AppService} from "./app.service";
import {MockBackend, MockConnection} from "@angular/http/testing";
import {BaseRequestOptions, Http, ResponseOptions, Response, RequestMethod} from "@angular/http";

describe('AppService', () => {

    let backend: MockBackend, service: AppService;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [
            ],
            declarations: [
            ],
            providers: [
                AppService,
                MockBackend,
                BaseRequestOptions,
                {provide: Http, useFactory: (backend, options) => new Http(backend, options), deps: [MockBackend, BaseRequestOptions]}
            ],
        });
    });

    beforeEach(inject([MockBackend, AppService], (_backend, _service) => {
        backend = _backend;
        service = _service;
    }));

    it("should get versions", async(() => {
        let count = 0;
        backend.connections.subscribe((connection: MockConnection) => {
            expect(connection.request.method).toBe(RequestMethod.Get);
            expect(connection.request.url).toBe('/versions');
            connection.mockRespond(new Response(new ResponseOptions({body: '["1","2","3"]', status: 200})));
            count++;
        });

        service.getVersions().subscribe((data: any) => {
            expect(data).toEqual(["1","2","3"]);
        });

        expect(backend.connectionsArray.length).toBe(1);
    }));

    it("should get profiles", async(() => {
        let count = 0;
        backend.connections.subscribe((connection: MockConnection) => {
            expect(connection.request.method).toBe(RequestMethod.Get);
            expect(connection.request.url).toBe('/3.2.3/profiles');
            connection.mockRespond(new Response(new ResponseOptions({body: '[]', status: 200})));
            count++;
        });

        service.getProfiles("3.2.3").subscribe((data: any) => {
            expect(data).toEqual([]);
        });

        expect(backend.connectionsArray.length).toBe(1);
    }));

    it("should get plugin profiles", async(() => {
        let count = 0;
        backend.connections.subscribe((connection: MockConnection) => {
            expect(connection.request.method).toBe(RequestMethod.Get);
            expect(connection.request.url).toBe('/3.2.3/profiles?type=plugin');
            connection.mockRespond(new Response(new ResponseOptions({body: '[]', status: 200})));
            count++;
        });

        service.getPluginProfiles("3.2.3").subscribe((data: any) => {
            expect(data).toEqual([]);
        });

        expect(backend.connectionsArray.length).toBe(1);
    }));

    it("should get features", async(() => {
        let count = 0;
        backend.connections.subscribe((connection: MockConnection) => {
            expect(connection.request.method).toBe(RequestMethod.Get);
            expect(connection.request.url).toBe('/3.2.3/web/features');
            connection.mockRespond(new Response(new ResponseOptions({body: '[]', status: 200})));
            count++;
        });

        service.getFeatures("3.2.3", "web").subscribe((data: any) => {
            expect(data).toEqual([]);
        });

        expect(backend.connectionsArray.length).toBe(1);
    }));

    it("should validate", async(() => {
        let count = 0;
        backend.connections.subscribe((connection: MockConnection) => {
            expect(connection.request.method).toBe(RequestMethod.Post);
            expect(connection.request.url).toBe('/validate');
            expect(connection.request.headers.get("Content-Type")).toEqual("application/x-www-form-urlencoded");
            expect(connection.request.headers.get("Accept")).toEqual("application/json");
            expect(connection.request.getBody()).toEqual('foo');
            connection.mockRespond(new Response(new ResponseOptions({body: '', status: 204})));
            count++;
        });

        service.validate('foo').subscribe((res: any) => {
        });

        expect(backend.connectionsArray.length).toBe(1);
    }));

    afterEach(() => {
        backend.verifyNoPendingRequests();
    })
});

