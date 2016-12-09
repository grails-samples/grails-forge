import {Injectable} from "@angular/core";
import {Http, Response, Headers, RequestOptions} from "@angular/http";
import 'rxjs/add/operator/map';
import {Observable} from "rxjs";

@Injectable()
export class AppService {

    constructor(private http: Http) { }

    getVersions(): Observable<string[]> {
        return this.http.get('/versions')
            .map((res: Response) => res.json());
    }

    getProfiles(version: string): Observable<Profile[]> {
        return this.http.get('/' + version + '/profiles')
            .map((res: Response) => res.json());
    }

    getFeatures(version: string, profile: string): Observable<Feature[]> {
        return this.http.get('/' + version + '/' + profile + '/features')
            .map((res: Response) => res.json());
    }

    validate(body: any) {
        var headers = new Headers();
        headers.append("Accept", 'application/json');
        headers.append('Content-Type', 'application/x-www-form-urlencoded');

        var requestOptions = new RequestOptions({
            headers: headers
        });
        return this.http.post('/validate', body, requestOptions);
    }
}

export declare abstract class Profile {
    name: string;
    description: string;
    version: string;
    features: Feature[];
}

export declare abstract class Feature {
    name: string;
    description: string;
    defaultFeature: boolean;
    required: boolean;
}