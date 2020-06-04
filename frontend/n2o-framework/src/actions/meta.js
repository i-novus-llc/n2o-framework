import createActionHelper from './createActionHelper';
import { CALL_ALERT_META } from '../constants/meta';

export const callAlert = meta => createActionHelper(CALL_ALERT_META)({}, meta);
