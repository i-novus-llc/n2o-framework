import React from 'react'
import omit from 'lodash/omit'
import classNames from 'classnames'

// @ts-ignore import from js file
import StandardButton from '../../../../../buttons/StandardButton/StandardButton'
import { IReactMarkdownExtendedProps } from '../helpers'

export function N2oButton(props: IReactMarkdownExtendedProps) {
    const { className, placement } = props

    return (
        <StandardButton
            {...omit(props, 'onClick')}
            hintPosition={placement}
            className={classNames('n2o-markdown-button', className)}
        />
    )
}
