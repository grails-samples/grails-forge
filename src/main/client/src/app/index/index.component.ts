import {Component, OnInit, Inject} from '@angular/core';
import {AppService, Feature, Profile} from "../app.service";
import {Response} from "@angular/http";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {ErrorModalComponent} from "./error.component";
import {Metadata, Type} from "../metadata";
import {CurlCommand} from "../curlCommand";
import {Observable} from "rxjs";
import {WindowRef} from "../window";

@Component({
  selector: 'app-index',
  templateUrl: './index.component.html',
  styleUrls: ['./index.component.css']
})
export class IndexComponent implements OnInit {

  types: Type[] = Type.all;

  constructor(private appService: AppService, private modalService: NgbModal, private winRef: WindowRef) {}

  versions: string[];
  metadata: Metadata = new Metadata();
  curlCommand: string;
  profiles: Profile[];
  nameChanged: boolean = false;
  defaultVersion: string;

  ngOnInit() {
    this.metadata.name = "myapp";
    this.metadata.type = Type.APPLICATION;
    this.appService.getVersions().subscribe((res: string[]) => {
      this.versions = res;
      this.metadata.version = this.defaultVersion = res.filter((version: string) => {
        return version.indexOf('BUILD-SNAPSHOT') == -1;
      }).pop();
      this.getProfiles(this.metadata.version);
    }, () => {
      this.showError("The attempt to retrieve the supported versions failed.");
    });
  }

  onTypeChange(type: Type) {
    if (!this.nameChanged) {
      this.metadata.name = type.defaultName;
    }
    this.getProfiles(this.metadata.version);
  }

  getProfiles(version: string): void {
    switch(this.metadata.type) {
      case Type.APPLICATION:
        this.handleProfiles(version, this.appService.getProfiles(version));
        break;
      case Type.PLUGIN:
        this.handleProfiles(version, this.appService.getPluginProfiles(version));
        break;
    }
  }

  protected handleProfiles(version: string, profiles: Observable<Profile[]>): void {
    profiles.subscribe(res => {
      this.profiles = res;
      this.profiles.forEach(profile => {
        if (profile.name == this.metadata.defaultProfile) {
          this.metadata.profile = profile;
          this.setDefaultFeatures(profile);
        }
      });
    }, () => {
      this.showError("The attempt to retrieve the list of profiles for Grails " + version + " failed.");
    });
  }

  setDefaultFeatures(profile: Profile): void {
    this.metadata.features = [];
    profile.features.forEach((feature: Feature) => {
      if (feature.defaultFeature || feature.required) {
        this.metadata.features.push(feature);
      }
    });
    this.buildCurlCommand();
  }

  toggleFeature(feature: Feature) {
    if (this.metadata.hasFeature(feature)) {
      var idx = this.metadata.features.indexOf(feature);
      this.metadata.features.splice(idx, 1);
    } else {
      this.metadata.features.push(feature);
    }
    this.buildCurlCommand();
  }

  buildCurlCommand(): void {
    this.curlCommand = CurlCommand.build(this.metadata, this.defaultVersion);
  }

  showError(message: string) {
    const modalRef = this.modalService.open(ErrorModalComponent);
    modalRef.componentInstance.message = message;
  }

  generateProject() {
    let urlParams = this.metadata.buildProjectUrl();
    this.appService.validate(urlParams).subscribe(() => {
      this.winRef.nativeWindow.location.href = "/generate?" + urlParams;
    }, (res: Response) => {
      this.metadata.errors = res.json();
    });
  }

}