import React from 'react';
import TooltipTrigger from 'react-popper-tooltip';
import isUndefined from 'lodash/isUndefined';
import isArray from 'lodash/isArray';
import map from 'lodash/map';
import cn from 'classnames';
import 'react-popper-tooltip/dist/styles.css';

const triggerClassName = labelDashed =>
  cn({
    'list-text-cell__trigger_dashed': labelDashed,
    'list-text-cell__trigger': !labelDashed || isUndefined(labelDashed),
  });

const tooltipContainerClassName = theme =>
  cn({
    'list-text-cell__tooltip-container dark': theme === 'dark',
    'list-text-cell__tooltip-container light': theme === 'light',
  });

const arrowClassName = theme =>
  cn({
    'tooltip-arrow light': theme === 'light',
    'tooltip-arrow dark': theme === 'dark',
  });

function Tooltip(props) {
  const { hint, label, labelDashed, theme } = props;

  //trigger для появления tooltip, отображает label
  const Trigger = ({ getTriggerProps, triggerRef }) => {
    return (
      <span
        {...getTriggerProps({
          ref: triggerRef,
          className: triggerClassName(labelDashed),
        })}
      >
        {label}
      </span>
    );
  };

  //hint отображает лист
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
          className: tooltipContainerClassName(theme),
        })}
      >
        <div
          {...getArrowProps({
            ref: arrowRef,
            'data-placement': placement,
            className: arrowClassName(theme),
          })}
        />
        {isArray(hint) ? (
          map(hint, (tooltipItem, index) => (
            <div
              key={index}
              className="list-text-cell__tooltip-container__body"
            >
              {tooltipItem}
            </div>
          ))
        ) : (
          <div className="list-text-cell__tooltip-container__body">{hint}</div>
        )}
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
      delayShow={200}
    >
      {Trigger}
    </TooltipTrigger>
  );
}

export default Tooltip;
