import { useRef } from 'react'

export function usePrevious<T>(value: T) {
    const ref = useRef<T>()
    const prevValue = ref.current

    ref.current = value

    return prevValue
}
