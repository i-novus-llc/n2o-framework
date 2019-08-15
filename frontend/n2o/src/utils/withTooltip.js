import React from 'react';
import { Tooltip } from 'reactstrap';
import { id } from '../utils/id';

/**
 * Если есть подсказка, возвращаем с тултипом
 */

const Id = id();

export default function withTooltip(props) {
  const { children, hint, placement, id, isOpen, hideArrow } = props;
  return (
    <React.Fragment>
      <Tooltip
        delay={0}
        placement={placement}
        target={id || Id}
        isOpen={isOpen}
        trigger={'hover focus'}
        hideArrow={hideArrow}
      >
        {hint}
      </Tooltip>
      <div id={Id}>{children}</div>
    </React.Fragment>
  );
}

Tooltip.defaultProps = {
  isOpen: true,
  hideArrow: false,
};
