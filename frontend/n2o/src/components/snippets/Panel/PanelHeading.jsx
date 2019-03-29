import React from 'react';
import PropTypes from 'prop-types';
import { CardHeader } from 'reactstrap';

/**
 * Компонент шапки для {@link Panel}
 * @reactProps {node} children - вставляемый внутрь PanelHeading элемент
 */

class PanelHeading extends React.Component {
  /**
   * Рендер
   */

  render() {
    return (
      <CardHeader className="panel-block-flex panel-block-flex panel-region-heading">
        {this.props.children}
      </CardHeader>
    );
  }
}

PanelHeading.propTypes = {
  children: PropTypes.node
};

export default PanelHeading;
