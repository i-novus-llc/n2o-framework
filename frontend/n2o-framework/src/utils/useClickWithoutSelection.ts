import { useCallback, MouseEvent, useRef } from 'react'

export function useClickWithoutSelection<E extends MouseEvent>(callback: (event: E) => void) {
    const callbackRef = useRef(callback)

    callbackRef.current = callback

    return useCallback((event: E) => {
        const selection = window.getSelection()

        const hasSelection = selection && (selection.toString().trim().length > 0)

        if (!hasSelection) { callbackRef.current(event) }
    }, [])
}
