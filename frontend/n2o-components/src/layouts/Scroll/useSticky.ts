import { RefObject, useContext, useEffect } from 'react'

import { ScrollContext } from '../ScrollContainer'

export const useSticky = (
    containerRef: RefObject<HTMLElement>,
    targetRef: RefObject<HTMLElement>,
) => {
    const { container: scrollContainer } = useContext(ScrollContext)
    const container = containerRef.current
    const target = targetRef.current

    useEffect(() => {
        let offset = 0

        if (!scrollContainer || !container || !target) { return }

        const onScroll = () => {
            const { scrollTop } = scrollContainer
            const containerH = container.clientHeight
            const targetH = target.clientHeight
            const maxTop = containerH - targetH

            let top = scrollTop - offset - 1

            if (top < 0) { top = 0 }
            if (top > maxTop) { top = maxTop }

            target.style.transform = `translateY(${top}px)`
        }
        const onResize = () => {
            const sc = scrollContainer.getBoundingClientRect()
            const cc = container.getBoundingClientRect()

            offset = Math.floor(cc.top - sc.top + scrollContainer.scrollTop)
        }

        const resizeObserver = new ResizeObserver(onResize)

        scrollContainer.addEventListener('scroll', onScroll)
        resizeObserver.observe(scrollContainer)
        onScroll()

        // eslint-disable-next-line consistent-return
        return () => {
            resizeObserver.unobserve(scrollContainer)
            scrollContainer.removeEventListener('scroll', onScroll)
        }
    }, [scrollContainer, container, target])
}
