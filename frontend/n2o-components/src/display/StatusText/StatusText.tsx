import React from 'react'

import { Text } from '../../Typography/Text'

import { statusTextContainerStyle, statusTextIconStyle } from './utils'
import { StatusTextProps } from './types'

/**
 * @example
 * <StatusText text={'Test text'} className={'test-class'} color={'success'}/>
 */

export function StatusText({ text, textPosition = 'right', className, color }: StatusTextProps) {
    return (
        <div className={statusTextContainerStyle(textPosition, className)}>
            {color && <span className={statusTextIconStyle(textPosition, color)} />}
            {text && <p className="n2o-status-text_text"><Text>{text}</Text></p>}
        </div>
    )
}

StatusText.displayName = '@n2o-components/display/StatusText'
