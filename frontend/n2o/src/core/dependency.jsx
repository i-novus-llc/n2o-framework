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
import { getModelsByDependency } from '../selectors/models';

import { DEPENDENCY_TYPES } from './dependencyTypes';

const omittedProps = ['models'];
/**
 * НОС - создает зависимость
 *
 */
const dependency = WrappedComponent => {
  class UniversalDependency extends React.Component {
    constructor(props) {
      super(props);
      forEach(props.dependency, (dependency, type) => {
        this[type](dependency);
      });
    }
    /**
     * Вызов resolveDependency при изменении пропсов(пришла новая модель)
     * @param prevProps
     */
    componentDidUpdate(prevProps) {
      const { dependency, models } = this.props;
      forEach(dependency, (dependency, type) => {
        if (
          type !== DEPENDENCY_TYPES.fetch ||
          (type === DEPENDENCY_TYPES.fetch &&
            !isEqual(prevProps.models[DEPENDENCY_TYPES.fetch], models[DEPENDENCY_TYPES.fetch]))
        )
          this[type](dependency);
      });
    }

    shouldComponentUpdate(nextProps) {
      return (
        !isEqual(nextProps.models, this.props.models) ||
        nextProps.isVisible !== this.props.isVisible ||
        nextProps.isEnabled !== this.props.isEnabled
      );
    }

    /**
     * Вызывается в резолве при зависимости типа fetch, диспатчит экшен фетча
     */
    [DEPENDENCY_TYPES.fetch](dependency) {
      this.props.resolveWidgetDependency(DEPENDENCY_TYPES.fetch, dependency, this.props.isVisible);
    }

    /**
     * Вызывается в резолве при зависимости типа visible, диспатчит экшены изменния видимости виджета
     */
    [DEPENDENCY_TYPES.visible](dependency) {
      this.props.resolveWidgetDependency(DEPENDENCY_TYPES.visible, dependency);
    }

    /**
     * Вызывается в резолве при зависимости типа enabled, диспатчит enable/disable экшены
     */
    [DEPENDENCY_TYPES.enabled](dependency) {
      this.props.resolveWidgetDependency(DEPENDENCY_TYPES.enabled, dependency);
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
      isEnabled: makeWidgetEnabledSelector(props.id)(state, props),
      models: {
        [DEPENDENCY_TYPES.fetch]:
          dependency && getModelsByDependency(dependency.fetch)(state, props),
        [DEPENDENCY_TYPES.visible]:
          dependency && getModelsByDependency(dependency.visible)(state, props),
        [DEPENDENCY_TYPES.enabled]:
          dependency && getModelsByDependency(dependency.enabled)(state, props)
      }
    };
  };

  const mapDispatchToProps = (dispatch, ownProps) => {
    const { id: widgetId } = ownProps;
    return {
      resolveWidgetDependency: (dependencyType, dependency, isVisible) => {
        dispatch(resolveWidgetDependency(widgetId, dependencyType, dependency, isVisible));
      }
    };
  };

  return connect(
    mapStateToProps,
    mapDispatchToProps
  )(UniversalDependency);
};

export default dependency;
