import {Profile, Feature} from "./app.service";
import {URLSearchParams} from "@angular/http";

export class Metadata {

  name: string = "myapp";
  type: Type;
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

  get defaultProfile(): string {
    return this.type.defaultProfile;
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

export class Type {
  constructor(public displayName: string, public defaultName: string, public defaultProfile: string) {}

  static APPLICATION = new Type("Application", "myapp", "web");
  static PLUGIN = new Type("Plugin", "myplugin", "web-plugin");
  static PROFILE = new Type("Profile", "myprofile", "profile");

  static get all(): Type[] {
    return [this.APPLICATION, this.PLUGIN, /*this.PROFILE*/]
  }
}