import { useState, RefObject, useEffect } from 'react'

/**
 * Хук для отслеживания ширины элемента-триггера при fullSize === true
 */
export function useTriggerWidth(
    triggerRef: RefObject<HTMLElement>,
    fullSize?: boolean,
): string {
    const [width, setWidth] = useState<number | null>(null)

    useEffect(() => {
        if (!fullSize) { return }

        const element = triggerRef.current

        if (!element) { return }

        const updateWidth = () => {
            if (triggerRef.current) {
                setWidth(triggerRef.current.offsetWidth)
            }
        }

        updateWidth()

        const resizeObserver = new ResizeObserver(updateWidth)

        resizeObserver.observe(element)

        // eslint-disable-next-line consistent-return
        return () => {
            resizeObserver.disconnect()
        }
    }, [fullSize, triggerRef])

    return width ? `${width}px` : 'auto'
}
