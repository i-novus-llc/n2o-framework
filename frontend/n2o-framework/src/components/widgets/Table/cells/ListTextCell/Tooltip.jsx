import React from 'react'
import TooltipTrigger from 'react-popper-tooltip'
import isArray from 'lodash/isArray'
import isUndefined from 'lodash/isUndefined'
import map from 'lodash/map'
import get from 'lodash/get'
import cn from 'classnames'
import 'react-popper-tooltip/dist/styles.css'

import getNoun from '../../../../../utils/getNoun'

const triggerClassName = labelDashed => cn({
    'list-text-cell__trigger_dashed': labelDashed,
    'list-text-cell__trigger': !labelDashed || isUndefined(labelDashed),
})

const tooltipContainerClassName = (validTooltipList, theme) => cn({
    'list-text-cell__tooltip-container dark':
      validTooltipList && theme === 'dark',
    'list-text-cell__tooltip-container light':
      validTooltipList && theme === 'light',
    'list-text-cell__tooltip-container_empty': !validTooltipList,
})

const arrowClassName = theme => cn({
    'tooltip-arrow light': theme === 'light',
    'tooltip-arrow dark': theme === 'dark',
})

function Tooltip(props) {
    const {
        model,
        fieldKey,
        label,
        oneLabel,
        fewLabel,
        manyLabel,
        labelDashed,
        theme,
    } = props

    const tooltipList = get(model, fieldKey)
    const validTooltipList = model && fieldKey && isArray(tooltipList)
    const listLength = validTooltipList ? tooltipList.length : 0

    const withPlaceholder = label => label.replace(/{size}/gm, listLength)

    const getLabel = () => getNoun(
        listLength,
        oneLabel || label,
        fewLabel || label,
        manyLabel || label,
    )

    // ищет placeholder {size} в label, заменяет на длину массива
    const replacePlaceholder = (label) => {
        const placeholder = '{size}'
        const hasPlaceholder = !isUndefined(label) && label.match(placeholder)

        if (isUndefined(label)) {
            return listLength
        } if (hasPlaceholder) {
            return withPlaceholder(label)
        }

        return label
    }

    // trigger для появления tooltip, отображает label
    const Trigger = ({ getTriggerProps, triggerRef }) => (
        <span
            {...getTriggerProps({
                ref: triggerRef,
                className: triggerClassName(labelDashed),
            })}
        >
            {replacePlaceholder(getLabel())}
        </span>
    )

    // hint отображает лист полученный из model
    const TooltipBody = ({
        getTooltipProps,
        getArrowProps,
        tooltipRef,
        arrowRef,
        placement,
    }) => (
        <div
            {...getTooltipProps({
                ref: tooltipRef,
                className: tooltipContainerClassName(validTooltipList, theme),
            })}
        >
            {validTooltipList && (
                <div
                    {...getArrowProps({
                        ref: arrowRef,
                        'data-placement': placement,
                        className: arrowClassName(theme),
                    })}
                />
            )}
            {validTooltipList &&
          map(tooltipList, (tooltipItem, index) => (
              <div
                  key={index}
                  className="list-text-cell__tooltip-container__body"
              >
                  {tooltipItem}
              </div>
          ))}
        </div>
    )

    return (
        <TooltipTrigger
            placement={props.placement}
            trigger={props.trigger}
            tooltip={TooltipBody}
            fieldKey={props.fieldKey}
            label={props.label}
            labelDashed={props.labelDashed}
            delayShow={200}
        >
            {Trigger}
        </TooltipTrigger>
    )
}

export default Tooltip
