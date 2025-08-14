import { RefObject, useEffect } from 'react'

type RefType = | RefObject<HTMLElement | null> | { current: HTMLElement | null }

interface UseClickOutsideProps {
    isActive: boolean
    onClickOutside(): void
    excludeRefs: RefType[]
}

export function useClickOutside({
    isActive,
    onClickOutside,
    excludeRefs,
}: UseClickOutsideProps) {
    useEffect(() => {
        if (!isActive) { return }

        const handleClickOutside = (e: MouseEvent) => {
            const target = e.target as Node

            const isInside = excludeRefs.some((ref) => {
                const element = ref.current

                return element instanceof HTMLElement && element.contains(target)
            })

            if (isInside) { return }

            onClickOutside()
        }

        document.addEventListener('pointerdown', handleClickOutside)

        // eslint-disable-next-line consistent-return
        return () => document.removeEventListener('pointerdown', handleClickOutside)
    }, [isActive, onClickOutside, excludeRefs])
}
