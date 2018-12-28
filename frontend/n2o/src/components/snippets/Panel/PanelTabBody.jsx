import React from 'react';
import PropTypes from 'prop-types';
import { TabPane } from 'reactstrap';

/**
 * Компонент тела таба для {@link Panel}
 * @reactProps {string|number} eventKey - идентификатор для таба
 * @reactProps {node} children - элемент вставляемый в PanelTabBody
 */

class PanelTabBody extends React.Component {
  /**
   * Рендер
   */

  render() {
    return <TabPane tabId={this.props.eventKey}>{this.props.children}</TabPane>;
  }
}

PanelTabBody.propTypes = {
  eventKey: PropTypes.oneOfType([PropTypes.string, PropTypes.number]).isRequired,
  children: PropTypes.node
};

export default PanelTabBody;
