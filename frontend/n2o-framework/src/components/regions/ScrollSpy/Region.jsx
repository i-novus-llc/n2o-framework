import React, { useRef, useCallback, useState, useEffect } from 'react'
import { connect } from 'react-redux'
import classNames from 'classnames'
import flowRight from 'lodash/flowRight'

import { getState } from '../../snippets/ScrollContainer/ScrollContainer'
import withWidgetProps from '../withWidgetProps'
import { createRegionContainer } from '../withRegionContainer'
import { widgetsSelector } from '../../../ducks/widgets/selectors'
import { mapToNumeric } from '../../../tools/helpers'

import { Menu } from './Menu'
import { Content } from './Content'
import { createStyle, mapContextItems } from './utils'

const DEFAULT_SCROLL_STATE = {
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

export function Region(
    {
        id,
        title,
        className,
        style,
        pageId,
        changeActiveEntity,
        activeEntity: active,
        widgets,
        disabled,
        content: items = [],
        maxHeight: propsMaxHeight = 'auto',
        placement = 'left',
        headlines = true,
    },
) {
    const [isInit, setInit] = useState(false)
    const [scrollState, setScrollState] = useState(DEFAULT_SCROLL_STATE)

    const containerRef = useRef(null)
    const contentRef = useRef(null)
    const container = containerRef.current

    const { maxHeight } = mapToNumeric({ maxHeight: propsMaxHeight })

    const scrollable = maxHeight || style?.maxHeight || false
    const firstMenuItem = items?.[0]
    const firstMenuItemId = firstMenuItem?.id

    const scrollTo = useCallback((prop = {}) => {
        container?.scrollTo(prop)
    }, [container])

    const onChangeActive = useCallback((active) => {
        if (scrollable) {
            contentRef.current?.scrollTo(active)
        }

        changeActiveEntity(active)
    }, [contentRef, scrollable, changeActiveEntity])

    // initial scroll
    useEffect(() => {
        if (!isInit && contentRef.current && active && scrollable) {
            setInit(true)
            contentRef.current?.scrollTo(active)
        }
    }, [contentRef, active, isInit, scrollable])

    const onScroll = useCallback(() => {
        if (container) {
            setScrollState(getState(container, scrollTo))
        }
    }, [container, scrollTo])

    const currentStyle = createStyle(maxHeight, style)
    const contextItems = mapContextItems(items)

    return (
        <div
            className={classNames(
                'd-flex',
                'n2o-scroll-spy-region',
                className,
                {
                    'flex-row-reverse position-right': placement === 'right',
                    'n2o-disabled': disabled,
                },
            )}
            id={id}
            style={currentStyle}
            ref={containerRef}
            onScroll={onScroll}
        >

            <Menu
                items={items}
                title={title}
                active={active || firstMenuItemId}
                setActive={onChangeActive}
                widgets={widgets}
            />
            <Content
                ref={contentRef}
                items={contextItems}
                pageId={pageId}
                headlines={headlines}
                active={active}
                setActive={changeActiveEntity}
                scrollState={scrollState}
            />
        </div>
    )
}

const mapStateToProps = state => ({ widgets: widgetsSelector(state) })

export default flowRight(
    createRegionContainer({ listKey: 'content' }),
    withWidgetProps,
    connect(mapStateToProps),
)(Region)
