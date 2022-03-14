import React, { useRef, useImperativeHandle, forwardRef, useCallback, useContext, useEffect } from 'react'
import PropTypes from 'prop-types'
import { debounce, isEqual } from 'lodash'

import { ScrollContext } from '../../snippets/ScrollContainer/ScrollContainer'
import { usePrevious } from '../../../utils/usePrevious'

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
    const scrollSate = useContext(ScrollContext)
    const prevScroll = usePrevious(scrollSate)
    const sections = useRef({})
    const contentRef = useRef(null)
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

    const onScroll = useCallback(debounce((scrollSate) => {
        const content = contentRef.current

        if (ignoreScroll || !content) { return }

        const { top } = content.getBoundingClientRect()
        const offset = Math.round(top - scrollSate.top + scrollSate.scrollTop)

        const newActive = getActive(scrollSate, sections.current, offset)

        if (newActive && newActive !== active) {
            setActive(newActive)
        }
    }, SCROLL_THROTTLE, { maxWait: SCROLL_THROTTLE }), [ignoreScroll, active, setActive])

    useEffect(() => {
        if (prevScroll && (scrollSate.scrollTop !== prevScroll.scrollTop)) {
            onScroll(scrollSate)
        }
    }, [scrollSate, prevScroll, onScroll])

    return (
        <section className={CONTENT_GROUP_CLASS_NAME} ref={contentRef}>
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
