import React, { useRef, useImperativeHandle, forwardRef, useCallback } from 'react'
import throttle from 'lodash/throttle'
import PropTypes from 'prop-types'

import { SectionGroup } from './Section'
import { CONTENT_GROUP_CLASS_NAME, SCROLL_DELAY, SCROLL_THROTTLE } from './const'
import { useTimeoutSwitcher, getActive } from './utils'

/**
 * ScrollSpy Контент-контейнер
 * Компонент отдаёт наружу метод scrollTo(id) для прокрутки до нужного элемента
 * и меняет активный элемент в зависимости от скрола
 */
export const Content = forwardRef(({
    items,
    pageId,
    active,
    setActive,
}, forwardedRef) => {
    const sections = useRef({})
    const contentRef = useRef(null)
    const prevScroll = useRef(0)
    const setSectionRef = useCallback((id, ref) => {
        sections.current[id] = ref
    }, [sections])
    const [ignoreScroll, setIgnoreScroll] = useTimeoutSwitcher(SCROLL_DELAY, false)

    useImperativeHandle(forwardedRef, () => ({
        scrollTo(id) {
            setIgnoreScroll()
            sections.current[id]?.scrollIntoView({
                behavior: 'smooth',
            })
        },
    }), [sections, setIgnoreScroll])

    const onScroll = useCallback(throttle(() => {
        const contentElement = contentRef.current

        if (ignoreScroll || !contentElement) { return }

        const { scrollTop } = contentElement

        const newActive = getActive(contentElement, sections.current, scrollTop - prevScroll.current)

        if (newActive && newActive !== active) {
            setActive(newActive)
        }
        prevScroll.current = scrollTop
    }, SCROLL_THROTTLE), [ignoreScroll, active, setActive])

    return (
        <section onScroll={onScroll} className={CONTENT_GROUP_CLASS_NAME} ref={contentRef}>
            {items.map(item => (
                <SectionGroup
                    {...item}
                    setSectionRef={setSectionRef}
                    pageId={pageId}
                    active={active}
                />
            ))}
        </section>
    )
})

Content.propTypes = {
    pageId: PropTypes.string,
    active: PropTypes.string,
    setActive: PropTypes.func,
    items: PropTypes.array,
}
