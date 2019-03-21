import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { isEqual, omit, get, forEach, isEmpty } from 'lodash';
import { resolveWidgetDependency } from '../actions/widgets';
import {
  makeWidgetVisibleSelector,
  makeWidgetEnabledSelector,
  makeWidgetIsInitSelector
} from '../selectors/widgets';
import { registerDependency } from '../actions/dependency';

const omittedProps = ['models'];
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
      const { registerDependency, dependency, isVisible, isInit } = props;
      !isInit &&
        registerDependency({
          dependencyType: 'widget',
          dependency,
          isVisible
        });
    }

    /**
     * Базовый рендер
     */
    render() {
      const { isVisible, isEnabled } = this.props;
      const provProps = omit(this.props, omittedProps);
      const style = { display: !isVisible ? 'none' : 'block' };
      return (
        <div style={style}>
          <WrappedComponent {...provProps} disabled={!isEnabled} visible={isVisible} />
        </div>
      );
    }
  }

  UniversalDependency.propTypes = {
    isInit: PropTypes.bool,
    isVisible: PropTypes.bool,
    isEnabled: PropTypes.bool,
    models: PropTypes.object,
    resolveWidgetDependency: PropTypes.func
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
      resolveWidgetDependency: (dependencyType, dependency, isVisible) => {
        dispatch(resolveWidgetDependency(widgetId, dependencyType, dependency, isVisible));
      },
      registerDependency: (widgetId, options) => dispatch(registerDependency(widgetId, options))
    };
  };

  return connect(
    mapStateToProps,
    mapDispatchToProps
  )(UniversalDependency);
};

export default dependency;
