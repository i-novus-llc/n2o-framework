import React from 'react';
import PropTypes from 'prop-types';
import observeStore from '../../utils/observeStore';
import { setWatchDependency } from '../../components/widgets/Form/utils';

const COMPONENT_TYPE = {
  FIELD: 'field',
  FIELDSET: 'fieldset'
};

const withReRenderDependency = Config => BaseComponent => {
  class Wrapper extends React.Component {
    constructor(props) {
      super(props);

      this.onChange = Config.onChange;
      this.observeState = this.observeState.bind(this);
      this.setRef = this.setRef.bind(this);
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

    setRef(el) {
      this.controlRef = el;
    }

    observeState() {
      const { store } = this.context;
      const { dependencySelector } = this.props;
      let context = null;
      if (Config.type === COMPONENT_TYPE.FIELD) {
        context = this.controlRef;
      }
      if (Config.type === COMPONENT_TYPE.FIELDSET) {
        context = this._hocRef;
      }
      if (context) {
        this._observer = observeStore(
          store,
          state => dependencySelector(state, this.props),
          () => {
            this.onChange.apply(context, [this.props]);
          }
        );
      }
    }

    render() {
      return <BaseComponent {...this.props} ref={this.setHocRef} setRef={this.setRef} />;
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
