import React from 'react';
import TooltipTrigger from 'react-popper-tooltip';
import cn from 'classnames';
import 'react-popper-tooltip/dist/styles.css';

const Tooltip = props => {
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
        {props.label}
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
          className: 'list-text-cell__tooltip-container',
        })}
      >
        <div
          {...getArrowProps({
            ref: arrowRef,
            'data-placement': placement,
            className: 'tooltip-arrow',
          })}
        />
        {props.tooltipList.map((tooltipItem, index) => (
          <div key={index} className="list-text-cell__tooltip-container__body">
            {tooltipItem}
            {index !== props.tooltipList.length - 1 && (
              <>
                <hr className="tooltip-container__body__divider" />
              </>
            )}
          </div>
        ))}
      </div>
    );
  };
  return (
    <TooltipTrigger
      placement={props.placement || 'bottom'}
      trigger={props.trigger}
      tooltip={TooltipBody}
      tooltipList={props.tooltipList}
      label={props.label}
      labelDashed={props.labelDashed || false}
    >
      {Trigger}
    </TooltipTrigger>
  );
};

export default Tooltip;
