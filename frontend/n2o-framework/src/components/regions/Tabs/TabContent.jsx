/**
 * Created by emamoshin on 09.10.2017.
 */
import React from 'react';
import PropTypes from 'prop-types';
import cx from 'classnames';
/**
 * Контент Таба
 * @reactProps {string} className - css-класс
 * @reactProps {boolean} activeId
 * @reactProps {node} children - элемет потомок компонента TabContent
 */
class TabContent extends React.Component {
  /**
   * Базовый рендер
   */
  render() {
    const { className, children, ...props } = this.props;
    return (
      <div className={cx('tab-content', className)}>
        {React.Children.map(children, child =>
          React.cloneElement(child, props)
        )}
      </div>
    );
  }
}

TabContent.propTypes = {
  className: PropTypes.string,
  activeId: PropTypes.bool,
  children: PropTypes.node,
};

export default TabContent;
