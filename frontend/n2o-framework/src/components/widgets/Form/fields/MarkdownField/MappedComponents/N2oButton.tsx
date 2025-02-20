import React from 'react'
import omit from 'lodash/omit'
import classNames from 'classnames'

import { StandardButton } from '../../../../../buttons/StandardButton/StandardButton'
import { ReactMarkdownExtendedProps } from '../helpers'

export function N2oButton(props: ReactMarkdownExtendedProps) {
    const { className, placement } = props

    return (
        <StandardButton
            {...omit(props, 'onClick')}
            hintPosition={placement}
            className={classNames('n2o-markdown-button', className)}
        />
    )
}
