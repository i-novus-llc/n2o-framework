import React, { Component } from 'react'
import { UncontrolledTooltip } from 'reactstrap'
import classNames from 'classnames'
import isInteger from 'lodash/isInteger'
import eq from 'lodash/eq'
import round from 'lodash/round'

import { TBaseProps } from '../../types'
import { id as generateId } from '../../utils/id'
import { mapToNum } from '../utils'

// Получение численного значения
const prepareValue = (rating: number | string, half: boolean): number => {
    if (half) {
        // округление до .5
        return Math.round(+rating / 0.5) * 0.5
    }

    return Math.round(+rating)
}

type RatingProps = TBaseProps & {
    half: boolean,
    max: number | string,
    onChange(value: number): void,
    rating: number | string,
    readonly: boolean,
    showTooltip: boolean,
    value: number | string
}

type RatingState = {
    rating: RatingProps['rating'],
    value: number
}

// TODO отказаться от rating  в параметрах в пользу value для единообразия со всеми полями ввода
export class Rating extends Component<RatingProps, RatingState> {
    id: string

    constructor(props: RatingProps) {
        super(props)
        this.state = {
            rating: props.rating,
            value: prepareValue(props.rating, props.half),
        }
        this.id = generateId()
        this.onChangeAndSetState = this.onChangeAndSetState.bind(this)
        this.renderStars = this.renderStars.bind(this)
        this.renderNullStar = this.renderNullStar.bind(this)
        this.renderTooltip = this.renderTooltip.bind(this)
    }

    static getDerivedStateFromProps(props: RatingProps, state: RatingState) {
        const { value } = props
        const rating = props.rating || value

        if (rating && (rating !== state.rating)) {
            return {
                rating,
                value: prepareValue(rating, props.half),
            }
        }

        return null
    }

    onChangeAndSetState(value: string) {
        const newValue = Number(value)
        const { onChange } = this.props

        this.setState({
            value: newValue,
            rating: newValue,
        })
        onChange(newValue)
    }

    renderTooltip() {
        const { showTooltip } = this.props
        const { rating } = this.state

        return showTooltip ? (
            <UncontrolledTooltip placement="top" target={this.id}>
                {round(+rating, 2)}
            </UncontrolledTooltip>
        ) : null
    }

    renderNullStar() {
        const { value } = this.state
        const { readonly } = this.props

        /* eslint-disable react/jsx-one-expression-per-line */
        return (
            <>
                <input
                    className="rating__input rating__input--none"
                    name={`rating-0-${this.id}`}
                    id={`rating-0-${this.id}`}
                    value="0"
                    type="radio"
                    onClick={(e) => {
                        if (!readonly) { this.onChangeAndSetState((e.target as HTMLInputElement).value) }
                    }}
                    checked={eq(0, value)}
                />
                <label
                    className={classNames('rating__label', {
                        'rating__label--no-pointer': readonly,
                    })}
                    htmlFor={`rating-0-${this.id}`}
                >

                    &nbsp;
                </label>
            </>
        )
    }

    renderStars(index: number) {
        const { value } = this.state
        const { readonly } = this.props

        return (
            <>
                <label
                    className={classNames('rating__label', {
                        'rating__label--half': !isInteger(index),
                        'rating__label--no-pointer': readonly,
                    })}
                    htmlFor={`rating-${index}-${this.id}`}
                >
                    <i
                        className={classNames('rating__icon rating__icon--star fa', {
                            'fa-star-half': !isInteger(index),
                            'fa-star': isInteger(index),
                        })}
                    />
                </label>
                <input
                    className={readonly ? 'rating__input--readonly' : 'rating__input'}
                    name={`rating-${index}-${this.id}`}
                    id={`rating-${index}-${this.id}`}
                    value={index}
                    type="radio"
                    onClick={(e) => {
                        if (!readonly) { this.onChangeAndSetState((e.target as HTMLInputElement).value) }
                    }}
                    checked={eq(index, value)}
                />
            </>
        )
    }

    render() {
        const { max, half, readonly } = this.props

        const options = {
            increment: half ? 0.5 : 1,
            start: half ? 0.5 : 1,
            inclusive: true,
        }

        return (
            <div className="n2o-rating-stars">
                <div
                    className={readonly ? 'rating-group--readonly' : 'rating-group'}
                    id={this.id}
                >
                    {this.renderNullStar()}
                    {mapToNum(max, this.renderStars, options)}
                </div>
                {this.renderTooltip()}
            </div>
        )
    }

    static defaultProps = {
        value: 0,
        max: 5,
        half: false,
        rating: 0,
        showTooltip: false,
        readonly: false,
        onChange: () => {},
    } as RatingProps
}
