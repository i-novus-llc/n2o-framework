import {
  CHANGE_COLUMN_DISABLED,
  CHANGE_COLUMN_VISIBILITY,
  REGISTER_COLUMN,
  TOGGLE_COLUMN_VISIBILITY,
} from '../constants/columns';
import {
  changeColumnVisiblity,
  setColumnVisible,
  setColumnHidden,
  toggleColumnVisiblity,
  changeColumnDisabled,
  registerColumn,
} from './columns';

const widgetId = 'widgetId';
const columnId = 'columnId';
const label = 'label';
const visible = 'label';
const disabled = 'label';

describe('Тесты для экшенов columns', () => {
  describe('Проверка экшена changeColumnVisibility', () => {
    it('Генирирует правильное событие', () => {
      const action = changeColumnVisiblity(widgetId, columnId, true);
      expect(action.type).toEqual(CHANGE_COLUMN_VISIBILITY);
    });
    it('Проверяет правильность payload', () => {
      const action = changeColumnVisiblity(widgetId, columnId, true);
      expect(action.payload.key).toEqual(widgetId);
      expect(action.payload.columnId).toEqual(columnId);
      expect(action.payload.visible).toEqual(true);
    });
  });

  describe('Проверка экшена setColumnsVisible', () => {
    it('Генирирует правильное событие', () => {
      const action = setColumnVisible(widgetId, columnId, true);
      expect(action.type).toEqual(CHANGE_COLUMN_VISIBILITY);
    });
    it('Проверяет правильность visible', () => {
      const action = setColumnVisible(widgetId, columnId);
      expect(action.payload.visible).toEqual(true);
    });
  });

  describe('Проверка экшена setColumnHidden', () => {
    it('Генирирует правильное событие', () => {
      const action = setColumnHidden(widgetId, columnId);
      expect(action.type).toEqual(CHANGE_COLUMN_VISIBILITY);
    });
    it('Проверяет правильность visible', () => {
      const action = setColumnHidden(widgetId, columnId);
      expect(action.payload.visible).toEqual(false);
    });
  });

  describe('Проверка экшена toggleColumnVisibility', () => {
    it('Генирирует правильное событие', () => {
      const action = toggleColumnVisiblity(widgetId, columnId);
      expect(action.type).toEqual(TOGGLE_COLUMN_VISIBILITY);
    });
    it('Проверяет правильность payload', () => {
      const action = toggleColumnVisiblity(widgetId, columnId);
      expect(action.payload.key).toEqual(widgetId);
      expect(action.payload.columnId).toEqual(columnId);
    });
  });

  describe('Проверка экшена changeColumnDisabled', () => {
    it('Генирирует правильное событие', () => {
      const action = changeColumnDisabled(widgetId, columnId, true);
      expect(action.type).toEqual(CHANGE_COLUMN_DISABLED);
    });
    it('Проверяет правильность payload', () => {
      const action = changeColumnDisabled(widgetId, columnId, true);
      expect(action.payload.key).toEqual(widgetId);
      expect(action.payload.columnId).toEqual(columnId);
      expect(action.payload.disabled).toEqual(true);
    });
  });

  describe('Проверка экшена registerColumn', () => {
    it('Генирирует правильное событие', () => {
      const action = registerColumn(widgetId, columnId, label);
      expect(action.type).toEqual(REGISTER_COLUMN);
    });
    it('Проверяет правильность payload', () => {
      const action = registerColumn(
        widgetId,
        columnId,
        label,
        visible,
        disabled
      );
      expect(action.payload.label).toEqual(label);
      expect(action.payload.visible).toEqual(visible);
      expect(action.payload.disabled).toEqual(disabled);
      expect(action.payload.columnId).toEqual(columnId);
      expect(action.payload.key).toEqual(widgetId);
    });
  });
});
