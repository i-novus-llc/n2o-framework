import React, { Component } from 'react'
import { Spinner as BaseSpinner } from 'reactstrap'
import classNames from 'classnames'
import eq from 'lodash/eq'
import values from 'lodash/values'
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

        this.renderCoverSpiner = this.renderCoverSpiner.bind(this)
        this.renderLineSpinner = this.renderLineSpinner.bind(this)
    }

    componentDidUpdate(prevProps) {
        const { loading } = this.props

        if (prevProps.loading !== loading) {
            setTimeout(() => {
                const { loading } = this.props

                this.setState({ showSpinner: loading })
            }, 1000)
        }
    }

    renderCoverSpiner() {
        const {
            children,
            className,
            text,
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
                })}
            >
                {showSpinner && (
                    <>
                        <div className="n2o-spinner-container ">
                            <BaseSpinner className="spinner-border" color={color} {...rest} />
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
            type,
            children,
            loading,
            ...rest
        } = this.props

        if (loading) {
            return <BaseSpinner className="spinner" {...rest} />
        }
        if (React.Children.count(children)) {
            return children
        }

        return null
    }

    render() {
        const { type } = this.props

        return eq(type, TYPE.COVER)
            ? this.renderCoverSpiner()
            : this.renderLineSpinner()
    }
}

Spinner.propTypes = {
    loading: PropTypes.bool,
    type: PropTypes.oneOf(values(TYPE)),
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
