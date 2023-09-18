/* eslint-disable @typescript-eslint/no-explicit-any */
import { createContext, useContext } from 'use-context-selector'
import React, {
    FC,
    MouseEvent,
    useCallback,
    useMemo,
    useRef,
    useState,
    RefObject,
} from 'react'
import { createPortal } from 'react-dom'

import useClickOutside from '../../../core/hooks/useClickOutside'

type OverlayContext = {
    onShowOverlay?(event: MouseEvent, model: any): void
    onHideOverlay?(): void
}
type OverlayProps<T extends HTMLElement = HTMLElement> = {
    overlay?: {
        className?: string
        toolbar: Array<{
            id: string
            buttons: Array<{
                component: FC<any>
                id: string
                [x: string]: any
            }>
        }>
    }
    refContainerElem: RefObject<T>
    onClickActionButton(data: any): void
}

const overlayContext = createContext<OverlayContext>({})

export const ToolbarOverlay: FC<OverlayProps> = ({
    children,
    overlay,
    ...props
}) => (
    <>
        {overlay ? (
            <WithOverlay
                {...props}
                overlay={overlay}
            >
                {children}
            </WithOverlay>
        ) : children}
    </>
)

const WithOverlay: FC<Required<OverlayProps>> = ({
    children,
    overlay,
    refContainerElem,
    onClickActionButton,
}) => {
    const { toolbar, className } = overlay
    const isDisabledToggleFunc = useRef(false)
    const rowElem = useRef<HTMLElement>()
    const model = useRef({})
    const [isVisibleOverlay, setIsVisibleOverlay] = useState(false)
    const overlayContainerRef = useRef<HTMLDivElement>(null)

    const methods = useMemo(() => ({
        onShowOverlay(event: MouseEvent<HTMLElement>, data: any) {
            const overlay = overlayContainerRef.current
            const container = refContainerElem.current

            if (!overlay || !container || isDisabledToggleFunc.current) {
                return
            }

            const row = event.currentTarget
            const rowParam = row.getBoundingClientRect()
            const tableContainerParam = container.getBoundingClientRect()

            rowElem.current = row
            setIsVisibleOverlay(true)
            model.current = data
            overlay.style.display = 'flex'
            overlay.style.top = `${rowParam.top + rowParam.height}px`
            overlay.style.left = `${tableContainerParam.left}px`
            overlay.style.width = `${tableContainerParam.width}px`
        },
        onHideOverlay() {
            const overlay = overlayContainerRef.current

            if (!overlay || isDisabledToggleFunc.current) {
                return
            }

            if (rowElem.current) {
                rowElem.current.dataset.overlayHovered = 'false'
            }

            setIsVisibleOverlay(false)
        },
    }), [refContainerElem])

    const onPinOverlay = () => {
        isDisabledToggleFunc.current = true
    }

    const continueShowOverlay = () => {
        const overlay = overlayContainerRef.current

        if (!overlay) {
            return
        }

        if (rowElem.current) {
            rowElem.current.dataset.overlayHovered = 'true'
        }

        overlay.style.display = 'flex'
        setIsVisibleOverlay(true)
    }

    const unPinOverlay = useCallback(() => {
        isDisabledToggleFunc.current = false
    }, [])

    const onOutsideClick = useCallback(() => {
        unPinOverlay()
        methods.onHideOverlay()
    }, [methods, unPinOverlay])

    const onClickActionButtonCallback = useCallback(() => {
        unPinOverlay()
        onClickActionButton(model.current)
    }, [onClickActionButton, unPinOverlay])

    useClickOutside(onOutsideClick, [overlayContainerRef])

    return (
        <overlayContext.Provider value={methods}>
            {children}
            {createPortal((
                <div
                    ref={overlayContainerRef}
                    onMouseLeave={methods.onHideOverlay}
                    onMouseEnter={continueShowOverlay}
                    className="table-row-overlay"
                >
                    {isVisibleOverlay ? (
                        <div
                            className={`toolbar-container ${className}`}
                            onClick={onPinOverlay}
                        >
                            {toolbar.map(({ id, buttons }) => (
                                <React.Fragment key={id}>
                                    {buttons.map(({ component: ButtonComponent, id, ...props }) => (
                                        <ButtonComponent
                                            key={id}
                                            id={id}
                                            actionCallback={onClickActionButtonCallback}
                                            {...props}
                                        />
                                    ))}
                                </React.Fragment>
                            ))}
                        </div>
                    ) : null}
                </div>
                // eslint-disable-next-line @typescript-eslint/no-non-null-assertion
            ), document.querySelector('#n2o')!)}
        </overlayContext.Provider>
    )
}

ToolbarOverlay.displayName = 'ToolbarOverlay'

export const useToolbarOverlay = () => useContext(overlayContext)
