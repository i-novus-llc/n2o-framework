import React from 'react';
import PropTypes from 'prop-types';
import { CardFooter } from 'reactstrap';

/**
 * Компонент подвала {@link Panel}
 * @reactProps {node} children - вставляемый внутрь PanelFooter элемент
 */

class PanelFooter extends React.Component {
  /**
   * Рендер
   */

  render() {
    return <CardFooter>{this.props.children}</CardFooter>;
  }
}

PanelFooter.propTypes = {
  children: PropTypes.node
};

export default PanelFooter;
