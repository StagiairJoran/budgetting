package be.ghostwritertje.domain;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "T_AMOUNT_TYPE")
@DiscriminatorColumn(name = "AMOUNT_NAME")
public class AmountType {
    private String name;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public <T extends AmountType, E extends AmountTypeEnum<T>> AmountType(E codeEnum) {
        this.name = codeEnum.toString();
    }
}
