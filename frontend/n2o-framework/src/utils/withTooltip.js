import React from 'react';
import { UncontrolledTooltip } from 'reactstrap';

/**
 * Если есть подсказка, возвращаем с тултипом
 */

export default function placementwithTooltip(
  component,
  hint,
  hintPosition,
  id
) {
  if (hint) {
    return (
      <React.Fragment>
        {component}
        <UncontrolledTooltip delay={0} placement={hintPosition} target={id}>
          {hint}
        </UncontrolledTooltip>
      </React.Fragment>
    );
  } else {
    return component;
  }
}
