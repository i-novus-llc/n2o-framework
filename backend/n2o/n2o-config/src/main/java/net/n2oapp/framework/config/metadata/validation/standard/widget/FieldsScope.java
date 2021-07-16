package net.n2oapp.framework.config.metadata.validation.standard.widget;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

/**
 * Map для хранении информации о наличии зависимостей по идентификатору поля
 */
@Getter
@Setter
public class FieldsScope extends HashMap<String, Boolean> {

}
