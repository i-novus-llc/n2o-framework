import React, { Fragment, Component } from 'react'
import UncontrolledTooltip from 'reactstrap/lib/UncontrolledTooltip'
import PropTypes from 'prop-types'
import cn from 'classnames'
import isInteger from 'lodash/isInteger'
import eq from 'lodash/eq'
import round from 'lodash/round'

import { id } from '../../../utils/id'

import { mapToNum } from './utils'

class SnippetRating extends Component {
    constructor(props) {
        super(props)
        this.state = {
            value: props.half ? props.rating : Math.ceil(props.rating),
        }
        this.id = id()
        this.onChangeAndSetState = this.onChangeAndSetState.bind(this)
        this.renderStars = this.renderStars.bind(this)
        this.renderNullStar = this.renderNullStar.bind(this)
        this.renderTooltip = this.renderTooltip.bind(this)
    }

    static getDerivedStateFromProps(nextProps, prevState) {
        if (nextProps.value !== prevState.value && nextProps.value) {
            return {
                value: nextProps.value,
            }
        }
    }

    onChangeAndSetState({ target: { value } }) {
        const newValue = Number(value)
        const { onChange } = this.props
        this.setState({ value: newValue })
        onChange(newValue)
    }

    renderTooltip() {
        const { showTooltip } = this.props
        const { value } = this.state

        return showTooltip ? (
            <UncontrolledTooltip placement="top" target={this.id}>
                {round(value, 2)}
            </UncontrolledTooltip>
        ) : null
    }

    renderNullStar() {
        const { value } = this.state
        const { readonly } = this.props
        return (
            <>
                <input
                    className="rating__input rating__input--none"
                    name="rating2"
                    id={`rating-0-${this.id}`}
                    value="0"
                    type="radio"
                    onClick={readonly ? null : this.onChangeAndSetState}
                    checked={eq(0, value)}
                />
                <label
                    className={cn('rating__label', {
                        'rating__label--no-pointer': readonly,
                    })}
                    htmlFor={`rating-0-${this.id}`}
                >

          &nbsp;
                </label>
            </>
        )
    }

    renderStars(index) {
        const { value } = this.state
        const { readonly } = this.props
        return (
            <>
                <label
                    className={cn('rating__label', {
                        'rating__label--half': !isInteger(index),
                        'rating__label--no-pointer': readonly,
                    })}
                    htmlFor={`rating-${index}-${this.id}`}
                >
                    <i
                        className={cn('rating__icon rating__icon--star fa', {
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
                    onClick={readonly ? null : this.onChangeAndSetState}
                    checked={eq(+index, +value)}
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
}

SnippetRating.propTypes = {
    /**
   * Максимальное значение
   */
    max: PropTypes.oneOfType([PropTypes.number, PropTypes.string]),
    /**
   * Значение
   */
    rating: PropTypes.oneOfType([PropTypes.number, PropTypes.string]),
    /**
   * Флаг включения выбора по половинке
   */
    half: PropTypes.bool,
    /**
   * Флаг показа подсказки
   */
    showTooltip: PropTypes.bool,
    /**
   * Callback на изменение
   */
    onChange: PropTypes.func,
    /**
   * Флаг только для чтения
   */
    readonly: PropTypes.bool,
}

SnippetRating.defaultProps = {
    max: 5,
    half: false,
    rating: 0,
    showTooltip: false,
    readonly: false,
    onChange: () => {},
}

export default SnippetRating
