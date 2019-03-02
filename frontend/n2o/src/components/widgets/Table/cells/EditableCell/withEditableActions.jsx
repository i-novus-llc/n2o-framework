import React from 'react';

export default EditableCell =>
  class EditableWrapper extends React.Component {
    render() {
      return <EditableCell {...this.props} />;
    }
  };
