import React, { useRef, useImperativeHandle, forwardRef, useCallback, useEffect } from 'react'
import PropTypes from 'prop-types'
import { debounce } from 'lodash'

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
    headlines,
    scrollState,
}, forwardedRef) => {
    const prevScroll = usePrevious(scrollState)
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

    // eslint-disable-next-line react-hooks/exhaustive-deps
    const onScroll = useCallback(debounce((scrollState) => {
        const content = contentRef.current

        if (ignoreScroll || !content) { return }

        const { top } = content.getBoundingClientRect()
        const offset = Math.round(top - scrollState.top + scrollState.scrollTop)

        const newActive = getActive(scrollState, sections.current, offset)

        if (newActive && newActive !== active) {
            setActive(newActive)
        }
    }, SCROLL_THROTTLE, { maxWait: SCROLL_THROTTLE }), [ignoreScroll, active, setActive])

    useEffect(() => {
        if (!prevScroll) { return }

        // Корректировочный скрол при изменении размеров контейнера
        if (
            (scrollState.scrollHeight !== prevScroll.scrollHeight) ||
            (scrollState.clientHeight !== prevScroll.clientHeight)
        ) {
            forwardedRef.current.scrollTo(active)

            return
        }

        if (scrollState.scrollTop !== prevScroll.scrollTop) {
            onScroll(scrollState)
        }
    }, [scrollState, prevScroll, onScroll, forwardedRef, active])

    return (
        <section className={CONTENT_GROUP_CLASS_NAME} ref={contentRef}>
            {items.map(item => (
                <SectionGroup
                    {...item}
                    setSectionRef={setSectionRef}
                    pageId={pageId}
                    active={active}
                    headlines={headlines}
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
