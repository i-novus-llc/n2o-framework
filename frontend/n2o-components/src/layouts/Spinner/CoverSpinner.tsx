import React, { Component } from 'react'
import classNames from 'classnames'

const delay = (ms: number) => new Promise((res) => { setTimeout(res, ms) })

/**
 * Cover-спиннер (лоадер), перекрывает родительский элемент (родитель должен иметь position НЕ static(relative, fixed, absolute))
 * @reactProps {string} animationClass - класс анимации
 * @reactProps {string} mode - выбор в цвета для оверлея спиннера
 * @reactProps {string} message - текст спиннера
 * @reactProps {number} deferredSpinnerStart - время показа спинера
 */

enum Mode {
    dark = 'dark',
    light = 'light',
    transparent = 'transparent',
}

type Props = {
    animationClass: string,
    deferredSpinnerStart: number,
    message: string,
    mode: Mode,
}

type State = {
    deferredStart: boolean
}

export class CoverSpinner extends Component<Props, State> {
    constructor(props: Props) {
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
                className={classNames(
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

    static defaultProps = {
        animationClass: '',
        message: '',
        deferredSpinnerStart: 0,
        mode: 'light',
    } as Props
}
