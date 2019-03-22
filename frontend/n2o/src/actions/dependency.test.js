import { startDependency, registerDependency } from './dependency';
import { START_DEPENDENCY, REGISTER_DEPENDENCY } from '../constants/dependency';

const dependency = 'test';
const values = ['value1', 'value2'];
const widget = 'widgetId';
const fieldName = 'field1';

describe('Тесты для экшенов dependency', () => {
  it('Генирирует правильное событие', () => {
    const action = startDependency(dependency, values, widget, fieldName);
    expect(action.type).toEqual(START_DEPENDENCY);
  });
  it('Возвращает правильный payload', () => {
    const action = startDependency(dependency, values, widget, fieldName);
    expect(action.payload.dependency).toEqual(dependency);
    expect(action.payload.values).toEqual(values);
    expect(action.payload.widget).toEqual(widget);
    expect(action.payload.fieldName).toEqual(fieldName);
  });

  it('registerDependency генирирует правильное событие', () => {
    const action = registerDependency('testWidget', {
      fetch: [
        {
          on: ['models.resolve.test'],
          condition: 'name !== "Мария"'
        }
      ]
    });
    expect(action.type).toEqual(REGISTER_DEPENDENCY);
  });

  it('registerDependency возвращает правильный payload', () => {
    const action = registerDependency('testWidget', {
      fetch: [
        {
          on: ['models.resolve.test'],
          condition: 'name !== "Мария"'
        }
      ]
    });
    expect(action.payload).toEqual({
      dependency: {
        fetch: [
          {
            on: ['models.resolve.test'],
            condition: 'name !== "Мария"'
          }
        ]
      },
      widgetId: 'testWidget'
    });
  });
});
