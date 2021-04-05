import React, { useEffect, useState } from 'react'
import { useInView } from 'react-intersection-observer'

import { Spinner } from '../Spinner/Spinner'

import style from './sandbox.module.scss'

const DEFAULT_HEIGHT = 500
const VISIBLE_DELAY = 500

export function visibilityHOC(Component) {
    const WrappedComponent = function (props) {
        const { projectId, height } = props
        const finalHeight = height || DEFAULT_HEIGHT

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
            <div
                ref={ref}
                style={{ height: finalHeight }}
                className={style.wrapper}
            >
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
