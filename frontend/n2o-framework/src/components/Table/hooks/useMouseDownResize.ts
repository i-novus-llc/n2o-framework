import { MouseEvent as HtmlElementMouseEvent, MutableRefObject, RefObject, useCallback } from 'react'

type TResizableElement<T> = RefObject<T> | MutableRefObject<T | undefined>

/**
 * Функция принимает HTMlElement и возвращает функцию, что будет менять размер переданного элемента
 */
export const useMouseDownResize = <T extends HTMLElement>(resizableElement: TResizableElement<T>) => (
    useCallback((event: HtmlElementMouseEvent) => {
        const cell = resizableElement.current

        if (!cell) {
            return
        }
        const initialWidth = parseFloat(
            window.getComputedStyle(cell, null).getPropertyValue('width'),
        )
        const initialMouseX = event.clientX

        const handleMouseMove = (event: MouseEvent) => {
            const offset = event.clientX - initialMouseX
            const newWidth = initialWidth + offset

            cell.style.width = `${newWidth}px`
        }

        const handleMouseUp = () => {
            document.removeEventListener('mousemove', handleMouseMove)
            document.removeEventListener('mouseup', handleMouseUp)
        }

        document.addEventListener('mousemove', handleMouseMove)
        document.addEventListener('mouseup', handleMouseUp)
    }, [resizableElement])
)
