package be.ghostwritertje.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "CURRENCY")
public class Currency extends AmountType {

    public  Currency(Enum codeEnum) {
        super(codeEnum);
    }

    public enum Enum implements AmountTypeEnum<Currency> {
        EUR("EUR"),
        USD("USD");

        private final String code;

        Enum(String code) {
            this.code = code;
        }

        @Override
        public Class<Currency> getType() {
            return Currency.class;
        }

        @Override
        public String toString() {
            return this.code;
        }
    }
}
