import {
  REGISTER,
  DATA_REQUEST,
  DATA_SUCCESS,
  DATA_FAIL,
  RESOLVE,
  REMOVE,
  SHOW,
  HIDE,
  ENABLE,
  DISABLE,
  LOADING,
  UNLOADING,
  SORT_BY,
  CHANGE_COUNT,
  CHANGE_SIZE,
  CHANGE_PAGE,
  CHANGE_FILTERS_VISIBILITY,
  TOGGLE_FILTERS_VISIBILITY,
  RESET_STATE,
  SET_TABLE_SELECTED_ID,
  SET_ACTIVE,
  DISABLE_ON_FETCH,
} from '../constants/widgets';
import widgets from './widgets';

describe('Тесты widget reducer', () => {
  it('Проверка REGISTER', () => {
    expect(
      widgets(
        {
          'Page.Widget': {},
        },
        {
          type: REGISTER,
          payload: {
            widgetId: 'Page.Widget',
            initProps: {
              containerId: 'containerId',
              count: 1,
              dataProvider: {
                url: 'n2o/data',
              },
              filter: {
                key: 'name',
                type: 'includes',
              },
              isActive: true,
              isEnabled: true,
              isFilterVisible: false,
              page: 2,
              pageId: 'page-id-2',
              selectedId: 'selected-3',
              size: 20,
              sorting: {
                name: 'ASC',
              },
              type: 'table',
            },
          },
        }
      )
    ).toEqual({
      'Page.Widget': {
        containerId: 'containerId',
        count: 1,
        dataProvider: {
          url: 'n2o/data',
        },
        filter: {
          key: 'name',
          type: 'includes',
        },
        isActive: true,
        isEnabled: true,
        isFilterVisible: false,
        isInit: true,
        isLoading: false,
        isResolved: false,
        isVisible: true,
        page: 2,
        pageId: 'page-id-2',
        selectedId: 'selected-3',
        size: 20,
        sorting: {
          name: 'ASC',
        },
        type: 'table',
        validation: {},
        error: null,
      },
    });
  });

  it('Проверка DATA_REQUEST', () => {
    expect(
      widgets(
        {
          widget: {},
        },
        {
          type: DATA_REQUEST,
          payload: {
            widgetId: 'widget',
          },
        }
      )
    ).toEqual({
      widget: {
        isLoading: true,
      },
    });
  });

  it('Проверка DATA_SUCCESS', () => {
    expect(
      widgets(
        {
          widget: {},
        },
        {
          type: DATA_SUCCESS,
          payload: {
            widgetId: 'widget',
          },
        }
      )
    ).toEqual({
      widget: {
        isLoading: false,
      },
    });
  });

  it('Проверка DATA_FAIL', () => {
    expect(
      widgets(
        {
          widget: {},
        },
        {
          type: DATA_FAIL,
          payload: {
            widgetId: 'widget',
            err: 'someError',
          },
        }
      )
    ).toEqual({
      widget: {
        isLoading: false,
        error: 'someError',
      },
    });
  });

  it('Проверка RESOLVE', () => {
    expect(
      widgets(
        {
          widget: {},
        },
        {
          type: RESOLVE,
          payload: {
            widgetId: 'widget',
          },
        }
      )
    ).toEqual({
      widget: {
        isResolved: true,
      },
    });
  });

  it('Проверка SHOW', () => {
    expect(
      widgets(
        {
          widget: {},
        },
        {
          type: SHOW,
          payload: {
            widgetId: 'widget',
          },
        }
      )
    ).toEqual({
      widget: {
        isVisible: true,
      },
    });
  });

  it('Проверка HIDE', () => {
    expect(
      widgets(
        {
          widget: {},
        },
        {
          type: HIDE,
          payload: {
            widgetId: 'widget',
          },
        }
      )
    ).toEqual({
      widget: {
        isVisible: false,
      },
    });
  });

  it('Проверка ENABLE', () => {
    expect(
      widgets(
        {
          widget: {},
        },
        {
          type: ENABLE,
          payload: {
            widgetId: 'widget',
          },
        }
      )
    ).toEqual({
      widget: {
        isEnabled: true,
      },
    });
  });

  it('Проверка DISABLE', () => {
    expect(
      widgets(
        {
          widget: {},
        },
        {
          type: DISABLE,
          payload: {
            widgetId: 'widget',
          },
        }
      )
    ).toEqual({
      widget: {
        isEnabled: false,
      },
    });
  });

  it('Проверка DISABLE_ON_FETCH', () => {
    expect(
      widgets(
        {
          widget: {},
        },
        {
          type: DISABLE_ON_FETCH,
          payload: {
            widgetId: 'widget',
          },
        }
      )
    ).toEqual({
      widget: {
        isEnabled: false,
      },
    });
  });

  it('Проверка ENABLE', () => {
    expect(
      widgets(
        {
          widget: {},
        },
        {
          type: ENABLE,
          payload: {
            widgetId: 'widget',
          },
        }
      )
    ).toEqual({
      widget: {
        isEnabled: true,
      },
    });
  });

  it('Проверка LOADING', () => {
    expect(
      widgets(
        {
          widget: {},
        },
        {
          type: LOADING,
          payload: {
            widgetId: 'widget',
          },
        }
      )
    ).toEqual({
      widget: {
        isLoading: true,
      },
    });
  });

  it('Проверка UNLOADING', () => {
    expect(
      widgets(
        {
          widget: {},
        },
        {
          type: UNLOADING,
          payload: {
            widgetId: 'widget',
          },
        }
      )
    ).toEqual({
      widget: {
        isLoading: false,
      },
    });
  });

  it('Проверка SORT_BY', () => {
    expect(
      widgets(
        {
          widget: {},
        },
        {
          type: SORT_BY,
          payload: {
            widgetId: 'widget',
            sortDirection: 'DESC',
            fieldKey: 'name',
          },
        }
      )
    ).toEqual({
      widget: {
        sorting: {
          name: 'DESC',
        },
      },
    });
  });

  it('Проверка CHANGE_SIZE', () => {
    expect(
      widgets(
        {
          widget: {
            size: 20,
          },
        },
        {
          type: CHANGE_SIZE,
          payload: {
            widgetId: 'widget',
            size: 50,
          },
        }
      )
    ).toEqual({
      widget: {
        size: 50,
      },
    });
  });

  it('Проверка CHANGE_PAGE', () => {
    expect(
      widgets(
        {
          widget: {
            page: 1,
          },
        },
        {
          type: CHANGE_PAGE,
          payload: {
            widgetId: 'widget',
            page: 5,
          },
        }
      )
    ).toEqual({
      widget: {
        page: 5,
      },
    });
  });

  it('Проверка CHANGE_COUNT', () => {
    expect(
      widgets(
        {
          widget: {
            count: 1,
          },
        },
        {
          type: CHANGE_COUNT,
          payload: {
            widgetId: 'widget',
            count: 3,
          },
        }
      )
    ).toEqual({
      widget: {
        count: 3,
      },
    });
  });

  it('Проверка CHANGE_FILTERS_VISIBILITY', () => {
    expect(
      widgets(
        {
          widget: {
            isFilterVisible: false,
          },
        },
        {
          type: CHANGE_FILTERS_VISIBILITY,
          payload: {
            widgetId: 'widget',
            isFilterVisible: true,
          },
        }
      )
    ).toEqual({
      widget: {
        isFilterVisible: true,
      },
    });
  });

  it('Проверка TOGGLE_FILTERS_VISIBILITY', () => {
    expect(
      widgets(
        {
          widget: {
            isFilterVisible: true,
          },
        },
        {
          type: TOGGLE_FILTERS_VISIBILITY,
          payload: {
            widgetId: 'widget',
          },
        }
      )
    ).toEqual({
      widget: {
        isFilterVisible: false,
      },
    });
  });

  it('Проверка RESET_STATE', () => {
    expect(
      widgets(
        {
          widget: {},
        },
        {
          type: RESET_STATE,
          payload: {
            widgetId: 'widget',
          },
        }
      )
    ).toEqual({
      widget: {
        isInit: false,
      },
    });
  });

  it('Проверка SET_TABLE_SELECTED_ID', () => {
    expect(
      widgets(
        {
          widget: {},
        },
        {
          type: SET_TABLE_SELECTED_ID,
          payload: {
            widgetId: 'widget',
            value: 'testId',
          },
        }
      )
    ).toEqual({
      widget: {
        selectedId: 'testId',
      },
    });
  });

  it('Проверка SET_TABLE_SELECTED_ID', () => {
    expect(
      widgets(
        {
          widget: {},
        },
        {
          type: SET_TABLE_SELECTED_ID,
          payload: {
            widgetId: 'widget',
            value: 613241,
          },
        }
      )
    ).toEqual({
      widget: {
        selectedId: 613241,
      },
    });
  });

  it('Проверка SET_ACTIVE', () => {
    expect(
      widgets(
        {
          widget: {
            isActive: false,
          },
        },
        {
          type: SET_ACTIVE,
          payload: {
            widgetId: 'widget',
          },
        }
      )
    ).toEqual({
      widget: {
        isActive: true,
      },
    });
  });

  it('Проверка REMOVE', () => {
    expect(
      widgets(
        {
          widget: {
            isActive: true,
            containerId: 'id',
          },
        },
        {
          type: REMOVE,
          payload: {
            widgetId: 'widget',
          },
        }
      )
    ).toEqual({});
  });
});
