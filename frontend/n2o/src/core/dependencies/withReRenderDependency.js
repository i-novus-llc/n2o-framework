import React from 'react';
import PropTypes from 'prop-types';
import observeStore from '../../utils/observeStore';
import { setWatchDependency } from '../../components/widgets/Form/utils';

const withReRenderDependency = Config => BaseComponent => {
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
            this.onChange.apply(this._hocRef, [this.props]);
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

export default withReRenderDependency;
