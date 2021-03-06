import React, { useEffect, useState } from 'react'
import { useInView } from 'react-intersection-observer'

import { Spinner } from '../Spinner/Spinner'

import style from './sandbox.module.scss'

const DEFAULT_HEIGHT = 500
const VISIBLE_DELAY = 500

/**
 *
 * @param {number|'sm'|'m'} height
 * @returns {number}
 */

const calculateFinalHeight = (height) => {
    if (typeof height === 'number') {
        return height
    }

    switch (height) {
        case 'sm':
            return DEFAULT_HEIGHT / 3
        case 'm':
            return (DEFAULT_HEIGHT * 2) / 3
        default:
            return DEFAULT_HEIGHT
    }
}

export function visibilityHOC(Component) {
    const WrappedComponent = function (props) {
        const { projectId, height } = props

        const finalHeight = calculateFinalHeight(height)

        const { ref, inView } = useInView()

        const [visible, setVisible] = useState(false)

        useEffect(() => {
            if (!visible && inView) {
                const timerId = setTimeout(() => {
                    setVisible(true)
                }, VISIBLE_DELAY)

                return () => {
                    clearTimeout(timerId)
                }
            }
        }, [visible, inView])

        return (
                <div ref={ref} className={style.wrapper} style={{ minHeight: finalHeight }}>
                    {
                        visible
                                ? <Component key={projectId} {...props} height={finalHeight}/>
                                : <Spinner/>
                    }
                </div>
        )
    }

    WrappedComponent.displayName = `visibilityHOC(${getDisplayName(Component)})`

    return WrappedComponent
}

function getDisplayName(WrappedComponent) {
    return WrappedComponent.displayName || WrappedComponent.name || 'Component'
}
