import React, { createContext, useState, useEffect, useRef, useCallback, ReactNode } from 'react'
import { isEqual, pick } from 'lodash'

import { TBaseProps } from '../types'

type State = Partial<{
    clientHeight: number,
    clientWidth: number,
    scrollHeight: number,
    scrollWidth: number,
    scrollTop: number,
    scrollLeft: number,
    top: number,
    left: number,
    scrollTo(): void,
}>

const DEFAULT_STATE: State = {
    clientHeight: 0,
    clientWidth: 0,
    scrollHeight: 0,
    scrollWidth: 0,
    scrollTop: 0,
    scrollLeft: 0,
    top: 0,
    left: 0,
    scrollTo() {},
}
const STATE_KEYS = Object.keys(DEFAULT_STATE)

export const ScrollContext = createContext(DEFAULT_STATE)

const useResizeObserver = (element: Element | null, callback: () => void) => {
    const callbackRef = useRef(callback)

    useEffect(() => { callbackRef.current = callback }, [callback])

    useEffect(() => {
        if (!element) { return }
        const resizeObserver = new ResizeObserver(() => {
            callbackRef.current()
        })

        resizeObserver.observe(element)

        // eslint-disable-next-line consistent-return
        return () => {
            resizeObserver.unobserve(element)
        }
    }, [callbackRef, element])
}

export const getState = (element: Element | null, scrollTo: () => void) => {
    const { top, left } = element?.getBoundingClientRect() || {}

    return {
        ...pick(element, STATE_KEYS),
        top,
        left,
        scrollTo,
    }
}

type Props = TBaseProps & {
    children: ReactNode,
}

export const ScrollContainer = ({
    children,
    className = '',
}: Props) => {
    const containerRef = useRef(null)
    const contentRef = useRef(null)
    const container = containerRef.current
    const [state, setState] = useState<State>(DEFAULT_STATE)
    const scrollTo = useCallback((prop = {}) => {
        if (container) {
            (container as Element).scrollTo(prop)
        }
    }, [container])
    const onScroll = useCallback(() => {
        if (container) {
            setState(getState(container, scrollTo))
        }
    }, [container, scrollTo])

    useResizeObserver(contentRef.current, () => {
        if (!container) {
            setState(DEFAULT_STATE)

            return
        }
        const newState = getState(container, scrollTo)

        if (!isEqual(state, newState)) {
            setState(newState)
        }
    })

    return (
        <div
            className={`n2o-scrollcontainer overflow-auto ${className}`}
            onScroll={onScroll}
            ref={containerRef}
        >
            <div
                className={`n2o-scrollcontainer__content ${className}`}
                ref={contentRef}
            >
                <ScrollContext.Provider value={state}>
                    {children}
                </ScrollContext.Provider>
            </div>
        </div>
    )
}
