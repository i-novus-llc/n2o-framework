import React, { ComponentClass, CSSProperties, FocusEventHandler, useMemo } from 'react'
import isUndefined from 'lodash/isUndefined'
import isNumber from 'lodash/isNumber'
import omit from 'lodash/omit'
import assign from 'lodash/assign'
import classNames from 'classnames'
import BaseSlider, { Range, createSliderWithTooltip, Marks } from 'rc-slider'

import { TBaseInputProps, TBaseProps } from '../types'

import '../styles/controls/Slider.scss'

type SliderProps = TBaseProps & TBaseInputProps<number> & {
    dotStyle?: CSSProperties,
    dots?: boolean,
    marks?: Marks,
    max?: number,
    min?: number,
    multiple?: boolean,
    onChange(value: number | number[]): void,
    pushable?: boolean,
    railStyle?: CSSProperties,
    showTooltip?: boolean,
    step?: number,
    style?: boolean,
    tooltipPlacement?: string,
    trackStyle?: CSSProperties | CSSProperties[],
    vertical?: boolean
}

const getComponent = (multiple: boolean, showTooltip: boolean) => {
    let Component: ComponentClass = BaseSlider

    if (multiple) {
        Component = Range
    }

    if (showTooltip) {
        return createSliderWithTooltip(Component)
    }

    return Component
}

export const Slider = ({
    multiple = false,
    showTooltip = false,
    tooltipPlacement = 'top',
    vertical = false,
    style,
    className,
    onChange,
    min,
    max,
    step,
    value,
    ...rest
}: SliderProps) => {
    const Component = useMemo(() => getComponent(multiple, showTooltip), [multiple, showTooltip])

    const tooltipProps = {
        placement: tooltipPlacement,
    }

    const handleAfterChange = (val: number | number[]) => {
        onChange(val)
    }

    /* here the conflict with n2o redux form onBlur */
    const onBlur: FocusEventHandler = event => event

    // eslint-disable-next-line no-nested-ternary
    const currentValue = isNumber(value) ? value : !isUndefined(min) ? min : 0

    const restProps = multiple
        ? omit(rest, ['value'])
        : assign({}, rest, {
            value: currentValue,
        })

    return (
        <Component
            className={classNames('n2o-slider', className, {
                'n2o-slider--vertical': vertical,
            })}
            tipProps={tooltipProps}
            vertical={vertical}
            min={min}
            max={max}
            step={step}
            onChange={handleAfterChange}
            {...restProps}
            onBlur={onBlur}
        />
    )
}
