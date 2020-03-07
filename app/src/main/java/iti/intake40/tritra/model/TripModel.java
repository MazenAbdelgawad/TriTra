package iti.intake40.tritra.model;

public class TripModel {
    private String id;
    private String name;
    private String type;
    private String startPoint;
    private String endPoint;
    private String date;
    private String time;

    public TripModel() {
        this.id = "id";
        this.name = "name";
        this.type = "type";
        this.startPoint = "startPoint";
        this.endPoint = "endPoint";
        this.date = "date";
        this.time = "time";
    }

    public TripModel(String id ,String name, String type, String startPoint, String endPoint, String date, String time) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.date = date;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
