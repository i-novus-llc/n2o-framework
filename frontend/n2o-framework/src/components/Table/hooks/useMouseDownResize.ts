import { CSSProperties, MouseEvent as HtmlElementMouseEvent, MutableRefObject, RefObject, useCallback } from 'react'

type ResizableElement<T> = RefObject<T> | MutableRefObject<T | undefined>

const parseWidth = (width?: string | number): number | null => {
    if (!width) { return null }
    if (typeof width === 'number') { return width }
    if (width.match(/\d+(\s*px)?/)) { return parseFloat(width) }

    return null
}

/**
 * Функция принимает HTMlElement и возвращает функцию, что будет менять размер переданного элемента
 */
export const useMouseDownResize = <T extends HTMLElement>(
    resizableElement: ResizableElement<T>,
    style: CSSProperties = {},
) => (useCallback((event: HtmlElementMouseEvent) => {
        const cell = resizableElement.current

        if (!cell) { return }

        const initialWidth = parseFloat(
            window.getComputedStyle(cell, null).getPropertyValue('width'),
        )
        const initialMouseX = event.clientX

        const handleMouseMove = (event: MouseEvent) => {
            const { minWidth, maxWidth } = style
            const min = parseWidth(minWidth)
            const max = parseWidth(maxWidth)
            const offset = event.clientX - initialMouseX
            let newWidth = initialWidth + offset

            if (min && (newWidth < min)) { newWidth = min }
            if (max && (newWidth > max)) { newWidth = max }

            cell.style.width = `${newWidth}px`
            // Костыль, иначе таблица с горизонтальным скролом задаёт размер столбца по ширине контента
            cell.style.minWidth = `${newWidth}px`
        }

        const handleMouseUp = () => {
            document.removeEventListener('mousemove', handleMouseMove)
            document.removeEventListener('mouseup', handleMouseUp)
        }

        document.addEventListener('mousemove', handleMouseMove)
        document.addEventListener('mouseup', handleMouseUp)
    }, [resizableElement, style]))
