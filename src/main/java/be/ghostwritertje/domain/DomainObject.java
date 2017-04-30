package be.ghostwritertje.domain;

import be.ghostwritertje.webapp.form.Display;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
@MappedSuperclass
public abstract class DomainObject implements Serializable, Cloneable, Display {
    private static final long serialVersionUID = -7793260716425423013L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String uuid;

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        DomainObject clone = (DomainObject) super.clone();
        clone.uuid = this.uuid;
        return clone;
    }

    @Override
    public String getDisplayValue(){
        return this.getUuid();
    }

    @Override
    public String getId() {
        return this.getUuid();
    }
}
