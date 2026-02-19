import { RefObject, useEffect, useRef } from 'react'

export interface UsePanelHeightProps {
    isActive: boolean
    ref: RefObject<HTMLDivElement>
}

// Эффект для управления высотой при изменении isActive
export const usePanelHeight = ({ isActive, ref }: UsePanelHeightProps) => {
    const isFirstRender = useRef(true)

    useEffect(() => {
        const contentElement = ref.current

        if (!contentElement) { return }

        // На первом рендере высота без анимации
        if (isFirstRender.current) {
            if (isActive) {
                contentElement.style.height = 'auto'
            } else {
                contentElement.style.height = '0px'
                contentElement.style.overflow = 'hidden'
            }
            isFirstRender.current = false

            return
        }

        if (isActive) {
            contentElement.style.height = '0px'
            contentElement.style.overflow = 'hidden'

            requestAnimationFrame(() => {
                const currentElement = ref.current

                if (!currentElement) { return }

                const height = currentElement.scrollHeight

                currentElement.style.height = `${height}px`

                setTimeout(() => {
                    if (currentElement) {
                        currentElement.style.height = 'auto'
                        currentElement.style.overflow = 'visible'
                    }
                }, 300)
            })
        } else {
            const height = contentElement.scrollHeight

            contentElement.style.height = `${height}px`
            contentElement.style.overflow = 'hidden'

            requestAnimationFrame(() => {
                const currentElement = ref.current

                if (currentElement) {
                    currentElement.style.height = '0px'
                }
            })
        }
    }, [isActive])
}
