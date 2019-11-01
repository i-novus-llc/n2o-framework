import React from 'react';
import PropTypes from 'prop-types';
import { Field as ReduxFormField } from 'redux-form';
import StandardField from '../../widgets/Form/fields/StandardField/StandardField';
import withFieldContainer from '../../widgets/Form/fields/withFieldContainer';
import { compose, withProps } from 'recompose';
import some from 'lodash/some';
import withObserveDependency from '../../../core/dependencies/withObserveDependency';

const config = {
  onChange: function({ dependency }, dependencyType) {
    if (!this.controlRef) return;
    const { _fetchData, size, labelFieldId } = this.controlRef.props;
    let haveReRenderDependency = some(dependency, { type: dependencyType });
    if (haveReRenderDependency) {
      _fetchData({
        size,
        [`sorting.${labelFieldId}`]: 'ASC',
      });
    }
  },
};

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

    this.setRef = this.setRef.bind(this);
    this.Field = compose(
      withProps(() => ({
        setReRenderRef: props.setReRenderRef,
      })),
      withFieldContainer
    )(props.component);
  }

  setRef(el) {
    this.controlRef = el;
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
  store: PropTypes.object,
};

ReduxField.defaultProps = {
  component: StandardField,
};

ReduxField.propTypes = {
  id: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  component: PropTypes.oneOfType([
    PropTypes.string,
    PropTypes.func,
    PropTypes.node,
  ]),
};

export default withObserveDependency(config)(ReduxField);
