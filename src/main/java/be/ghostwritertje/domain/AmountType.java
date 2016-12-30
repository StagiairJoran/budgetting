package be.ghostwritertje.domain;

import org.hibernate.annotations.Immutable;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "T_AMOUNT_TYPE")
@DiscriminatorColumn(name = "AMOUNT_NAME")
@Immutable
public class AmountType extends DomainObject {
    private static final long serialVersionUID = -2979061875257223800L;

    private String name;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    <T extends AmountType, E extends AmountTypeEnum<T>> AmountType(E codeEnum) {
        this.name = codeEnum.toString();
    }
}
