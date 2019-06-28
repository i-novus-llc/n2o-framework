import { START_INVOKE } from '../constants/actionImpls';
import createActionHelper from './createActionHelper';

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
