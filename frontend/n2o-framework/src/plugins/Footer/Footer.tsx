import React, { CSSProperties } from 'react'
import classNames from 'classnames'

import { WindowType } from '../../components/core/WindowType'

import { resolveText } from './helpers'
import { PositionedText } from './PositionedText'

export interface Props {
    textRight?: string
    textLeft?: string
    className?: string
    style?: CSSProperties
}

export function Footer({ textRight, textLeft, className, style }: Props) {
    const { N2O_ELEMENT_VISIBILITY } = window as WindowType

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
