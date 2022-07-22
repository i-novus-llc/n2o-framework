import React, { Component } from 'react'
import { Spinner as BaseSpinner } from 'reactstrap'
import classNames from 'classnames'
import eq from 'lodash/eq'
import values from 'lodash/values'
import omit from 'lodash/omit'
import PropTypes from 'prop-types'

const TYPE = {
    INLINE: 'inline',
    COVER: 'cover',
}

export class Spinner extends Component {
    constructor(props) {
        super(props)

        this.state = {
            showSpinner: false,
        }

        this.renderCoverSpinner = this.renderCoverSpinner.bind(this)
        this.renderLineSpinner = this.renderLineSpinner.bind(this)
    }

    componentDidUpdate(prevProps) {
        const { loading } = this.props

        if (prevProps.loading !== loading) {
            setTimeout(() => {
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
            className,
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

        return eq(type, TYPE.COVER)
            ? this.renderCoverSpinner()
            : this.renderLineSpinner()
    }
}

Spinner.propTypes = {
    loading: PropTypes.bool,
    type: PropTypes.oneOf(values(TYPE)),
    title: PropTypes.string,
    text: PropTypes.string,
    transparent: PropTypes.bool,
    color: PropTypes.string,
    className: PropTypes.string,
    minSpinnerTimeToShow: PropTypes.number,
    children: PropTypes.any,
}

Spinner.defaultProps = {
    loading: true,
    type: 'inline',
    text: '',
    transparent: false,
    color: 'primary',
    minSpinnerTimeToShow: 250,
}
