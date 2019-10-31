import {
  setIn,
  generateFlatQuery,
  getWidgetId,
  isPromise,
  difference,
  omitDeep,
} from './helpers';

describe('Проверка helpers', () => {
  it('setIn сделает set', () => {
    expect(
      setIn(
        {
          widgetId: {
            value: 'test',
          },
        },
        'widgetId.value',
        'supaTest'
      )
    ).toEqual({
      widgetId: {
        value: 'supaTest',
      },
    });
  });

  it('getWidgetId вернет widgetId', () => {
    expect(getWidgetId('page', 'container')).toEqual('page.container');
  });

  it('isPromise вернет true для промиса', () => {
    expect(isPromise(new Promise(resolve => {}))).toEqual(true);
  });

  it('isPromise вернет false для не промиса', () => {
    expect(isPromise(() => {})).toEqual(false);
    expect(isPromise(true)).toEqual(false);
    expect(isPromise('string')).toEqual(false);
    expect(isPromise(11)).toEqual(false);
    expect(isPromise(undefined)).toEqual(false);
    expect(isPromise(null)).toEqual(false);
  });

  it('difference вернет разницу объектов', () => {
    expect(
      difference(
        {
          test: {
            value: 1,
            value1: 'test',
          },
        },
        {
          test: {
            value: 5,
            value1: 'test',
          },
        }
      )
    ).toEqual({
      test: {
        value: 1,
      },
    });
    expect(difference({}, {})).toEqual({});
  });

  it('omitDeep удалит ключи', () => {
    expect(
      omitDeep(
        {
          test: {
            test1: {
              test2: 'value',
            },
          },
        },
        ['test2']
      )
    ).toEqual({
      test: {
        test1: {},
      },
    });
  });
});
