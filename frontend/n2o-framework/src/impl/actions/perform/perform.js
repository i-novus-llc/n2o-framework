import { createAction } from 'redux-actions'

import createActionHelper from '../../../actions/createActionHelper'

export default function performAction({
    dispatch,
    type,
    payload,
    meta,
    actionId,
}) {
    dispatch(createActionHelper(type)(payload, meta))
}
