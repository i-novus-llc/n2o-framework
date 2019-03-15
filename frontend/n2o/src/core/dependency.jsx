import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { isEqual, omit, get, forEach, chain, compact, pickBy, pick, isEmpty } from 'lodash';
import { resolveWidgetDependency } from '../actions/widgets';
import {
  makeWidgetVisibleSelector,
  makeWidgetEnabledSelector,
  makeWidgetIsInitSelector
} from '../selectors/widgets';

import { DEPENDENCY_TYPES } from './dependencyTypes';
import { getModelsByDependency } from '../selectors/models';

const omittedProps = ['models'];
/**
 * НОС - создает зависимость
 *
 */
const dependency = WrappedComponent => {
  class UniversalDependency extends React.Component {
    constructor(props) {
      super(props);
      forEach(props.models, (models = [], type) => {
        if (!isEmpty(models)) {
          this[type](models);
        }
      });
    }
    /**
     * Вызов resolveDependency при изменении пропсов(пришла новая модель)
     * @param prevProps
     */
    componentDidUpdate(prevProps) {
      const currentModels = pickBy(this.props.models);
      const prevModels = pickBy(prevProps.models);
      forEach(currentModels, (models, type) => {
        if (!isEmpty(currentModels[type])) this[type](currentModels[type], prevModels[type]);
      });
    }

    componentDidMount() {
      this.isInitialized = true;
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
    [DEPENDENCY_TYPES.fetch](models, prevModels) {
      this.props.resolveWidgetDependency(DEPENDENCY_TYPES.fetch, {
        models,
        prevModels,
        isVisible: this.props.isVisible,
        dependency: this.props.dependency
      });
    }

    /**
     * Вызывается в резолве при зависимости типа visible, диспатчит экшены изменния видимости виджета
     */
    [DEPENDENCY_TYPES.visible](models) {
      this.props.resolveWidgetDependency(DEPENDENCY_TYPES.visible, {
        models,
        dependency: this.props.dependency
      });
    }

    /**
     * Вызывается в резолве при зависимости типа enabled, диспатчит enable/disable экшены
     */
    [DEPENDENCY_TYPES.enabled](models) {
      this.props.resolveWidgetDependency(DEPENDENCY_TYPES.enabled, {
        models,
        dependency: this.props.dependency
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
    models: PropTypes.obj,
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
      resolveWidgetDependency: (dependencyType, options) => {
        dispatch(resolveWidgetDependency(widgetId, dependencyType, options));
      }
    };
  };

  return connect(
    mapStateToProps,
    mapDispatchToProps
  )(UniversalDependency);
};

export default dependency;
