import React from 'react'
import classNames from 'classnames'
import { FormattedText } from '@i-novus/n2o-components/lib/Typography/FormattedText'

import { EMPTY_OBJECT } from '../../../../../utils/emptyTypes'

import { type Props } from './types'

/**
 * Компонент поле текст
 */
export function TextField({ text, format, className, visible = true, style = EMPTY_OBJECT }: Props) {
    if (!visible) { return null }
    /* контроль переносов строк и пробелов осуществляется через xml */
    const currentStyle = { ...style, whiteSpace: 'break-spaces' } as Props['style']

    return (
        <div className={classNames('n2o-text-field n2o-snippet', className)} style={currentStyle}>
            <FormattedText format={format}>{text}</FormattedText>
        </div>
    )
}

export default TextField
