import React from 'react'
import { useStore } from 'react-redux'
import get from 'lodash/get'
import classNames from 'classnames'

import StandardButton from '../StandardButton/StandardButton'

function getIcon(nested: boolean, icon: string, textWrap: boolean): string | null {
    if (!nested) { return icon }
    if (textWrap) { return 'fa fa-check word-wrap-btn__check' }

    return null
}

export interface Props {
    className?: string
    nested: boolean
    icon: string
    action: {
        payload: {
            id: string
        }
    }
}

export const WordWrap = (props: Props) => {
    const { className, nested, icon, action } = props

    const { getState } = useStore()
    const state = getState()
    const id = get(action, 'payload.id')
    const textWrap = get(state, `widgets.${id}.table.textWrap`)
    const currentIcon = getIcon(nested, icon, textWrap)

    return (
        <StandardButton
            {...props}
            className={classNames(className, 'word-wrap-btn')}
            icon={currentIcon}
        />
    )
}

export default WordWrap
