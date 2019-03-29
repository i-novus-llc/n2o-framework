import React from 'react';
import PropTypes from 'prop-types';
import { CardBody, TabContent } from 'reactstrap';

/**
 * Компонент тела {@link Panel}
 * @reactProps {string} id - id для контейнера с табами
 * @reactProps {boolean} hasTabs - флаг наличия табов
 * @reactProps {string|number} activeKey - ключ активного таба
 * @reactProps {node} children - вставляемый внутрь PanelBody элемент
 */

class PanelBody extends React.Component {
  /**
   * Рендер
   */

  render() {
    const tabContainer = (
      <TabContent id={this.props.id} activeTab={this.props.activeKey}>
        {this.props.children}
      </TabContent>
    );
    const element = () => {
      return this.props.hasTabs ? tabContainer : this.props.children;
    };

    return <CardBody>{element()}</CardBody>;
  }
}

PanelBody.propTypes = {
  id: PropTypes.string,
  hasTabs: PropTypes.bool,
  activeKey: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  children: PropTypes.node,
};

PanelBody.defaultProps = {
  hasTabs: false,
};

export default PanelBody;
