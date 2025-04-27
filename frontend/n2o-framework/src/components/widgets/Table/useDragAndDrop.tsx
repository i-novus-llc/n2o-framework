import { useEffect, useRef, useState } from 'react'

const GHOST_CLASSNAME = 'n2o-draggable-header-ghost'
const DROP_INDICATOR_CLASSNAME = 'n2o-draggable-header-ghost-indicator'
const DATA_DRAGGABLE = 'data-draggable'
const dragTitleSelector = 'n2o-drag-title-selector'

export enum DIRECTION {
    HORIZONTAL = 'horizontal',
    VERTICAL = 'vertical',
}

export type UseDragAndDropProps = {
    onDrop(sourceId: string, targetId: string): void
    direction: DIRECTION
    targetSelector: string
    ghostContainerId?: string
    dropIndicator?: boolean
}

export type HeaderRect = {
    id: string
    left: number
    right: number
    top: number
    bottom: number
}

const createGhostElement = (target: HTMLElement, container: HTMLElement, direction: DIRECTION, offsetX: number, offsetY: number) => {
    const ghost = document.createElement('div')

    ghost.className = GHOST_CLASSNAME

    Object.assign(ghost.style, {
        position: 'absolute',
        width: `${target.offsetWidth}px`,
        height: direction === DIRECTION.HORIZONTAL ? `${container.offsetHeight}px` : `${target.offsetHeight}px`,
        transform: `translate(${offsetX}px, ${offsetY}px)`,
        pointerEvents: 'none',
        top: 0,
        zIndex: '1000',
        opacity: '0.8',
    })

    ghost.textContent = target.querySelector(`.${dragTitleSelector}`)?.textContent || target.textContent || ''

    return ghost
}

const createDropIndicator = (container: HTMLElement) => {
    const line = document.createElement('div')

    line.className = DROP_INDICATOR_CLASSNAME
    line.style.position = 'absolute'
    line.style.backgroundColor = '#000'
    line.style.width = '2px'
    line.style.height = `${container.offsetHeight}px`
    line.style.pointerEvents = 'none'
    line.style.zIndex = '1000'
    line.style.top = '0'
    container.appendChild(line)

    return line
}

function updateGhostPosition(ghost: HTMLDivElement, newX: number, newY: number) {
    ghost.style.transform = `translate(${newX}px, ${newY}px)`
}

const findTargetRect = (headerRects: HeaderRect[], mouseX: number, mouseY: number, direction: DIRECTION) => {
    return headerRects.find((rect) => {
        if (direction === DIRECTION.HORIZONTAL) {
            return mouseX >= rect.left && mouseX <= rect.right
        }

        return mouseY >= rect.top && mouseY <= rect.bottom
    })
}

const calculateLineX = (headerRects: HeaderRect[], draggingId: string | null, targetRect: HeaderRect) => {
    if (draggingId === targetRect.id) { return targetRect.left }

    const sortedRects = [...headerRects].sort((a, b) => a.left - b.left)
    const draggingIndex = sortedRects.findIndex(r => r.id === draggingId)
    const targetIndex = sortedRects.findIndex(r => r.id === targetRect.id)

    if (draggingIndex === -1 || targetIndex === -1) { return null }

    return draggingIndex < targetIndex ? targetRect.right : targetRect.left
}

const updateDropIndicatorPosition = (line: HTMLDivElement, lineX: number, scrollLeft: number, containerRect: DOMRect) => {
    const linePositionX = lineX - containerRect.left - window.scrollX + scrollLeft

    line.style.left = `${linePositionX}px`
    line.style.display = 'block'
}

