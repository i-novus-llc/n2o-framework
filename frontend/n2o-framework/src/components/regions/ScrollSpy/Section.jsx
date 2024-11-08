import React, { forwardRef, useCallback } from 'react'
import get from 'lodash/get'
import classNames from 'classnames'

import { RegionContent } from '../RegionContent'

import { Title } from './Title'

const Section = forwardRef(({ id, title, headlines, active, children }, forwardedRef) => {
    /* simplifies auto testing */
    const hasActiveChild = Array.isArray(children) &&
        children.some(child => get(child, 'props.id', null) === active)

    return (
        <div
            ref={forwardedRef}
            id={id}
            key={id}
            className={classNames(
                'n2o-scroll-spy-region__content-wrapper',
                {
                    active: id === active || hasActiveChild,
                },
            )
            }
        >
            <Title
                title={title}
                className="n2o-scroll-spy-region__content-title"
                visible={headlines}
            />
            {children}
        </div>
    )
})

/**
 * Рекурсивная отрисовка вложенных друг в друга секций и регистрация рефок на них
 */
export function SectionGroup({ id, title, headlines, content, menu: items, active, setSectionRef, pageId }) {
    let contentElement = null
    const hasContent = !(items?.length)

    const setRef = useCallback((ref) => {
        if (hasContent) {
            setSectionRef(id, ref)
        }
    }, [id, hasContent, setSectionRef])

    if (hasContent) {
        contentElement = (
            <RegionContent
                content={content}
                pageId={pageId}
                id={id}
                className="n2o-scroll-spy-region__content"
                headlines={headlines}
            />
        )
    } else {
        contentElement = items.map(item => (
            <SectionGroup
                {...item}
                setSectionRef={setSectionRef}
                pageId={pageId}
                headlines={headlines}
            />
        ))
    }

    return (
        <Section
            title={title}
            id={id}
            headlines={headlines}
            active={active}
            ref={setRef}
        >
            {contentElement}
        </Section>
    )
}
