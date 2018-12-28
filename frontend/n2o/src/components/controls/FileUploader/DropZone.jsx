import React from 'react';
import withFileUploader from './withFileUploader';
import { isEmpty } from 'lodash';
import PropTypes from 'prop-types';
import FileUploader from './FileUploader';

function defaultDropZone(icon, label) {
  return (
    <React.Fragment>
      <div className={icon} />
      {label}
    </React.Fragment>
  );
}

class DropZone extends React.Component {
  render() {
    const { icon, label } = this.props;
    const children = this.props.children || defaultDropZone(icon, label);
    return <FileUploader {...this.props} children={children} componentClass={'n2o-drop-zone'} />;
  }
}

DropZone.defaultProps = {
  saveBtnStyle: {
    marginTop: '10px'
  }
};

DropZone.propTypes = {
  label: PropTypes.string,
  uploading: PropTypes.object,
  icon: PropTypes.string,
  files: PropTypes.arrayOf(PropTypes.object),
  className: PropTypes.string,
  onDrop: PropTypes.func,
  autoUpload: PropTypes.bool,
  onRemove: PropTypes.func,
  onStartUpload: PropTypes.func,
  saveBtnStyle: PropTypes.object,
  visible: PropTypes.bool,
  disabled: PropTypes.bool,
  name: PropTypes.string,
  maxSize: PropTypes.number,
  minSize: PropTypes.number,
  multiple: PropTypes.bool,
  onChange: PropTypes.func,
  children: PropTypes.oneOf(PropTypes.node, PropTypes.func)
};

export default withFileUploader(DropZone);
