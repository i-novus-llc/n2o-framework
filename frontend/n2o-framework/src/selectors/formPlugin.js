import { createSelector } from 'reselect';
import _ from 'lodash';

/**
 * селектор для редакс-форм
 * @param state
 * @returns {{}}
 */
export const formsSelector = state => {
  return state.form || {};
};

/**
 * селектор для  конкретной формы
 * @param name
 */
export const makeFormByName = name =>
  createSelector(
    formsSelector,
    formsState => {
      return _.get(formsState, name) || {};
    }
  );

/**
 * селктор для поля формы
 * @param formName
 * @param fieldName
 */
export const makeFieldByName = (formName, fieldName) =>
  createSelector(
    makeFormByName(formName),
    form => {
      return (
        (form && form.registeredFields && form.registeredFields[fieldName]) ||
        {}
      );
    }
  );

/**
 * селектор для значения видимости поля
 * @param formName
 * @param fieldName
 */
export const isVisibleSelector = (formName, fieldName) =>
  createSelector(
    makeFieldByName(formName, fieldName),
    field => {
      return field.visible;
    }
  );

/**
 * селектор для значения активности поля
 * @param formName
 * @param fieldName
 */
export const isDisabledSelector = (formName, fieldName) =>
  createSelector(
    makeFieldByName(formName, fieldName),
    field => {
      return field.disabled;
    }
  );

/**
 * селектор для свойства, отвечающего за инициализацию дополнительных свойств
 * @param formName
 * @param fieldName
 */
export const isInitSelector = (formName, fieldName) =>
  createSelector(
    makeFieldByName(formName, fieldName),
    field => {
      return field.isInit;
    }
  );

export const messageSelector = (formName, fieldName) =>
  createSelector(
    makeFieldByName(formName, fieldName),
    field => {
      return field.message;
    }
  );

export const dependencySelector = (formName, fieldName) =>
  createSelector(
    makeFieldByName(formName, fieldName),
    field => {
      return field.dependency;
    }
  );

export const filterSelector = (formName, fieldName) =>
  createSelector(
    makeFieldByName(formName, fieldName),
    field => {
      return field.filter;
    }
  );

export const requiredSelector = (formName, fieldName) =>
  createSelector(
    makeFieldByName(formName, fieldName),
    field => {
      return field.required;
    }
  );
