import React from 'react';
import TooltipTrigger from 'react-popper-tooltip';
import 'react-popper-tooltip/dist/styles.css';
import { RenderTooltipTrigger, RenderTooltipBody } from './utils';

function Tooltip(props) {
  const {
    hint,
    label,
    labelDashed,
    placement,
    trigger,
    theme,
    fieldKey,
  } = props;

  //trigger для появления tooltip, отображает label
  const Trigger = ({ getTriggerProps, triggerRef }) => {
    return (
      <RenderTooltipTrigger
        getTriggerProps={getTriggerProps}
        triggerRef={triggerRef}
        label={label}
        labelDashed={labelDashed}
        hint={hint}
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
        hint={hint}
        placement={placement}
        theme={theme}
      />
    );
  };

  const RenderTooltip = () => {
    return (
      <TooltipTrigger
        label={label}
        labelDashed={labelDashed}
        placement={placement}
        trigger={trigger}
        tooltip={TooltipBody}
        fieldKey={fieldKey}
        delayShow={200}
      >
        {Trigger}
      </TooltipTrigger>
    );
  };

  return <RenderTooltip />;
}

Tooltip.defaultProps = {
  labelDashed: false,
  placement: 'bottom',
  trigger: 'hover',
  theme: 'dark',
};

export default Tooltip;
