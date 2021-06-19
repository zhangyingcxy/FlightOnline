public class FlightItem {
    private int id;
    private String flightID;
    private String dtime;
    private String atime;
    private String dplace;
    private String aplace;
    private float price;


    public FlightItem(String flightID, String dtime, String atime, String dplace, String aplace, float price) {
        super();
        this.flightID = flightID;
        this.dtime = dtime;
        this.atime = atime;
        this.dplace = dplace;
        this.aplace = aplace;
        this.price = price;
    }

    public FlightItem() {
        super();
        this.flightID = "";
        this.dtime = "";
        this.atime = "";
        this.dplace = "";
        this.aplace = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFlightID() {
        return flightID;
    }

    public void setFlightID(String flightID) {
        this.flightID = flightID;
    }

    public String getDtime() {
        return dtime;
    }

    public void setDtime(String dtime) {
        this.dtime = dtime;
    }

    public String getAtime() {
        return atime;
    }

    public void setAtime(String atime) {
        this.atime = atime;
    }

    public String getDplace() {
        return dplace;
    }

    public void setDplace(String dplace) {
        this.dplace = dplace;
    }

    public String getAplace() {
        return aplace;
    }

    public void setAplace(String aplace) {
        this.aplace = aplace;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}

