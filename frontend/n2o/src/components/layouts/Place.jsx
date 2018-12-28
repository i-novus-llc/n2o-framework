/**
 * Created by emamoshin on 06.10.2017.
 */
import React from 'react';
import PropTypes from 'prop-types';

/**
 * Компонент, в который динамически вставляется содержимое сецкий. Дефолтное значение: "div"
 * @reactProps {boolean} tagName - элмент, в который будут помещены children Section
 * @reactProps {string} name - имя секции
 * @reactProps {string} className - css-класс
 * @reactProps {node} children - элемент потомок компонента Place
 * @example
 * <Place tagName="div" name="top"/>
 */
class Place extends React.Component {
  /**
   * Рендер содержимого секций или самого плейса(если секция не найдена)
   * @return {*}
   */
  render() {
    const { name, component, children, ...props } = this.props;
    const section = this.context.getSection(name);
    const output = section ? section.props.children : children;

    if (!output) {
      return false;
    }

    return React.createElement(component, props, output);
  }
}

Place.propTypes = {
  name: PropTypes.string.isRequired,
  component: PropTypes.oneOfType([PropTypes.string, PropTypes.element]),
  className: PropTypes.string,
  children: PropTypes.node
};

Place.defaultProps = {
  component: 'div'
};

Place.contextTypes = {
  getSection: PropTypes.func.isRequired,
  getParentProps: PropTypes.func.isRequired
};

export default Place;
