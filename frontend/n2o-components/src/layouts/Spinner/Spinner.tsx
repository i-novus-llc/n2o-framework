import React, { useState, useEffect, useRef, ReactNode, Children } from 'react'
import { Spinner as ReactstrapSpinner } from 'reactstrap'
import classNames from 'classnames'

import { type TBaseProps } from '../../types'

export enum SpinnerType {
    cover = 'cover',
    inline = 'inline',
}

export interface Props extends TBaseProps {
    children?: ReactNode
    color?: string
    loading: boolean
    minSpinnerTimeToShow?: number
    showDelayMs?: number
    text?: string
    title?: string
    transparent?: boolean
    type?: SpinnerType
    size?: string
}

export const Spinner = ({
    children,
    loading,
    size,
    className,
    color = 'primary',
    minSpinnerTimeToShow = 250,
    showDelayMs = 0,
    text = '',
    title = '',
    transparent = false,
    type = SpinnerType.inline,
    ...rest
}: Props) => {
    const [showSpinner, setShowSpinner] = useState(false)
    const [loadingStartTime, setLoadingStartTime] = useState<number | null>(null)

    const showTimeoutRef = useRef<NodeJS.Timeout | null>(null)
    const hideTimeoutRef = useRef<NodeJS.Timeout | null>(null)

    useEffect(() => {
        return () => {
            if (showTimeoutRef.current) { clearTimeout(showTimeoutRef.current) }
            if (hideTimeoutRef.current) { clearTimeout(hideTimeoutRef.current) }
        }
    }, [])

    useEffect(() => {
        if (!loading || showSpinner) { return undefined }

        if (showTimeoutRef.current) { clearTimeout(showTimeoutRef.current) }

        showTimeoutRef.current = setTimeout(() => {
            setShowSpinner(true)
            setLoadingStartTime(Date.now())
            showTimeoutRef.current = null
        }, showDelayMs)

        return () => {
            if (showTimeoutRef.current) {
                clearTimeout(showTimeoutRef.current)
            }
        }
    }, [loading, showDelayMs, showSpinner])

    useEffect(() => {
        if (loading || !showSpinner || loadingStartTime === null) { return undefined }

        const elapsed = Date.now() - loadingStartTime
        const remaining = minSpinnerTimeToShow - elapsed

        if (remaining <= 0) {
            setShowSpinner(false)
            setLoadingStartTime(null)

            return undefined
        }

        if (hideTimeoutRef.current) { clearTimeout(hideTimeoutRef.current) }

        hideTimeoutRef.current = setTimeout(() => {
            setShowSpinner(false)
            setLoadingStartTime(null)
        }, remaining)

        return () => {
            if (hideTimeoutRef.current) { clearTimeout(hideTimeoutRef.current) }
        }
    }, [loading, showSpinner, loadingStartTime, minSpinnerTimeToShow])

    const renderCoverSpinner = () => (
        <div className={classNames('n2o-spinner-wrapper', className, { 'n2o-disabled-page': loading })}>
            {showSpinner && (
                <>
                    <div className="n2o-spinner-container">
                        <ReactstrapSpinner className="spinner-border" color={color} size={size} type={type} {...rest} />
                        {title && <div className="loading_title loading_text">{title}</div>}
                        {text && <div className="loading_text">{text}</div>}
                    </div>
                    {!transparent && <div className="spinner-background" />}
                </>
            )}
            {children}
        </div>
    )

    const renderLineSpinner = () => {
        if (showSpinner) {
            return <ReactstrapSpinner className="spinner" color={color} size={size} type={type} {...rest} />
        }

        // eslint-disable-next-line react/jsx-no-useless-fragment
        return Children.count(children) ? <>{children}</> : null
    }

    return type === SpinnerType.cover ? renderCoverSpinner() : renderLineSpinner()
}
