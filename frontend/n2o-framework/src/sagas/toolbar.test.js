import { put } from '@redux-saga/core/effects';
import {
  CHANGE_BUTTON_DISABLED,
  CHANGE_BUTTON_VISIBILITY,
  CHANGE_BUTTON_MESSAGE,
} from '../constants/toolbar';
import {
  resolveButton,
  setParentVisibleIfAllChildChangeVisible,
} from './toolbar';
import { changeButtonVisiblity } from '../actions/toolbar';

const setupResolveButton = () => {
  return resolveButton({
    conditions: {
      visible: [
        {
          expression: "test === 'test'",
          modelLink: 'model',
        },
      ],
      enabled: [
        {
          expression: "test !== 'test'",
          modelLink: 'model',
          message: 'test message',
        },
      ],
    },
  });
};

describe('Проверка саги toolbar', () => {
  it('Тестирование вызова  экшена на саге', () => {
    const gen = setupResolveButton();
    gen.next();
    let { value } = gen.next({ model: { test: 'test' } });
    expect(value.payload.action.type).toEqual(CHANGE_BUTTON_VISIBILITY);
    expect(value.payload.action.payload.visible).toBe(true);
    gen.next();
    value = gen.next().value;
    expect(value.payload.action.type).toEqual(CHANGE_BUTTON_DISABLED);
    expect(value.payload.action.payload.disabled).toBe(true);
    value = gen.next().value;
    expect(value.payload.action.type).toEqual(CHANGE_BUTTON_MESSAGE);
    expect(value.payload.action.payload.message).toBe('test message');
    gen.next();
    expect(gen.next().done).toBe(true);
  });
});

describe('setParentVisibleIfAllChildChangeVisible', () => {
  it('Тестирование скрытия родителя если все потомки скрыты', () => {
    const testData = {
      btnId: {
        visible: true,
      },
      btnChild1Id: {
        visible: false,
        parentId: 'btnId',
      },
      btnChild2Id: {
        visible: false,
        parentId: 'btnId',
      },
    };
    const gen = setParentVisibleIfAllChildChangeVisible({
      id: 'btnChild1Id',
      key: 'fieldKey',
    });
    gen.next();
    expect(gen.next(testData).value).toEqual(
      put(changeButtonVisiblity('fieldKey', 'btnId', false))
    );
    expect(gen.next().done).toBe(true);
  });
  it('Тестирование показа родителя если все потомки видимы', () => {
    const testData = {
      btnId: {
        visible: false,
      },
      btnChild1Id: {
        visible: true,
        parentId: 'btnId',
      },
      btnChild2Id: {
        visible: true,
        parentId: 'btnId',
      },
    };
    const gen = setParentVisibleIfAllChildChangeVisible({
      id: 'btnChild1Id',
      key: 'fieldKey',
    });
    gen.next();
    expect(gen.next(testData).value).toEqual(
      put(changeButtonVisiblity('fieldKey', 'btnId', true))
    );
    expect(gen.next().done).toBe(true);
  });
  it('Экшен не отправляется если родитель имеет такую же видимость как и потомки', () => {
    const testData = {
      btnId: {
        visible: true,
      },
      btnChild1Id: {
        visible: true,
        parentId: 'btnId',
      },
      btnChild2Id: {
        visible: true,
        parentId: 'btnId',
      },
    };
    const gen = setParentVisibleIfAllChildChangeVisible({
      id: 'btnChild1Id',
      key: 'fieldKey',
    });
    gen.next();
    expect(gen.next().done).toBe(true);
  });
});
