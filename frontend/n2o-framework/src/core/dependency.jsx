import React from 'react';
import PropTypes from 'prop-types';
import get from 'lodash/get';
import { connect } from 'react-redux';
import {
  makeWidgetVisibleSelector,
  makeWidgetEnabledSelector,
  makeWidgetIsInitSelector,
} from '../selectors/widgets';
import { registerDependency } from '../actions/dependency';

export const InitMetadataContext = React.createContext({
  metadata: {
    visible: true,
  },
});

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
      const initMetadata = {
        metadata: { visible: get(this.props, 'visible', true) },
      };
      const style = { display: !isVisible ? 'none' : 'block' };
      return (
        <InitMetadataContext.Provider value={initMetadata}>
          <div style={style}>
            <WrappedComponent
              {...this.props}
              disabled={!isEnabled}
              visible={isVisible}
            />
          </div>
        </InitMetadataContext.Provider>
      );
    }
  }

  UniversalDependency.propTypes = {
    isInit: PropTypes.bool,
    isVisible: PropTypes.bool,
    isEnabled: PropTypes.bool,
    models: PropTypes.object,
  };

  UniversalDependency.defaultProps = {
    isInit: false,
    isVisible: true,
    isEnabled: true,
  };

  const mapStateToProps = (state, props) => {
    const { dependency } = props;
    return {
      isInit: makeWidgetIsInitSelector(props.id)(state, props),
      isVisible: makeWidgetVisibleSelector(props.id)(state, props),
      isEnabled: makeWidgetEnabledSelector(props.id)(state, props),
    };
  };

  const mapDispatchToProps = (dispatch, ownProps) => {
    const { id: widgetId } = ownProps;
    return {
      registerDependency: dependency =>
        dispatch(registerDependency(widgetId, dependency)),
    };
  };

  return connect(
    mapStateToProps,
    mapDispatchToProps
  )(UniversalDependency);
};

export default dependency;
