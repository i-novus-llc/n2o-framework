import React from 'react';
import PropTypes from 'prop-types';
import { Popover, PopoverHeader, PopoverBody, Button } from 'reactstrap';
import { id } from '../../../utils/id';
import cx from 'classnames';

/**
 * Popover
 * @reactProps {string} trigger - список триггеров, разделенных пробелами (например, "click hover focus")
 * @reactProps {string|func|Element} target - привязываемый компонент
 * @reactProps {string|func|Element} container - куда размещать popper, по умолчанию body
 * @reactProps {string} className - className компонента
 * @reactProps {string} innerClassName - класс внутреннего поповера
 * @reactProps {bool} hideArrow - отображается стрелка Popover или нет
 * @reactProps {string} placementPrefix - префикс рамещения
 * @reactProps {number} delay -задержка поповера
 * @reactProps {object} modifiers - пользовательские модификаторы, которые передаются в Popper.js, см. Https://popper.js.org/popper-documentation.html#modifiers.
 * @reactProps {string|number} offset - смещение
 * @reactProps {bool} fade - показать/скрыть поповер с эффектом затухания
 * @reactProps {bool} flip - стоит ли менять напаравление поповера, если край контейнера слишком близко
 * @reactProps {string} placement - позиция Popover
 * @reactProps {string} header - заголовок Popover
 * @reactProps {string} body - основная часть Popover
 * @reactProps {Element} children - дочерний компонент
 * @reactProps {string} help - текст подсказки
 * @reactProps {string} icon - className иконки подсказки
 * @reactProps {string} iconClassName - className для css иконки
 * @reactProps {bool} popConfirm - включение confirmPopover(header отсается как заголовок, св-во body не используется)
 * @reactProps {string} okText - текст кнопки положительного ответа
 * @reactProps {string} cancelText - текст кнопки отрицательного ответа
 * @reactProps {function} onConfirm - callback при подтверждении
 * @reactProps {function} onCancel - callback при отмене
 * @example
 * <Popover header="header text" body="body text" />
 */

class N2OPopover extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showPopover: false,
      answer: false,
    };
    this.fieldId = id();
    this.onToggle = this.onToggle.bind(this);
    this.onClickYes = this.onClickYes.bind(this);
    this.onClickNo = this.onClickNo.bind(this);
  }

  onClickYes() {
    this.setState(
      {
        showPopover: !this.state.showPopover,
        answer: true,
      },
      () => this.props.onConfirm()
    );
  }

  onClickNo() {
    this.setState(
      {
        showPopover: !this.state.showPopover,
        answer: false,
      },
      () => this.props.onCancel()
    );
  }

  onToggle() {
    this.setState({
      showPopover: !this.state.showPopover,
    });
  }

  render() {
    const {
      trigger,
      target,
      container,
      className,
      innerClassName,
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
      popConfirm,
      okText,
      cancelText,
    } = this.props;
    return (
      <div className={cx('n2o-popover', className)}>
        <div id={this.fieldId} onClick={this.onToggle}>
          {!icon && help && !popConfirm && (
            <i className={cx('fa fa-question-circle', iconClassName)} />
          )}
          {icon && help && <i className={icon} />}
          {children}
        </div>
        <Popover
          trigger={trigger}
          container={container}
          innerClassName={innerClassName}
          hideArrow={hideArrow}
          placementPrefix={placementPrefix}
          delay={delay}
          placement={placement}
          modifiers={modifiers}
          offset={offset}
          fade={fade}
          flip={flip}
          isOpen={this.state.showPopover}
          target={target ? target : this.fieldId}
          toggle={this.onToggle}
        >
          {help && !popConfirm ? (
            <div dangerouslySetInnerHTML={{ __html: help }} />
          ) : (
            <React.Fragment>
              {header && !popConfirm && <PopoverHeader>{header}</PopoverHeader>}
              {header && popConfirm && !help && (
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
              {!popConfirm && body && <PopoverBody>{body}</PopoverBody>}
              {popConfirm && !help && (
                <PopoverBody className="d-flex justify-content-between">
                  <Button className="btn-sm mr-auto" onClick={this.onClickNo}>
                    {cancelText}
                  </Button>
                  <Button className="btn-sm" onClick={this.onClickYes}>
                    {okText}
                  </Button>
                </PopoverBody>
              )}
            </React.Fragment>
          )}
        </Popover>
      </div>
    );
  }
}

N2OPopover.propTypes = {
  trigger: PropTypes.string,
  target: PropTypes.oneOfType([PropTypes.string, PropTypes.func, Element]),
  container: PropTypes.oneOfType([PropTypes.string, PropTypes.func, Element]),
  className: PropTypes.string,
  innerClassName: PropTypes.string,
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
  popConfirm: PropTypes.bool,
  okText: PropTypes.string,
  cancelText: PropTypes.string,
  onConfirm: PropTypes.func,
  onCancel: PropTypes.func,
};

N2OPopover.defaultProps = {
  trigger: '',
  className: '',
  innerClassName: '',
  hideArrow: false,
  modifiers: {},
  fade: true,
  flip: true,
  header: 'header',
  body: 'body',
  placement: 'right',
  icon: '',
  popConfirm: false,
  okText: 'Ok',
  cancelText: 'Cancel',
  onConfirm: function() {},
  onCancel: function() {},
};

export default N2OPopover;
