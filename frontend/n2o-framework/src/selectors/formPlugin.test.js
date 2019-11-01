import {
  formsSelector,
  makeFieldByName,
  makeFormByName,
  isVisibleSelector,
  isDisabledSelector,
  isInitSelector,
  messageSelector,
  dependencySelector,
  filterSelector,
  loadingSelector,
} from './formPlugin';

const state = {
  form: {
    formName: {
      registeredFields: {
        field1: {
          visible: true,
          disabled: false,
          isInit: true,
          message: 'test',
          dependency: 'some dependency',
          filter: 'field filter',
          loading: true,
        },
        field2: {
          visible: false,
          disabled: true,
          isInit: false,
          message: 'message',
          loading: false,
        },
      },
    },
    otherForm: {},
  },
};

describe('Проверка селекторов formPlugin', () => {
  it('formSelector должен вернуть form', () => {
    expect(formsSelector(state)).toEqual(state.form);
  });
  it('makeFormByName должен вернуть форму по имени', () => {
    expect(makeFormByName('formName')(state)).toEqual(state.form.formName);
  });
  it('makeFieldByName должен вернуть поле по имени формы и поля', () => {
    expect(makeFieldByName('formName', 'field1')(state)).toEqual(
      state.form.formName.registeredFields.field1
    );
  });
  it('isVisibleSelector должен вернуть visible поля', () => {
    expect(isVisibleSelector('formName', 'field1')(state)).toEqual(
      state.form.formName.registeredFields.field1.visible
    );
  });
  it('isDisabledSelector должен вернуть disabled поля', () => {
    expect(isDisabledSelector('formName', 'field1')(state)).toEqual(
      state.form.formName.registeredFields.field1.disabled
    );
  });
  it('isInitSelector должен вернуть isInit поля', () => {
    expect(isInitSelector('formName', 'field1')(state)).toEqual(
      state.form.formName.registeredFields.field1.isInit
    );
  });
  it('messageSelector должен вернуть message поля', () => {
    expect(messageSelector('formName', 'field1')(state)).toEqual(
      state.form.formName.registeredFields.field1.message
    );
  });
  it('dependencySelector должен вернуть dependency поля', () => {
    expect(dependencySelector('formName', 'field1')(state)).toEqual(
      state.form.formName.registeredFields.field1.dependency
    );
  });
  it('filterSelector должен вернуть filter поля', () => {
    expect(filterSelector('formName', 'field1')(state)).toEqual(
      state.form.formName.registeredFields.field1.filter
    );
  });
  it('loadingSelector должен вернуть loading поля', () => {
    expect(loadingSelector('formName', 'field1')(state)).toBe(true);
    expect(loadingSelector('formName', 'field2')(state)).toBe(false);
  });
});
