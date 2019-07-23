import React from 'react';
import PropTypes from 'prop-types';
import { Popover, PopoverHeader, PopoverBody } from 'reactstrap';
import { id } from '../../../utils/id';
import cx from 'classnames';

/**
 * Popover
 * @reactProps {string} trigger - Список триггеров, разделенных пробелами (например, "click hover focus")
 * @reactProps {string|func|Element} - привязываемый компонент
 * @reactProps {string|func|Element} container - куда размещать popper, по умолчанию body
 * @reactProps {string} className - className компонента
 * @reactProps {string} innerClassName - класс внутреннего поповера
 * @reactProps {bool} hideArrow - отоброжается стрелка Popover или нет
 * @reactProps {string} placementPrefix - перфикс рамещения
 * @reactProps {number} delay -задержка поповера
 * @reactProps {object} modifiers - пользовательские модификаторы, которые передаются в Popper.js, см. Https://popper.js.org/popper-documentation.html#modifiers.
 * @reactProps {string|number} offset - смещение
 * @reactProps {bool} fade - показать/скрыть поповкер с эффектом затухания
 * @reactProps {bool} flip - стоит ли менять напаравление поповера если край контейнера слишком близко
 * @reactProps {string} placement - позиция Popover
 * @reactProps {string} header - заголовок Popover
 * @reactProps {string} body - основная часть Popover
 * @reactProps {string} children - дочерний компонент
 * @reactProps {string} help - текст подсказки
 * @reactProps {string} icon - className иконки подсказки
 * @reactProps {string} iconClassName - className для css иконки подсказки
 * @example
 * <Popover body='body text'/>
 */

class N2OPopover extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showPopover: false,
    };
    this.fieldId = id();
    this.onToggle = this.onToggle.bind(this);
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
    } = this.props;
    return (
      <div className={cx('n2o-popover', className)}>
        <div id={this.fieldId} onClick={this.onToggle}>
          {!icon && help && (
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
          {help ? (
            <div dangerouslySetInnerHTML={{ __html: help }} />
          ) : (
            <React.Fragment>
              {header && <PopoverHeader>{header}</PopoverHeader>}
              {body && <PopoverBody>{body}</PopoverBody>}
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
  modifiers: PropTypes.object,
  offset: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  fade: PropTypes.bool,
  flip: PropTypes.bool,
  header: PropTypes.string,
  body: PropTypes.oneOfType([PropTypes.string, PropTypes.object]),
  icon: PropTypes.string,
  help: PropTypes.string,
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
};

export default N2OPopover;
