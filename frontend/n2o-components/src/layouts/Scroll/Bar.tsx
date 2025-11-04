import React, { useCallback, useEffect, useRef, useState, RefObject } from 'react'

export type Props = {
    targetRef: RefObject<HTMLElement>
}

export function Scrollbar({
    targetRef,
}: Props) {
    const scrollContainerRef = useRef<HTMLDivElement>(null)
    const [dimensions, setDimensions] = useState({
        clientWidth: 0,
        scrollWidth: 0,
    })

    // Обновление размеров при изменении targetRef
    const updateDimensions = useCallback(() => {
        const target = targetRef.current

        if (target) {
            setDimensions({
                clientWidth: target.clientWidth,
                scrollWidth: target.scrollWidth,
            })
        }
    }, [targetRef])

    // Синхронизация скролла
    const onScroll = useCallback(() => {
        const target = targetRef.current
        const container = scrollContainerRef.current

        if (target && container) {
            target.scrollLeft = container.scrollLeft
        }
    }, [targetRef])

    useEffect(() => {
        const target = targetRef.current

        if (!target) { return }

        // Наблюдатель за изменениями размеров
        const resizeObserver = new ResizeObserver(updateDimensions)
        // Наблюдатель за изменениями в DOM (новые элементы, изменения контента)
        const mutationObserver = new MutationObserver(updateDimensions)
        const onTargetScroll = () => {
            const container = scrollContainerRef.current

            if (container) {
                container.scrollLeft = target.scrollLeft
            }
        }

        resizeObserver.observe(target)
        mutationObserver.observe(target, {
            childList: true,
            subtree: true,
            attributes: true,
            attributeFilter: ['style', 'class'],
        })
        target.addEventListener('scroll', onTargetScroll)
        updateDimensions()

        // eslint-disable-next-line consistent-return
        return () => {
            resizeObserver.disconnect()
            mutationObserver.disconnect()
            target.removeEventListener('scroll', onTargetScroll)
        }
    }, [targetRef.current])

    if (dimensions.scrollWidth <= dimensions.clientWidth) { return null }

    return (
        <div
            className="scrollbar-container"
            ref={scrollContainerRef}
            onScroll={onScroll}
            style={{
                width: `${dimensions.clientWidth}px`,
                overflowX: 'auto',
            }}
        >
            <div
                className="scroll-track"
                style={{
                    width: `${dimensions.scrollWidth}px`,
                    height: '1px',
                }}
            >
                {/* Пустой элемент для создания пространства скролла */}
            </div>
        </div>
    )
}
