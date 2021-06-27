package in.ongrid.kshitijroy.model.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

@MappedSuperclass
public class ResourceInfo {

    @Column
    private Date resourceCreate;

    @Column
    private Date resourceUpdate;

    @PrePersist
    public void onCreate(){
        resourceCreate=new Date();
    }

    @PreUpdate
    public void onUpdate(){
        resourceUpdate=new Date();
    }


    public Date getResourceCreate() {
        return resourceCreate;
    }

    public void setResourceCreate(Date resourceCreate) {
        this.resourceCreate = resourceCreate;
    }

    public Date getResourceUpdate() {
        return resourceUpdate;
    }

    public void setResourceUpdate(Date resourceUpdate) {
        this.resourceUpdate = resourceUpdate;
    }
}
