import React, { Fragment } from 'react';
import PropTypes from 'prop-types';
import { Popover, PopoverHeader, PopoverBody, Button } from 'reactstrap';
import { compose, defaultProps, withState, withHandlers } from 'recompose';
import { id } from '../../../utils/id';
import cx from 'classnames';

/**
 * PopoverConfirm
 * @reactProps {string} className - className компонента
 * @reactProps {string} header - заголовок PopoverConfirm
 * @reactProps {string} okText - текст кнопки положительного ответа
 * @reactProps {string} cancelText - текст кнопки отрицательного ответа
 * @example
 * <PopoverConfirm header="are you sure?" okText="ok" cancelText="no" />
 */

export function PopoverConfirm(props) {
  const {
    className,
    header,
    children,
    okText,
    cancelText,
    targetId,
    onClickNo,
    onClickYes,
    ...rest
  } = props;
  return (
    <div className={cx('n2o-popover', className)}>
      <div id={targetId} onClick={rest.toggle} className="toggle-popover">
        {children}
      </div>
      <Popover {...rest} target={rest.target ? rest.target : targetId}>
        <Fragment>
          <PopoverHeader>
            <i className={cx('fa fa-question-circle-o mr-1')} />
            {header}
          </PopoverHeader>
          <PopoverBody className="d-flex justify-content-between">
            <Button className="btn-sm" onClick={onClickNo}>
              {cancelText}
            </Button>
            <Button className="btn-sm" onClick={onClickYes}>
              {okText}
            </Button>
          </PopoverBody>
        </Fragment>
      </Popover>
    </div>
  );
}

PopoverConfirm.propTypes = {
  className: PropTypes.string,
  header: PropTypes.oneOfType([PropTypes.string, Element]),
  children: PropTypes.oneOfType([PropTypes.string, Element]),
  okText: PropTypes.string,
  cancelText: PropTypes.string,
  onClickNo: PropTypes.func,
  onClickYes: PropTypes.func,
};

PopoverConfirm.defaultProps = {
  className: '',
  header: 'Вы уверены?',
  okText: 'Да',
  cancelText: 'Нет',
};

const enhance = compose(
  defaultProps({
    onConfirm: function() {},
    onCancel: function() {},
  }),
  withState('targetId', 'setTargetId', () => id()),
  withState('isOpen', 'stateUpdate', ({ isOpen }) => isOpen),
  withHandlers({
    onClickYes: ({ isOpen, stateUpdate, onConfirm }) => () => {
      stateUpdate(!isOpen);
      onConfirm();
    },
    onClickNo: ({ isOpen, stateUpdate, onCancel }) => () => {
      stateUpdate(!isOpen);
      onCancel();
    },
    toggle: ({ isOpen, stateUpdate }) => () => {
      stateUpdate(!isOpen);
    },
  })
);

export default enhance(PopoverConfirm);
