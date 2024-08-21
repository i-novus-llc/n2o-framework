import React, { CSSProperties } from 'react'
import classNames from 'classnames'

import { ElementVisibility, WindowType } from '../../components/core/WindowType'

import { resolveText } from './helpers'
import { PositionedText } from './PositionedText'

interface Props {
    textRight?: string
    textLeft?: string
    className?: string
    style?: CSSProperties
}

function Footer(props: Props) {
    const { textRight, textLeft, className, style } = props
    const { N2O_ELEMENT_VISIBILITY = {} as ElementVisibility } = window as WindowType

    let currentStyle = { ...style }

    if (N2O_ELEMENT_VISIBILITY && N2O_ELEMENT_VISIBILITY.footer === false) {
        currentStyle = { display: 'none' }
    }

    return (
        <footer className={classNames('n2o-footer py-2', className)} style={currentStyle}>
            <div className={classNames('container-fluid d-flex', { 'inherits-color': currentStyle?.color })}>
                <PositionedText text={resolveText(textLeft)} position="left" />
                <PositionedText text={resolveText(textRight)} position="right" />
            </div>
        </footer>
    )
}

export default Footer
