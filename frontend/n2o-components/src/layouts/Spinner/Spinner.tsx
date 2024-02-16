import React, { Component, ReactNode } from 'react'
import { Spinner as BaseSpinner } from 'reactstrap'
import classNames from 'classnames'
import eq from 'lodash/eq'
import omit from 'lodash/omit'

import { TBaseProps } from '../../types'

export enum SpinnerType {
    cover = 'cover',
    inline = 'inline'
}

type Props = TBaseProps & {
    children: ReactNode,
    color: string,
    loading: boolean,
    minSpinnerTimeToShow: number,
    text: string,
    title: string,
    transparent: boolean,
    type: SpinnerType
}

type State = {
    showSpinner: boolean
}

export class Spinner extends Component<Props, State> {
    timeoutId: NodeJS.Timeout | null

    unmounted: boolean | null

    constructor(props: Props) {
        super(props)

        this.state = {
            showSpinner: false,
        }

        this.timeoutId = null
        this.unmounted = null

        this.renderCoverSpinner = this.renderCoverSpinner.bind(this)
        this.renderLineSpinner = this.renderLineSpinner.bind(this)
    }

    componentDidUpdate(prevProps: Props) {
        const { loading } = this.props

        if (prevProps.loading !== loading) {
            if (this.timeoutId) {
                clearTimeout(this.timeoutId)
            }
            this.timeoutId = setTimeout(() => {
                const { loading } = this.props

                if (!this.unmounted) {
                    this.setState({ showSpinner: loading })
                }
            }, 2000)
        }
    }

    componentWillUnmount() {
        this.unmounted = true
    }

    renderCoverSpinner() {
        const {
            children,
            className = '',
            text,
            title,
            transparent,
            color,
            loading,
            ...rest
        } = this.props
        const { showSpinner } = this.state

        return (
            <div
                className={classNames('n2o-spinner-wrapper', {
                    [className]: className,
                    'n2o-disabled-page': loading,
                })}
            >
                {showSpinner && (
                    <>
                        <div className="n2o-spinner-container ">
                            <BaseSpinner className="spinner-border" color={color} {...omit(rest, ['loading'])} />
                            <div className="loading_title loading_text">{title}</div>
                            <div className="loading_text">{text}</div>
                        </div>
                        {!transparent ? <div className="spinner-background" /> : null}
                    </>
                )}
                {children}
            </div>
        )
    }

    renderLineSpinner() {
        const {
            children,
            loading,
            className,
            ...rest
        } = this.props

        if (loading) {
            return <BaseSpinner className="spinner" {...omit(rest, ['type'])} />
        }
        if (React.Children.count(children)) {
            return children
        }

        return null
    }

    render() {
        const { type } = this.props

        return eq(type, SpinnerType.cover)
            ? this.renderCoverSpinner()
            : this.renderLineSpinner()
    }

    static defaultProps = {
        loading: true,
        type: 'inline',
        text: '',
        transparent: false,
        color: 'primary',
        minSpinnerTimeToShow: 250,
    } as Props
}
