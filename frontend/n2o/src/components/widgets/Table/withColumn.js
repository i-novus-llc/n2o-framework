import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { isEmpty, isEqual } from 'lodash';
import { compose, pure } from 'recompose';
import { createStructuredSelector } from 'reselect';

import { registerColumn } from '../../../actions/columns';
import SecurityCheck from '../../../core/auth/SecurityCheck';
import { isInitSelector, isVisibleSelector, isDisabledSelector } from '../../../selectors/columns';

/**
 * колонка-контейнер
 */
const withColumn = WrappedComponent => {
  class ColumnContainer extends React.Component {
    constructor(props) {
      super(props);
      this.initIfNeeded();
    }

    /**
     * Диспатч экшена регистрации виджета
     */
    initIfNeeded() {
      const { columnId, widgetId, label, isInit, dispatch } = this.props;
      !isInit && dispatch(registerColumn(widgetId, columnId, label));
    }

    /**
     *Базовый рендер
     */
    render() {
      const { visible, security } = this.props;
      const cellEl = <WrappedComponent {...this.props} />;
      return (visible || null) && isEmpty(security) ? (
        cellEl
      ) : (
        <SecurityCheck
          config={security}
          render={({ permissions }) => (permissions ? cellEl : null)}
        />
      );
    }
  }

  const mapStateToProps = createStructuredSelector({
    isInit: (state, props) => isInitSelector(props.widgetId, props.columnId)(state),
    visible: (state, props) => isVisibleSelector(props.widgetId, props.columnId)(state),
    disabled: (state, props) => isDisabledSelector(props.widgetId, props.columnId)(state)
  });

  return compose(
    connect(mapStateToProps),
    pure
  )(ColumnContainer);
};

export default withColumn;
