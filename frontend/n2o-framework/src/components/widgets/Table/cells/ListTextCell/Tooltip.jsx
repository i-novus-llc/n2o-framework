import React from 'react';
import TooltipTrigger from 'react-popper-tooltip';
import isArray from 'lodash/isArray';
import isUndefined from 'lodash/isUndefined';
import map from 'lodash/map';
import cn from 'classnames';
import 'react-popper-tooltip/dist/styles.css';

function Tooltip(props) {
  const { fieldKey, label } = props;
  const validFieldKey = fieldKey && isArray(fieldKey);
  const listLength = validFieldKey ? fieldKey.length : 0;

  //ищет placeholder {value} в label, заменяет на длину массива
  const withPlaceholder = label => {
    const placeholder = '{value}';
    const hasPlaceholder = !isUndefined(label) && label.match(placeholder);
    if (isUndefined(label)) {
      return listLength;
    } else if (hasPlaceholder) {
      return label.replace(/{value}/gm, listLength);
    } else {
      return `${listLength + ' ' + label}`;
    }
  };

  const Trigger = ({ getTriggerProps, triggerRef }) => {
    return (
      <span
        {...getTriggerProps({
          ref: triggerRef,
          className: cn({
            'list-text-cell__trigger_dashed': props.labelDashed,
            'list-text-cell__trigger': !props.labelDashed,
          }),
        })}
      >
        {withPlaceholder(label)}
      </span>
    );
  };

  const TooltipBody = ({
    getTooltipProps,
    getArrowProps,
    tooltipRef,
    arrowRef,
    placement,
  }) => {
    return (
      <div
        {...getTooltipProps({
          ref: tooltipRef,
          className: cn({
            'list-text-cell__tooltip-container': validFieldKey,
            'list-text-cell__tooltip-container_empty': !validFieldKey,
          }),
        })}
      >
        {validFieldKey && (
          <div
            {...getArrowProps({
              ref: arrowRef,
              'data-placement': placement,
              className: 'tooltip-arrow',
            })}
          />
        )}
        {validFieldKey &&
          map(fieldKey, (tooltipItem, index) => (
            <div
              key={index}
              className="list-text-cell__tooltip-container__body"
            >
              {tooltipItem}
            </div>
          ))}
      </div>
    );
  };
  return (
    <TooltipTrigger
      placement={props.placement}
      trigger={props.trigger}
      tooltip={TooltipBody}
      fieldKey={props.fieldKey}
      label={props.label}
      labelDashed={props.labelDashed}
    >
      {Trigger}
    </TooltipTrigger>
  );
}

export default Tooltip;
