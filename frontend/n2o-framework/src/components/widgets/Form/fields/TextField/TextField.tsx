import React from 'react'
import classNames from 'classnames'
import { parseFormatter } from '@i-novus/n2o-components/lib/utils/parseFormatter'

import { type Props } from './types'

/**
 * Компонент поле текст
 */
export function TextField({ text, format, className, visible = true, style = {} }: Props) {
    if (!visible) { return null }
    /* контроль переносов строк и пробелов осуществляется через xml */
    const currentStyle = { ...style, whiteSpace: 'break-spaces' } as Props['style']

    return (
        <div
            className={classNames('n2o-text-field n2o-snippet', className)}
            style={currentStyle}
        >
            {format ? parseFormatter(text, format) : text}
        </div>
    )
}

export default TextField
