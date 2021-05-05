import React, { Component, Fragment } from 'react'
import cx from 'classnames'
import PropTypes from 'prop-types'

const delay = ms => new Promise(res => setTimeout(res, ms))

/**
 * Cover-спиннер (лоадер), перекрывает родительский элемент (родитель должен иметь position НЕ static(relative, fixed, absolute))
 * @reactProps {string} animationClass - класс анимации
 * @reactProps {string} mode - выбор в цвета для оверлея спиннера
 * @reactProps {string} message - текст спиннера
 * @reactProps {number} deferredSpinnerStart - время показа спинера
 */
class CoverSpinner extends Component {
    constructor(props) {
        super(props)
        this.state = {
            deferredStart: !!props.deferredSpinnerStart,
        }
    }

    async componentDidMount() {
        const { deferredStart } = this.state
        const { deferredSpinnerStart } = this.props

        if (deferredStart) {
            this.setState({ deferredStart: true })
            await delay(deferredSpinnerStart)
            this.setState({ deferredStart: false })
        }
    }

    render() {
        const { deferredStart } = this.state
        const { animationClass, message, mode } = this.props

        return (
            <div
                className={cx(
                    'spinner-container',
                    { 'spinner-none-background': deferredStart },
                    { 'spinner-container--dark': mode === 'dark' },
                    { 'spinner-container--transparent': mode === 'transparent' },
                )}
            >
                {!deferredStart && (
                    <>
                        <div className={`spinner spinner-cover ${animationClass}`} />
                        <span className="message">{message}</span>
                    </>
                )}
            </div>
        )
    }
}

CoverSpinner.propTypes = {
    animationClass: PropTypes.string,
    message: PropTypes.string,
    deferredSpinnerStart: PropTypes.number,
    mode: PropTypes.oneOf(['dark', 'light', 'transparent']),
}

CoverSpinner.defaultProps = {
    animationClass: '',
    message: '',
    deferredSpinnerStart: 0,
    mode: 'light',
}

export default CoverSpinner
