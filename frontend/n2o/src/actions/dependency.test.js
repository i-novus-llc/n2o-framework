import { startDependency } from './dependency';
import { START_DEPENDENCY } from '../constants/dependency';

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
});
