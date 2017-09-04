package schedule;

import java.time.Instant;

public class Customer {
    private int customerId;
    private String customerName;
    private Address address;
    private int active;
    private Instant createDate;
    private String createdBy;
    private Instant lastUpdate;
    private String lastUpdateBy;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
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

    public String getAddress1() { return this.getAddress().getAddress(); }

    public String getAddress2() { return this.getAddress().getAddress2(); }

    public String getCity() { return this.getAddress().getCity().getCity(); }

    public String getZip() { return this.getAddress().getPostalCode(); }

    public String getPhone() { return this.getAddress().getPhone(); }

    public String getCountry() { return this.getAddress().getCity().getCountry().getCountry(); }

    public Customer(int customerId, String customerName, Address address, int active, Instant createDate, String createdBy, Instant lastUpdate, String lastUpdateBy) {

        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.active = active;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
    }
}
