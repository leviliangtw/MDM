package org.ncsist.mdm;

public class Record {
    private int type;
    private String event;
    private String recordtime;
    private String uploadtime;
    public Record(){

    }
    public Record(int type, String event, String recordtime, String uploadtime) {
        this.type = type;
        this.event = event;
        this.recordtime = recordtime;
        this.uploadtime = uploadtime;
        // TODO Auto-generated constructor stub
    }
    public int getType(){
        return type;
    }
    public void setType(int type){
        this.type = type;
    }
    public String getEvent(){
        return event;
    }
    public void setEvent(String event){
        this.event = event;
    }
    public String getRecordtime(){
        return recordtime;
    }
    public void setRecordtime(String recordtime){
        this.recordtime = recordtime;
    }
    public String getUploadtime(){
        return uploadtime;
    }
    public void setUploadtime(String uploadtime){
        this.uploadtime = uploadtime;
    }
}
