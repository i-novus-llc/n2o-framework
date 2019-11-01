import React from 'react';
import { registerFieldDependency } from '../../actions/formPlugin';
import { connect } from 'react-redux';

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
      const { dependency } = this.props;
      if (dependency) {
        this.registerDependency();
      }
    }

    registerDependency() {
      const { id, form, dependency, registerFieldDependency } = this.props;
      registerFieldDependency(form, id, dependency);
    }

    render() {
      return <Component {...this.props} />;
    }
  }
  const mapDispatchToProps = dispatch => {
    return {
      registerFieldDependency: (form, id, dependency) =>
        dispatch(registerFieldDependency(form, id, dependency)),
    };
  };

  return connect(
    null,
    mapDispatchToProps
  )(DependencyContainer);
};
