export class PlantFeedback{
    id:string;
    commentCount:number;
    likeCount:number;
    dislikeCount:number;


    constructor(){
        this.commentCount = 0;
        this.likeCount = 0;
        this.dislikeCount = 0;
    }
}