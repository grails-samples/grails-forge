import {Metadata, Type} from "./metadata";
import {CurlCommand} from "./curlCommand";
import {Profile, Feature} from "./app.service";

describe('CurlCommand', () => {

  let metadata = new Metadata();

  beforeEach(() => {
    metadata.version = "1";
    metadata.profile = <Profile>{name: "web", description: "Web", version: "1.2", features: []};
    metadata.type = Type.APPLICATION;
    metadata.features = [];
    metadata.name = "test";

    expect(CurlCommand.build(metadata, "2").startsWith("curl -O start.grails.org/test.zip")).toBe(true);
  });

  it("should set the version if it is not the default", () => {
    metadata.version = "3";

    let curl = CurlCommand.build(metadata, "2");

    expect(curl.indexOf("-d version=3")).toBeGreaterThan(-1);

    curl = CurlCommand.build(metadata, "3");

    expect(curl.indexOf("-d version=3")).toBe(-1);
  });


  it("should set the profile if it isn't the default", () => {
    let curl = CurlCommand.build(metadata, "2");

    expect(curl.indexOf("-d profile=web")).toBe(-1);
    expect(curl.indexOf("-d type=plugin")).toBe(-1);

    metadata.profile.name = "rest-api";

    curl = CurlCommand.build(metadata, "2");

    expect(curl.indexOf("-d profile=rest-api")).toBeGreaterThan(-1);
    expect(curl.indexOf("-d type=plugin")).toBe(-1);

    metadata.type = Type.PLUGIN;
    metadata.profile.name = "web-plugin";

    curl = CurlCommand.build(metadata, "2");

    expect(curl.indexOf("-d profile=web-plugin")).toBe(-1);
    expect(curl.indexOf("-d type=plugin")).toBeGreaterThan(-1);

    metadata.type = Type.PLUGIN;
    metadata.profile.name = "plugin";

    curl = CurlCommand.build(metadata, "2");

    expect(curl.indexOf("-d profile=plugin")).toBeGreaterThan(-1);
    expect(curl.indexOf("-d type=plugin")).toBe(-1);
  });

  it("should set the features if they are different from the default", () => {
    let curl = CurlCommand.build(metadata, "2");
    expect(curl.indexOf("-d features")).toBe(-1);

    let profileFeatures = <Feature[]>[
      {name: "0", defaultFeature: false, required: false},
      {name: "1", defaultFeature: true, required: false},
      {name: "2", defaultFeature: false, required: true},
      {name: "3", defaultFeature: false, required: false},
      {name: "4", defaultFeature: true, required: false},
      {name: "5", defaultFeature: false, required: true}
    ];

    metadata.profile.features = profileFeatures;

    //Selecting all required and default should not cause parameter to show
    metadata.features = [profileFeatures[1], profileFeatures[2], profileFeatures[4], profileFeatures[5]];
    curl = CurlCommand.build(metadata, "2");
    expect(curl.indexOf("-d features")).toBe(-1);

    //Selecting only required should not cause parameter to show
    metadata.features = [profileFeatures[2], profileFeatures[5]];
    curl = CurlCommand.build(metadata, "2");
    expect(curl.indexOf("-d features")).toBe(-1);

    //Deselecting a default feature should include other default features in the list
    metadata.features = [profileFeatures[2], profileFeatures[4], profileFeatures[5]];
    curl = CurlCommand.build(metadata, "2");
    expect(curl.indexOf("-d features=4")).toBeGreaterThan(-1);

    //Adding a feature and removing a default
    metadata.features = [profileFeatures[2], profileFeatures[3], profileFeatures[4], profileFeatures[5]];
    curl = CurlCommand.build(metadata, "2");
    expect(curl.indexOf("-d features=3,4")).toBeGreaterThan(-1);
  });
});