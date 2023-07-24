import React from 'react'

import { statusTextContainerStyle, statusTextIconStyle } from './utils'
import { StatusTextProps } from './types'

/**
 * @example
 * <StatusText text={'Test text'} className={'test-class'} color={'success'}/>
 */

export function StatusText({ text, textPosition, className, color }: StatusTextProps) {
    return (
        <div className={statusTextContainerStyle(textPosition, className)}>
            {color && <span className={statusTextIconStyle(textPosition, color)} />}
            {text && <p className="n2o-status-text_text">{text}</p>}
        </div>
    )
}

StatusText.defaultProps = {
    textPosition: 'right',
} as StatusTextProps
