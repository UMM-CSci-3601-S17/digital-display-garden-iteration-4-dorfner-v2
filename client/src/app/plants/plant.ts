export class Plant {
    _id: {};
    id: string;
    plantID: string;
    plantType: string;
    commonName: string;
    cultivar: string;
    source: string;
    gardenLocation: string;
    year: number;
    pageURL: string;
    plantImageURLs: string[];
    recognitions: string[];

    constructor () {
        this.id="";
        this.plantID="";
        this.plantType="";
        this.commonName="";
        this.cultivar="";
        this.source="";
        this.gardenLocation="";
        this.year=1987;
        this.pageURL="";
        this.plantImageURLs=[];
        this.recognitions=[];
    }
}



