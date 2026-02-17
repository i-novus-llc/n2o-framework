import React from 'react'
import { useStore } from 'react-redux'
import get from 'lodash/get'
import classNames from 'classnames'

import { FactoryStandardButton, type Props as FactoryStandardButtonProps } from '../FactoryStandardButton'

function getIcon(nested: boolean, icon: string, textWrap: boolean): string | null {
    if (!nested) { return icon }
    if (textWrap) { return 'fa fa-check word-wrap-btn__check' }

    return null
}

export interface Props extends FactoryStandardButtonProps {
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
        <FactoryStandardButton
            {...props}
            className={classNames(className, 'word-wrap-btn')}
            icon={currentIcon}
        />
    )
}

export default WordWrap
