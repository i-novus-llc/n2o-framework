import fetchMock from 'fetch-mock';
import apiProvider, {
  FETCH_APP_CONFIG,
  FETCH_PAGE_METADATA,
  FETCH_WIDGET_DATA,
  FETCH_INVOKE_DATA,
  FETCH_VALIDATE,
} from './api';

const stubOptions = {
  a: '1',
  b: {
    c: '2',
  },
  d: {
    e: {
      f: '3',
    },
  },
  x: {
    y: ['3', '4', '5'],
  },
  z: ['6', '7'],
};

const stubOptionsString = '?a=1&b.c=2&d.e.f=3&x.y=3&x.y=4&x.y=5&z=6&z=7';

describe('Проверка api провайдера', () => {
  beforeEach(() => {
    fetchMock.restore().get('*', url => {
      return {
        status: 200,
        sendAsJson: true,
        body: {
          url,
        },
      };
    });
  });
  it('без параметров', async () => {
    expect.assertions(1);
    return expect(() => apiProvider()).toThrowError();
  });
  it('не существующий тип', async () => {
    expect.assertions(1);
    return expect(() => apiProvider('test')).toThrowError();
  });
  it('проверка запроса за конфигом', async () => {
    expect.assertions(1);
    return expect(apiProvider(FETCH_APP_CONFIG, stubOptions)).resolves.toEqual({
      url: 'n2o/config?a=1&b.c=2&d.e.f=3&x.y=3&x.y=4&x.y=5&z=6&z=7',
    });
  });
  it('проверка запроса за метаданными', async () => {
    expect.assertions(1);
    return expect(
      apiProvider(FETCH_PAGE_METADATA, { pageUrl: '/my/page/test' })
    ).resolves.toEqual({
      url: 'n2o/page/my/page/test',
    });
  });
  it('проверка запроса за данными', async () => {
    expect.assertions(1);
    return expect(
      apiProvider(FETCH_WIDGET_DATA, {
        basePath: 'my/data/test',
        baseQuery: stubOptions,
      })
    ).resolves.toEqual({ url: `my/data/test${stubOptionsString}` });
  });
  it('проверка запроса экшена (invoke)', async () => {
    expect.assertions(1);
    return expect(
      apiProvider(FETCH_INVOKE_DATA, {
        basePath: 'my/data/test',
        baseQuery: stubOptions,
        baseMethod: 'GET',
      })
    ).resolves.toEqual({ url: `my/data/test${stubOptionsString}` });
  });
  it('проверка запроса за валидацией', async () => {
    expect.assertions(1);
    return expect(apiProvider(FETCH_VALIDATE, stubOptions)).resolves.toEqual({
      url: `n2o/validation${stubOptionsString}`,
    });
  });
});
