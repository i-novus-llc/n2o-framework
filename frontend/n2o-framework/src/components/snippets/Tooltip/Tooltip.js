import React from 'react';
import TooltipTrigger from 'react-popper-tooltip';
import { RenderTrigger, RenderTooltipBody, triggerClassName } from './utils';
import 'react-popper-tooltip/dist/styles.css';

function Tooltip(props) {
  const { hint, label, labelDashed, placement, theme } = props;

  //trigger для появления tooltip, отображает label
  const Trigger = ({ getTriggerProps, triggerRef }) => {
    return (
      <RenderTrigger
        getTriggerProps={getTriggerProps}
        triggerRef={triggerRef}
        labelDashed={labelDashed}
        label={label}
      />
    );
  };

  //hint отображает лист
  const TooltipBody = ({
    getTooltipProps,
    getArrowProps,
    tooltipRef,
    arrowRef,
  }) => {
    return (
      <RenderTooltipBody
        getTooltipProps={getTooltipProps}
        getArrowProps={getArrowProps}
        tooltipRef={tooltipRef}
        arrowRef={arrowRef}
        placement={placement}
        theme={theme}
        hint={hint}
      />
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

Tooltip.defaultProps = {
  theme: 'dark',
  placement: 'bottom',
  trigger: 'hover',
};

export default Tooltip;
