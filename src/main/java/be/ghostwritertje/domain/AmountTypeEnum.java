package be.ghostwritertje.domain;

import java.util.HashMap;
import java.util.Map;

public interface AmountTypeEnum<T extends AmountType> {
    Map<AmountTypeEnum, Object> CODES = new HashMap<>();

    Class<T> getType();
}
