package schedule;

import java.sql.Date;
import java.sql.Timestamp;

public class Appointment {
    private int appointmentid;
    private int customerid;
    private String title;
    private String descritpion;
    private String location;
    private String contact;
    private String url;
    private Date start;
    private Date end;
    private Date createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;
}
