import React from 'react';
import PropTypes from 'prop-types';
import { Field as ReduxFormField } from 'redux-form';
import StandardField from '../../widgets/Form/fields/StandardField/StandardField';
import withFieldContainer from '../../widgets/Form/fields/withFieldContainer';
import { pure, compose } from 'recompose';
import observeStore from '../../../utils/observeStore';
import setWatchDependency from '../../../utils/setWatchDependency';
import { isEqual } from 'lodash';
/**
 * Поле для {@link ReduxForm}
 * @reactProps {number} id
 * @reactProps {node} component - компонент, который оборачивает поле. Дефолт: {@link StandardField}
 * @see https://redux-form.com/6.0.0-alpha.4/docs/api/field.md/
 * @example
 *
 */
class ReduxField extends React.Component {
  /**
   *Базовый рендер
   */
  constructor(props) {
    super(props);

    this.state = {
      state: null
    };

    this.onDependencyChange = this.onDependencyChange.bind(this);
    this.observeState = this.observeState.bind(this);
    this.setRef = this.setRef.bind(this);
    this.Field = compose(withFieldContainer)(props.component);
  }

  componentDidMount() {
    this.observeState();
  }

  componentDidUpdate(prevProps, prevState) {
    if (!isEqual(prevState, this.state) && this.controlRef && this.controlRef.props._fetchData) {
      this.controlRef.props._fetchData({
        size: this.controlRef.props.size,
        [`sorting.${this.controlRef.props.labelFieldId}`]: 'ASC'
      });
    }
  }

  componentWillUnmount() {
    this._observer && this._observer();
  }

  setRef(node) {
    this.controlRef = node;
  }

  onDependencyChange(state) {
    this.setState({ state });
  }

  observeState() {
    const { store } = this.context;
    this._observer = observeStore(
      store,
      state => setWatchDependency(state, this.props),
      currentState => {
        this.onDependencyChange(currentState);
      }
    );
  }

  render() {
    return (
      <ReduxFormField
        name={this.props.id}
        {...this.props}
        component={this.Field}
        setRef={this.setRef}
      />
    );
  }
}

ReduxField.contextTypes = {
  store: PropTypes.object
};

ReduxField.defaultProps = {
  component: StandardField
};

ReduxField.propTypes = {
  id: PropTypes.number,
  component: PropTypes.node
};

export default pure(ReduxField);
