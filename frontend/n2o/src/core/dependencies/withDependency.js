import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { each, pick, isEqual } from 'lodash';

import { registerFieldDependency } from '../../actions/formPlugin';
import { pure } from 'recompose';
import { dependencySelector } from '../../selectors/formPlugin';
import { FIELDS, WIDGETS } from '../factory/factoryLevels';

import { getFormValues } from 'redux-form';

export default Component => {
  class DependencyContainer extends React.Component {
    constructor(props) {
      super(props);
      this.initIfNeeded();
    }

    /**
     * Регистрация дополнительных свойств поля
     */
    initIfNeeded() {
      const { dependency, dependencyRedux } = this.props;
      if (dependency && !dependencyRedux) {
        this.registerDependency();
      }
    }

    registerDependency() {
      const { dispatch, id, form, widgetId, dependency } = this.props;
      dispatch(registerFieldDependency(form, id, dependency));
    }

    render() {
      return <Component {...this.props} />;
    }
  }
  const mapStateToProps = (state, props) => {
    return {
      dependencyRedux: dependencySelector(
        props.form || props.widgetId,
        props.id || props.input.name
      )(state)
    };
  };

  return connect(mapStateToProps)(DependencyContainer);
};
