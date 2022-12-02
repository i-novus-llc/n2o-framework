import { push } from 'connected-react-router'

import { dataProviderResolver } from '../../../core/dataProviderResolver'

// TODO сделать единый redirect func для кнопок, ячеек, linkImpl, meta saga итд.
//  разобраться с синтаксисом, прим. где-то _self, а где-то self
export default function linkImpl({
    dispatch,
    state,
    target,
    path,
    pathMapping,
    queryMapping,
}) {
    const { url: newUrl } = dataProviderResolver(state, {
        url: path,
        pathMapping,
        queryMapping,
    })

    if (target === 'application') {
        dispatch(push(newUrl))
    } else if (target === 'self') {
        window.top.location = newUrl
    } else {
        window.top.open(newUrl)
    }
}
