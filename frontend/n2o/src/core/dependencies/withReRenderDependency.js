import React from 'react';
import PropTypes from 'prop-types';
import observeStore from '../../utils/observeStore';
import { setWatchDependency } from '../../components/widgets/Form/utils';

export default Config => {
  return Component => {
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
        if (Config.type === 'field' && this.controlRef) {
          this._observer = observeStore(
            store,
            state => dependencySelector(state, this.props),
            () => {
              this.onChange.apply(this.controlRef, [this.props]);
            }
          );
        }
        if (Config.type === 'fieldset' && this._hocRef) {
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
        // console.log('point')
        // console.log(this.props)
        return <Component {...this.props} ref={this.setHocRef} setRef={this.setRef} />;
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
};
