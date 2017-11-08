import {Metadata, Type} from "./metadata";
import {Feature} from "./app.service";

export class CurlCommand {

  static build(metadata: Metadata, defaultVersion: string): string {
    let cmd = 'curl -O start.grails.org/' + metadata.name + '.zip';

    if (metadata.version != defaultVersion) {
      cmd = cmd + ' -d version=' + metadata.version;
    }
    if (metadata.profile.name != metadata.type.defaultProfile) {
      cmd = cmd + ' -d profile=' + metadata.profile.name;
    } else if (metadata.type == Type.PLUGIN) {
      cmd = cmd + ' -d type=plugin'
    }

    function isDifferent(set1: string[], set2: string[]): boolean {
      if (set1.length == set2.length) {
        let combined: string[] = [].concat(set1);

        set2.forEach((it: string) => {
          if (combined.indexOf(it) === -1) {
            combined.push(it);
          }
        });
        return combined.length != set1.length;
      } else {
        return true;
      }
    }

    let features = metadata.features.filter((f: Feature) => !f.required);
    let allDefaultFeatures = metadata.profile.features.filter((f: Feature) => !f.required && f.defaultFeature).map((f: Feature) => f.name);
    let selectedFeatures = features.map((f: Feature) => f.name);

    if (isDifferent(allDefaultFeatures, selectedFeatures) && features.length > 0) {
      cmd = cmd + ' -d features=' + features.map((f: Feature) => f.name).join(',');
    }
    return cmd;
  }
}