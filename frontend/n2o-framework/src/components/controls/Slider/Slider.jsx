import React from 'react'
import PropTypes from 'prop-types'
import isUndefined from 'lodash/isUndefined'
import isNumber from 'lodash/isNumber'
import omit from 'lodash/omit'
import assign from 'lodash/assign'
import classNames from 'classnames'
import BaseSlider, { createSliderWithTooltip } from 'rc-slider'

import { prepareStyle } from './utils'

const SliderWithTooltip = createSliderWithTooltip(BaseSlider)
const RangeSliderWithTooltip = createSliderWithTooltip(BaseSlider.Range)

/**
 * Компонент Slider
 * @reactProps {boolean} multiple - Множественный выбор
 * @reactProps {boolean} showTooltip - Показать тултип
 * @reactProps {string} tooltipPlacement - Позиция тултипа
 * @reactProps {string} tooltipFormatter - Форматированный вывод тултипа
 * @reactProps {boolean} vertical - Отобразить slider вертикально
 * @reactProps {boolean} style - Стили root компонента
 * @reactProps {boolean} className - Дополнительный класс
 * @reactProps {string|number} step - шаг ползунка
 * @reactProps {boolean} disabled - Нередактаруем
 * @reactProps {boolean} dots - Показать шкалу
 * @reactProps {number|string} min - Начало шкалы
 * @reactProps {number|string} max - Конец шкалы
 * @reactProps {object} marks - Подписи к шкале
 * @reactProps {boolean} pushable - В мульти режиме блокирует смену несколькох ползунков
 * @reactProps {object} trackStyle - стиль трека
 * @reactProps {object} railStyle - стиль непройденной части шкалы
 * @reactProps {object} dotStyle - стиль шкалы
 * @returns {*}
 * @constructor
 */
function Slider(props) {
    const {
        multiple,
        showTooltip,
        tooltipPlacement,
        tooltipFormatter,
        vertical,
        style,
        className,
        onChange,
        min,
        max,
        step,
        value,
        ...rest
    } = props

    const expressionFn = tooltipFormatter
        // eslint-disable-next-line no-new-func
        ? value => new Function('', `return \`${tooltipFormatter}\``).bind(value)()
        : value => value

    const Component = multiple ? BaseSlider.Range : BaseSlider
    // eslint-disable-next-line no-nested-ternary
    const RenderSlider = showTooltip
        ? multiple
            ? RangeSliderWithTooltip
            : SliderWithTooltip
        : Component

    const tooltipProps = {
        placement: tooltipPlacement,
    }

    const handleAfterChange = (value) => {
        onChange(value)
    }

    // eslint-disable-next-line no-nested-ternary
    const currentValue = isNumber(value) ? value : !isUndefined(min) ? min : 0

    const restProps = multiple
        ? omit(rest, ['value'])
        : assign({}, rest, {
            value: currentValue,
        })

    return (
        <RenderSlider
            className={classNames('n2o-slider', className)}
            tipProps={tooltipProps}
            tipFormatter={expressionFn}
            vertical={vertical}
            style={prepareStyle(vertical, style)}
            onChange={handleAfterChange}
            min={min}
            max={max}
            step={step}
            {...restProps}
        />
    )
}

Slider.propTypes = {
    /**
     * Множественный выбор
     */
    multiple: PropTypes.bool,
    /**
     * Показать тултип
     */
    showTooltip: PropTypes.bool,
    /**
     * Позиция тултипа
     */
    tooltipPlacement: PropTypes.string,
    step: PropTypes.number,
    /**
     * Отобразить slider вертикально
     */
    vertical: PropTypes.bool,
    /**
     * Нередактаруем
     */
    disabled: PropTypes.bool,
    /**
     * Показать шкалу
     */
    dots: PropTypes.bool,
    /**
     * Начало шкалы
     */
    min: PropTypes.number,
    /**
     * Конец шкалы
     */
    max: PropTypes.number,
    /**
     * Подписи к шкале
     */
    marks: PropTypes.object,
    /**
     * В мульти режиме блокирует смену несколькох ползунков
     */
    pushable: PropTypes.bool,
    /**
     * Форматированный вывод тултипа
     */
    tooltipFormatter: PropTypes.string,
    className: PropTypes.string,
    style: PropTypes.object,
    onChange: PropTypes.func,
    value: PropTypes.any,
}

Slider.defaultProps = {
    multiple: false,
    showTooltip: false,
    tooltipPlacement: 'top',
}

export { Slider }
export default Slider
