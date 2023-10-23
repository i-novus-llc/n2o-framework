import React from 'react'
import { useStore } from 'react-redux'
import get from 'lodash/get'
import classNames from 'classnames'

import StandardButton from '../StandardButton/StandardButton'

export function WordWrap(props) {
    const { className } = props
    const id = get(props, 'action.payload.id')

    const { getState } = useStore()
    const state = getState()

    const textWrap = get(state, `widgets.${id}.table.textWrap`)

    const icon = textWrap ? 'fa fa-check word-wrap-btn__check' : null

    return <StandardButton {...props} className={classNames(className, 'word-wrap-btn')} icon={icon} />
}
