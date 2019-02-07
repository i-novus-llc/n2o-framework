import { checkOnNeed, getDependencyButtons } from './toolbar';
import { CHANGE_BUTTON_DISABLED, CHANGE_BUTTON_VISIBILITY } from '../constants/toolbar';
import { put } from 'redux-saga/effects';
import {
  resolveButton,
  resolveConditions,
  setParentVisibleIfAllChildChangeVisible
} from './toolbar';
import { changeButtonVisiblity } from '../actions/toolbar';

const setupResolveButton = () => {
  return resolveButton({
    conditions: {
      visible: [
        {
          expression: "test === 'test'",
          modelLink: 'model'
        }
      ],
      enabled: [
        {
          expression: "test !== 'test'",
          modelLink: 'model'
        }
      ]
    }
  });
};

describe('Проверка саги toolbar', () => {
  it('Тестирование вызова  экшена на саге', () => {
    const gen = setupResolveButton();
    gen.next();
    let { value } = gen.next({ model: { test: 'test' } });
    expect(value['PUT'].action.type).toEqual(CHANGE_BUTTON_VISIBILITY);
    expect(value['PUT'].action.payload.visible).toBe(true);
    gen.next();
    value = gen.next().value;
    expect(value['PUT'].action.type).toEqual(CHANGE_BUTTON_DISABLED);
    expect(value['PUT'].action.payload.disabled).toBe(true);
    expect(gen.next().done).toBe(true);
  });
  it('Тестирование resolveConditions', () => {
    expect(
      resolveConditions(
        [
          {
            expression: "test === 'test'",
            modelLink: 'model'
          }
        ],
        { model: { test: 'test' } }
      )
    ).toBe(true);
    expect(
      resolveConditions(
        [
          {
            expression: "test === 'test'",
            modelLink: 'no_model'
          }
        ],
        { model: { test: 'test' } }
      )
    ).toBe(false);
  });
  it('Тестирование resolveConditions на null condition', () => {
    expect(
      resolveConditions(
        [
          {
            expression: "test === 'test'",
            modelLink: 'model'
          }
        ],
        null
      )
    ).toBe(false);
  });
});

describe('setParentVisibleIfAllChildChangeVisible', () => {
  it('Тестирование скрытия родителя если все потомки скрыты', () => {
    const testData = {
      btnId: {
        visible: true
      },
      btnChild1Id: {
        visible: false,
        parentId: 'btnId'
      },
      btnChild2Id: {
        visible: false,
        parentId: 'btnId'
      }
    };
    const gen = setParentVisibleIfAllChildChangeVisible({ id: 'btnChild1Id', key: 'fieldKey' });
    gen.next();
    expect(gen.next(testData).value).toEqual(
      put(changeButtonVisiblity('fieldKey', 'btnId', false))
    );
    expect(gen.next().done).toBe(true);
  });
  it('Тестирование показа родителя если все потомки видимы', () => {
    const testData = {
      btnId: {
        visible: false
      },
      btnChild1Id: {
        visible: true,
        parentId: 'btnId'
      },
      btnChild2Id: {
        visible: true,
        parentId: 'btnId'
      }
    };
    const gen = setParentVisibleIfAllChildChangeVisible({ id: 'btnChild1Id', key: 'fieldKey' });
    gen.next();
    expect(gen.next(testData).value).toEqual(put(changeButtonVisiblity('fieldKey', 'btnId', true)));
    expect(gen.next().done).toBe(true);
  });
  it('Экшен не отправляется если родитель имеет такую же видимость как и потомки', () => {
    const testData = {
      btnId: {
        visible: true
      },
      btnChild1Id: {
        visible: true,
        parentId: 'btnId'
      },
      btnChild2Id: {
        visible: true,
        parentId: 'btnId'
      }
    };
    const gen = setParentVisibleIfAllChildChangeVisible({ id: 'btnChild1Id', key: 'fieldKey' });
    gen.next();
    expect(gen.next().done).toBe(true);
  });
  it('checkOnNeed должен вернуть объект кнопки с совпадающим в conditions visible modelLink', () => {
    expect(
      checkOnNeed(
        {
          name: 'test',
          conditions: {
            visible: [
              {
                modelLink: 'test_link'
              }
            ]
          }
        },
        'test_link'
      )
    ).toEqual({
      name: 'test',
      conditions: {
        visible: [
          {
            modelLink: 'test_link'
          }
        ]
      }
    });
    expect(
      checkOnNeed(
        {
          name: 'test',
          conditions: {
            enabled: [
              {
                modelLink: 'other_link'
              }
            ]
          }
        },
        'other_link'
      )
    ).toEqual({
      name: 'test',
      conditions: {
        enabled: [
          {
            modelLink: 'other_link'
          }
        ]
      }
    });
  });
  it('checkOnNeed должен вернуть false для объекта кнопки с несовпадающим modelLink', () => {
    expect(
      checkOnNeed(
        {
          name: 'button without conditions',
          conditions: {
            visible: [
              {
                modelLink: 'other link to model'
              }
            ]
          }
        },
        'models[resolve][test]'
      )
    ).toEqual(false);
  });
  it('checkOnNeed должен вернуть false для объекта кнопки без conditions', () => {
    expect(checkOnNeed({})).toEqual(false);
  });
  it('getDependencyButton должен вернуть кнопки с подходящим modelLink', () => {
    const modelLink = 'neededLink';
    const toolbar = {
      widgetId1: {
        firstButton: {
          name: 'firstButton',
          conditions: {
            visible: [
              {
                modelLink
              }
            ]
          }
        },
        secondButton: {
          name: 'secondButton',
          conditions: {
            enabled: [
              {
                modelLink
              }
            ]
          }
        }
      },
      widgetId2: {
        buttonWithCond: {
          name: 'newButton',
          conditions: {
            visible: [
              {
                modelLink
              }
            ]
          }
        },
        buttonWithoutCond: {
          name: 'buttonWithoutLink'
        }
      }
    };
    expect(getDependencyButtons(toolbar, modelLink)).toEqual([
      toolbar.widgetId1.firstButton,
      toolbar.widgetId1.secondButton,
      toolbar.widgetId2.buttonWithCond
    ]);
  });
});
