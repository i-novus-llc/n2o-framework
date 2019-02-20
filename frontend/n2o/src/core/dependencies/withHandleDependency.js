import React from 'react';
import PropTypes from 'prop-types';
import observeStore from '../../utils/observeStore';
import { setWatchDependency } from '../../components/widgets/Form/utils';

/**
 * HOC для ре рендера по депенденси reRender
 * @param Config - объект с функцией onChange, которая будет выполнена при изменении
 * зависимого поля
 * @reactProps dependencySelector - селектор выбора значений
 * @returns {function}
 * @example
 * compose(
 *    hoc1,
 *    hoc2,
 *    ...,
 *    withHandleDependency(config)
 *  )(WrappedComponent)
 */
const withHandleDependency = Config => BaseComponent => {
  class Wrapper extends React.Component {
    constructor(props) {
      super(props);

      this.onChange = Config.onChange;
      this.observeState = this.observeState.bind(this);
      this.setHocRef = this.setHocRef.bind(this);
    }

    componentDidMount() {
      this.observeState();
    }

    componentWillUnmount() {
      this._observer && this._observer();
    }

    setHocRef(el) {
      this._hocRef = el;
    }

    observeState() {
      const { store } = this.context;
      const { dependencySelector } = this.props;
      if (this._hocRef) {
        this._observer = observeStore(
          store,
          state => dependencySelector(state, this.props),
          () => {
            this.forceUpdate(() => this.onChange.apply(this._hocRef, [this.props]));
          }
        );
      }
    }

    render() {
      return <BaseComponent {...this.props} ref={this.setHocRef} />;
    }
  }

  Wrapper.propTypes = {
    dependencySelector: PropTypes.func
  };

  Wrapper.defaultProps = {
    dependencySelector: setWatchDependency
  };

  Wrapper.contextTypes = {
    store: PropTypes.object
  };
  return Wrapper;
};

export default withHandleDependency;
