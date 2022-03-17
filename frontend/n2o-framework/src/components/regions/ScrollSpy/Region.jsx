import React, { useRef, useCallback, useState, useEffect } from 'react'
import classNames from 'classnames'
import { compose, setDisplayName } from 'recompose'

import withWidgetProps from '../withWidgetProps'
import withRegionContainer from '../withRegionContainer'

import { ScrollSpyTypes } from './ScrollSpyTypes'
import { Menu } from './Menu'
import { Content } from './Content'

export function Region(
    {
        id,
        placement = 'left',
        title,
        className,
        headlines = true,
        style,
        menu: items, // TODO rename param from backend
        pageId,
        changeActiveEntity,
        activeEntity: active,
    },
) {
    const [isInit, setInit] = useState(false)
    const contentRef = useRef(null)

    const onChangeActive = useCallback((active) => {
        contentRef.current?.scrollTo(active)
        changeActiveEntity(active)
    }, [contentRef, changeActiveEntity])

    // initial scroll
    useEffect(() => {
        if (!isInit && contentRef.current && active) {
            setInit(true)
            contentRef.current?.scrollTo(active)
        }
    }, [contentRef, active, isInit])

    return (
        <div
            className={classNames(
                'd-flex',
                'n2o-scroll-spy-region',
                className,
                {
                    'flex-row-reverse position-right': placement === 'right',
                },
            )}
            id={id}
            style={style}
        >

            <Menu
                menu={items}
                title={title}
                active={active}
                setActive={onChangeActive}
            />
            <Content
                ref={contentRef}
                items={items}
                pageId={pageId}
                headlines={headlines}
                active={active}
                setActive={changeActiveEntity}
            />
        </div>
    )
}

Region.propTypes = ScrollSpyTypes

export default compose(
    setDisplayName('ScrollSpy'),
    withRegionContainer({ listKey: 'menu' }), // TODO rename menu to items from backend
    withWidgetProps,
)(Region)
