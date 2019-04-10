import React from 'react';
import { get } from 'lodash';
import { connect } from 'react-redux';
import { setModel } from '../../../../../actions/models';
import { PREFIXES } from '../../../../../constants/models';
import { createStructuredSelector } from 'reselect';
import { makeGetResolveModelSelector } from '../../../../../selectors/models';

export default EditableCell => {
  class EditableCellWithActions extends React.Component {
    render() {
      const dataProvider = get(
        this.props,
        'action.options.payload.dataProvider',
        {}
      );
      return <EditableCell dataProvider={dataProvider} {...this.props} />;
    }
  }

  const mapStateToProps = createStructuredSelector({
    prevResolveModel: (state, props) =>
      makeGetResolveModelSelector(props.widgetId)(state) || {},
  });

  const mapDispatchToProps = (dispatch, ownProps) => {
    return {
      onResolve: () =>
        dispatch(setModel(PREFIXES.resolve, ownProps.widgetId, ownProps.model)),
    };
  };

  return connect(
    mapStateToProps,
    mapDispatchToProps
  )(EditableCellWithActions);
};
