import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import {
  makeWidgetVisibleSelector,
  makeWidgetEnabledSelector,
  makeWidgetIsInitSelector
} from '../selectors/widgets';
import { registerDependency } from '../actions/dependency';

/**
 * НОС - создает зависимость
 *
 */
const dependency = WrappedComponent => {
  class UniversalDependency extends React.Component {
    constructor(props) {
      super(props);

      this.initIfNeeded(props);
    }

    initIfNeeded(props) {
      const { registerDependency, dependency, isInit } = props;
      !isInit && registerDependency(dependency);
    }

    /**
     * Базовый рендер
     */
    render() {
      const { isVisible, isEnabled } = this.props;
      const style = { display: !isVisible ? 'none' : 'block' };
      return (
        <div style={style}>
          <WrappedComponent {...this.props} disabled={!isEnabled} visible={isVisible} />
        </div>
      );
    }
  }

  UniversalDependency.propTypes = {
    isInit: PropTypes.bool,
    isVisible: PropTypes.bool,
    isEnabled: PropTypes.bool,
    models: PropTypes.object
  };

  UniversalDependency.defaultProps = {
    isInit: false,
    isVisible: true,
    isEnabled: true
  };

  const mapStateToProps = (state, props) => {
    const { dependency } = props;
    return {
      isInit: makeWidgetIsInitSelector(props.id)(state, props),
      isVisible: makeWidgetVisibleSelector(props.id)(state, props),
      isEnabled: makeWidgetEnabledSelector(props.id)(state, props)
    };
  };

  const mapDispatchToProps = (dispatch, ownProps) => {
    const { id: widgetId } = ownProps;
    return {
      registerDependency: dependency => dispatch(registerDependency(widgetId, dependency))
    };
  };

  return connect(
    mapStateToProps,
    mapDispatchToProps
  )(UniversalDependency);
};

export default dependency;