export const useDragAndDrop = ({
    onDrop,
    direction,
    targetSelector,
    ghostContainerId,
    dropIndicator = false,
}: UseDragAndDropProps) => {
    const ghostRef = useRef<HTMLDivElement | null>(null)
    const lineRef = useRef<HTMLDivElement | null>(null)
    const draggingIdRef = useRef<string | null>(null)
    const offsetRef = useRef({ x: 0, y: 0 })
    const headerRectsRef = useRef<HeaderRect[]>([])
    const containerRectRef = useRef<DOMRect | null>(null)
    const [draggingId, setDraggingId] = useState<string | null>(null)

    const handleMouseMove = (e: MouseEvent) => {
        if (!ghostRef.current || !containerRectRef.current) { return }

        const containerRect = containerRectRef.current
        const container = ghostContainerId ? document.getElementById(ghostContainerId) : document.body
        const scrollLeft = container?.scrollLeft || 0
        const scrollTop = container?.scrollTop || 0

        let newX = e.clientX - containerRect.left - offsetRef.current.x + scrollLeft
        let newY = e.clientY - containerRect.top - offsetRef.current.y + scrollTop

        if (direction === DIRECTION.HORIZONTAL) {
            newX = Math.max(0, Math.min(newX, containerRect.width - ghostRef.current.offsetWidth))
            newY = 0
        } else {
            newY = Math.max(0, Math.min(newY, containerRect.height - ghostRef.current.offsetHeight))
            newX = 0
        }

        updateGhostPosition(ghostRef.current, newX, newY)

        // Обработка drop-indicator
        if (!dropIndicator || direction !== DIRECTION.HORIZONTAL) {
            lineRef.current?.remove()
            lineRef.current = null

            return
        }

        const mouseX = e.clientX + window.scrollX
        const targetRect = findTargetRect(headerRectsRef.current, mouseX, 0, direction)

        if (!targetRect) {
            lineRef.current?.style.setProperty('display', 'none')

            return
        }

        const lineX = calculateLineX(headerRectsRef.current, draggingIdRef.current, targetRect)

        if (lineX === null) {
            lineRef.current?.style.setProperty('display', 'none')

            return
        }

        if (!lineRef.current && container) {
            lineRef.current = createDropIndicator(container)
        }

        if (lineRef.current && containerRect) {
            updateDropIndicatorPosition(lineRef.current, lineX, scrollLeft, containerRect)
        }
    }

    const handleMouseUp = (e: MouseEvent) => {
        const currentDraggingId = draggingIdRef.current
        const targetId = findTargetRect(headerRectsRef.current, e.clientX + window.scrollX, e.clientY + window.scrollY, direction)?.id || null

        if (currentDraggingId && targetId && currentDraggingId !== targetId) {
            onDrop(currentDraggingId, targetId)
        }

        ghostRef.current?.remove()
        ghostRef.current = null
        lineRef.current?.remove()
        lineRef.current = null
        draggingIdRef.current = null
        setDraggingId(null)

        document.removeEventListener('mousemove', handleMouseMove)
        document.removeEventListener('mouseup', handleMouseUp)
    }

    const onMouseDown = (e: MouseEvent, id: string) => {
        e.preventDefault()
        const target = e.currentTarget as HTMLElement
        const rect = target.getBoundingClientRect()
        const { scrollX, scrollY } = window

        offsetRef.current = {
            x: e.clientX - rect.left,
            y: e.clientY - rect.top,
        }

        const ghostContainer = ghostContainerId ? document.getElementById(ghostContainerId) : null
        const container = ghostContainer || document.body
        const containerRect = container.getBoundingClientRect()

        containerRectRef.current = containerRect

        const offsetX = rect.left - containerRect.left + (container.scrollLeft || 0)
        const offsetY = rect.top - containerRect.top + (container.scrollTop || 0)

        headerRectsRef.current = Array.from(document.querySelectorAll(`${targetSelector}[${DATA_DRAGGABLE}]`)).map((element) => {
            const rect = element.getBoundingClientRect()

            return {
                id: element.id,
                left: rect.left + scrollX,
                right: rect.right + scrollX,
                top: rect.top + scrollY,
                bottom: rect.bottom + scrollY,
            }
        })

        const ghost = createGhostElement(target, container, direction, offsetX, offsetY)

        container.appendChild(ghost)
        ghostRef.current = ghost
        draggingIdRef.current = id
        setDraggingId(id)

        document.addEventListener('mousemove', handleMouseMove)
        document.addEventListener('mouseup', handleMouseUp)
    }

    useEffect(() => {
        return () => {
            document.removeEventListener('mousemove', handleMouseMove)
            document.removeEventListener('mouseup', handleMouseUp)
            ghostRef.current?.remove()
        }
    }, [])

    return {
        onMouseDown,
        draggingId,
        draggableAttributes: {
            [DATA_DRAGGABLE]: true,
            style: {
                cursor: 'grab',
                opacity: draggingId ? 0.5 : 1,
                transition: 'opacity 0.2s',
            },
            dragTitleSelector,
        },
    }
}
