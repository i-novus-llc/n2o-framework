import React, { Fragment } from 'react';
import PropTypes from 'prop-types';
import { Popover, PopoverHeader, PopoverBody, Button } from 'reactstrap';
import { compose, defaultProps, withState, withHandlers } from 'recompose';
import { id } from '../../../utils/id';
import cx from 'classnames';

/**
 * PopoverConfirm
 * @reactProps {string} className - className компонента
 * @reactProps {string} title - заголовок PopoverConfirm
 * @reactProps {string} okLabel - текст кнопки положительного ответа
 * @reactProps {string} cancelLabel - текст кнопки отрицательного ответа
 * @example
 * <PopoverConfirm title="are you sure?" okLabel="ok" cancelLabel="no" />
 */

export function PopoverConfirm(props) {
  const {
    className,
    title,
    text,
    children,
    okLabel,
    cancelLabel,
    targetId,
    onDeny,
    onConfirm,
    ...rest
  } = props;
  return (
    <div className={cx('n2o-popover', className)}>
      <div id={targetId} onClick={rest.toggle} className="toggle-popover">
        {children}
      </div>
      <Popover {...rest} target={rest.target || targetId}>
        <Fragment>
          <PopoverHeader>
            <i className={cx('fa fa-question-circle-o mr-1')} />
            {title}
          </PopoverHeader>
          <PopoverBody>
            <div className="mb-1">{text}</div>
            <div className="d-flex justify-content-between">
              <Button className="btn-sm" onClick={onDeny}>
                {cancelLabel}
              </Button>
              <Button className="btn-sm" onClick={onConfirm}>
                {okLabel}
              </Button>
            </div>
          </PopoverBody>
        </Fragment>
      </Popover>
    </div>
  );
}

PopoverConfirm.propTypes = {
  className: PropTypes.string,
  title: PropTypes.oneOfType([PropTypes.string, Element]),
  children: PropTypes.oneOfType([PropTypes.string, Element]),
  okLabel: PropTypes.string,
  cancelLabel: PropTypes.string,
  onDeny: PropTypes.func,
  onConfirm: PropTypes.func,
};

PopoverConfirm.defaultProps = {
  className: '',
  title: 'Вы уверены?',
  text: '',
  okLabel: 'Да',
  cancelLabel: 'Нет',
};

const enhance = compose(
  defaultProps({
    onConfirm: function() {},
    onCancel: function() {},
  }),
  withState('targetId', 'setTargetId', () => id()),
  withState('isOpen', 'stateUpdate', ({ isOpen }) => isOpen),
  withHandlers({
    onConfirm: ({ isOpen, stateUpdate, onConfirm }) => () => {
      stateUpdate(!isOpen);
      onConfirm();
    },
    onDeny: ({ isOpen, stateUpdate, onCancel }) => () => {
      stateUpdate(!isOpen);
      onCancel();
    },
    toggle: ({ isOpen, stateUpdate }) => () => {
      stateUpdate(!isOpen);
    },
  })
);

export default enhance(PopoverConfirm);
