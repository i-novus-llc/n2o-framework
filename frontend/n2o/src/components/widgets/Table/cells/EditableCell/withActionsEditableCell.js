import React from 'react';
import { connect } from 'react-redux';
import { setModel } from '../../../../../actions/models';
import { PREFIXES } from '../../../../../constants/models';
import { createStructuredSelector } from 'reselect';
import { makeGetResolveModelSelector } from '../../../../../selectors/models';

export default EditableCell => {
  class EditableCellWithActions extends React.Component {
    render() {
      return <EditableCell {...this.props} />;
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
