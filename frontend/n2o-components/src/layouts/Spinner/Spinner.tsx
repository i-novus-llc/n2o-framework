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
    text = '',
    title = '',
    transparent = false,
    type = SpinnerType.inline,
    ...rest
}: Props): JSX.Element | null => {
    const [showSpinner, setShowSpinner] = useState(false)
    const [loadingStartTime, setLoadingStartTime] = useState<number | null>(null)
    const timeoutId = useRef<NodeJS.Timeout | null>(null)

    useEffect(() => {
        return () => {
            if (timeoutId.current) {
                clearTimeout(timeoutId.current)
            }
        }
    }, [])

    useEffect(() => {
        if (!loading) { return }

        setShowSpinner(true)
        setLoadingStartTime(Date.now())
    }, [loading])

    useEffect(() => {
        if (loading || loadingStartTime === null) { return }

        const elapsedTime = Date.now() - loadingStartTime
        const remainingTime = minSpinnerTimeToShow - elapsedTime

        if (remainingTime <= 0) {
            setShowSpinner(false)
            setLoadingStartTime(null)

            return
        }

        timeoutId.current = setTimeout(() => {
            setShowSpinner(false)
            setLoadingStartTime(null)
        }, remainingTime)

        // eslint-disable-next-line consistent-return
        return () => {
            if (timeoutId.current) {
                clearTimeout(timeoutId.current)
            }
        }
    }, [loading, loadingStartTime, minSpinnerTimeToShow])

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
