import { RefObject, useContext, useEffect } from 'react'

import { ScrollContext } from '../ScrollContainer'

export const useSticky = (
    containerRef: RefObject<HTMLElement>,
    targetRef: RefObject<HTMLElement>,
) => {
    const { container: scrollContainer, content: scrollContent } = useContext(ScrollContext)
    const container = containerRef.current
    const target = targetRef.current

    useEffect(() => {
        if (!scrollContainer || !scrollContent || !container || !target) { return }

        const paddingTop = parseInt(getComputedStyle(scrollContainer).paddingTop, 10)
        let offset = paddingTop

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
            const sc = scrollContent.getBoundingClientRect()
            const cc = container.getBoundingClientRect()

            offset = Math.floor(cc.top - sc.top + paddingTop)
            onScroll()
        }

        const resizeObserver = new ResizeObserver(onResize)
        const intersectionObserver = new IntersectionObserver(onResize)

        scrollContainer.addEventListener('scroll', onScroll)
        resizeObserver.observe(scrollContent)
        intersectionObserver.observe(container)
        onScroll()

        // eslint-disable-next-line consistent-return
        return () => {
            resizeObserver.unobserve(scrollContainer)
            intersectionObserver.unobserve(container)
            scrollContainer.removeEventListener('scroll', onScroll)
        }
    }, [scrollContainer, container, target])
}
