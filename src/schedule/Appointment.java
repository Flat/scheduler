package schedule;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;

public class Appointment {
    private int appointmentId;
    private Customer customer;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String url;
    private Instant start;
    private Instant end;
    private Instant createDate;
    private String createdBy;
    private Instant lastUpdate;
    private String lastUpdateBy;

    public int getAppointmentid() {
        return appointmentId;
    }

    public void setAppointmentid(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ZonedDateTime getStart() {
        return start.atZone(ZoneId.systemDefault());
    }

    public void setStart(Instant start) {
        this.start = start;
    }

    public ZonedDateTime getEnd() {
        return end.atZone(ZoneId.systemDefault());
    }

    public void setEnd(Instant end) {
        this.end = end;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    public String getCustomerName() {
        return customer.getCustomerName();
    }

    public String getCustomerAddress() {
        return customer.getAddress().getAddress();
    }

    public String getCustomerAddress2() {
        return customer.getAddress().getAddress2();
    }

    public String getCity() {
        return customer.getAddress().getCity().getCity();
    }

    public String getZip() {
        return customer.getAddress().getPostalCode();
    }

    public String getPhone() {
        return customer.getAddress().getPhone();
    }

    public String getCountry() {
        return customer.getAddress().getCity().getCountry().getCountry();
    }

    public Appointment(int appointmentId, Customer customer, String title, String description, String location, String contact, String url, Instant start, Instant end, Instant createDate, String createdBy, Instant lastUpdate, String lastUpdateBy) {

        this.appointmentId = appointmentId;
        this.customer = customer;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.url = url;
        this.start = start;
        this.end = end;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
    }
}
