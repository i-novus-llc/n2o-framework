import React from 'react';
import PropTypes from 'prop-types';
import { Field as ReduxFormField } from 'redux-form';
import StandardField from '../../widgets/Form/fields/StandardField/StandardField';
import withFieldContainer from '../../widgets/Form/fields/withFieldContainer';
import { pure, compose } from 'recompose';
import observeStore from '../../../utils/observeStore';
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
    this.observeStore = this.observeStore.bind(this);
    this.Field = compose(withFieldContainer)(props.component);
  }

  componentDidMount() {
    this.observeStore();
  }

  observeStore() {
    const { store } = this.context;
    observeStore(store, this.props.setWatchDependency, () => {});
  }

  render() {
    return <ReduxFormField name={this.props.id} {...this.props} component={this.Field} />;
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
