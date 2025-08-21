import { useCallback, useEffect, useRef, useState, RefObject } from 'react'
import { autoUpdate, flip, offset, shift, useFloating, Placement } from '@floating-ui/react'

import { useClickOutside } from '../../utils/useClickOutside'

export enum Container {
    BODY = 'body',
    TARGET = 'target',
}

export interface TooltipFloating {
    delay: number
    placement: Placement
    container?: Container
    closeOnClickOutside: boolean
}

/**
 * Хук для управления состоянием тултипа
 */
export function useTooltipFloating({ delay, placement, container, closeOnClickOutside }: TooltipFloating) {
    const [open, onOpenChange] = useState(false)
    const timeoutRef = useRef<number | undefined>()
    const isMountedRef = useRef(false)

    useEffect(() => {
        isMountedRef.current = true

        return () => {
            isMountedRef.current = false
            clearTimeout(timeoutRef.current)
        }
    }, [])

    const handleOpen = useCallback(() => {
        if (!isMountedRef.current) { return }
        clearTimeout(timeoutRef.current)
        timeoutRef.current = window.setTimeout(() => onOpenChange(true), delay)
    }, [delay])

    const handleClose = useCallback(() => {
        if (!isMountedRef.current) { return }
        clearTimeout(timeoutRef.current)
        onOpenChange(false)
    }, [])

    const toggle = useCallback(() => {
        if (!isMountedRef.current) { return }
        clearTimeout(timeoutRef.current)
        onOpenChange(prev => !prev)
    }, [])

    /** Хук возвращающий позиционирование */
    const { refs, floatingStyles } = useFloating({
        open,
        onOpenChange,
        placement,
        strategy: container === Container.TARGET ? 'absolute' : 'fixed',
        middleware: [offset(6), flip(), shift()],
        whileElementsMounted: autoUpdate,
    })

    useClickOutside({
        isActive: open && closeOnClickOutside,
        onClickOutside: handleClose,
        excludeRefs: [
            refs.reference as RefObject<HTMLElement | null>,
            refs.floating as RefObject<HTMLElement | null>,
        ],
    })

    const style = {
        zIndex: 9999,
        ...(container === Container.TARGET ? { width: 'max-content' } : {}),
    }

    return {
        open,
        onOpenChange,
        handleOpen,
        handleClose,
        toggle,
        refs,
        floatingRef: refs.setFloating,
        floatingStyles: { ...floatingStyles, ...style },
        portal: container === Container.BODY,
    }
}
