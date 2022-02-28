import React, { forwardRef, useCallback } from 'react'
import classNames from 'classnames'
import PropTypes from 'prop-types'

import { RegionContent } from '../RegionContent'

import { Title } from './Title'

const Section = forwardRef(({ id, title, headlines, children, active }, forwardedRef) => (
    <div
        ref={forwardedRef}
        id={id}
        key={id}
        className={classNames('n2o-scroll-spy-region__content-wrapper', { active: id === active })}
    >
        <Title
            title={title}
            className="n2o-scroll-spy-region__content-title"
            visible={headlines}
        />
        {children}
    </div>
))

Section.propTypes = {
    id: PropTypes.string.isRequired,
    title: PropTypes.string,
    headlines: PropTypes.bool,
    children: PropTypes.any,
    active: PropTypes.string,
}

/**
 * Рекурсивная отрисовка вложенных в друг друга секций и регистрация рефок на них
 */
export function SectionGroup({ id, title, headlines, content, menu: items, active, setSectionRef, pageId }) {
    let contentElement = null
    let sectionTitle
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
            />
        )
        sectionTitle = title
    } else {
        contentElement = items.map(item => (
            <SectionGroup
                {...item}
                setSectionRef={setSectionRef}
                pageId={pageId}
            />
        ))
        sectionTitle = null
    }

    return (
        <Section
            title={sectionTitle}
            id={id}
            headlines={headlines}
            active={active}
            ref={setRef}
        >
            {contentElement}
        </Section>
    )
}

SectionGroup.propTypes = {
    id: PropTypes.string.isRequired,
    title: PropTypes.string,
    headlines: PropTypes.bool,
    active: PropTypes.string,
    setSectionRef: PropTypes.func.isRequired,
    menu: PropTypes.array,
    pageId: PropTypes.string,
    content: PropTypes.shape(SectionGroup.propTypes),
}
