import React from 'react';
import PropTypes from 'prop-types';
import UncontrolledTooltip from 'reactstrap/lib/UncontrolledTooltip';

/**
 * HOC для добавления подсказки при наведение
 * @param target
 * @param hint
 * @param delay
 * @param placement
 * @param hideArrow
 * @param children
 * @returns {function(*)}
 */
export default function Tooltip({
  target,
  hint,
  delay,
  placement,
  hideArrow,
  children,
}) {
  return (
    <React.Fragment>
      {children}
      {hint && (
        <UncontrolledTooltip
          delay={delay}
          placement={placement}
          target={target}
          hideArrow={hideArrow}
        >
          {hint}
        </UncontrolledTooltip>
      )}
    </React.Fragment>
  );
}

Tooltip.propTypes = {
  target: PropTypes.oneOfType([PropTypes.string, PropTypes.func]).isRequired,
  hint: PropTypes.string,
  delay: PropTypes.oneOfType([
    PropTypes.shape({ show: PropTypes.number, hide: PropTypes.number }),
    PropTypes.number,
  ]),
  placement: PropTypes.oneOf([
    'auto',
    'auto-start',
    'auto-end',
    'top',
    'top-start',
    'top-end',
    'right',
    'right-start',
    'right-end',
    'bottom',
    'bottom-start',
    'bottom-end',
    'left',
    'left-start',
    'left-end',
  ]),
  hideArrow: PropTypes.bool,
};

Tooltip.defaultProps = {
  delay: 0,
  placement: 'top',
};
