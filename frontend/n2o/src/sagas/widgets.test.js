import {
  prepareFetch,
  resolveUrl,
  runResolve,
  clearOnDisable,
  setWidgetDataSuccess,
  routesQueryMapping,
  handleFetch,
  resolveVisibleDependency,
  resolveEnabledDependency,
  resolveFetchDependency,
  resolveWidgetDependency
} from './widgets';
import { runSaga } from 'redux-saga';
import { put } from 'redux-saga/effects';
import { setModel } from '../actions/models';
import { PREFIXES } from '../constants/models';
import {
  changeCountWidget,
  changePageWidget,
  setWidgetMetadata,
  resetWidgetState,
  dataSuccessWidget
} from '../actions/widgets';
import * as api from './fetch';
import { UPDATE } from '../constants/models';
import { dataFailWidget } from '../actions/widgets';
import { DATA_REQUEST, DISABLE, ENABLE, HIDE, SHOW } from '../constants/widgets';

describe('Проверка саги widgets', () => {
  it('handleFetch должен выпасть с ошибкой', async () => {
    const dispatched = [];
    const fakeStore = {
      getState: () => ({}),
      dispatch: action => dispatched.push(action)
    };
    const widgetId = 'testId';
    const options = {};
    const withoutSelectedId = true;
    await runSaga(fakeStore, handleFetch, widgetId, options, () => {}, withoutSelectedId);
    expect(dispatched[0]).toEqual(put(dataFailWidget(widgetId)).PUT.action);
  });

  it('Проверка генератора setWidgetDataSuccess', async () => {
    const dispatched = [];
    const widgetId = 'testId';
    const widgetState = {
      pageId: 'pageId'
    };
    const basePath = '/n2o/test';
    const baseQuery = {
      size: 10
    };
    const fakeStore = {
      getState: () => ({}),
      dispatch: action => dispatched.push(action)
    };
    const response = {
      page: 2,
      size: 10,
      metadata: {
        meta: {}
      },
      list: [],
      count: 14
    };
    api.default = jest.fn(() => Promise.resolve(response));
    await runSaga(fakeStore, setWidgetDataSuccess, widgetId, widgetState, basePath, baseQuery);
    expect(dispatched[0]).toEqual(put(setModel(PREFIXES.datasource, widgetId, [])).PUT.action);
    expect(dispatched[1]).toEqual(put(setModel(PREFIXES.resolve, widgetId, null)).PUT.action);
    expect(dispatched[2]).toEqual(put(changeCountWidget(widgetId, response.count)).PUT.action);
    expect(dispatched[3]).toEqual(put(changePageWidget(widgetId, response.page)).PUT.action);
    expect(dispatched[4]).toEqual(
      put(setWidgetMetadata(widgetState.pageId, widgetId, response.metadata)).PUT.action
    );
    expect(dispatched[5]).toEqual(put(resetWidgetState(widgetId)).PUT.action);
    expect(dispatched[6]).toEqual(put(dataSuccessWidget(widgetId, response)).PUT.action);
  });

  it('Должен произойти clearOnDisable', () => {
    const action = {
      payload: {
        widgetId: 'testId'
      }
    };
    const gen = clearOnDisable(action);
    expect(gen.next().value.PUT).toEqual(
      put(setModel(PREFIXES.datasource, action.payload.widgetId, null)).PUT
    );
    expect(gen.next().value.PUT).toEqual(put(changeCountWidget(action.payload.widgetId, 0)).PUT);
  });

  it('Должен произойти runResolve', () => {
    const action = {
      payload: {
        widgetId: 'testId',
        model: {
          some: 'value'
        }
      }
    };
    const gen = runResolve(action);
    expect(gen.next().value.PUT).toEqual(
      put(setModel(PREFIXES.resolve, action.payload.widgetId, action.payload.model)).PUT
    );
  });

  it('resolveUrl должен вернуть базовый путь и параметры', async () => {
    const widgetState = {
      id: 12345
    };
    const state = {
      models: {
        resolve: {
          proto_patients: {
            ...widgetState
          }
        }
      }
    };
    const dataProvider = {
      url: '/n2o/:test',
      pathMapping: {
        test: {
          link: 'models.resolve["proto_patients"].id'
        }
      },
      queryMapping: {}
    };
    const options = {};
    const fakeStore = {
      getState: () => ({
        ...state
      })
    };
    const promise = await runSaga(fakeStore, resolveUrl, state, dataProvider, widgetState, options)
      .done;
    const result = await Promise.resolve(promise);
    expect(result).toEqual({
      basePath: '/n2o/12345',
      baseQuery: {
        size: undefined,
        page: undefined
      }
    });
  });

  it('prepareFetch должен вернуть подготовленные даные', async () => {
    const router = {
      location: '/root'
    };
    const dataProvider = {
      url: '/n2o/test',
      queryMapping: {},
      pathMapping: {}
    };
    const widgets = {
      testWidget: {
        pageId: 'testPage',
        some: 'value',
        dataProvider
      }
    };
    const routes = [
      {
        path: '/test',
        exact: true,
        isOtherPage: false
      }
    ];
    const pages = {
      testPage: {
        metadata: {
          routes
        }
      }
    };
    const fakeStore = {
      getState: () => ({
        router,
        widgets,
        pages
      })
    };
    const widgetId = 'testWidget';
    const saga = await runSaga(fakeStore, prepareFetch, widgetId);
    const result = await Promise.resolve(saga.done);
    expect(result).toEqual({
      dataProvider,
      location: router.location,
      routes,
      state: {
        pages,
        router,
        widgets
      },
      widgetState: widgets[widgetId]
    });
  });

  it('resolveVisibleDependency должен показать виджет', async () => {
    const dispatched = [];
    const model = [
      {
        model: {
          name: 'invisible'
        },
        config: {
          on: "models.resolve['testWidget']",
          condition: "name !== 'visible'"
        }
      }
    ];
    const widgetId = 'testWidget';
    const fakeStore = {
      getState: () => ({}),
      dispatch: action => dispatched.push(action)
    };
    const saga = await runSaga(fakeStore, resolveVisibleDependency, model, widgetId);
    expect(dispatched[0].type).toEqual(SHOW);
    expect(dispatched[0].payload).toEqual({
      widgetId
    });
    expect(dispatched[0].meta).toEqual({});
  });

  it('resolveVisibleDependency должен скрыть виджет', async () => {
    const dispatched = [];
    const model = [
      {
        model: {
          name: 'visible'
        },
        config: {
          on: "models.resolve['testWidget']",
          condition: "name !== 'visible'"
        }
      }
    ];
    const widgetId = 'testWidget';
    const fakeStore = {
      getState: () => ({}),
      dispatch: action => dispatched.push(action)
    };
    const saga = await runSaga(fakeStore, resolveVisibleDependency, model, widgetId);
    expect(dispatched[0].type).toEqual(HIDE);
    expect(dispatched[0].payload).toEqual({
      widgetId
    });
    expect(dispatched[0].meta).toEqual({});
  });

  it('resolveEnabledDependency должен разблокировать виджет', async () => {
    const dispatched = [];
    const model = [
      {
        model: {
          name: 'enabled'
        },
        config: {
          on: "models.resolve['testWidget']",
          condition: "name !== 'disabled'"
        }
      }
    ];
    const widgetId = 'testWidget';
    const fakeStore = {
      getState: () => ({}),
      dispatch: action => dispatched.push(action)
    };
    const saga = await runSaga(fakeStore, resolveEnabledDependency, model, widgetId);
    expect(dispatched[0].type).toEqual(ENABLE);
    expect(dispatched[0].payload).toEqual({
      widgetId
    });
    expect(dispatched[0].meta).toEqual({});
  });

  it('resolveEnabledDependency должен заблокировать виджет', async () => {
    const dispatched = [];
    const model = [
      {
        model: {
          name: 'disabled'
        },
        config: {
          on: "models.resolve['testWidget']",
          condition: "name !== 'disabled'"
        }
      }
    ];
    const widgetId = 'testWidget';
    const fakeStore = {
      getState: () => ({}),
      dispatch: action => dispatched.push(action)
    };
    const saga = await runSaga(fakeStore, resolveEnabledDependency, model, widgetId);
    expect(dispatched[0].type).toEqual(DISABLE);
    expect(dispatched[0].payload).toEqual({
      widgetId
    });
    expect(dispatched[0].meta).toEqual({});
  });

  it('resolveFetchDependency должен запустить fetch', async () => {
    const dispatched = [];
    const model = [
      {
        model: {
          name: 'fetch'
        },
        config: {
          on: "models.resolve['testWidget']",
          condition: "name === 'fetch'"
        }
      }
    ];

    const prevModel = [
      {
        model: {
          name: 'prevFetch'
        },
        config: {
          on: "models.resolve['testWidget']",
          condition: "name === 'fetch'"
        }
      }
    ];
    const widgetId = 'testWidget';
    const fakeStore = {
      getState: () => ({}),
      dispatch: action => dispatched.push(action)
    };
    const saga = await runSaga(fakeStore, resolveFetchDependency, model, prevModel, widgetId, true);
    expect(dispatched[0].type).toEqual(DATA_REQUEST);
    expect(dispatched[0].payload).toEqual({
      widgetId,
      options: {}
    });
    expect(dispatched[0].meta).toEqual({});
  });
});
