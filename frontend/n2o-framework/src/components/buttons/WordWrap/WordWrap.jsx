import React from 'react'
import { useStore } from 'react-redux'
import get from 'lodash/get'
import classNames from 'classnames'

import StandardButton from '../StandardButton/StandardButton'

function getIcon(nested, icon, textWrap) {
    if (!nested) {
        return icon
    }

    if (textWrap) {
        return 'fa fa-check word-wrap-btn__check'
    }

    return null
}

export function WordWrap(props) {
    const { className, nested, icon } = props
    const id = get(props, 'action.payload.id')

    const { getState } = useStore()
    const state = getState()

    const textWrap = get(state, `widgets.${id}.table.textWrap`)
    const currentIcon = getIcon(nested, icon, textWrap)

    return <StandardButton {...props} className={classNames(className, 'word-wrap-btn')} icon={currentIcon} />
}
