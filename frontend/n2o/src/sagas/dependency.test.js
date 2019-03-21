import { modify, checkAndModify, resolveDependency } from './fieldDependency';
import { REGISTER_FIELD_EXTRA } from '../constants/formPlugin';
import { DISABLE_FIELD, ENABLE_FIELD, SHOW_FIELD, HIDE_FIELD } from '../constants/formPlugin';

const setupModify = (type, options) => {
  const values = {
    testField: 0
  };

  const formName = 'testForm';
  const fieldName = 'testField';

  return modify(values, formName, fieldName, type, options);
};

const setupCheckAndModify = () => {
  const values = {
    first: 'first value',
    second: 'second value'
  };
  const fields = {
    first: {
      fieldId: 'first',
      field: {
        dependency: {
          on: ['first'],
          type: 'enabled'
        }
      }
    }
  };
  const formName = 'testForm';
  const fieldName = 'first';
  return checkAndModify(values, fields, formName, fieldName, REGISTER_FIELD_EXTRA);
};

describe('Проверка саги dependency', () => {
  describe('Проверка modify', () => {
    it('Проверка type enabled с истинным expression', () => {
      const gen = setupModify('enabled', {
        expression: `testField === 0`
      });
      let next = gen.next();
      expect(next.value.PUT.action.type).toEqual(ENABLE_FIELD);
      expect(next.value.PUT.action.payload).toEqual({
        name: 'testField',
        form: 'testForm'
      });
      expect(next.value.PUT.action.meta).toEqual({
        form: 'testForm'
      });
      next = gen.next();
      expect(next.done).toEqual(true);
    });
    it('Проверка type enabled с ложным expression', () => {
      const gen = setupModify('enabled', {
        expression: `testField != 0`
      });
      let next = gen.next();
      expect(next.value.PUT.action.type).toEqual(DISABLE_FIELD);
      expect(next.value.PUT.action.payload).toEqual({
        name: 'testField',
        form: 'testForm'
      });
      expect(next.value.PUT.action.meta).toEqual({
        form: 'testForm'
      });
      next = gen.next();
      expect(next.done).toEqual(true);
    });
    it('Проверка type visible с истинным expression', () => {
      const gen = setupModify('visible', {
        expression: `testField === 0`
      });
      let next = gen.next();
      expect(next.value.PUT.action.type).toEqual(SHOW_FIELD);
      expect(gen.next().done).toEqual(true);
    });
    it('Проверка type visible с ложным expression', () => {
      const gen = setupModify('visible', {
        expression: `testField != 0`
      });
      let next = gen.next();
      expect(next.value.PUT.action.type).toEqual(HIDE_FIELD);
      expect(gen.next().done).toEqual(true);
    });
    it('Проверка type setValue', () => {
      const gen = setupModify('setValue', {
        expression: `10 + 2`
      });
      let next = gen.next();
      expect(next.value.PUT.action.payload).toEqual(12);
      expect(gen.next().done).toEqual(true);
    });
    it('Проверка type reset с ложным expression', () => {
      const gen = setupModify('reset', {
        expression: `testField != 0`
      });
      let next = gen.next();
      expect(next.value).toEqual(false);
      expect(gen.next().done).toEqual(true);
    });
    it('Проверка type reset с истинным expression', () => {
      const gen = setupModify('reset', {
        expression: `testField === 0`
      });
      let next = gen.next();
      expect(next.value.PUT.action.payload).toEqual(null);
      expect(gen.next().done).toEqual(true);
    });
  });
});
