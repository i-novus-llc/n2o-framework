import React from 'react';
import withFileUploader from './withFileUploader';
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

function DropZone(props) {
  const { icon, label, children } = props;

  return (
    <FileUploader
      {...props}
      children={children || defaultDropZone(icon, label)}
      componentClass={'n2o-drop-zone'}
    />
  );
}

DropZone.defaultProps = {
  saveBtnStyle: {
    marginTop: '10px',
  },
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
  requestParam: PropTypes.string,
  maxSize: PropTypes.number,
  minSize: PropTypes.number,
  multiple: PropTypes.bool,
  onChange: PropTypes.func,
  children: PropTypes.oneOfType([PropTypes.node, PropTypes.func]),
};

export default withFileUploader(DropZone);
