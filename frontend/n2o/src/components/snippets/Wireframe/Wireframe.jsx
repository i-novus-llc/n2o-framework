import React from 'react';
import PropTypes from 'prop-types';
import cx from 'classnames';

/**
 * Компонент WireFrame
 * @reactProps {string} className - класс компонента WireFrame
 * @reactProps {string} title - текст тайтла
 * @reactProps {object} style - стили компонента WireFrame
 */
class Wireframe extends React.Component {
  render() {
    return (
      <div style={this.props.style} className={cx('n2o-wireframe', this.props.className)}>
        {this.props.title}
      </div>
    );
  }
}

Wireframe.propTypes = {
  className: PropTypes.string,
  title: PropTypes.string,
  style: PropTypes.object
};

export default Wireframe;
