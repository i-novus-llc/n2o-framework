import React from 'react';
import { connect } from 'react-redux';
import { every, get } from 'lodash';
import evalExpression from '../../utils/evalExpression';

/**
 * HOC для резолва зависимости видимости у регионов
 * @param Region
 */
export default Region => {
  class TabsWithVisibleDependency extends React.Component {
    constructor(props) {
      super(props);

      this.getModel = this.getModel.bind(this);
      this.resolveVisibleDependency = this.resolveVisibleDependency.bind(this);
    }

    getModel(on) {
      return get(this.props.state, on);
    }

    resolveVisibleDependency(dependency) {
      return every(dependency.visible, ({ condition, on }) =>
        evalExpression(condition, this.getModel(on))
      );
    }

    render() {
      return <Region {...this.props} resolveVisibleDependency={this.resolveVisibleDependency} />;
    }
  }

  const mapStateToProps = state => ({
    state
  });

  return connect(mapStateToProps)(TabsWithVisibleDependency);
};
