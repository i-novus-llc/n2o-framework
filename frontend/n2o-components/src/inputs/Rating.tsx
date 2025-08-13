import React, { useState, useEffect, useRef } from 'react'
import { UncontrolledTooltip } from 'reactstrap'
import classNames from 'classnames'
import isInteger from 'lodash/isInteger'
import eq from 'lodash/eq'
import round from 'lodash/round'

import { TBaseProps } from '../types'
import { id as generateId } from '../utils/id'
import { NOOP_FUNCTION } from '../utils/emptyTypes'

import { mapToNum } from './utils'

const prepareValue = (rating: number | string, half: boolean): number => {
    if (half) {
        return Math.round(+rating / 0.5) * 0.5
    }

    return Math.round(+rating)
}

export type Props = TBaseProps & {
    half?: boolean;
    max?: number | string;
    onChange?(value: number): void;
    rating?: number | string;
    readonly?: boolean;
    showTooltip?: boolean;
    value?: number | string;
}

export const Rating = ({
    half = false,
    max = 5,
    onChange = NOOP_FUNCTION,
    rating = 0,
    readonly = false,
    showTooltip = false,
    value = 0,
    ...rest
}: Props) => {
    const idRef = useRef(generateId())
    const id = idRef.current

    const initialRating = rating || value
    const [currentValue, setCurrentValue] = useState<number>(
        prepareValue(initialRating, half),
    )

    useEffect(() => {
        const externalRating = rating || value

        setCurrentValue(prepareValue(externalRating, half))
    }, [rating, value, half])

    const handleChange = (newValue: string) => {
        const numericValue = Number(newValue)

        setCurrentValue(numericValue)
        onChange(numericValue)
    }

    const renderTooltip = () => {
        if (!showTooltip) { return null }

        return (
            <UncontrolledTooltip placement="top" target={id}>
                {round(currentValue, 2)}
            </UncontrolledTooltip>
        )
    }

    const renderNullStar = () => (
        <>
            <input
                className="rating__input rating__input--none"
                name={`rating-0-${id}`}
                id={`rating-0-${id}`}
                value="0"
                type="radio"
                onClick={() => !readonly && handleChange('0')}
                checked={eq(0, currentValue)}
                readOnly={readonly}
            />
            <label
                className={classNames('rating__label', {
                    'rating__label--no-pointer': readonly,
                })}
                htmlFor={`rating-0-${id}`}
                aria-label="null-rating"
            >
                &nbsp;
            </label>
        </>
    )

    const renderStar = (index: number) => (
        <React.Fragment key={index}>
            <input
                className={readonly ? 'rating__input--readonly' : 'rating__input'}
                name={`rating-${index}-${id}`}
                id={`rating-${index}-${id}`}
                value={index}
                type="radio"
                onClick={() => !readonly && handleChange(String(index))}
                checked={eq(index, currentValue)}
                readOnly={readonly}
            />
            <label
                className={classNames('rating__label', {
                    'rating__label--half': !isInteger(index),
                    'rating__label--no-pointer': readonly,
                })}
                aria-label="rating"
                htmlFor={`rating-${index}-${id}`}
            >
                <i
                    className={classNames('rating__icon rating__icon--star fa', {
                        'fa-star-half': !isInteger(index),
                        'fa-star': isInteger(index),
                    })}
                />
            </label>
        </React.Fragment>
    )

    const starOptions = {
        increment: half ? 0.5 : 1,
        start: half ? 0.5 : 1,
        inclusive: true,
    }

    return (
        <div className="n2o-rating-stars" {...rest}>
            <div
                className={classNames({
                    'rating-group': !readonly,
                    'rating-group--readonly': readonly,
                })}
                id={id}
            >
                {renderNullStar()}
                {mapToNum(max, renderStar, starOptions)}
            </div>
            {renderTooltip()}
        </div>
    )
}
