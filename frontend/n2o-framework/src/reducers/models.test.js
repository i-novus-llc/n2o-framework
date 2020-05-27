import {
  SET,
  REMOVE,
  SYNC,
  UPDATE,
  UPDATE_MAP,
  COPY,
  REMOVE_ALL,
} from '../constants/models';
import models from './models';
import { resolveCopyAction } from './models';

describe('Тесты models reducer', () => {
  it('Проверка SET', () => {
    expect(
      models(
        {},
        {
          type: SET,
          payload: {
            prefix: 'prefix',
            key: 'key',
            model: {
              name: 'modelName',
            },
          },
        }
      )
    ).toEqual({
      prefix: {
        key: {
          name: 'modelName',
        },
      },
    });
  });

  it('Проверка REMOVE', () => {
    expect(
      models(
        {
          datasource: [
            {
              name: 'test',
            },
          ],
        },
        {
          type: REMOVE,
          payload: {
            prefix: 'datasource',
            key: 0,
          },
        }
      )
    ).toEqual({
      datasource: {},
    });
  });

  it('Проверка SYNC', () => {
    expect(
      models(
        {},
        {
          type: SYNC,
          payload: {
            prefix: 'edit',
            keys: ['Page.Widget1'],
            model: {
              id: 1,
              name: 'test',
            },
          },
        }
      )
    ).toEqual({
      edit: {
        0: {
          'Page.Widget1': {
            id: 1,
            name: 'test',
          },
        },
      },
    });
  });

  it('Проверка UPDATE', () => {
    expect(
      models(
        {
          edit: {
            editKey: [{ a: 1 }, { b: 2 }],
          },
        },
        {
          type: UPDATE,
          payload: {
            prefix: 'edit',
            key: 'editKey',
            field: '[1].field1',
            value: 'value1',
          },
        }
      )
    ).toEqual({
      edit: {
        editKey: [{ a: 1 }, { b: 2, field1: 'value1' }],
      },
    });
    expect(
      models(
        {
          edit: {
            editKey: {},
          },
        },
        {
          type: UPDATE,
          payload: {
            prefix: 'edit',
            key: 'editKey',
            field: 'field1',
            value: 'value1',
          },
        }
      )
    ).toEqual({
      edit: {
        editKey: {
          field1: 'value1',
        },
      },
    });
    expect(
      models(
        {
          edit: {},
        },
        {
          type: UPDATE,
          payload: {
            prefix: 'edit',
            key: 'editKey',
            field: 'field1',
            value: 'value1',
          },
        }
      )
    ).toEqual({
      edit: {
        editKey: {
          field1: 'value1',
        },
      },
    });
    expect(
      models(
        {
          edit: {
            editKey: { a: { b: 2 } },
          },
        },
        {
          type: UPDATE,
          payload: {
            prefix: 'edit',
            key: 'editKey',
            field: 'a.b',
            value: 'value1',
          },
        }
      )
    ).toEqual({
      edit: {
        editKey: { a: { b: 'value1' } },
      },
    });
    expect(
      models(
        {
          edit: {
            editKey: {},
          },
        },
        {
          type: UPDATE,
          payload: {
            prefix: 'edit',
            key: 'editKey',
            field: 'a.b',
            value: 'value1',
          },
        }
      )
    ).toEqual({
      edit: {
        editKey: { a: { b: 'value1' } },
      },
    });
  });

  it('Проверка UPDATE_MAP', () => {
    expect(
      models(
        {
          edit: {
            editKey: {},
          },
        },
        {
          type: UPDATE_MAP,
          payload: {
            prefix: 'edit',
            key: 'editKey',
            field: 'field',
            map: 'map',
            value: [1, 2],
          },
        }
      )
    ).toEqual({
      edit: {
        editKey: {
          field: [
            {
              map: 1,
            },
            {
              map: 2,
            },
          ],
        },
      },
    });
  });

  describe('Проверка COPY', () => {
    it('Проверка COPY без mode', () => {
      expect(
        models(
          {
            resolve: {
              testKey: {
                name: 'new name',
              },
            },
            edit: {
              testKey: {},
            },
          },
          {
            type: COPY,
            payload: {
              source: {
                prefix: 'resolve',
                key: 'testKey',
              },
              target: {
                prefix: 'edit',
                key: 'testKey',
              },
            },
          }
        )
      ).toEqual({
        edit: {
          testKey: {
            name: 'new name',
          },
        },
        resolve: {
          testKey: {
            name: 'new name',
          },
        },
      });
    });

    it('Проверка COPY mode = merge', () => {
      expect(
        models(
          {
            resolve: {
              testWidget: {
                one: 1,
              },
            },
            filter: {
              anotherWidget: {
                two: 2,
              },
            },
          },
          {
            type: COPY,
            payload: {
              target: {
                prefix: 'filter',
                key: 'anotherWidget',
              },
              source: {
                prefix: 'resolve',
                key: 'testWidget',
              },
              mode: 'merge',
            },
          }
        )
      ).toEqual({
        resolve: {
          testWidget: {
            one: 1,
          },
        },
        filter: {
          anotherWidget: {
            two: 2,
            one: 1,
          },
        },
      });
    });

    it('Проверка COPY mode = add', () => {
      expect(
        models(
          {
            resolve: {
              sourceWidget: {
                one: [1, 2, 3],
              },
            },
            filter: {
              targetWidget: {
                two: {
                  arr: [4, 5, 6],
                },
              },
            },
          },
          {
            type: COPY,
            payload: {
              source: { prefix: 'resolve', key: 'sourceWidget', field: 'one' },
              target: {
                prefix: 'filter',
                key: 'targetWidget',
                field: 'two.arr',
              },
              mode: 'add',
            },
          }
        )
      ).toEqual({
        resolve: {
          sourceWidget: {
            one: [1, 2, 3],
          },
        },
        filter: {
          targetWidget: {
            two: {
              arr: [4, 5, 6, 1, 2, 3],
            },
          },
        },
      });
    });
  });

  it('Проверка REMOVE_ALL', () => {
    expect(
      models(
        {
          resolve: {
            testKey: {
              name: 'new name',
            },
          },
          edit: {
            testKey: {
              name: 'new name',
            },
          },
        },
        {
          type: REMOVE_ALL,
          payload: {
            key: 'testKey',
          },
        }
      )
    ).toEqual({
      resolve: {},
      edit: {},
    });
  });
});

