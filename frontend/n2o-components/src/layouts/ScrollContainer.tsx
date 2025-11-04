import React, { createContext, useRef, useCallback, ReactNode } from 'react'
import { pick } from 'lodash'

import { TBaseProps } from '../types'

export type State = {
    container: null | HTMLDivElement
    scrollTo(config: Record<string, unknown>): void
}

const DEFAULT_STATE: State = {
    container: null,
    scrollTo() {},
}

const STATE_KEYS = [
    'clientHeight',
    'clientWidth',
    'scrollHeight',
    'scrollWidth',
    'scrollTop',
    'scrollLeft',
    'scrollTo',
    'top',
    'left',
]

export const ScrollContext = createContext(DEFAULT_STATE)

export const getState = (element: Element | null, scrollTo: () => void) => {
    const { top = 0, left = 0 } = element?.getBoundingClientRect() || {}

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
    const containerRef = useRef<HTMLDivElement>(null)
    const contentRef = useRef<HTMLDivElement>(null)
    const state = useRef<State>({ ...DEFAULT_STATE })
    const scrollTo = useCallback((prop?: ScrollToOptions) => {
        containerRef.current?.scrollTo(prop)
    }, [containerRef])

    state.current.container = containerRef.current
    state.current.scrollTo = scrollTo

    return (
        <div
            className={`scroll_container overflow-auto ${className} d-flex`}
            ref={containerRef}
        >
            <div
                className="scroll_content d-flex flex-column flex-grow-1"
                ref={contentRef}
            >
                <ScrollContext.Provider value={state.current}>
                    {children}
                </ScrollContext.Provider>
            </div>
        </div>
    )
}
