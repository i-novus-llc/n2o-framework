import {
  reduceFunction,
  registerWidgetDependency,
  resolveWidgetDependency,
  resolveDependency,
  resolveFetchDependency,
  resolveVisibleDependency,
  resolveEnabledDependency
} from './widgetDependency';

const getConfig = (model, config) => ({
  model,
  config
});

describe('Проверка саги widgetDependency', () => {
  describe('reduceFunction', () => {
    it('вернет false', () => {
      const model = {
        test: 'test'
      };
      const config = {
        condition: 'test !== "test"'
      };

      expect(reduceFunction(false, getConfig(model, config))).toEqual(false);
      expect(reduceFunction(true, getConfig(model, config))).toEqual(false);
    });
    it('вернет true', () => {
      const model = {
        test: 'test'
      };
      const config = {
        condition: 'test == "test"'
      };

      expect(reduceFunction(true, getConfig(model, config))).toEqual(true);
    });
  });
});
