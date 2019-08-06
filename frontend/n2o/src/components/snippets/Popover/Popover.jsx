import React, { Fragment } from 'react';
import PropTypes from 'prop-types';
import { Popover, PopoverHeader, PopoverBody, Button } from 'reactstrap';
import { compose, defaultProps, withState, withHandlers } from 'recompose';
import { id } from '../../../utils/id';
import cx from 'classnames';

/**
 * Popover
 * @reactProps {string} trigger - список триггеров, разделенных пробелами (например, "click hover focus")
 * @reactProps {bool} isOpen - состотяние Popover
 * @reactProps {function} toggle - callback для переключения isOpen в управляющем компоненте
 * @reactProps {string|Element} boundariesElement - границы для popper, могут быть scrollParent, window, viewport или любым элементом DOM
 * @reactProps {string|func|Element} target - привязываемый компонент
 * @reactProps {string|func|Element} container - куда размещать popper, по умолчанию body
 * @reactProps {string} className - className компонента
 * @reactProps {string} innerClassName - класс внутреннего поповера
 * @reactProps {bool} disabled - вк/выкл Popover
 * @reactProps {bool} hideArrow - отображается стрелка Popover или нет
 * @reactProps {string} placementPrefix - префикс рамещения
 * @reactProps {number} delay -задержка поповера
 * @reactProps {string} placement - позиция Popover
 * @reactProps {object} modifiers - пользовательские модификаторы, которые передаются в Popper.js, см. Https://popper.js.org/popper-documentation.html#modifiers.
 * @reactProps {string|number} offset - смещение
 * @reactProps {bool} fade - показать/скрыть поповер с эффектом затухания
 * @reactProps {bool} flip - стоит ли менять напаравление поповера, если край контейнера слишком близко
 * @reactProps {string} header - заголовок Popover
 * @reactProps {string} body - основная часть Popover
 * @reactProps {Element} children - дочерний элемент DOM
 * @reactProps {string} help - текст подсказки
 * @reactProps {string} icon - className иконки подсказки
 * @reactProps {string} iconClassName - className для css иконки
 * @reactProps {bool} popoverConfirm - включение confirmPopover(header остается как заголовок, св-во body не используется)
 * @reactProps {string} okText - текст кнопки положительного ответа
 * @reactProps {string} cancelText - текст кнопки отрицательного ответа
 * @reactProps {function} onConfirm - callback при подтверждении
 * @reactProps {function} onCancel - callback при отмене
 * @example
 * <Popover header="header text" body="body text" />
 */

export function N2OPopover(props) {
  const {
    trigger,
    isOpen,
    boundariesElement,
    target,
    container,
    className,
    innerClassName,
    disabled,
    hideArrow,
    placementPrefix,
    delay,
    placement,
    modifiers,
    offset,
    fade,
    flip,
    header,
    body,
    children,
    help,
    icon,
    iconClassName,
    popoverConfirm,
    okText,
    cancelText,
    stateUpdate,
    targetId,
    ...rest
  } = props;
  return (
    <div className={cx('n2o-popover', className)}>
      <div id={targetId} onClick={rest.onToggle} className="toggle-popover">
        {!icon && help && !popoverConfirm && (
          <i className={cx('fa fa-question-circle', iconClassName)} />
        )}
        {icon && help && <i className={icon} />}
        {children}
      </div>
      <Popover
        trigger={trigger}
        isOpen={isOpen}
        toggle={rest.onToggle}
        boundariesElement={boundariesElement}
        target={target ? target : targetId}
        container={container}
        innerClassName={innerClassName}
        disabled={disabled}
        hideArrow={hideArrow}
        placementPrefix={placementPrefix}
        delay={delay}
        placement={placement}
        modifiers={modifiers}
        offset={offset}
        fade={fade}
        flip={flip}
      >
        {help && !popoverConfirm ? (
          <div dangerouslySetInnerHTML={{ __html: help }} />
        ) : (
          <Fragment>
            {header && !popoverConfirm && (
              <PopoverHeader>{header}</PopoverHeader>
            )}
            {header && popoverConfirm && !help && (
              <PopoverHeader>
                <i
                  className={cx(
                    'fa',
                    icon ? icon : 'fa fa-question-circle-o',
                    'mr-1'
                  )}
                />
                {header}
              </PopoverHeader>
            )}
            {!popoverConfirm && body && <PopoverBody>{body}</PopoverBody>}
            {popoverConfirm && !help && (
              <PopoverBody className="d-flex justify-content-between">
                <Button className="btn-sm" onClick={rest.onClickNo}>
                  {cancelText}
                </Button>
                <Button className="btn-sm" onClick={rest.onClickYes}>
                  {okText}
                </Button>
              </PopoverBody>
            )}
          </Fragment>
        )}
      </Popover>
    </div>
  );
}

N2OPopover.propTypes = {
  trigger: PropTypes.string,
  isOpen: PropTypes.bool,
  toggle: PropTypes.func,
  boundariesElement: PropTypes.oneOfType([PropTypes.string, Element]),
  target: PropTypes.oneOfType([PropTypes.string, PropTypes.func, Element]),
  container: PropTypes.oneOfType([PropTypes.string, PropTypes.func, Element]),
  className: PropTypes.string,
  innerClassName: PropTypes.string,
  disabled: PropTypes.bool,
  hideArrow: PropTypes.bool,
  placementPrefix: PropTypes.string,
  delay: PropTypes.oneOfType([
    PropTypes.shape({ show: PropTypes.number, hide: PropTypes.number }),
    PropTypes.number,
  ]),
  placement: PropTypes.string,
  modifiers: PropTypes.object,
  offset: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  fade: PropTypes.bool,
  flip: PropTypes.bool,
  header: PropTypes.string,
  body: PropTypes.oneOfType([PropTypes.string, PropTypes.object]),
  children: PropTypes.oneOfType([PropTypes.string, Element]),
  help: PropTypes.string,
  icon: PropTypes.string,
  iconClassName: PropTypes.string,
  popoverConfirm: PropTypes.bool,
  okText: PropTypes.string,
  cancelText: PropTypes.string,
  onConfirm: PropTypes.func,
  onCancel: PropTypes.func,
};

const enhance = compose(
  defaultProps({
    className: '',
    header: 'header',
    body: 'body',
    placement: 'right',
    icon: '',
    popoverConfirm: false,
    okText: 'Ok',
    cancelText: 'Cancel',
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
    onToggle: ({ isOpen, stateUpdate }) => () => {
      stateUpdate(!isOpen);
    },
  })
);

export default enhance(N2OPopover);
