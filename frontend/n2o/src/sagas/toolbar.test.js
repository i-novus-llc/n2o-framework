import { checkOnNeed, getDependencyButtons } from './toolbar';

describe('Проверка саги toolbar', () => {
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
