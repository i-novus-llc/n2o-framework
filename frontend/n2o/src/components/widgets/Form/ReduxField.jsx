import React from 'react';
import PropTypes from 'prop-types';
import { Field as ReduxFormField } from 'redux-form';
import StandardField from '../../widgets/Form/fields/StandardField/StandardField';
import withFieldContainer from '../../widgets/Form/fields/withFieldContainer';
import { pure, compose, withProps } from 'recompose';
import { isEqual, some } from 'lodash';
import withReRenderDependency from '../../../core/dependencies/withReRenderDependency';
import { DEPENDENCY_TYPES } from '../../../core/dependencyTypes';

const config = {
  type: 'field',
  onChange: function({ dependency }) {
    const { _fetchData, size, labelFieldId } = this.props;
    let haveReRenderDependency = some(dependency, { type: DEPENDENCY_TYPES.RE_RENDER });
    if (haveReRenderDependency) {
      _fetchData({
        size,
        [`sorting.${labelFieldId}`]: 'ASC'
      });
    }
  }
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

    this.Field = compose(withFieldContainer)(props.component);
  }

  render() {
    return (
      <ReduxFormField
        name={this.props.id}
        {...this.props}
        component={this.Field}
        setRef={this.props.setRef}
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

export default compose(withReRenderDependency(config))(ReduxField);
