import React, { createContext, useState, useEffect, useRef, useCallback } from 'react'
import PropTypes from 'prop-types'
import { isEqual, pick } from 'lodash'

const DEFAULT_STATE = {
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

const getState = (element, scrollTo) => {
    const { top, left } = element.getBoundingClientRect()

    return {
        ...pick(element, STATE_KEYS),
        top,
        left,
        scrollTo,
    }
}

export const ScrollContainer = ({
    children,
    className = '',
}) => {
    const containerRef = useRef(null)
    const container = containerRef.current
    const [state, setState] = useState(DEFAULT_STATE)
    const onScroll = useCallback(() => {
        if (container) {
            setState(getState(container))
        }
    }, [container])
    const scrollTo = useCallback((prop = {}) => {
        container?.scrollTo(prop)
    }, [container])

    useEffect(() => {
        if (!container) {
            setState(DEFAULT_STATE)

            return
        }
        const newState = getState(container, scrollTo)

        if (!isEqual(state, newState)) {
            setState(newState)
        }
    }, [container, state, scrollTo])

    return (
        <div
            className={`n2o-scrollcontainer overflow-auto ${className}`}
            onScroll={onScroll}
            ref={containerRef}
        >
            <ScrollContext.Provider value={state}>
                {children}
            </ScrollContext.Provider>
        </div>
    )
}

ScrollContainer.propTypes = {
    children: PropTypes.any,
    className: PropTypes.string,
}
