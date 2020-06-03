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
