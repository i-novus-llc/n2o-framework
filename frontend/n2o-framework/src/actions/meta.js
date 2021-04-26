import { CALL_ALERT_META } from '../constants/meta'

import createActionHelper from './createActionHelper'

export const callAlert = meta => createActionHelper(CALL_ALERT_META)({}, meta)
