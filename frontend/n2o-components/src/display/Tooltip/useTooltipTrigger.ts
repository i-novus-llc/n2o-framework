import {
    cloneElement,
    useEffect,
    ReactElement,
    Ref,
    MutableRefObject,
} from 'react'
import { ReferenceType } from '@floating-ui/react'

export enum Trigger {
    HOVER = 'hover',
    CLICK = 'click',
}

export interface TooltipTrigger {
    children: ReactElement & { ref?: Ref<unknown> }
    refs: {
        setReference(node: ReferenceType | null): void
        reference: MutableRefObject<ReferenceType | null>
    }
    trigger: Trigger
    handleOpen(): void
    handleClose(): void
    toggle(): void
}

/** Хук для работы с системой позиционирования
 * Устанавливает слушателей эвентов
 * Объединяет существующие ref с ссылками, необходимыми для позиционирования
 */
export function useTooltipTrigger({
                                      children,
                                      refs,
                                      trigger,
                                      handleOpen,
                                      handleClose,
                                      toggle,
                                  }: TooltipTrigger) {
    useEffect(() => {
        const element = refs.reference.current as HTMLElement | null

        if (!element) { return }

        const eventHandlers: Array<[string, () => void]> = []

        if (trigger === Trigger.HOVER) {
            eventHandlers.push(
                ['mouseenter', handleOpen],
                ['mouseleave', handleClose],
                ['focus', handleOpen],
                ['blur', handleClose],
            )
        }

        if (trigger === Trigger.CLICK) {
            eventHandlers.push(['click', toggle])
        }

        eventHandlers.forEach(([event, handler]) => {
            element.addEventListener(event, handler)
        })

        // eslint-disable-next-line consistent-return
        return () => {
            eventHandlers.forEach(([event, handler]) => {
                element.removeEventListener(event, handler)
            })
        }
    }, [trigger, handleOpen, handleClose, toggle, refs.reference])

    if (!children) { return null }

    return cloneElement(children, {
        ref: (node: HTMLElement | null) => {
            refs.setReference(node)
            if (node) {
                // Проброс оригинального ref
                if (typeof children.ref === 'function') {
                    children.ref(node)
                } else if (children.ref) {
                    (children.ref as MutableRefObject<HTMLElement | null>).current = node
                }
            }
        },
    })
}
