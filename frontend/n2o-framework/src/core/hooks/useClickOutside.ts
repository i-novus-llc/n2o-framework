import { RefObject, useEffect } from 'react'

export default function useClickOutside(
    effect: (event: MouseEvent | TouchEvent) => void,
    refs: Array<RefObject<HTMLElement>>,
) {
    useEffect(() => {
        const listener = (event: MouseEvent | TouchEvent) => {
            const res = refs.some(ref => !ref.current || ref.current.contains(event.target as Node))

            if (res) { return }

            effect(event)
        }

        document.addEventListener('mousedown', listener)
        document.addEventListener('touchstart', listener)

        return () => {
            document.removeEventListener('mousedown', listener)
            document.removeEventListener('touchstart', listener)
        }
    }, [...refs, effect])
}
