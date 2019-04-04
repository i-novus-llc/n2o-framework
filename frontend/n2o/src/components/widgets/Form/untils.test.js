import { setWatchDependency } from './utils';

const testState = {
  models: {
    resolve: {
      formName: {
        field1: {
          item1: 'test',
          item2: 'test2',
        },
        field2: {
          item1: 'testtest',
          item2: {
            id: 1,
          },
        },
      },
    },
  },
};

describe('Проверка until js', () => {
  describe('setWatchDependency', () => {
    it('Вызов с параметрами', () => {
      expect(
        setWatchDependency(
          testState,
          {
            dependency: [
              {
                type: 'fetch',
                on: ['field1'],
              },
            ],
            form: 'formName',
            modelPrefix: 'resolve',
          },
          'fetch'
        )
      ).toEqual({
        models: {
          resolve: {
            formName: {
              field1: {
                item1: 'test',
                item2: 'test2',
              },
            },
          },
        },
      });
    });
    it('dependency через точку', () => {
      expect(
        setWatchDependency(
          testState,
          {
            dependency: [
              {
                type: 'fetch',
                on: ['field2.item2'],
              },
            ],
            form: 'formName',
            modelPrefix: 'resolve',
          },
          'fetch'
        )
      ).toEqual({
        models: { resolve: { formName: { field2: { item2: { id: 1 } } } } },
      });
    });
    it('несколько одинаковых dependency', () => {
      expect(
        setWatchDependency(
          testState,
          {
            dependency: [
              {
                type: 'fetch',
                on: ['field2.item2'],
              },
              {
                type: 'fetch',
                on: ['field1'],
              },
            ],
            form: 'formName',
            modelPrefix: 'resolve',
          },
          'fetch'
        )
      ).toEqual({
        models: {
          resolve: {
            formName: {
              field1: { item1: 'test', item2: 'test2' },
              field2: { item2: { id: 1 } },
            },
          },
        },
      });
    });
  });
});
