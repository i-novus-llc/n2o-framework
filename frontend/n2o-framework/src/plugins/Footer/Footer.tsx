import React, { CSSProperties, useEffect, useState } from 'react'
import isEmpty from 'lodash/isEmpty'
import classNames from 'classnames'

import { WindowType } from '../../components/core/WindowType'
import { VISIBILITY_EVENT } from '../constants'

import { resolveText } from './helpers'
import { PositionedText } from './PositionedText'

export interface Props {
    textRight?: string
    textLeft?: string
    className?: string
    style?: CSSProperties
}

function getFooterVisibilityStyle(): CSSProperties {
    const { N2O_ELEMENT_VISIBILITY } = window as WindowType
    const isVisible = N2O_ELEMENT_VISIBILITY ? !!N2O_ELEMENT_VISIBILITY.footer : true

    return isVisible ? {} : { display: 'none' }
}

export function Footer({ textRight, textLeft, className, style: propsStyle }: Props) {
    const [style, setStyle] = useState<CSSProperties>(getFooterVisibilityStyle())

    useEffect(() => {
        const handleVisibilityUpdate = () => {
            const nextStyle = getFooterVisibilityStyle()

            setStyle((prevStyle) => {
                const prevDisplay = prevStyle?.display
                const nextDisplay = nextStyle?.display

                if (prevDisplay === nextDisplay) { return prevStyle }

                return nextStyle
            })
        }

        window.addEventListener(VISIBILITY_EVENT, handleVisibilityUpdate)

        handleVisibilityUpdate()

        return () => {
            window.removeEventListener(VISIBILITY_EVENT, handleVisibilityUpdate)
        }
    }, [])

    return (
        <footer className={classNames('n2o-footer py-2', className)} style={isEmpty(style) ? propsStyle : style}>
            <div className={classNames('container-fluid d-flex', { 'inherits-color': propsStyle?.color })}>
                <PositionedText text={resolveText(textLeft)} position="left" />
                <PositionedText text={resolveText(textRight)} position="right" />
            </div>
        </footer>
    )
}

export default Footer
