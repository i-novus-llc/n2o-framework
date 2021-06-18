import createActionHelper from '../../../actions/createActionHelper'

export default function performAction({
    dispatch,
    type,
    payload,
    meta,
}) {
    dispatch(createActionHelper(type)(payload, meta))
}
