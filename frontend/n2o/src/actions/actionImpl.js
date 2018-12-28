import { START_INVOKE } from '../constants/actionImpls';
import createActionHelper from './createActionHelper';

export function startInvoke(widgetId, dataProvider, data, modelLink) {
  return createActionHelper(START_INVOKE)(
    {
      widgetId,
      dataProvider,
      data,
      modelLink
    },
    {
      refresh: true
    }
  );
}
