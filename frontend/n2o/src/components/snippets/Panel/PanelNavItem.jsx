import React from 'react';
import PropTypes from 'prop-types';
import cx from 'classnames';

/**
 * Компонент элемента меню для {@link Panel}
 * @reactProps {string} id - параметр, который уйдёт аргументов в событии по клику
 * @reactProps {boolean} active - выбран лм элемент
 * @reactProps {boolean} disabled - неактивен ли элемент
 * @reactProps {function} onClick - событие при клике
 * @reactProps (string} className - имя класса
 * @reactProps {boolean} isToolBar - флаг элемента тулбара
 * @reactProps {node} children - элемент вставляемый в PanelNavItem
 */
class PanelNavItem extends React.Component {
  constructor(props) {
    super(props);

    this.handleClick = this.handleClick.bind(this);
  }

  /**
   * обработка клика
   * @param e - событие
   */
  handleClick(e) {
    const { id, onClick } = this.props;
    e.preventDefault();
    if (onClick) {
      onClick(e, id);
    }
  }

  /**
   * Рендер
   */
  render() {
    const { active, disabled } = this.props;

    return (
      <li className={cx('nav-item', 'panel-block-flex', this.props.className)}>
        <a
          className={cx('nav-link panel-block-flex panel-heading-link', {
            'panel-toolbar-link': this.props.isToolBar,
            active,
            disabled,
          })}
          onClick={this.handleClick}
          href="#"
        >
          {this.props.children}
        </a>
      </li>
    );
  }
}

PanelNavItem.propTypes = {
  id: PropTypes.string,
  active: PropTypes.bool,
  disabled: PropTypes.bool,
  onClick: PropTypes.func,
  className: PropTypes.string,
  isToolBar: PropTypes.bool,
  children: PropTypes.node,
};

PanelNavItem.defaultProps = {
  active: false,
  disabled: false,
  isToolBar: false,
};

export default PanelNavItem;
