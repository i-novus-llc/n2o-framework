import {
  START_INVOKE,
  SUCCESS_INVOKE,
  FAIL_INVOKE,
} from '../constants/actionImpls';
import createActionHelper from './createActionHelper';

/**
 * @deprecated
 */

/**
 * Экшен начала инвока
 * @param widgetId
 * @param dataProvider
 * @param data
 * @param modelLink
 * @param meta
 */
export function startInvoke(
  widgetId,
  dataProvider,
  data,
  modelLink,
  meta = { refresh: true }
) {
  return createActionHelper(START_INVOKE)(
    {
      widgetId,
      dataProvider,
      data,
      modelLink,
    },
    meta
  );
}

/**
 * Экшен удачного завершения инвока
 * @param widgetId
 * @param meta
 */
export function successInvoke(widgetId, meta) {
  return createActionHelper(SUCCESS_INVOKE)(
    {
      widgetId,
    },
    meta
  );
}

/**
 * Экшен неудачного завершения инвока
 * @param widgetId
 * @param meta
 */
export function failInvoke(widgetId, meta) {
  return createActionHelper(FAIL_INVOKE)(
    {
      widgetId,
    },
    meta
  );
}
