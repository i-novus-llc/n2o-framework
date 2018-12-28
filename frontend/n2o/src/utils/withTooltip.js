import React from 'react';
import { UncontrolledTooltip } from 'reactstrap';

/**
 * Если есть подсказка, возвращаем с тултипом
 */

export default function withTooltip(component, hint, id) {
  if (hint) {
    return (
      <React.Fragment>
        {component}
        <UncontrolledTooltip delay={0} placement="top" target={id}>
          {hint}
        </UncontrolledTooltip>
      </React.Fragment>
    );
  } else {
    return component;
  }
}
