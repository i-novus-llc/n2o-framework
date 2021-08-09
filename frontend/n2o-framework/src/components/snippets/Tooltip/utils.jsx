import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'
import isUndefined from 'lodash/isUndefined'
import isArray from 'lodash/isArray'
import map from 'lodash/map'

// dashed стиль лейбла tooltip или нет
export const triggerClassName = (labelDashed, type) => classNames({
    'list-text-cell__trigger_dashed': labelDashed,
    'list-text-cell__trigger': !labelDashed || isUndefined(labelDashed),
    'd-flex': type === 'mapProps(StandardButton)',
})

// тема tooltip body
export const tooltipContainerClassName = theme => classNames({
    'list-text-cell__tooltip-container dark': theme === 'dark',
    'list-text-cell__tooltip-container light': theme === 'light',
})

// классы для arrow в tooltip body
export const arrowClassName = theme => classNames({
    'tooltip-arrow light': theme === 'light',
    'tooltip-arrow dark': theme === 'dark',
})

// триггер tooltip отображает label
export function RenderTooltipTrigger({ getTriggerProps, triggerRef, labelDashed, label, hint }) {
    const exceptionsList = [
        'MapProps',
        'WithState',
        'ProgressBarCell',
        'ImageCell',
        'EditableCell',
    ]

    const triggerException = exceptionsList.includes(label.type.name)

    if (isUndefined(hint)) {
        return label
    }

    const bodyProps = {
        ...getTriggerProps({
            ref: triggerRef,
            className: triggerClassName(labelDashed, label.type.displayName),
        }),
    }

    return triggerException ? (
        <div {...bodyProps}>{label}</div>
    ) : (
        <span {...bodyProps}>{label}</span>
    )
}
RenderTooltipTrigger.propTypes = {
    getTriggerProps: PropTypes.func,
    triggerRef: PropTypes.func,
    labelDashed: PropTypes.any,
    label: PropTypes.object,
    hint: PropTypes.any,
}

// лист items в tooltip
export function RenderTooltipHint({ hint }) {
    return !isUndefined(hint) && isArray(hint)
        ? map(hint, (tooltipItem, index) => (
            <div key={index} className="list-text-cell__tooltip-container__body">
                {tooltipItem}
            </div>
        ))
        : !isUndefined(hint) && (
            <div className="list-text-cell__tooltip-container__body">{hint}</div>
        )
}

export function RenderTooltipBody({
    getTooltipProps,
    tooltipRef,
    getArrowProps,
    arrowRef,
    theme,
    placement,
    hint,
}) {
    return (
        <div
            {...getTooltipProps({
                ref: tooltipRef,
                className: tooltipContainerClassName(theme),
            })}
        >
            {hint && (
                <>
                    <div
                        {...getArrowProps({
                            ref: arrowRef,
                            'data-placement': placement,
                            className: arrowClassName(theme),
                        })}
                    />
                    <RenderTooltipHint hint={hint} />
                </>
            )}
        </div>
    )
}
RenderTooltipBody.propTypes = {
    getTooltipProps: PropTypes.func,
    tooltipRef: PropTypes.func,
    getArrowProps: PropTypes.func,
    arrowRef: PropTypes.any,
    theme: PropTypes.any,
    placement: PropTypes.any,
    hint: PropTypes.any,
}
