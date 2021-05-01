import React from 'react';
import {
  watcherDefaultModels,
  flowDefaultModels,
  compareAndResolve,
  getMetadata,
  processUrl,
  mappingUrlToRedux,
  queryMapping,
  pathMapping,
  applyPlaceholders,
} from './pages';
import { channel, runSaga } from 'redux-saga';
import {
  select,
  race,
  call,
  take,
  put,
  actionChannel,
} from 'redux-saga/effects';
import {
  METADATA_FAIL,
  METADATA_SUCCESS,
  RESET,
  SET_STATUS,
} from '../constants/pages';
import {
  SET,
  COPY,
  SYNC,
  REMOVE,
  UPDATE,
  UPDATE_MAP,
} from '../constants/models';
import { combineModels } from '../actions/models';
import { defaultApiProvider, FETCH_PAGE_METADATA } from '../core/api';
import fetchMock from 'fetch-mock';
import { FETCH_END, FETCH_START } from '../constants/fetch';
import { CHANGE_ROOT_PAGE } from '../constants/global';
import { DESTROY } from '../constants/overlays';
const delay = ms => new Promise(r => setTimeout(r, ms));
const resolveModelsValue = {
  "resolve['page_main_create'].name": {
    value: 'Test',
  },
};

const resolveModelsLink = {
  "resolve['page_main_create'].name": {
    link: "filter['field'].id",
  },
};

const resolveModelsLinkAndValue = {
  "resolve['page_main_create'].name": {
    link: "filter['field']",
    value: '`id`',
  },
};

fetchMock.get('n2o/page/testPage?name=Sergey', () => ({
  id: 'testPage',
  widgets: {},
}));

fetchMock.get('n2o/page/order/123?q=test', () => ({
  id: 'test',
  regions: {},
}));

