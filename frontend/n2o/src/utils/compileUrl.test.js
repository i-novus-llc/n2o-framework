import compileUrl from './compileUrl';

describe('Проверка compileUrl', () => {
  it('должен вернуть url', () => {
    const expectedUrl = '/test/url';
    const url = compileUrl(expectedUrl, {
      pathMapping: {},
      queryMapping: {},
    });
    expect(url).toEqual(expectedUrl);
  });
  it('должен вернуть url с pathMapping', () => {
    const expectedUrl = '/n2o/test/123/321';
    const url = compileUrl(
      '/n2o/:param1/:param2/:param3',
      {
        pathMapping: {
          param1: {
            link: 'test.param1',
          },
          param2: {
            link: 'test.param2',
          },
          param3: {
            link: 'test.param3',
          },
        },
      },
      {
        test: {
          param1: 'test',
          param2: '123',
          param3: '321',
        },
      }
    );
    expect(url).toEqual(expectedUrl);
  });

  it('должен вернуть url с queryMapping', () => {
    const expectedUrl = '/google.com?q=test';
    const url = compileUrl(
      'google.com',
      {
        queryMapping: {
          q: {
            link: 'test',
            value: '`q`',
          },
        },
      },
      {
        test: {
          q: 'test',
        },
      }
    );
    expect(url).toEqual(expectedUrl);
  });

  it('должен вернуть url с pathMapping и queryMapping', () => {
    const expectedUrl = '/n2o/firstParam/123?q=value';
    const url = compileUrl(
      '/n2o/:param1/:param2',
      {
        pathMapping: {
          param1: {
            link: 'test.param1',
          },
          param2: {
            link: 'test.param2',
          },
        },
        queryMapping: {
          q: {
            link: 'test.test',
            value: '`q`',
          },
        },
      },
      {
        test: {
          param1: 'firstParam',
          param2: '123',
          test: {
            q: 'value',
          },
        },
      }
    );
    expect(url).toEqual(expectedUrl);
  });
});
