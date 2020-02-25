import { put, select } from 'redux-saga/effects';
import { resolveConditions } from './conditions';
import { changeColumnVisiblity } from '../actions/columns';

/**
 * Resolve columns conditions
 * @param column
 * @return
 */
export function* resolveColumn(column) {
  const state = yield select();

  if (column.conditions) {
    const { visible } = column.conditions;

    if (visible) {
      const nextVisible = resolveConditions(visible, state);

      yield put(
        changeColumnVisiblity(column.key, column.columnId, nextVisible)
      );
    }
  }
}
