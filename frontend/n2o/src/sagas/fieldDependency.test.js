import { modify, checkAndModify } from './fieldDependency';
import { REGISTER_FIELD_EXTRA } from '../constants/formPlugin';
import {
  DISABLE_FIELD,
  ENABLE_FIELD,
  SHOW_FIELD,
  HIDE_FIELD,
} from '../constants/formPlugin';
import { put } from 'redux-saga/effects';
import { showField } from '../actions/formPlugin';
import { isEmpty } from 'lodash';

const setupModify = (type, options) => {
  const values = {
    testField: 0,
  };

  const formName = 'testForm';
  const fieldName = 'testField';

  return modify(values, formName, fieldName, type, options);
};

const setupCheckAndModify = () => {
  const values = {
    first: 'first value',
    second: 'second value',
  };
  const fields = {
    first: {
      fieldId: 'first',
      field: {
        dependency: {
          on: ['first'],
          type: 'enabled',
        },
      },
    },
  };
  const formName = 'testForm';
  const fieldName = 'first';
  return checkAndModify(
    values,
    fields,
    formName,
    fieldName,
    REGISTER_FIELD_EXTRA
  );
};

describe('Проверка саги dependency', () => {
  describe('Проверка modify', () => {
    it('Проверка type enabled с истинным expression', () => {
      const gen = setupModify('enabled', {
        expression: `testField === 0`,
      });
      let next = gen.next();
      expect(!isEmpty(next.value['SELECT'])).toEqual(true);
      next = gen.next();
      expect(next.value.PUT.action.type).toEqual(ENABLE_FIELD);
      expect(next.value.PUT.action.payload).toEqual({
        name: 'testField',
        form: 'testForm',
      });
      expect(next.value.PUT.action.meta).toEqual({
        form: 'testForm',
      });
      expect(gen.next().done).toEqual(true);
    });
    it('Проверка type enabled с ложным expression', () => {
      const gen = setupModify('enabled', {
        expression: `testField != 0`,
      });
      let next = gen.next();
      next = gen.next();
      expect(next.value.PUT.action.type).toEqual(DISABLE_FIELD);
      expect(next.value.PUT.action.payload).toEqual({
        name: 'testField',
        form: 'testForm',
      });
      expect(next.value.PUT.action.meta).toEqual({
        form: 'testForm',
      });
      next = gen.next();
      expect(next.done).toEqual(true);
    });
    it('Проверка type visible с истинным expression', () => {
      const gen = setupModify('visible', {
        expression: `testField === 0`,
      });
      let next = gen.next();
      expect(!isEmpty(next.value['SELECT'])).toEqual(true);
      next = gen.next();
      expect(next.value.PUT.action.type).toEqual(SHOW_FIELD);
      expect(gen.next().done).toEqual(true);
    });
    it('Проверка type visible с ложным expression', () => {
      const gen = setupModify('visible', {
        expression: `testField != 0`,
      });
      let next = gen.next();
      expect(!isEmpty(next.value['SELECT'])).toEqual(true);
      next = gen.next();
      expect(next.value.PUT.action.type).toEqual(HIDE_FIELD);
      expect(gen.next().done).toEqual(true);
    });
    it('Проверка type setValue', () => {
      const gen = setupModify('setValue', {
        expression: `10 + 2`,
      });
      let next = gen.next();
      expect(!isEmpty(next.value['SELECT'])).toEqual(true);
      next = gen.next();
      expect(next.value.PUT.action.payload).toEqual({
        field: 'testField',
        key: 'testForm',
        prefix: 'resolve',
        value: 12,
      });
      expect(gen.next().done).toEqual(true);
    });
    it('Проверка type reset с ложным expression', () => {
      const gen = setupModify('reset', {
        expression: `testField != 0`,
      });
      let next = gen.next();
      expect(!isEmpty(next.value['SELECT'])).toEqual(true);
      next = gen.next();
      expect(next.value).toEqual(false);
      expect(gen.next().done).toEqual(true);
    });
    it('Проверка type reset с истинным expression', () => {
      const gen = setupModify('reset', {
        expression: `testField === 0`,
      });
      let next = gen.next();
      expect(!isEmpty(next.value['SELECT'])).toEqual(true);
      next = gen.next();
      expect(next.value.PUT.action.payload).toEqual({
        field: 'testField',
        key: 'testForm',
        prefix: 'resolve',
        value: null,
      });
      expect(gen.next().done).toEqual(true);
    });
    it('Проверка on c точкой', () => {
      const gen = modify(
        {
          field: {
            id: 0,
          },
        },
        'testForm',
        'field.id',
        'visible',
        {
          expression: `field.id === 0`,
        }
      );
      let next = gen.next();
      expect(!isEmpty(next.value['SELECT'])).toEqual(true);
      next = gen.next();
      expect(next.value).toEqual(put(showField('testForm', 'field.id')));
      expect(gen.next().done).toEqual(true);
    });
  });
});