describe('Сага для для наблюдения за изменением модели', () => {
  describe('тесты applyPlaceholders', () => {
    it('должна отработать корректно для всех случаев', () => {
      const key = 'id';
      const placeholders = { id: ':id' };
      const obj = {
        id: 'test.id',
        name: {
          name: 'test.name',
        },
        surname: '::self',
        other: 'test.other',
      };

      expect(applyPlaceholders(key, obj, placeholders)).toEqual({
        id: 'test.id',
        name: {
          name: 'test.name',
        },
        surname: ':id',
        other: 'test.other',
      });
    });
  });
  describe('тесты pathMapping', () => {
    it('должен вызывать pathMapping', () => {
      const gen = pathMapping(
        {
          search: '?name=Sergey',
          pathname: '/testRoot/:id',
          hash: '',
        },
        {
          list: [
            {
              path: '/test',
              exact: true,
              isOtherPage: true,
            },
            {
              path: '/testRoot/:id',
              exact: true,
              isOtherPage: true,
              params: {
                test: {},
              },
            },
          ],
          pathMapping: {
            id: {
              link: 'test.id',
            },
          },
          queryMapping: {
            q: {
              link: 'model',
              value: '`q`',
            },
          },
        }
      );

      const value = gen.next();
      expect(value.value.type).toBe('PUT');
      expect(value.value.payload.action.type).toBe('BATCHING_REDUCER.BATCH');
      expect(gen.next().done).toBeTruthy();
    });
  });
  describe('тесты queryMapping', () => {
    it('должен вызвать queryMapping', () => {
      const gen = queryMapping(
        {
          search: '?name=Sergey',
          pathname: '/testRoot/:id',
          hash: '',
        },
        {
          list: [
            {
              path: '/test',
              exact: true,
              isOtherPage: true,
            },
            {
              path: '/testRoot/:id',
              exact: true,
              isOtherPage: true,
              params: {
                test: {},
              },
            },
          ],
          pathMapping: {
            id: {
              link: 'test.id',
            },
          },
          queryMapping: {
            q: {
              link: 'model',
              value: '`q`',
            },
          },
        }
      );
      const value = gen.next();
      expect(value.value.type).toBe('PUT');
      expect(value.value.payload.action.type).toBe('BATCHING_REDUCER.BATCH');
      expect(gen.next().done).toBeTruthy();
    });
  });
  describe('тесты mappingUrlToRedux', () => {
    it('должен вызвать маппинги', async () => {
      const dispatched = [];
      const fakeStore = {
        getState: () => ({
          test: {
            id: 123,
          },
          router: {
            location: {
              search: '?name=Sergey',
              pathname: '/testRoot/:id',
              hash: '',
            },
            action: 'REPLACE',
          },
          global: {
            rootPageId: 'testRoot',
          },
          pages: {
            testRoot: {
              metadata: {
                routes: {
                  list: [
                    {
                      path: '/test',
                      exact: true,
                      isOtherPage: true,
                    },
                    {
                      path: '/testRoot/:id',
                      exact: true,
                      isOtherPage: true,
                      params: {
                        test: {},
                      },
                    },
                  ],
                  pathMapping: {
                    id: {
                      link: 'test.id',
                    },
                  },
                  queryMapping: {
                    q: {
                      link: 'model',
                      value: '`q`',
                    },
                  },
                },
              },
            },
          },
        }),
        dispatch: action => dispatched.push(action),
      };

      await runSaga(fakeStore, mappingUrlToRedux, {
        list: [
          {
            path: '/test',
            exact: true,
            isOtherPage: true,
          },
          {
            path: '/testRoot/:id',
            exact: true,
            isOtherPage: true,
            params: {
              test: {},
            },
          },
        ],
        pathMapping: {
          id: {
            link: 'test.id',
          },
        },
        queryMapping: {
          q: {
            link: 'model',
            value: '`q`',
          },
        },
      });
      await delay(300);

      expect(dispatched.length === 2).toBeTruthy();
    });
  });
  describe('тесты processUrl', () => {
    it('должен замапить url в редакс', async () => {
      const dispatched = [];
      const fakeStore = {
        getState: () => ({
          test: {
            id: 123,
          },
          router: {
            location: {
              search: '?name=Sergey',
              pathname: '/testRoot/:id',
              hash: '',
            },
            action: 'REPLACE',
          },
          global: {
            rootPageId: 'testRoot',
          },
          pages: {
            testRoot: {
              metadata: {
                routes: {
                  list: [
                    {
                      path: '/test',
                      exact: true,
                      isOtherPage: true,
                    },
                    {
                      path: '/testRoot/:id',
                      exact: true,
                      isOtherPage: true,
                      params: {
                        test: {},
                      },
                    },
                  ],
                  pathMapping: {
                    id: {
                      link: 'test.id',
                    },
                  },
                  queryMapping: {
                    q: {
                      link: 'model',
                      value: '`q`',
                    },
                  },
                },
              },
            },
          },
        }),
        dispatch: action => dispatched.push(action),
      };

      await runSaga(fakeStore, processUrl);
      await delay(300);

      expect(dispatched[0].type).toBe('BATCHING_REDUCER.BATCH');
      expect(dispatched[0].payload).toEqual([{ link: 'test.id' }]);
      expect(dispatched[1].type).toBe('BATCHING_REDUCER.BATCH');
      expect(dispatched[1].payload).toEqual([]);
    });
  });
  describe('тесты getMetadata', () => {
    it('должен получить метаданные, если есть rootPage', async () => {
      const dispatched = [];
      const fakeStore = {
        getState: () => ({
          router: {
            location: {
              search: '?name=Sergey',
            },
          },
        }),
        dispatch: action => dispatched.push(action),
      };
      const action = {
        payload: {
          pageId: 'testPage',
          rootPage: 'testPage',
          pageUrl: '/testPage',
        },
      };

      await runSaga(fakeStore, getMetadata, undefined, action);
      await delay(200);
      expect(dispatched[0].type).toBe(FETCH_START);
      expect(dispatched[0].payload.options.pageUrl).toBe(
        '/testPage?name=Sergey'
      );
      expect(dispatched[1].type).toBe(FETCH_END);
      expect(dispatched[1].payload).toEqual({
        fetchType: FETCH_PAGE_METADATA,
        options: {
          pageUrl: '/testPage?name=Sergey',
        },
        response: {
          id: 'testPage',
          widgets: {},
        },
      });
      expect(dispatched[2].type).toBe(CHANGE_ROOT_PAGE);
      expect(dispatched[2].payload).toEqual({
        rootPageId: 'testPage',
      });
      expect(dispatched[3].type).toBe(DESTROY);
      expect(dispatched[4].type).toBe(SET_STATUS);
      expect(dispatched[4].payload).toEqual({
        pageId: 'testPage',
        status: 200,
      });
      expect(dispatched[5].type).toBe(METADATA_SUCCESS);
      expect(dispatched[5].payload).toEqual({
        pageId: 'testPage',
        json: {
          id: 'testPage',
          widgets: {},
        },
      });
    });

    it('должен вернуть метаданные с маппингом url', async () => {
      const dispatched = [];
      const fakeStore = {
        getState: () => ({
          router: {
            location: {
              search: '',
            },
          },
          model: {
            id: 123,
            q: 'test',
          },
        }),
        dispatch: action => dispatched.push(action),
      };
      const action = {
        payload: {
          pageId: 'order',
          pageUrl: '/order/:id',
          mapping: {
            pathMapping: {
              id: {
                link: 'model.id',
              },
            },
            queryMapping: {
              q: {
                link: 'model',
                value: '`q`',
              },
            },
          },
        },
      };

      await runSaga(fakeStore, getMetadata, undefined, action);
      await delay(200);
      expect(dispatched[0].payload.options.pageUrl).toBe('/order/123?q=test');
      expect(dispatched[1].payload.response).toEqual({
        id: 'test',
        regions: {},
      });
      expect(dispatched[2].payload).toEqual({
        pageId: 'test',
        status: 200,
      });
    });

    it('должен вызвать ошибку', async () => {
      const dispatched = [];
      const fakeStore = {
        getState: () => ({}),
        dispatch: action => dispatched.push(action),
      };
      const action = {
        payload: {
          pageId: 'errorPage',
        },
      };

      await runSaga(fakeStore, getMetadata, undefined, action);
      await delay(200);

      expect(dispatched[0].type).toBe(METADATA_FAIL);
      expect(dispatched[0].payload).toEqual({
        pageId: 'errorPage',
        err: {
          closeButton: false,
          label: 'Ошибка',
          severity: 'danger',
          text: "Cannot read property 'search' of undefined",
        },
      });
    });
  });
  it('Проверяем watcher дефолтных моделей', () => {
    const config = { 'a.b.c': { value: 'test' } };
    const gen = watcherDefaultModels(config);
    expect(gen.next().value).toEqual(
      race([call(flowDefaultModels, config), take(RESET)])
    );
    expect(gen.next().done).toEqual(true);
  });
  it('Проверяем flowDefaultModels - выход без конфига', () => {
    const gen = flowDefaultModels();
    expect(gen.next().value).toEqual(false);
    expect(gen.next().done).toEqual(true);
  });
  it('Проверяем flowDefaultModels - только init значение, без подписи', () => {
    const config = { 'a.b.c': { value: 'test' } };
    const state = { a: { b: { c: 1 } } };
    const gen = flowDefaultModels(config);
    expect(gen.next().value).toEqual(select());
    expect(gen.next(state).value).toEqual(
      call(compareAndResolve, config, state)
    );
    expect(gen.next(compareAndResolve(config, state)).value).toEqual(
      put(combineModels(compareAndResolve(config, state)))
    );
    expect(gen.next().done).toEqual(true);
  });
  it('Проверяем flowDefaultModels - observe без link', () => {
    const config = { 'a.b.c': { value: 'test', observe: true } };
    const gen = flowDefaultModels(config);
    gen.next();
    gen.next();
    expect(gen.next().done).toEqual(true);
  });
  it('Проверяем flowDefaultModels - observe', () => {
    const config = { 'a.b.c': { value: 'test', link: 'z.x.c', observe: true } };
    const state = { z: { x: { c: 1 } } };
    const gen = flowDefaultModels(config);
    gen.next();
    gen.next();
    const mockChan = channel();
    expect(gen.next().value).toEqual(
      actionChannel([SET, COPY, SYNC, REMOVE, UPDATE, UPDATE_MAP])
    );
    expect(gen.next(mockChan).value).toEqual(select());
    expect(gen.next().value).toEqual(take(mockChan));
    expect(gen.next().value).toEqual(select());
    expect(gen.next(state).value).toEqual(
      call(compareAndResolve, config, state)
    );
    expect(gen.next(compareAndResolve(config, state)).value).toEqual(
      put(combineModels(compareAndResolve(config, state)))
    );
  });
  it('Проверка compareAndResolve если модели в стейте пустые', () => {
    expect(compareAndResolve(resolveModelsValue, {})).toEqual({
      resolve: { page_main_create: { name: 'Test' } },
    });
  });
  it('Проверка compareAndResolve если пришел только link и модель в стейте пустая', () => {
    expect(compareAndResolve(resolveModelsLink, {})).toEqual({});
  });
  it('Проверка compareAndResolve если пришел только link и есть что зарезолвить', () => {
    expect(
      compareAndResolve(resolveModelsLink, { filter: { field: { id: 1 } } })
    ).toEqual({
      resolve: { page_main_create: { name: 1 } },
    });
  });
  it('Проверка compareAndResolve если пришел пришед link и value и есть что зарезолвить', () => {
    expect(
      compareAndResolve(resolveModelsLinkAndValue, {
        filter: { field: { id: 1 } },
      })
    ).toEqual({
      resolve: { page_main_create: { name: 1 } },
    });
  });
});
