import React from 'react';
import { Tooltip } from 'reactstrap';
import { id } from '../utils/id';
import { withState } from 'recompose';

/**
 * Если есть подсказка, возвращаем с тултипом
 */

function withTooltip(props) {
  const { children, hint, placement, id, Id, isOpen, hideArrow } = props;
  return (
    <React.Fragment>
      <Tooltip
        delay={0}
        placement={placement}
        target={Id || id}
        isOpen={isOpen}
        hideArrow={hideArrow}
        trigger={'hover'}
      >
        {hint}
      </Tooltip>
      <div id={id ? '' : Id}>{children}</div>
    </React.Fragment>
  );
}

export default withState('Id', 'setId', () => id())(withTooltip);
