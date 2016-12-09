import {Component, OnInit, Inject} from '@angular/core';
import {AppService, Feature, Profile} from "../app.service";
import {URLSearchParams, Response} from "@angular/http";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {ErrorModalComponent} from "./error.component";

@Component({
  selector: 'app-index',
  templateUrl: './index.component.html',
  styleUrls: ['./index.component.css']
})
export class IndexComponent implements OnInit {

  versions: string[];
  profiles: Profile[];

  metadata: Metadata = new Metadata();

  constructor(private appService: AppService, private modalService: NgbModal, @Inject(Window) private window: Window) { }

  ngOnInit(): void {
    this.appService.getVersions().subscribe((res: string[]) => {
      this.versions = res;
      this.metadata.version = res.filter((version: string) => {
        return version.indexOf('BUILD-SNAPSHOT') == -1;
      }).pop();
      this.getProfiles(this.metadata.version);
    }, () => {
      this.showError("The attempt to retrieve the supported versions failed.");
    });
  }

  getProfiles(version: string): void {
    this.appService.getProfiles(version).subscribe(res => {
      this.profiles = res;
      this.profiles.forEach(profile => {
        if (profile.name == "web") {
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
  }

  toggleFeature(feature: Feature) {
    if (this.metadata.hasFeature(feature)) {
      var idx = this.metadata.features.indexOf(feature);
      this.metadata.features.splice(idx, 1);
    } else {
      this.metadata.features.push(feature);
    }
  }

  generateProject() {
    let urlParams = this.metadata.buildProjectUrl();
    this.appService.validate(urlParams).subscribe(() => {
      this.window.location.href = "/generate?" + urlParams;
    }, (res: Response) => {
      this.metadata.errors = res.json();
    });

  }

  showError(message: string) {
    const modalRef = this.modalService.open(ErrorModalComponent);
    modalRef.componentInstance.message = message;
  }

}

class Metadata {
  name: string = "myapp";
  version: string;
  profile: Profile;
  features: Feature[] = [];
  errors: Object = {};

  hasFeature(feature: Feature): boolean {
    return this.features.indexOf(feature) > -1;
  }

  hasError(prop: string): boolean {
    return this.errors.hasOwnProperty(prop);
  }

  buildProjectUrl(): string {
    let params: URLSearchParams = new URLSearchParams();
    params.set('name', this.name);
    params.set('version', this.version);
    params.set('profile', this.profile.name);
    this.features.forEach((feature: Feature) => {
      if (!feature.required) {
        params.append('features', feature.name)
      }
    });
    return params.toString();
  }

}