const createState = (resolve = {}, filter = {}) => ({
  resolve: {
    proto_clients: {
      id: 1,
      name: 'Ivan',
      surname: 'Ivanov',
    },
    ...resolve,
  },
  filter: {
    proto_form: {
      test: 'test',
    },
    ...filter,
  },
});

const createAction = (
  mode = 'replace',
  sourceMapper = null,
  source = {},
  target = {}
) => ({
  payload: {
    source: {
      prefix: 'resolve',
      key: 'proto_clients',
      ...source,
    },
    target: {
      prefix: 'filter',
      key: 'proto_form',
      ...target,
    },
    mode,
    sourceMapper,
  },
});

describe('Тесты resolveCopyAction', () => {
  it('должен вернуть новый state после копирования mode = "replace"', () => {
    const newState = resolveCopyAction(createState(), createAction());

    expect(newState).toEqual({
      resolve: createState().resolve,
      filter: {
        proto_form: {
          id: 1,
          name: 'Ivan',
          surname: 'Ivanov',
        },
      },
    });
  });

  it('должен вернуть новый state после копирования mode = "merge"', () => {
    const newState = resolveCopyAction(createState(), createAction('merge'));

    expect(newState).toEqual({
      resolve: createState().resolve,
      filter: {
        proto_form: {
          id: 1,
          name: 'Ivan',
          surname: 'Ivanov',
          test: 'test',
        },
      },
    });
  });

  it('должен вернуть новый state после копирования mode = "add"', () => {
    const newState = resolveCopyAction(
      createState(
        {
          sourceTest: {
            arr: [1, 2, 3],
          },
        },
        {
          targetTest: {
            newArr: [3, 4, 5],
          },
        }
      ),
      createAction(
        'add',
        null,
        {
          prefix: 'resolve',
          key: 'sourceTest',
          field: 'arr',
        },
        {
          prefix: 'filter',
          key: 'targetTest',
          field: 'newArr',
        }
      )
    );

    expect(newState.filter.targetTest).toEqual({
      newArr: [3, 4, 5, 1, 2, 3],
    });
  });

  it('должен отработать sourceMapper', () => {
    const newState = resolveCopyAction(
      createState(),
      createAction('replace', '`{ test: "test", name }`')
    );

    expect(newState.filter.proto_form).toEqual({
      test: 'test',
      name: 'Ivan',
    });
  });
});
