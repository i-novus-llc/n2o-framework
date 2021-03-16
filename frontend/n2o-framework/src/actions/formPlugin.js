import {
  HIDE_FIELD,
  SHOW_FIELD,
  DISABLE_FIELD,
  ENABLE_FIELD,
  HIDE_FIELDS,
  SHOW_FIELDS,
  ENABLE_FIELDS,
  DISABLE_FIELDS,
  REGISTER_FIELD_EXTRA,
  ADD_FIELD_MESSAGE,
  REMOVE_FIELD_MESSAGE,
  REGISTER_DEPENDENCY,
  SET_FIELD_FILTER,
  SET_REQUIRED,
  UNSET_REQUIRED,
  SET_LOADING,
} from '../constants/formPlugin';
import createActionHelper from './createActionHelper';

/**
 * экшен скрытия поля
 * @param form
 * @param name
 */
export function hideField(form, name) {
  return createActionHelper(
    HIDE_FIELD,
    payload => payload,
    ({ form }) => ({ form })
  )({
    name,
    form,
  });
}

/**
 * экшен показа поля
 * @param form
 * @param name
 */
export function showField(form, name) {
  return createActionHelper(
    SHOW_FIELD,
    payload => payload,
    ({ form }) => ({ form })
  )({
    name,
    form,
  });
}

/**
 * экшен активирования поля(из неактивного состояния(disabled))
 * @param form
 * @param name
 */
export function enableField(form, name) {
  return createActionHelper(
    ENABLE_FIELD,
    payload => payload,
    ({ form }) => ({ form })
  )({
    name,
    form,
  });
}

/**
 * экшен дизактивации поля
 * @param form
 * @param name
 */
export function disableField(form, name) {
  return createActionHelper(
    DISABLE_FIELD,
    payload => payload,
    ({ form }) => ({ form })
  )({
    name,
    form,
  });
}

/**
 * экшен показа полей формы
 * @param form - название формы
 * @param names - массив названия филдов формы
 */
export function showFields(form, names) {
  return createActionHelper(
    SHOW_FIELDS,
    payload => payload,
    ({ form }) => ({ form })
  )({
    form,
    names,
  });
}

/**
 * экшен скрытия полей формы
 * @param form - название формы
 * @param names - массив названия филдов формы
 */
export function hideFields(form, names) {
  return createActionHelper(
    HIDE_FIELDS,
    payload => payload,
    ({ form }) => ({ form })
  )({
    form,
    names,
  });
}

/**
 * экшен активации полей формы
 * @param form - название формы
 * @param names - массив названия филдов формы
 */
export function enableFields(form, names) {
  return createActionHelper(
    ENABLE_FIELDS,
    payload => payload,
    ({ form }) => ({ form })
  )({
    form,
    names,
  });
}

/**
 *  экшен дизактивации полей формы
 * @param form - название формы
 * @param names - массив названия филдов формы
 */
export function disableFields(form, names) {
  return createActionHelper(
    DISABLE_FIELDS,
    payload => payload,
    ({ form }) => ({ form })
  )({
    form,
    names,
  });
}

/**
 * Добавить сообщение (после валидации) к полю
 * @param form - название формы
 * @param name - названия филдов формы
 * @param message - сообщение
 * @param isTouched - флаг для вставки touch у филда
 */
export function addFieldMessage(form, name, message, isTouched) {
  return createActionHelper(
    ADD_FIELD_MESSAGE,
    payload => payload,
    ({ form }) => ({ form, isTouched })
  )({
    name,
    form,
    message,
  });
}

/**
 * Удалить сообщение поля
 * @param form
 * @param name
 */
export function removeFieldMessage(form, name) {
  return createActionHelper(
    REMOVE_FIELD_MESSAGE,
    payload => payload,
    ({ form }) => ({ form })
  )({
    name,
    form,
  });
}

/**
 * регистрирование дополнительных свойств у поля формы
 * @param form
 * @param name
 * @param initialState
 */
export function registerFieldExtra(form, name, initialState) {
  return createActionHelper(
    REGISTER_FIELD_EXTRA,
    payload => payload,
    ({ form }) => ({ form })
  )({
    name,
    form,
    initialState,
  });
}

/**
 * Зарегестрировать зависимости поля от других полей
 * @param form
 * @param name
 * @param dependency
 */
export function registerFieldDependency(form, name, dependency) {
  return createActionHelper(
    REGISTER_DEPENDENCY,
    payload => payload,
    ({ form }) => ({ form })
  )({
    name,
    form,
    dependency,
  });
}

export function setFilterValue(form, name, filter) {
  return createActionHelper(
    SET_FIELD_FILTER,
    payload => payload,
    ({ form }) => ({ form })
  )({
    form,
    name,
    filter,
  });
}

/**
 * установить флаг обазяательного поля
 * @param form
 * @param name
 */
export function setRequired(form, name) {
  return createActionHelper(
    SET_REQUIRED,
    payload => payload,
    ({ form }) => ({ form, field: name })
  )({
    name,
    form,
  });
}

/**
 * снять флаг обазяательного поля
 * @param form
 * @param name
 */
export function unsetRequired(form, name) {
  return createActionHelper(
    UNSET_REQUIRED,
    payload => payload,
    ({ form }) => ({ form, field: name })
  )({
    name,
    form,
  });
}

/**
 * Поставить флаг загрузки
 * @param form
 * @param name
 * @param loading
 * @return {{type}}
 */
export function setLoading(form, name, loading) {
  return createActionHelper(
    SET_LOADING,
    payload => payload,
    ({ form }) => ({ form })
  )({
    form,
    name,
    loading,
  });
}
