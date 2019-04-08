import {
  getContainerColumns,
  isDisabledSelector,
  isInitSelector,
  isVisibleSelector,
} from './columns';

const state = {
  columns: {
    testKey: {
      columnId: {
        visible: true,
        disabled: false,
        isInit: true,
      },
    },
    otherKey: {},
  },
};

describe('Проверка селекторов columns', () => {
  it('getContainerColumns должен вернуть columns по ключу', () => {
    expect(getContainerColumns('testKey')(state)).toEqual(
      state.columns.testKey
    );
  });
  it('isVisibleSelector должен вернуть isVisible по key и id', () => {
    expect(isVisibleSelector('testKey', 'columnId')(state)).toEqual(
      state.columns.testKey.columnId.visible
    );
  });
  it('isDisabledSelector должен вернуть isDisabled по key и id', () => {
    expect(isDisabledSelector('testKey', 'columnId')(state)).toEqual(
      state.columns.testKey.columnId.disabled
    );
  });
  it('isInitSelector должен вернуть isInit по key и id', () => {
    expect(isInitSelector('testKey', 'columnId')(state)).toEqual(
      state.columns.testKey.columnId.isInit
    );
  });
});
