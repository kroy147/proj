package in.ongrid.kshitijroy.model.entity;

import javax.persistence.*;

@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String houseDetail;

    @Column
    private String city;

    @Column
    private String pinCode;

    @OneToOne(mappedBy = "address")
    private User AddressUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHouseDetail() {
        return houseDetail;
    }

    public void setHouseDetail(String houseDetail) {
        this.houseDetail = houseDetail;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public User getAddressUser() {
        return AddressUser;
    }

    public void setAddressUser(User addressUser) {
        AddressUser = addressUser;
    }

    public Address(String houseDetail, String city, String pinCode) {
        this.houseDetail = houseDetail;
        this.city = city;
        this.pinCode = pinCode;
    }

    public Address() {
    }

    public Address(String houseDetail, String city, String pinCode, User addressUser) {
        this.houseDetail = houseDetail;
        this.city = city;
        this.pinCode = pinCode;
        AddressUser = addressUser;
    }

}
