/* tslint:disable:no-unused-variable */

import {TestBed, async, inject} from '@angular/core/testing';
import { IndexComponent } from './index.component';
import {Observable, Observer} from "rxjs";
import {AppService, Profile, Feature} from "../app.service";
import {FormsModule} from "@angular/forms";
import {NgbModule, NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {ResponseOptions, Response} from "@angular/http";
import {Type} from "../metadata";


describe('Component: Index', () => {

    let component: IndexComponent;

    const appService = {
        getVersions(): Observable<string[]> {
            return Observable.create((observer: Observer<any>) => {
                observer.next(["1","2","3","4.BUILD-SNAPSHOT"]);
                observer.complete();
            });
        },

        getProfiles(version: string): Observable<Profile[]> {
            return Observable.create((observer: Observer<Profile[]>) => {

                let profile1: Profile = {name: "a", version: "1.0", description: "A", features: []};
                let profile2: Profile = {name: "web", version: "1.1", description: "Web", features: [
                    <Feature>{name: "f1", description: "Feature 1", defaultFeature: false, required: false},
                    <Feature>{name: "f2", description: "Feature 2", defaultFeature: true, required: false},
                    <Feature>{name: "f3", description: "Feature 3", defaultFeature: false, required: true},
                    <Feature>{name: "f4", description: "Feature 4", defaultFeature: true, required: true},
                    <Feature>{name: "f5", description: "Feature 5", defaultFeature: false, required: false},
                    <Feature>{name: "f6", description: "Feature 6", defaultFeature: true, required: false},
                    <Feature>{name: "f7", description: "Feature 7", defaultFeature: false, required: true},
                    <Feature>{name: "f8", description: "Feature 8", defaultFeature: true, required: true}
                ]};
                let profile3: Profile = {name: "c", version: "2.0", description: "C", features: []};

                observer.next([profile1, profile2, profile3]);
                observer.complete();
            });
        },

        getPluginProfiles(version: string): Observable<Profile[]> {
            return Observable.create((observer: Observer<Profile[]>) => {

                let profile1: Profile = {name: "a", version: "1.0", description: "A", features: []};
                let profile2: Profile = {name: "web-plugin", version: "1.1", description: "Web", features: []};
                let profile3: Profile = {name: "c", version: "2.0", description: "C", features: []};

                observer.next([profile1, profile2, profile3]);
                observer.complete();
            });
        },

        validate(body: string): Observable<any> {
            return Observable.create((observer: Observer<any>) => {
                observer.next(null);
                observer.complete();
            });
        }
    };

    const window = {
        location: {
            href: "default"
        }
    };

    class ModalService {

        componentInstance: Object = {};

        open(content: any) {
            return {
                componentInstance: this.componentInstance
            }
        }
    }

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [
                FormsModule,
                NgbModule.forRoot()
            ],
            declarations: [
                IndexComponent
            ],
            providers: [
                {provide: AppService, useValue: appService},
                {provide: Window, useValue: window},
                {provide: NgbModal, useValue: new ModalService()}
            ],
        });

        let fixture = TestBed.createComponent(IndexComponent);
        component = fixture.debugElement.componentInstance;
    });

    it('should create the app', () => {
        expect(component).toBeTruthy();
    });

    it("should get versions on init", async(inject([AppService], (service) => {
        spyOn(service, "getVersions").and.callThrough();
        spyOn(service, "getProfiles").and.callThrough();
        component.ngOnInit();

        expect(component.metadata.name).toEqual("myapp");
        expect(component.metadata.type).toEqual(Type.APPLICATION);
        expect(component.versions).toEqual(["1","2","3","4.BUILD-SNAPSHOT"]);
        expect(component.metadata.version).toEqual("3");
        expect(component.defaultVersion).toEqual("3");
        expect(component.profiles.length).toEqual(3);
        expect(component.profiles.length).toEqual(3);
        expect(component.metadata.profile.name).toEqual("web");
        expect(component.metadata.profile.version).toEqual("1.1");
        expect(component.metadata.features.map((f) => f.name)).toEqual(["f2","f3","f4","f6","f7","f8"]);
        expect(service.getVersions).toHaveBeenCalledWith();
        expect(service.getVersions).toHaveBeenCalledTimes(1);
        expect(service.getProfiles).toHaveBeenCalledWith("3");
        expect(service.getProfiles).toHaveBeenCalledTimes(1);
    })));

    it("should show error on init if getVersions fails", async(inject([AppService, NgbModal], (service, modalService) => {
        spyOn(service, "getVersions").and.callFake(() => {
            return Observable.create((observer: Observer<any>) => {
                observer.error('');
            });
        });
        spyOn(service, "getProfiles").and.callThrough();
        spyOn(modalService, "open").and.callThrough();
        component.ngOnInit();

        expect(modalService.componentInstance.message).toEqual("The attempt to retrieve the supported versions failed.");
        expect(modalService.open).toHaveBeenCalledTimes(1);
        expect(service.getVersions).toHaveBeenCalledWith();
        expect(service.getVersions).toHaveBeenCalledTimes(1);
        expect(service.getProfiles).not.toHaveBeenCalled();
    })));

    it("should show error on init if getProfiles fails", async(inject([AppService, NgbModal], (service, modalService) => {
        spyOn(service, "getVersions").and.callThrough();
        spyOn(service, "getProfiles").and.callFake(() => {
            return Observable.create((observer: Observer<any>) => {
                observer.error('');
            });
        });
        spyOn(modalService, "open").and.callThrough();
        component.ngOnInit();

        expect(modalService.componentInstance.message).toEqual("The attempt to retrieve the list of profiles for Grails 3 failed.");
        expect(modalService.open).toHaveBeenCalledTimes(1);
        expect(service.getVersions).toHaveBeenCalledWith();
        expect(service.getVersions).toHaveBeenCalledTimes(1);
        expect(service.getProfiles).toHaveBeenCalledWith("3");
        expect(service.getProfiles).toHaveBeenCalledTimes(1);
    })));

    it("should toggle feature", () => {
        spyOn(component, "buildCurlCommand");

        let features: Feature[] =  [
            <Feature>{name: "f1", description: "Feature 1", defaultFeature: false, required: false},
            <Feature>{name: "f2", description: "Feature 2", defaultFeature: true, required: false},
            <Feature>{name: "f3", description: "Feature 3", defaultFeature: false, required: true},
            <Feature>{name: "f4", description: "Feature 4", defaultFeature: true, required: true},
            <Feature>{name: "f5", description: "Feature 5", defaultFeature: false, required: false},
            <Feature>{name: "f6", description: "Feature 6", defaultFeature: true, required: false},
            <Feature>{name: "f7", description: "Feature 7", defaultFeature: false, required: true},
            <Feature>{name: "f8", description: "Feature 8", defaultFeature: true, required: true}
            ];

        component.toggleFeature(features[0]);
        component.toggleFeature(features[1]);
        component.toggleFeature(features[2]);
        component.toggleFeature(features[0]);
        component.toggleFeature(features[2]);
        component.toggleFeature(features[3]);

        expect(component.metadata.features.map((f) => f.name)).toEqual(["f2","f4"]);
        expect(component.buildCurlCommand).toHaveBeenCalledTimes(6);
    });

    it("should set errors if generate fails", inject([Window, AppService],(window, service) => {
        spyOn(service, 'validate').and.callFake((params: string) => {
            return Observable.create((observer: Observer<any>) => {
                observer.error(new Response(new ResponseOptions({body: '{"name":"Error"}', status: 400})));
            });
        });

        component.metadata.version = "3.2.0";
        component.metadata.profile = <Profile>{name: "web", version: "1.1", description: "Web", features: []};
        component.metadata.features = [
            <Feature>{name: "f1", description: "Feature 1", defaultFeature: true, required: false},
            <Feature>{name: "f2", description: "Feature 2", defaultFeature: false, required: true},
            <Feature>{name: "f3", description: "Feature 3", defaultFeature: false, required: false},
            <Feature>{name: "f4", description: "Feature 4", defaultFeature: true, required: true},
        ];
        component.generateProject();

        expect(service.validate).toHaveBeenCalledWith("name=myapp&version=3.2.0&profile=web&features=f1&features=f3");
        expect(component.metadata.errors['name']).toEqual("Error");
        expect(window.location.href).toEqual('default');
    }));

    it("should set the url if generate succeeds", inject([Window, AppService],(window, service) => {
        spyOn(service, 'validate').and.callThrough();

        component.metadata.version = "3.2.0";
        component.metadata.profile = <Profile>{name: "web", version: "1.1", description: "Web", features: []};
        component.metadata.features = [
            <Feature>{name: "f1", description: "Feature 1", defaultFeature: true, required: false},
            <Feature>{name: "f2", description: "Feature 2", defaultFeature: false, required: true},
            <Feature>{name: "f3", description: "Feature 3", defaultFeature: false, required: false},
            <Feature>{name: "f4", description: "Feature 4", defaultFeature: true, required: true},
        ];
        component.generateProject();

        expect(service.validate).toHaveBeenCalledTimes(1);
        expect(window.location.href).toEqual("/generate?name=myapp&version=3.2.0&profile=web&features=f1&features=f3");
    }));

    it("should set the name on type change and get profiles (APPLICATION)", inject([AppService],(service) => {
        spyOn(service, 'getProfiles').and.callThrough();
        expect(component.nameChanged).toBe(false);
        component.metadata.name = "default";
        component.metadata.version = "2";
        component.metadata.type = Type.APPLICATION;
        component.onTypeChange(Type.APPLICATION);
        expect(component.metadata.name).toEqual(Type.APPLICATION.defaultName);
        expect(service.getProfiles).toHaveBeenCalledWith("2");
    }));

    it("should set the name on type change and get profiles (PLUGIN)", inject([AppService],(service) => {
        spyOn(service, 'getPluginProfiles').and.callThrough();
        expect(component.nameChanged).toBe(false);
        component.metadata.name = "default";
        component.metadata.version = "2";
        component.metadata.type = Type.PLUGIN;
        component.onTypeChange(Type.PLUGIN);
        expect(component.metadata.name).toEqual(Type.PLUGIN.defaultName);
        expect(service.getPluginProfiles).toHaveBeenCalledWith("2");
    }));

});
